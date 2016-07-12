/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import coreapp.clases.general.FCom;
import beaconsAgencia.ejb.CoreSegContrasenaFacadeLocal;
import beaconsAgencia.entities.CoreSegContrasena;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "SegContrasenaServlet", urlPatterns = {"/catalogos/generals/reglasContrasena.do"})
public class SegContrasenaServlet extends HttpServlet {
    @EJB
    CoreSegContrasenaFacadeLocal coreSegContrasenaEJBLocal;

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
            
            try {
                if(request.getParameter("modo")==null) {
                    int rows=10;
                    int page=1;   
                    String sort="CORE_SEG_CONTRASENA_ID"; 
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
                    response.getWriter().print(coreSegContrasenaEJBLocal.select(rows,page,sort,order,filter));
                }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("coreSegContrasenaId");
                    CoreSegContrasena contrasenaSegura=new CoreSegContrasena();
                    if(modo.equals("eliminar")) {
                        coreSegContrasenaEJBLocal.remove(coreSegContrasenaEJBLocal.find(id));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("find")) {
                        response.getWriter().print(coreSegContrasenaEJBLocal.find(1).toString());
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(!id.equals(""))
                            contrasenaSegura.setCoreSegContrasenaId(Integer.parseInt(id.toUpperCase()));
                        else
                            contrasenaSegura.setCoreSegContrasenaId(null);
                        contrasenaSegura.setLongcontrasena(Integer.parseInt(request.getParameter("longitudMinimaContrasena").toUpperCase()));
                        contrasenaSegura.setLongusuario(Integer.parseInt(request.getParameter("longitudMinimaUsuario")));
                        if(request.getParameter("chkCaracterEspecial")!=null)
                            contrasenaSegura.setReccar(true);
                        else
                           contrasenaSegura.setReccar(false); 
                        if(request.getParameter("chkNumero")!=null)
                            contrasenaSegura.setRecnum(true);
                        else
                           contrasenaSegura.setRecnum(false);
                        contrasenaSegura.setVigencia(Integer.parseInt(request.getParameter("diasVigencia")));
                        response.getWriter().print(coreSegContrasenaEJBLocal.persistir(contrasenaSegura,modo));                          
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
            case "tipoDato":
                return "TIPO_DATO";
            case "esBorrable":
                return "ES_BORRABLE";
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
