/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.general;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beaconsAgencia.clases.general.ObjetoSesion;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "GeneralServlet", urlPatterns = {"/general.do"})
public class GeneralServlet extends HttpServlet {

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
        }else{
            String modo=request.getParameter("modo");
            ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
            if(modo.equals("perfilesUsuario")){
                if(objetoSesion.getUsuario().getCorePerfilList().size()>0)
                {
                    String lista="[";
                    if(objetoSesion.getUsuario().getEsSuper().equals("S"))
                        lista+="{\"text\":\"SUPER\",\"value\":0},";
                    for(int i=0;i<objetoSesion.getUsuario().getCorePerfilList().size();i++)
                        lista+="{\"text\":\""+objetoSesion.getUsuario().getCorePerfilList().get(i).getPerfil()+"\",\"value\":"+objetoSesion.getUsuario().getCorePerfilList().get(i).getCorePerfilId()+"},";
                    lista=lista.substring(0,lista.length()-1);
                    lista+="]";
                    response.getWriter().print(lista);
                }else{
                    if(objetoSesion.getUsuario().getEsSuper().equals("S"))
                        response.getWriter().print("[{\"text\":\"SUPER\",\"value\":0}]");
                    else
                        response.getWriter().print("[]");
                }
            }else if(modo.equals("setPerfil")){
                if(request.getParameter("userPerfil").equals("0")){
                    objetoSesion.setSuperAdministrador(true);
                    session.setAttribute(getServletContext().getInitParameter("vsi")+"SESION",objetoSesion);
                    response.getWriter().print("{\"success\":true}");
                }else{
                    objetoSesion.setSuperAdministrador(false);
                    for(int i=0;i<objetoSesion.getUsuario().getCorePerfilList().size();i++){
                        if(request.getParameter("userPerfil").equals(objetoSesion.getUsuario().getCorePerfilList().get(i).getCorePerfilId().toString())){
                            objetoSesion.setPerfilSelected(objetoSesion.getUsuario().getCorePerfilList().get(i));
                            session.setAttribute(getServletContext().getInitParameter("vsi")+"SESION",objetoSesion);
                            response.getWriter().print("{\"success\":true}");
                            break;
                        }
                    }
                }
            }
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
