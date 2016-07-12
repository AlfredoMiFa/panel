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
import coreapp.clases.general.Arbol;
import coreapp.clases.general.Nodo;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.entities.CoreDerecho;
import beaconsAgencia.entities.CorePerfil;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "MenuServlet", urlPatterns = {"/menu.do"})
public class MenuServlet extends HttpServlet {

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
        if(session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")==null)
        {
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                response.getWriter().print("{\"errors\":\"901\"}");
            else
                response.getWriter().print("Error de carga");
        }else{
            ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
            CorePerfil corePerfil;
            if(objetoSesion.isSuperAdministrador())
                corePerfil=objetoSesion.getSuperAdmin();
            else
                corePerfil=objetoSesion.getPerfilSelected();
            if(corePerfil!=null) {
                Arbol arbol=new Arbol(new Nodo(-1,-2,"MENU",null));
                for(CoreDerecho derecho:corePerfil.getCoreDerechoList()){
                    if(derecho.getFkPadre()!=null)
                        arbol.agregarNodo(new Nodo(derecho.getCoreDerechoId(),derecho.getFkPadre().getCoreDerechoId(),derecho.getNombreMenu(),derecho));
                    else
                        arbol.agregarNodo(new Nodo(derecho.getCoreDerechoId(),-1,derecho.getNombreMenu(),derecho));
                }
                String html = "<div class='collapse navbar-collapse navbar-ex1-collapse'><ul class='nav navbar-nav side-nav'>";
                String activate="";
                String selected="";
                if(request.getParameter("M_ACTIVE")!=null)
                    activate=request.getParameter("M_ACTIVE");
                if(request.getParameter("M_SELECT")!=null)
                    selected=request.getParameter("M_SELECT");
                for (int i = 1; i < arbol.getListaNodos().size(); i++) {
                    if(((CoreDerecho)arbol.getListaNodos().get(i).getObjeto()).getEsVisible().equals("S"))
                    {
                        String classAct="";
                        String classSel="";
                        String icono="";
                        Nodo nodoTemp = new Nodo(arbol.getListaNodos().get(i));
                        if(((CoreDerecho)nodoTemp.getObjeto()).getIcon()!=null && !((CoreDerecho)nodoTemp.getObjeto()).getIcon().equals(""))
                            icono="<i class='fa fa-fw "+((CoreDerecho)nodoTemp.getObjeto()).getIcon()+"'></i> ";
                        if (nodoTemp.isNodoHoja()) {
                            if(selected.indexOf(((CoreDerecho)nodoTemp.getObjeto()).getDerecho())>-1)
                                classSel=" class='active'";
                            int ultimoHijo = 
                                arbol.getListaNodos().get(arbol.posicionNodo(nodoTemp.getPadre())).getUltimoHijo();
                            if (ultimoHijo == nodoTemp.getId()){
                                if(!((CoreDerecho)nodoTemp.getObjeto()).getEsEnlace().equals("S"))
                                    html += "<li>"+icono + nodoTemp.getTitulo()+ 
                                        "</li>" + arbol.cerrarRama(nodoTemp, "", false); 
                                else
                                    html += "<li"+classSel+"><a href='"+request.getContextPath()+"/"+((CoreDerecho)nodoTemp.getObjeto()).getRutaMenu()+"' target='_self'>"+icono + nodoTemp.getTitulo() + 
                                        "</a></li>" + arbol.cerrarRama(nodoTemp, "", false); 
                            }
                            else{
                                if(!((CoreDerecho)nodoTemp.getObjeto()).getEsEnlace().equals("S"))
                                    html += "<li>"+icono + nodoTemp.getTitulo()+"</li>";
                                else
                                    html += "<div"+classSel+"><a href='"+request.getContextPath()+"/"+((CoreDerecho)nodoTemp.getObjeto()).getRutaMenu()+"' target='_self'>" +icono+ nodoTemp.getTitulo()+ 
                                        "</a></div>";
                                
                            }
                        } else{
                            if(activate.indexOf(((CoreDerecho)nodoTemp.getObjeto()).getDerecho())<0)
                                classAct=" class='collapse' aria-expanded='false'";
                            else
                                classAct=" class='collapse in' aria-expanded='true'";                            
                            html += "<li><a href='javascript:;' data-toggle='collapse' data-target='#M"+((CoreDerecho)nodoTemp.getObjeto()).getDerecho()+"' >"+icono+ nodoTemp.getTitulo()+" <i class='fa fa-fw fa-caret-down'></i></a>"+
                                    "<ul id='M"+((CoreDerecho)nodoTemp.getObjeto()).getDerecho()+"' "+classAct+">";
                        }
                    }
                }//For
                html += "</ul></div>";
                response.getWriter().println(html);
                
            }else
                response.getWriter().print("<div class='collapse navbar-collapse navbar-ex1-collapse'><ul class='nav navbar-nav side-nav'></ul></div>");
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
