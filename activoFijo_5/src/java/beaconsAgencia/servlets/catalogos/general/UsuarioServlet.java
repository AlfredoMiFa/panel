/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beaconsAgencia.ejb.CoreUsuarioFacadeLocal;
import beaconsAgencia.entities.CoreUsuario;
import coreapp.clases.general.FCom;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CoreParametrosGeneralesFacadeLocal;
import beaconsAgencia.ejb.CoreSegContrasenaFacadeLocal;
import beaconsAgencia.ejb.EmpresaFacadeLocal;
import beaconsAgencia.ejb.OficinasFacadeLocal;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "UsuarioServlet", urlPatterns = {"/catalogos/generals/usuario.do"})
public class UsuarioServlet extends HttpServlet {
    @EJB
    CoreUsuarioFacadeLocal usuarioEJBLocal;
    @EJB
    CoreParametrosGeneralesFacadeLocal coreParametrosGeneralesEJBLocal;
    @EJB
    CoreSegContrasenaFacadeLocal coreSegContrasenaEJBLocal;
    @EJB
    EmpresaFacadeLocal EmpresaEJB;
    @EJB
    OficinasFacadeLocal OficinasEJB;
    String idusur,usuar;

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
        CoreUsuario usuario;
        if(session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")==null){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                response.getWriter().print("{\"errors\":\"901\"}");
            else
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            usuario = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario();
            try {
                boolean isMultipart = ServletFileUpload.isMultipartContent( request );
                if ( !isMultipart )
                {
                    if(request.getParameter("modo")==null) {
                        int rows=10;
                        int page=1;   
                        String sort="CORE_USUARIO_ID"; 
                        String order="desc"; 
                        String filter="where ";
                        String oficinas = "";
                        String idEmpr = usuario.getIdEmpresa().getIdEmpresa().toString();
                        if(usuario.getGrupo().equals("ADMINISTRADOR")) {
                        oficinas = OficinasEJB.oficinaPorEmpresa(idEmpr);
                        for(int i=0;i<1;i++) {
                            filter += " ID_OFICINA IN("+oficinas+")";
                            i++;
                        }
                    }if(usuario.getGrupo().equals("SUPERVISOR")) {
                            filter += " ID_OFICINA = "+usuario.getIdOficina().getIdOficina().toString() + " ";
                        }
                        
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
                            
                            for(int i=0;i<mFiltroDoble.length;i++)
                            {                            
                                if(mFiltroDoble[i]) {                      
                                    filter += "("+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][0][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][0][field]")),request.getParameter("filter[filters]["+i+"][filters][0][value]"))
                                            +" "+request.getParameter("filter[filters]["+i+"][logic]")+" "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][1][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][1][field]")),request.getParameter("filter[filters]["+i+"][filters][1][value]"))+")";
                                }
                                else {
                                    String field = fieldParse(request.getParameter("filter[filters]["+i+"][field]"));
                                    String valor = request.getParameter("filter[filters]["+i+"][value]");
                                    if(field.equals("ID_EMPRESA"))
                                        filter = " where "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),EmpresaEJB.idEmpresa(valor));
                                    if(field.equals("ID_OFICINA"))
                                        filter = " where "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),OficinasEJB.idOficina(valor));
                                    if(field.equals("CORE_USUARIO_ID") || field.equals("usuario") || field.equals("nombre") || field.equals("apellidos") 
                                            || field.equals("email") || field.equals("telefono") || field.equals("estatus") || field.equals("ES_SUPER")
                                            || field.equals("estilo") || field.equals("vigencia") || field.equals("APICA_BITACORA") || field.equals("IS_LOGIN")
                                            || field.equals("MULTI_SESION") || field.equals("IP_RESTRICCION") || field.equals("grupo"))
                                        filter += " AND "+ FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));
                                }
                                if(i<mFiltroDoble.length-1)
                                    filter += " "+request.getParameter("filter[logic]")+" ";  
                            }
                        }
                        if(filter.equals("where ")) { filter = " ";}
                        idusur = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario().getCoreUsuarioId().toString();
                        usuar = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario().getUsuario();
                        response.getWriter().print(usuarioEJBLocal.selectUsuario(rows,page,sort,order,filter));
                    }else {
                        String modo=request.getParameter("modo");
                        String id=request.getParameter("coreUsuarioId");
                        CoreUsuario usuarioc=new CoreUsuario();
                        if(modo.equals("eliminar")) {
                            usuarioEJBLocal.remove(usuarioEJBLocal.find(Integer.parseInt(id)));
                            response.getWriter().print(usuarioEJBLocal.persistirUsuario(usuarioc,"editar")); 
                        }else if(modo.equals("editar")) {
                            String validaContrasena=coreSegContrasenaEJBLocal.validaClave(request.getParameter("usuario"), request.getParameter("contrasena"), 0);
                            if(validaContrasena.indexOf("\"success\":true")>-1 || request.getParameter("contrasena").equals("SAME")){
                                if(!id.equals(""))
                                    usuarioc=usuarioEJBLocal.find(Integer.parseInt(id));
                                else{
                                    usuarioc.setCoreUsuarioId(null);
                                }
                                usuarioc.setEstatus("AC");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                usuarioc.setNombre(request.getParameter("nombre"));
                                usuarioc.setApellidos(request.getParameter("apellidos"));
                                usuarioc.setUsuario(request.getParameter("usuario").toUpperCase());
                                if(!request.getParameter("contrasena").equals("SAME")){
                                    String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                                    usuarioc.setContrasena(FCom.encrypt(request.getParameter("contrasena"),cadenaEncriptacion));
                                }
                                usuarioc.setEmail(request.getParameter("email"));
                                usuarioc.setTelefono(request.getParameter("telefono"));
                                usuarioc.setEstilo(request.getParameter("estilo"));
                                usuarioc.setEsSuper(request.getParameter("esSuper"));
                                usuarioc.setGrupo(request.getParameter("grupo"));
                                usuarioc.setAplicaBitacora(Boolean.parseBoolean(request.getParameter("aplicaBitacora")));
                                usuarioc.setMultiSesion(Boolean.parseBoolean(request.getParameter("multiSesion")));
                                usuarioc.setIpRestriccion(Boolean.parseBoolean(request.getParameter("ipRestriccion")));
                                usuarioc.setIdEmpresa(EmpresaEJB.find(Integer.parseInt(request.getParameter("idEmpresa[id]"))));
                                usuarioc.setIdOficina(OficinasEJB.find(Integer.parseInt(request.getParameter("idOficina[id]"))));
                                usuarioc.setVigencia(new Date());
                                response.getWriter().print(usuarioEJBLocal.persistirUsuario(usuarioc,modo)); 
                            }else
                                response.getWriter().print(validaContrasena);
                        }else if(modo.equals("nuevo")) {
                            String validaContrasena=coreSegContrasenaEJBLocal.validaClave(request.getParameter("usuario"), request.getParameter("contrasena"), 0);
                            if(validaContrasena.indexOf("\"success\":true")>-1 || request.getParameter("contrasena").equals("SAME")){
                                if(!id.equals(""))
                                    usuarioc=usuarioEJBLocal.find(Integer.parseInt(id));
                                else{
                                    usuarioc.setCoreUsuarioId(null);
                                }
                                usuarioc.setEstatus("AC");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                usuarioc.setNombre(request.getParameter("nombre"));
                                usuarioc.setApellidos(request.getParameter("apellidos"));
                                usuarioc.setUsuario(request.getParameter("usuario").toUpperCase());
                                if(!request.getParameter("contrasena").equals("SAME")){
                                    String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                                    usuarioc.setContrasena(FCom.encrypt(request.getParameter("contrasena"),cadenaEncriptacion));
                                }
                                usuarioc.setEmail(request.getParameter("email"));
                                usuarioc.setTelefono(request.getParameter("telefono"));
                                usuarioc.setEstilo(request.getParameter("estilo"));
                                usuarioc.setEsSuper(request.getParameter("esSuper"));
                                usuarioc.setGrupo(request.getParameter("grupo"));
                                usuarioc.setAplicaBitacora(Boolean.parseBoolean(request.getParameter("aplicaBitacora")));
                                usuarioc.setMultiSesion(Boolean.parseBoolean(request.getParameter("multiSesion")));
                                usuarioc.setIpRestriccion(Boolean.parseBoolean(request.getParameter("ipRestriccion")));
                                usuarioc.setIdEmpresa(EmpresaEJB.find(Integer.parseInt(request.getParameter("idEmpresa"))));
                                usuarioc.setIdOficina(OficinasEJB.find(Integer.parseInt(request.getParameter("idOficina"))));
                                usuarioc.setVigencia(new Date());
                                response.getWriter().print(usuarioEJBLocal.persistirUsuario(usuarioc,modo)); 
                            }else
                                response.getWriter().print(validaContrasena);
                        }else if(modo.equals("combo")) {
                            ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
                            response.getWriter().print(usuarioEJBLocal.generaComboUsuario(objetoSesion.getUsuario()));
                        }else if(modo.equals("getImage")) {
                            CoreUsuario user=((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario();
                            response.setContentType("image/png"); 
                            if(user.getFoto()!=null)
                            {
                                response.getOutputStream().write(user.getFoto());
                            }else
                                request.getRequestDispatcher("../../recursos/img/userDefault.png").forward(request,response);
                        }else if(modo.equals("delImage")) {
                            ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
                            objetoSesion.getUsuario().setFoto(null);
                            session.setAttribute(getServletContext().getInitParameter("vsi")+"SESION",objetoSesion);
                            response.getWriter().print("{\"success\":true}");
                        }else if(modo.equals("editarPerfil")) {
                            String validaContrasena=coreSegContrasenaEJBLocal.validaClave(request.getParameter("usuario"), request.getParameter("contrasena"), 0);
                            if(validaContrasena.indexOf("{\"success\":true\"}")>-1 || request.getParameter("contrasena").equals("")){
                                ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
                                objetoSesion.getUsuario().setNombre(request.getParameter("nombre"));
                                objetoSesion.getUsuario().setApellidos(request.getParameter("apellidos"));
                                objetoSesion.getUsuario().setUsuario(request.getParameter("usuario"));
                                if(!request.getParameter("contrasena").equals("")){
                                    String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                                    objetoSesion.getUsuario().setContrasena(FCom.encrypt(request.getParameter("contrasena"),cadenaEncriptacion));
                                }
                                objetoSesion.getUsuario().setEmail(request.getParameter("email"));
                                objetoSesion.getUsuario().setTelefono(request.getParameter("telefono"));
                                objetoSesion.getUsuario().setEstilo(request.getParameter("estilo"));
                                session.setAttribute(getServletContext().getInitParameter("vsi")+"SESION",objetoSesion);
                                response.getWriter().print(usuarioEJBLocal.persistirUsuario(objetoSesion.getUsuario(),"editar"));
                            }else
                                response.getWriter().print(validaContrasena);
                        }else if(modo.equals("verificarUnico")) {
                            String campo=request.getParameter("campo");
                            String valor=request.getParameter("valor");
                            response.getWriter().print(usuarioEJBLocal.verificarUnico(campo,valor,id));
                        }else if(modo.equals("empresa")) {
                            String idUsuario = request.getParameter("idUsuar");
                            response.getWriter().print(usuarioEJBLocal.validaUsuario(idUsuario, ""));
                        }
                    } 
                }else {
                    ServletFileUpload upload = new ServletFileUpload();
                    ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
                    boolean correcto=false;
                    FileItemIterator iter = upload.getItemIterator( request );
                    while ( iter.hasNext() )
                    {
                        FileItemStream item = iter.next();
                        String fieldName = item.getFieldName();
                        if ( fieldName.equals( "foto" ) )
                        {
                            byte[] bytes = IOUtils.toByteArray( item.openStream() );
                            objetoSesion.getUsuario().setFoto(bytes);
                            correcto=true;
                            break;
                        }
                    }
                    if(correcto)
                    {
                        session.setAttribute(getServletContext().getInitParameter("vsi")+"SESION",objetoSesion);
                        response.getWriter().print("{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}");
                    }else
                        response.getWriter().print("{\"success\":false,\"title\":\"Error: \",\"msg\":\"Ha ocurrido un error, favor de indicarlo al administrador\"}");

                }
            }catch (  IOException | NumberFormatException | ServletException | FileUploadException e) {
                if(request.getParameter("modo")==null)
                    response.getWriter().print("{\"data\":[],\"total\":0}"); 
                else
                    response.getWriter().print("{\"errors\":\"Error al realizar la operacion.\"}");
            } 
        }
    }
    private String fieldParse(String campo){
        switch (campo) {
            case "catalogoUsuarioId":
                return "CORE_USUARIO_ID";
            case "esSuper":
                return "ES_SUPER";
            case "aplicaBitacora":
                return "APLICA_BITACORA";
            case "multiSesion":
                return "MULTI_SESION";
            case "ipRestriccion":
                return "IP_RESTRICCION";
            case "idEmpresa":
                return "ID_EMPRESA";
            case "idOficina":
                return "ID_OFICINA";
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
