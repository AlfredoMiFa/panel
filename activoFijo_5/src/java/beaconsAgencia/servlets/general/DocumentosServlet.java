/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.general;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import coreapp.clases.general.Arbol;
import coreapp.clases.general.FCom;
import coreapp.clases.general.Nodo;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CoreCarpetaFacadeLocal;
import beaconsAgencia.ejb.CoreDerechoSistemaFacadeLocal;
import beaconsAgencia.ejb.CoreDocumentoFacadeLocal;
import beaconsAgencia.entities.CoreCarpeta;
import beaconsAgencia.entities.CoreDocumento;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "DocumentosServlet", urlPatterns = {"/general/documentos.do"})
public class DocumentosServlet extends HttpServlet {
    @EJB
    CoreCarpetaFacadeLocal carpetaEJBLocal;
    @EJB
    CoreDocumentoFacadeLocal documentoEJBLocal;
    @EJB
    CoreDerechoSistemaFacadeLocal coreDerechoSistemaEJBLocal;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session=request.getSession();
        response.setHeader("Cache-Control","no-store"); //HTTP 1.1
        response.setHeader("Pragma","no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0);
        response.setDateHeader("Last-Modified", 0);
        if(session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")==null ){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                response.getWriter().print("{\"errors\":\"901\"}");
            else
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {            
            try {
                ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
                boolean isMultipart = ServletFileUpload.isMultipartContent( request );
                if ( !isMultipart )
                {
                    String modo=request.getParameter("modo");                
                    if(modo.equals("arbolCarpetas")) {
                        Arbol arbol=new Arbol(new Nodo(-1,-2,"DIRECTORIO",null));
                        List<CoreCarpeta> listaCarpetas=carpetaEJBLocal.getListaCarpetas(objetoSesion.getUsuario().getCoreUsuarioId());
                        for(CoreCarpeta carpeta:listaCarpetas){
                            if(carpeta.getCoreCarpetaPadreId()!=null)
                                arbol.agregarNodo(new Nodo(carpeta.getCoreCarpetaId(),carpeta.getCoreCarpetaPadreId().getCoreCarpetaId(),carpeta.getNombre(),carpeta));
                            else
                                arbol.agregarNodo(new Nodo(carpeta.getCoreCarpetaId(),-1,carpeta.getNombre(),carpeta));
                        }                
                        String json = "[";
                        int i=0;
                        for (i = 1; i < arbol.getListaNodos().size(); i++) {
                            Nodo nodoTemp = new Nodo(arbol.getListaNodos().get(i));
                            if (nodoTemp.isNodoHoja()) {
                                int ultimoHijo = 
                                    arbol.getListaNodos().get(arbol.posicionNodo(nodoTemp.getPadre())).getUltimoHijo();
                                if (ultimoHijo == nodoTemp.getId()){
                                        json += "{\"id\":"+((CoreCarpeta)nodoTemp.getObjeto()).getCoreCarpetaId()+", \"text\": \""+nodoTemp.getTitulo()+"\", \"expanded\": true, \"spriteCssClass\": \"folder\"}"+ arbol.cerrarRama(nodoTemp, "", true)+",";                                             
                                }
                                else{
                                    json += "{\"id\":"+((CoreCarpeta)nodoTemp.getObjeto()).getCoreCarpetaId()+", \"text\": \""+nodoTemp.getTitulo()+"\", \"expanded\": true, \"spriteCssClass\": \"folder\"},";                                                                              
                                }
                            } else{
                                json += "{\"id\":"+((CoreCarpeta)nodoTemp.getObjeto()).getCoreCarpetaId()+", \"text\": \""+nodoTemp.getTitulo()+"\", \"expanded\": true, \"spriteCssClass\": \"rootfolder\", \"items\": [";
                            }
                        }
                        if (i > 0)
                            json = json.substring(0, json.length() - 1);
                        json += "]";
                        response.getWriter().println(json);
                    }else if(modo.equals("agregarCarpeta")) {
                        if(objetoSesion.isSuperAdministrador() || coreDerechoSistemaEJBLocal.hasDerecho(objetoSesion.getPerfilSelected().getCorePerfilId(), "D_ADMIN_CARPETAS_DEPOSITO") ){
                            CoreCarpeta carpeta=new CoreCarpeta();
                            carpeta.setEstatus("AC");
                            carpeta.setCoreCarpetaPadreId(carpetaEJBLocal.find(Integer.parseInt(request.getParameter("idPadre"))));
                            carpeta.setNombre(request.getParameter("nombre"));
                            carpeta.setDescripcion(request.getParameter("nombre"));
                            carpeta.setPropietarioId(null);
                            response.getWriter().println(carpetaEJBLocal.persistir(carpeta,"nuevo"));   
                        }else
                            response.getWriter().println("{\"success\":false,\"title\":\"Error\",\"msg\":\"Usted no cuenta con los permisos necesarios.\"}");
                    }else if(modo.equals("borrarCarpeta")) {
                        if(objetoSesion.isSuperAdministrador()  || coreDerechoSistemaEJBLocal.hasDerecho(objetoSesion.getPerfilSelected().getCorePerfilId(), "D_ADMIN_CARPETAS_DEPOSITO")){
                            CoreCarpeta carpeta=carpetaEJBLocal.find(Integer.parseInt(request.getParameter("id")));
                            if(carpeta.getCoreDocumentoList().size()>0)
                                response.getWriter().println("{\"success\":false,\"title\":\"Error\",\"msg\":\"La carpeta no se puede borra ya que contiene documentos.\"}");
                            else{
                                carpetaEJBLocal.remove(carpeta);
                                response.getWriter().println("{\"success\":true,\"title\":\"Operación exitosa\",\"msg\":\"\"}");
                            }
                        }else
                            response.getWriter().println("{\"success\":false,\"title\":\"Error\",\"msg\":\"Usted no cuenta con los permisos necesarios.\"}");
                    }else if(modo.equals("eliminarDocumento")) {
                        if(objetoSesion.isSuperAdministrador()  || coreDerechoSistemaEJBLocal.hasDerecho(objetoSesion.getPerfilSelected().getCorePerfilId(), "D_ELIMINAR_DEPOSITO")){                        
                            CoreDocumento documento=documentoEJBLocal.find(Integer.parseInt(request.getParameter("coreDocumentoId")));
                            documentoEJBLocal.remove(documento);
                            response.getWriter().println("{\"success\":true,\"title\":\"Operación exitosa\",\"msg\":\"\"}");
                        }else
                            response.getWriter().println("{\"success\":false,\"title\":\"Error\",\"msg\":\"Usted no cuenta con los permisos necesarios.\"}");
                    }else if(modo.equals("editarDocumento")) {
                        if(objetoSesion.isSuperAdministrador()  || coreDerechoSistemaEJBLocal.hasDerecho(objetoSesion.getPerfilSelected().getCorePerfilId(), "D_EDITAR_DEPOSITO")){                        
                            CoreDocumento documento=documentoEJBLocal.find(Integer.parseInt(request.getParameter("coreDocumentoId")));
                            documento.setDescripcion(request.getParameter("descripcion"));
                            response.getWriter().println(documentoEJBLocal.persistir(documento,"editar"));
                        }else
                            response.getWriter().println("{\"success\":false,\"title\":\"Error\",\"msg\":\"Usted no cuenta con los permisos necesarios.\"}");
                    }else if(modo.equals("descargarDocumento")) {
                        if(objetoSesion.isSuperAdministrador() || coreDerechoSistemaEJBLocal.hasDerecho(objetoSesion.getPerfilSelected().getCorePerfilId(), "D_VER_DEPOSITO")){                        
                            CoreDocumento documento=documentoEJBLocal.find(Integer.parseInt(request.getParameter("coreDocumentoId")));
                            response.setContentType("APPLICATION/OCTET-STREAM");   
                            response.setHeader("Content-Disposition","attachment; filename=\"" + documento.getNombreArchivo() + "\""); 
                            response.getOutputStream().write(documento.getDocumento());
                        }else
                            response.getWriter().println("{\"success\":false,\"title\":\"Error\",\"msg\":\"Usted no cuenta con los permisos necesarios.\"}");
                    }else if(modo.equals("listaDocumentos")) {
                        
                        if(objetoSesion.isSuperAdministrador() || coreDerechoSistemaEJBLocal.hasDerecho(objetoSesion.getPerfilSelected().getCorePerfilId(), "D_VER_DEPOSITO")){
                            int rows=10;
                            int page=1;   
                            String sort="CORE_DOCUMENTO_ID"; 
                            String order="desc"; 
                            String filter="";

                            if(request.getParameter("pageSize")!=null)
                                rows = Integer.parseInt(request.getParameter("pageSize"));
                            if(request.getParameter("page")!=null)
                                page = Integer.parseInt(request.getParameter("page")); 
                            if(request.getParameter("sort[0][field]")!=null)
                                sort = fieldParse(request.getParameter("sort[0][field]"));
                            if(request.getParameter("sort[0][dir]")!=null)
                                order = request.getParameter("sort[0][dir]");

                            if(request.getParameter("filter[logic]")!=null) {
                                int x=0;
                                for(int i=0;i<request.getParameterMap().size();i++){
                                    if(request.getParameter("filter[filters]["+i+"][value]")!=null || request.getParameter("filter[filters]["+i+"][filters][0][value]")!=null)
                                        x++;                            
                                }
                                boolean[] mFiltroDoble=new boolean[x];
                                for(int i=0;i<x;i++){
                                    if(request.getParameter("filter[filters]["+i+"][logic]")!=null)
                                        mFiltroDoble[i]=true;
                                    else
                                        mFiltroDoble[i]=false;
                                }
                                filter=" where ";
                                for(int i=0;i<mFiltroDoble.length;i++)
                                {                            
                                    if(mFiltroDoble[i])                                
                                        filter += "("+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][0][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][0][field]")),request.getParameter("filter[filters]["+i+"][filters][0][value]"))
                                                +" "+request.getParameter("filter[filters]["+i+"][logic]")+" "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][1][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][1][field]")),request.getParameter("filter[filters]["+i+"][filters][1][value]"))+")";
                                    else
                                        filter += FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));

                                    if(i<mFiltroDoble.length-1)
                                        filter += " "+request.getParameter("filter[logic]")+" ";  
                                }
                            } 
                            response.getWriter().print(documentoEJBLocal.select(rows,page,sort,order,filter));
                        }else
                            response.getWriter().print("{\"errors\":\"Usted no cuenta con los permisos necesarios.\"}");
                    }
                }else{
                    if(objetoSesion.isSuperAdministrador() || coreDerechoSistemaEJBLocal.hasDerecho(objetoSesion.getPerfilSelected().getCorePerfilId(), "D_AGREGAR_DEPOSITO")){                        
                        ServletFileUpload upload = new ServletFileUpload();                
                        try
                        {
                            byte[] bytes=null;
                            String name=null;
                            FileItemIterator iter = upload.getItemIterator( request );
                            CoreDocumento documento=new CoreDocumento();
                            documento.setEstatus("AC");
                            documento.setDescripcion("");
                            while ( iter.hasNext() )
                            {
                                FileItemStream item = iter.next();
                                String fieldName = item.getFieldName();
                                if ( fieldName.equals( "archivo" ) )
                                {
                                    name=item.getName();
                                    bytes = IOUtils.toByteArray( item.openStream() );
                                    documento.setNombreArchivo(name);
                                    documento.setTipomime(name.split("\\.")[1]);
                                    documento.setDocumento(bytes);
                                }else if(fieldName.equals( "coreDocumentoId" ))
                                    documento.setCoreDocumentoId(Integer.parseInt(Streams.asString(item.openStream())));
                                else if(fieldName.equals( "idCarpeta" ))
                                    documento.setCoreCarpetaId(carpetaEJBLocal.find(Integer.parseInt(Streams.asString(item.openStream()))));
                            }
                            if(name!=null ) {
                                if(documento.getCoreDocumentoId()!=null)
                                    response.getWriter().print(documentoEJBLocal.persistir(documento,"editar"));
                                else{
                                    CoreDocumento documentoS=documentoEJBLocal.findDocumentoByCarpetaNombre(documento);
                                    if(documentoS!=null){
                                        documento.setCoreDocumentoId(documentoS.getCoreDocumentoId());
                                        documento.setDescripcion(documentoS.getDescripcion());
                                        response.getWriter().print(documentoEJBLocal.persistir(documento,"editar"));
                                    }
                                    else
                                        response.getWriter().print(documentoEJBLocal.persistir(documento,"nuevo"));
                                }
                            }else
                                response.getWriter().print("{\"success\":false,\"title\":\"Error\",\"msg\":\"Error al realizar la operacion.\"}");
                        }
                        catch ( IOException ex )
                        {
                            throw ex;
                        }
                        catch ( Exception ex )
                        {
                            throw new ServletException( ex );
                        }
                    }else
                        response.getWriter().println("{\"success\":false,\"title\":\"Error\",\"msg\":\"Usted no cuenta con los permisos necesarios.\"}");
                }
            }catch (Exception e) {
                if(request.getParameter("modo")==null || request.getParameter("modo").equals("listaDocumentos"))
                    response.getWriter().print("{\"data\":[],\"total\":0}"); 
                else
                    response.getWriter().print("{\"success\":false,\"title\":\"Error\",\"msg\":\"Error al realizar la operacion.\"}");
            }
        }
    }
    private String fieldParse(String campo){
        switch (campo) {
            case "coreDocumentoId":
                return "CORE_DOCUMENTO_ID";
            case "coreCarpetaId":
                return "CORE_CARPETA_ID";
            default:
                return campo;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
