/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.general;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CoreDashboardFacadeLocal;
import beaconsAgencia.entities.CoreDashboard;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "EscritorioServlet", urlPatterns = {"/escritorio.view"})
public class EscritorioServlet extends HttpServlet {
    @EJB
    CoreDashboardFacadeLocal coreDashboardEJBLocal;

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
            throws ServletException, IOException {response.setContentType("text/html;charset=UTF-8");
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
            try {
                String modo=request.getParameter("modo");
                ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
                if(modo.equals("escritorio")) {
                    List<CoreDashboard> coreDashboardList=coreDashboardEJBLocal.getDashboardList(objetoSesion.getPerfilSelected().getCorePerfilId());
                    String html="";
                    String bloque="";
                    String arrayData="[";
                    for(CoreDashboard coreDashboard:coreDashboardList){
                        String bloqueContenido="";
                        if(!coreDashboard.getBloque().equals(bloque))
                        {
                            bloque=coreDashboard.getBloque();
                            if(html.equals(""))
                                bloqueContenido="<div class='row'>";
                            else
                                bloqueContenido="</div><div class='row'>";
                        }
                        html+=bloqueContenido;
                        if(coreDashboard.getColumnas().equals("1"))
                            html+="<div class='col-lg-12'>";                        
                        else if(coreDashboard.getColumnas().equals("2"))
                            html+="<div class='col-lg-6'>";                      
                        else if(coreDashboard.getColumnas().equals("3"))
                            html+="<div class='col-lg-4'>";  
                        if(coreDashboard.getTipo().equals("TEXTO")){
                            html+=coreDashboard.getContenido();
                        }else{
                             html+="<div class='panel panel-default'>"+
                                "<div class='panel-heading'>"+
                                    "<h3 class='panel-title'>"+(coreDashboard.getTitulo()!=null?coreDashboard.getTitulo():"")+"</h3>"+
                                "</div>"+
                                "<div class='panel-body'>";
                                if(coreDashboard.getTipo().equals("GRAFICA")){                                    
                                    String[] aparametros=coreDashboard.getParametros().split("\\|");
                                    String alto=aparametros[6].split(":")[1]; 
                                    html+="<div id='GRA"+coreDashboard.getCoreDashboard()+"' style='height:"+alto+"px;'></div>";
                                    arrayData+="{\"tipo\":\"GRA\",\"id\":"+coreDashboard.getCoreDashboard()+",\"cveDatasource\":\""+coreDashboard.getCveDatasource().getCveDatasource()+"\",\"parametros\":\""+coreDashboard.getParametros()+"\"},";
                                }else{
                                    html+="<div id='TAB"+coreDashboard.getCoreDashboard()+"'></div>";
                                    arrayData+="{\"tipo\":\"TAB\",\"id\":"+coreDashboard.getCoreDashboard()+",\"cveDatasource\":\""+coreDashboard.getCveDatasource().getCveDatasource()+"\",\"parametros\":\"\"},";
                                }
                             html+="</div>"+
                            "</div>";
                        }           
                        html+="</div>";
                    }
                    if (!arrayData.equals(""))
                        arrayData = arrayData.substring(0, arrayData.length() - 1);
                    arrayData+="]";
                    if(!html.equals(""))
                        html+="</div><script>var arrayData="+arrayData+";</script>";
                    response.getWriter().print(html);                          
                }
            }catch (IOException | NumberFormatException e) {
                if(request.getParameter("modo")==null)
                    response.getWriter().print("{\"data\":[],\"total\":0}"); 
                else
                    response.getWriter().print("{\"errors\":\"Error al realizar la operacion...\"}");
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
