/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import coreapp.clases.general.FCom;
import beaconsAgencia.ejb.CoreCamposLayoutFacadeLocal;
import beaconsAgencia.ejb.CoreConexionesDaoFacadeLocal;
import beaconsAgencia.ejb.CoreLayoutFacadeLocal;
import beaconsAgencia.ejb.CoreValidadorFacadeLocal;
import beaconsAgencia.entities.CoreCamposLayout;
import beaconsAgencia.entities.CoreLayout;
import beaconsAgencia.entities.CoreValidador;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "LayoutServlet", urlPatterns = {"/catalogos/generals/layout.do"})
public class LayoutServlet extends HttpServlet {
    @EJB
    CoreLayoutFacadeLocal layoutEJBLocal;
    @EJB
    CoreCamposLayoutFacadeLocal camposLayoutEJBLocal;
    @EJB
    CoreValidadorFacadeLocal validadorEJBLocal;
    @EJB
    CoreConexionesDaoFacadeLocal conexionesEJBLocal;

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
        if(session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")==null)
        {
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                response.getWriter().print("{\"errors\":\"901\"}");
            else
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }else {
            boolean isMultipart = ServletFileUpload.isMultipartContent( request );
            if ( !isMultipart )
            {
                try {
                    String modo=request.getParameter("modo"); 

                    if(request.getParameter("modo").equals("seleccionarLayout")) {
                        int rows=10;
                        int page=1;   
                        String sort="CORE_LAYOUT_ID"; 
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
                        response.getWriter().print(layoutEJBLocal.select(rows,page,sort,order,filter));
                    }else if(modo.equals("eliminarLayout")) {
                        layoutEJBLocal.remove(layoutEJBLocal.find(Integer.parseInt(request.getParameter("coreLayoutId"))));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editarLayout") || modo.equals("nuevoLayout")) {
                        String id=request.getParameter("coreLayoutId");
                        CoreLayout layout=new CoreLayout();
                        if(id!=null && !id.equals(""))
                            layout=layoutEJBLocal.find(Integer.parseInt(id));
                        layout.setActivo(Boolean.parseBoolean(request.getParameter("activo")));
                        layout.setNombre(request.getParameter("nombre"));
                        layout.setTabla(request.getParameter("tabla"));
                        layout.setColumnaInicio(Integer.parseInt(request.getParameter("columnaInicio")));
                        layout.setColumnaFin(Integer.parseInt(request.getParameter("columnaFin")));
                        layout.setRenglonInicio(Integer.parseInt(request.getParameter("renglonInicio")));
                        response.getWriter().print(layoutEJBLocal.persistir(layout,modo));                          
                    }else if(request.getParameter("modo").equals("seleccionarCamposLayout")) {
                        int rows=10;
                        int page=1;   
                        String sort="CORE_CAMPOS_LAYOUT_ID"; 
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
                        response.getWriter().print(camposLayoutEJBLocal.select(rows,page,sort,order,filter));
                    }else if(modo.equals("eliminarCamposLayout")) {
                        camposLayoutEJBLocal.remove(camposLayoutEJBLocal.find(Integer.parseInt(request.getParameter("coreCamposLayoutId"))));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editarCamposLayout") || modo.equals("nuevoCamposLayout")) {
                        String id=request.getParameter("coreCamposLayoutId");
                        CoreCamposLayout campoLayout=new CoreCamposLayout();
                        if(id!=null && !id.equals(""))
                            campoLayout.setCoreCamposLayoutId(Integer.parseInt(id));
                        campoLayout.setNullable(Boolean.parseBoolean(request.getParameter("nullable")));
                        campoLayout.setNombreCampo(request.getParameter("nombreCampo").toUpperCase());
                        campoLayout.setNombreVariable(request.getParameter("nombreVariable"));
                        campoLayout.setTipoDato(request.getParameter("tipoDato"));
                        campoLayout.setOrden(Integer.parseInt(request.getParameter("orden")));
                        campoLayout.setValidar(Boolean.parseBoolean(request.getParameter("validar")));
                        campoLayout.setCoreLayoutId(layoutEJBLocal.find(Integer.parseInt(request.getParameter("coreLayoutId"))));
                        response.getWriter().print(camposLayoutEJBLocal.persistir(campoLayout,modo));                          
                    }else if(request.getParameter("modo").equals("seleccionarValidador")) {
                        int rows=10;
                        int page=1;   
                        String sort="CORE_VALIDADOR_ID"; 
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
                        response.getWriter().print(validadorEJBLocal.select(rows,page,sort,order,filter));
                    }else if(modo.equals("eliminarValidador")) {
                        validadorEJBLocal.remove(validadorEJBLocal.find(Integer.parseInt(request.getParameter("coreValidadorId"))));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editarValidador") || modo.equals("nuevoValidador")) {
                        String id=request.getParameter("coreValidadorId");
                        CoreValidador validador=new CoreValidador();
                        if(id!=null && !id.equals(""))
                            validador=validadorEJBLocal.find(Integer.parseInt(request.getParameter("coreValidadorId")));
                        validador.setActivo(Boolean.parseBoolean(request.getParameter("activo")));
                        
                        if(request.getParameter("conexionDao[id]")!=null){
                            if(!request.getParameter("conexionDao[id]").equals(""))
                                validador.setConexionDao(conexionesEJBLocal.find(Integer.parseInt(request.getParameter("conexionDao[id]"))));
                            else
                                validador.setConexionDao(null);
                        }else{
                            if(!request.getParameter("conexionDao").equals(""))
                                validador.setConexionDao(conexionesEJBLocal.find(Integer.parseInt(request.getParameter("conexionDao"))));
                            else
                                validador.setConexionDao(null);
                        }
                        validador.setCoreCamposLayoutId(camposLayoutEJBLocal.find(Integer.parseInt(request.getParameter("coreCamposLayoutId"))));
                        validador.setNombreClase(request.getParameter("nombreClase"));
                        validador.setNombreMetodo(request.getParameter("nombreMetodo"));
                        validador.setParametros(request.getParameter("parametros"));
                        validador.setRegex(request.getParameter("regex"));
                        validador.setSqlSentence(request.getParameter("sqlSentence"));
                        validador.setTipo(request.getParameter("tipo"));
                        validador.setValorFinal(request.getParameter("valorFinal"));
                        validador.setValorInicial(request.getParameter("valorInicial"));
                        validador.setMensajeError(request.getParameter("mensajeError"));
                        response.getWriter().print(validadorEJBLocal.persistir(validador,modo));                          
                    }
                }catch (IOException | NumberFormatException e) {
                    if(request.getParameter("modo")==null)
                        response.getWriter().print("{\"data\":[],\"total\":0}"); 
                    else
                        response.getWriter().print("{\"errors\":\"Error al realizar la operacion.\"}");
                }
                catch ( Exception ex )
                {
                    throw new ServletException( ex );
                }
            }else {
                ServletFileUpload upload = new ServletFileUpload();
                try
                {
                    Integer idValidador=null;
                    Integer idLayout=null;
                    byte[] bytes=null;
                    String name=null;
                    FileItemIterator iter = upload.getItemIterator( request );
                    while ( iter.hasNext() )
                    {
                        FileItemStream item = iter.next();
                        String fieldName = item.getFieldName();
                        if ( fieldName.equals( "archivo" ) || fieldName.equals( "archivoe" ))
                        {
                            name=item.getName();
                            bytes = IOUtils.toByteArray( item.openStream() );
                        }else if(fieldName.equals( "coreValidadorId" ))
                            idValidador=Integer.parseInt(Streams.asString(item.openStream()));
                        else if(fieldName.equals( "coreLayoutId" ))
                            idLayout=Integer.parseInt(Streams.asString(item.openStream()));
                    }
                    if(name!=null && idValidador!=null) {
                        response.getWriter().print(validadorEJBLocal.subirClase(bytes,idValidador,name));
                    }else if(name!=null && idLayout!=null) {
                        response.getWriter().print(layoutEJBLocal.probarLayout(new ByteArrayInputStream(bytes),idLayout));
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
            }
        }
    }
    private String fieldParse(String campo){
        switch (campo) {
            case "coreLayoutId":
                return "CORE_LAYOUT_ID";
            case "coreCamposLayoutId":
                return "CORE_CAMPOS_LAYOUT_ID";
            case "nombreCampo":
                return "NOMBRE_CAMPO";
            case "nombreVariable":
                return "NOMBRE_VARIABLE";
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
