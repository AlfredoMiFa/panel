/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import coreapp.clases.general.FCom;
import beaconsAgencia.ejb.CoreDashboardFacadeLocal;
import beaconsAgencia.ejb.CoreDatasourceFacadeLocal;
import beaconsAgencia.ejb.CorePerfilFacadeLocal;
import beaconsAgencia.entities.CoreDashboard;
import java.io.IOException;
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
@WebServlet(name = "DashboardServlet", urlPatterns = {"/catalogos/generals/dashboard.do"})
public class DashboardServlet extends HttpServlet {
    @EJB
    CoreDatasourceFacadeLocal coreDatasourceEJBLocal;
    @EJB
    CoreDashboardFacadeLocal coreDashboardEJBLocal;
    @EJB
    CorePerfilFacadeLocal corePerfilEJBLocal;

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
                    String sort="CORE_DASHBOARD_ID"; 
                    String order="asc"; 
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
                                filter += "("+filterToken(request.getParameter("filter[filters]["+i+"][filters][0][operator]"),request.getParameter("filter[filters]["+i+"][filters][0][field]"),request.getParameter("filter[filters]["+i+"][filters][0][value]"))+" "+request.getParameter("filter[filters]["+i+"][logic]")+" "+
                                        filterToken(request.getParameter("filter[filters]["+i+"][filters][1][operator]"),request.getParameter("filter[filters]["+i+"][filters][1][field]"),request.getParameter("filter[filters]["+i+"][filters][1][value]"))+")";
                            else
                                filter += filterToken(request.getParameter("filter[filters]["+i+"][operator]"),request.getParameter("filter[filters]["+i+"][field]"),request.getParameter("filter[filters]["+i+"][value]"));

                            if(i<mFiltroDoble.length-1)
                                filter += " "+request.getParameter("filter[logic]")+" ";  
                        }
                    }
                    response.getWriter().print(coreDashboardEJBLocal.select(rows,page,sort,order,filter));
                }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("coreDashboardId");
                    CoreDashboard coreDashboard=new CoreDashboard();
                    if(modo.equals("eliminar")) {
                        coreDashboardEJBLocal.remove(coreDashboardEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(modo.equals("editar"))
                            coreDashboard=coreDashboardEJBLocal.find(Integer.parseInt(id));
                        coreDashboard.setCorePerfilId(corePerfilEJBLocal.find(Integer.parseInt(request.getParameter("corePerfilId[id]"))));
                        coreDashboard.setCveDatasource(coreDatasourceEJBLocal.find(request.getParameter("cveDatasource[id]")));
                        coreDashboard.setBloque(request.getParameter("bloque"));
                        coreDashboard.setColumnas(request.getParameter("columnas"));
                        coreDashboard.setTitulo(request.getParameter("titulo"));
                        if(request.getParameter("contenido")!=null)
                            coreDashboard.setContenido(request.getParameter("contenido").replaceAll("\"", "'"));                        
                        coreDashboard.setParametros(request.getParameter("parametros"));
                        coreDashboard.setTipo(request.getParameter("tipo"));
                        coreDashboard.setActivo(Boolean.parseBoolean(request.getParameter("activo")));
                        response.getWriter().print(coreDashboardEJBLocal.persistir(coreDashboard,modo));                          
                    }
                } 
            }catch (IOException | NumberFormatException e) {
                if(request.getParameter("modo")==null)
                    response.getWriter().print("{\"data\":[],\"total\":0}"); 
                else
                    response.getWriter().print("{\"errors\":\"Error al realizar la operacion...\"}");
            }
        }
    }
    private String filterToken(String operator,String campo,String value){
        String campoparse=fieldParse(campo);
        switch (campo) {
            case "corePerfilId.text":
                return " CORE_PERFIl_ID IN (SELECT o.CORE_PERFIl_ID FROM core_perfil as o where "+FCom.ToFilterOperator(operator,campoparse,value)+")";
            default:
                return FCom.ToFilterOperator(operator,campoparse,value);
        }
    }
    private String fieldParse(String campo){
        switch (campo) {
            case "coreDashboardId":
                return "CORE_DASHBOARD_ID";
            case "corePerfilId.text":
                return "o.PERFIL";
            case "cveDatasource.text":
                return "CVE_DATASOURCE";
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
