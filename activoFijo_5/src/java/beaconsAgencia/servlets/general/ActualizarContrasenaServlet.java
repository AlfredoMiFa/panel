/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.general;

import java.io.IOException;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import coreapp.clases.general.FCom;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CoreParametrosGeneralesFacadeLocal;
import beaconsAgencia.ejb.CoreSegContrasenaFacadeLocal;
import beaconsAgencia.ejb.CoreUsuarioFacadeLocal;


/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "ActualizarContrasenaServlet", urlPatterns = {"/actualizarContrasena.do"})
public class ActualizarContrasenaServlet extends HttpServlet {
    @EJB
    CoreUsuarioFacadeLocal usuarioEJBLocal;
    @EJB
    CoreSegContrasenaFacadeLocal coreSegContrasenaEJBLocal;
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
        HttpSession session=request.getSession(true);
        String username=request.getParameter("logUsuario");
        String contrasena=request.getParameter("logContrasenaActual");
        String contrasenaNueva=request.getParameter("logContrasenaNueva");
        String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
        if(username!=null && contrasena!=null) {
            String validaContrasena=coreSegContrasenaEJBLocal.validaClave(username, contrasenaNueva, 1);
            if(validaContrasena.indexOf("{\"success\":true\"")>-1){
                username = FCom.injectionFree(username.toUpperCase());
                contrasena = FCom.injectionFree(contrasena);
                ObjetoSesion objetoSesion=usuarioEJBLocal.queryCoreUsuarioFindByUserContrasena(username,contrasena,cadenaEncriptacion);
                if(objetoSesion!=null) {
                   objetoSesion.getUsuario().setContrasena(FCom.encrypt(contrasenaNueva,cadenaEncriptacion));
                   objetoSesion.getUsuario().setVigencia(new Date());
                   if(usuarioEJBLocal.persistirUsuario(objetoSesion.getUsuario(), "editar").indexOf("{\"success\":true")>-1){
                        session.setAttribute(getServletContext().getInitParameter("vsi")+"SESION",objetoSesion);
                        response.sendRedirect(objetoSesion.getPerfilSelected().getHomePage());
                   }else                       
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }else 
                    response.sendRedirect("index.jsp?msg=La contraseña anterior no es correcta.");
            }else
              response.sendRedirect("index.jsp?msg="+validaContrasena.replaceAll("\"", ""));
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
