/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beaconsAgencia.clases.correo.EnviarEmail;
import coreapp.clases.general.FCom;
import beaconsAgencia.ejb.CoreCorreoFacadeLocal;
import beaconsAgencia.ejb.CoreParametrosGeneralesFacadeLocal;
import beaconsAgencia.entities.CoreCorreo;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "CorreoServlet", urlPatterns = {"/catalogos/generals/correo.do"})
public class CorreoServlet extends HttpServlet {
    @EJB
    CoreCorreoFacadeLocal coreCorreoEJBLocal;
    @EJB
    CoreParametrosGeneralesFacadeLocal coreParametrosGeneralesEJBLocal;

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
        if(session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")==null){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                response.getWriter().print("{\"errors\":\"901\"}");
            else
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            
            try {
                if(request.getParameter("modo")==null) {
                    int rows=10;
                    int page=1;   
                    String sort="CORE_CORREO_ID"; 
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
                    response.getWriter().print(coreCorreoEJBLocal.select(rows,page,sort,order,filter));
                }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("coreCorreoId");
                    CoreCorreo correo=new CoreCorreo();
                    if(modo.equals("eliminar")) {
                        coreCorreoEJBLocal.remove(coreCorreoEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(!id.equals(""))
                            correo=coreCorreoEJBLocal.find(Integer.parseInt(id));
                        else
                            correo.setCoreCorreoId(null);
                        correo.setEmisor(request.getParameter("emisor"));
                        correo.setUser(request.getParameter("user"));
                        if(!request.getParameter("password").equals("SAME")){
                            String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                            correo.setPassword(FCom.encrypt(request.getParameter("password"),cadenaEncriptacion));
                        }
                        correo.setSmtpServer(request.getParameter("smtpServer"));
                        correo.setSmtpPort(request.getParameter("smtpPort"));
                        response.getWriter().print(coreCorreoEJBLocal.persistir(correo,modo));                          
                    }else if(modo.equals("combo")) {
                        response.getWriter().print(coreCorreoEJBLocal.generaCombo());
                    }else if(modo.equals("probarConexion")) {
                        String nombreAplicacion=coreParametrosGeneralesEJBLocal.getValor("APP_NAME");
                        String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                        String ruta="http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath()+"/";
                        correo=coreCorreoEJBLocal.find(Integer.parseInt(id));
                        correo.setPassword(FCom.decrypt(correo.getPassword(),cadenaEncriptacion));
                        String html="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"> \n" + 
                        "<html lang=\"en\"> \n" + 
                        "  <head> \n" + 
                        "    <meta http-equiv=\"Content-type\" content=\"text/html; charset=UTF-8\"> \n" + 
                        "    <title> "+nombreAplicacion+" </title> \n" +  
                        "    <link rel=\"stylesheet\" href=\""+ruta+"recursos/css/foundation/normalize.css\" />\n" + 
                        "    <link rel=\"stylesheet\" href=\""+ruta+"recursos/css/foundation/foundation.min.css\" />" + 
                        "  </head> \n" + 
                        "  <body> \n" + 
                        "    <div class=\"row\">\n" + 
                        "       <div class=\"large-6 large-centered columns\">\n" + 
                        "          <br/><br/><br/>\n" + 
                        "          <img src=\""+ruta+"recursos/img/logo.png\" alt=\"Logo administracion\"/>\n" + 
                        "          <div class=\"panel callout\">\n" + 
                        "              <p>Correo enviado para la comprobacion de conexion SMTP</p>\n" + 
                        "          </div>" + 
                        "          <div class=\"panel\">\n" + 
                        "               <b>"+correo.getEmisor()+ "</b><br>" +
                        "               <p>La prueba de conexi&oacute;n al servidor de correos "+correo.getSmtpServer()+", ha sido exitosa.</p> \n" + 
                        "               <p><b>Correo enviado por:</b> <br>\n" + 
                        "               "+correo.getUser()+ "</p> \n" + 
                        "          </div>\n" + 
                        "          <div id=\"error\" class=\"panel\">\n" + 
                        "              <p  style=\"text-align:center;\"><b>"+nombreAplicacion+"</b></p>\n" + 
                        "          </div>" + 
                        "       </div>\n" + 
                        "     </div>" + 
                        "   </body> \n" + 
                        "</html> ";
                        if(EnviarEmail.enviar(correo,correo.getUser(),correo.getEmisor(),"Conexion SMTP/"+nombreAplicacion+" exitosa",html,new ArrayList<InternetAddress>()))
                            response.getWriter().print("{\"success\":true,\"title\":\"Correo enviado exitosamente.\",\"msg\":\"\"}");
                        else
                            response.getWriter().print("{\"success\":false,\"title\":\"Error de autentificación.\",\"msg\":\"Las credenciales no son correctas.\"}");
                    }
                } 
            }catch (IOException | NumberFormatException e) {
                if(request.getParameter("modo")==null)
                    response.getWriter().print("{\"data\":[],\"total\":0}"); 
                else
                    response.getWriter().print("{\"errors\":\"Error al realizar la operacion.\"}");
            }
        }
    }
    private String fieldParse(String campo){
        switch (campo) {
            case "coreCorreoId":
                return "CORE_CORREO_ID";
            case "smtpServer":
                return "SMTP_SERVER";
            case "smtpPort":
                return "SMTP_PORT";
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
