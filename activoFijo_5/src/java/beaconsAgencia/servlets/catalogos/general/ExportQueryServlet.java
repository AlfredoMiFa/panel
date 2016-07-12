/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import coreapp.clases.general.FCom;
import beaconsAgencia.ejb.CoreConexionesDaoFacadeLocal;
import beaconsAgencia.ejb.CoreExportQueryFacadeLocal;
import beaconsAgencia.ejb.CoreParametrosGeneralesFacadeLocal;
import beaconsAgencia.entities.CoreConexionesDao;
import beaconsAgencia.entities.CoreExportQuery;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
@WebServlet(name = "ExportQueryServlet", urlPatterns = {"/catalogos/generals/exportQuery.do"})
public class ExportQueryServlet extends HttpServlet {
    @EJB
    CoreConexionesDaoFacadeLocal coreConexionesEJBLocal;
    @EJB
    CoreExportQueryFacadeLocal coreExportQuerysEJBLocal;
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
                    String sort="CVE_EXPORT_QUERY"; 
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
                                filter += "("+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][0][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][0][field]")),request.getParameter("filter[filters]["+i+"][filters][0][value]"))
                                        +" "+request.getParameter("filter[filters]["+i+"][logic]")+" "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][1][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][1][field]")),request.getParameter("filter[filters]["+i+"][filters][1][value]"))+")";
                            else
                                filter += FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));

                            if(i<mFiltroDoble.length-1)
                                filter += " "+request.getParameter("filter[logic]")+" ";  
                        }
                    }
                    response.getWriter().print(coreExportQuerysEJBLocal.select(rows,page,sort,order,filter));
                }else {
                    String modo=request.getParameter("modo");
                    String clave=request.getParameter("cveExportQuery");
                    CoreExportQuery coreExportQuery=new CoreExportQuery();
                    if(modo.equals("eliminar")) {
                        coreExportQuerysEJBLocal.remove(coreExportQuerysEJBLocal.find(clave));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(modo.equals("editar"))
                            coreExportQuery=coreExportQuerysEJBLocal.find(clave);
                        else
                            coreExportQuery.setCveExportQuery(clave);
                        coreExportQuery.setAppendFilter(Boolean.parseBoolean(request.getParameter("appendFilter")));
                        coreExportQuery.setCampos(request.getParameter("campos"));
                            if(request.getParameter("conexionDao[id]")!=null)
                                coreExportQuery.setConexionDao(Integer.parseInt(request.getParameter("conexionDao[id]")));
                            else
                                coreExportQuery.setConexionDao(Integer.parseInt(request.getParameter("conexionDao")));
                        coreExportQuery.setEstatus(request.getParameter("estatus"));
                        coreExportQuery.setLimitc(request.getParameter("limitc"));
                        coreExportQuery.setOrderc(request.getParameter("orderc"));
                        coreExportQuery.setOrderBy(request.getParameter("orderBy"));
                        coreExportQuery.setTablas(request.getParameter("tablas"));
                        coreExportQuery.setWherec(request.getParameter("wherec"));
                        coreExportQuery.setHeader(request.getParameter("header"));
                        response.getWriter().print(coreExportQuerysEJBLocal.persistir(coreExportQuery,modo));                          
                    }else if(modo.equals("probarQuery")){
                        Connection conn= null;
                        Statement st=null;
                        ResultSet rs = null;
                        try{
                            coreExportQuery=coreExportQuerysEJBLocal.find(clave);
                            String query=request.getParameter("query");
                            if(coreExportQuery!=null){
                                CoreConexionesDao conexionDB=coreConexionesEJBLocal.find(coreExportQuery.getConexionDao());
                                try{
                                    String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                                    List<Object[]> data=new ArrayList< >();
                                    String html="<table id='grid2'>";
                                    conn =  DriverManager.getConnection(conexionDB.getServidor(),conexionDB.getUsuario(),FCom.decrypt(conexionDB.getContrasena(),cadenaEncriptacion));
                                    st = conn.createStatement();
                                    rs=st.executeQuery(query);
                                    ResultSetMetaData metaData = rs.getMetaData();
                                    int count = metaData.getColumnCount();
                                    html+="<colgroup>"; 
                                    for (int i = 1; i <= count; i++)
                                        html+="<col style='width:110px;'/>";
                                    html+="</colgroup>"; 
                                    html+="<thead><tr>"; 
                                    for (int i = 1; i <= count; i++)
                                        html+="<th data-field='"+metaData.getColumnName(i)+"'>"+metaData.getColumnName(i)+"</th>";
                                    html+="</tr></thead>"; 
                                    html+="<tbody>"; 
                                    while(rs.next()){
                                        html+="<tr>"; 
                                        for (int i = 1; i <= count; i++)
                                            html+="<td>"+rs.getString(i)+"</td>";
                                        html+="</tr>"; 
                                    }          
                                    html+="</tbody>"; 
                                    html+="</table>";                               
                                    response.getWriter().print("{\"data\":\""+html+"\"}");
                                }catch (SQLException e) {                                    
                                    response.getWriter().print("{\"errors\":\"Error al realizar la operacion.\"}");
                                }finally{
                                    if (st != null)
                                        st.close();
                                    if (conn != null) 
                                        conn.close();
                                }
                            }
                        }catch(Exception e1) {
                            log(e1.getMessage());
                            response.getWriter().print("{\"errors\":\"Error al realizar la operacion..\"}");
                        }
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
    private String fieldParse(String campo){
        switch (campo) {
            case "cveExportQuery":
                return "CVE_EXPORT_QUERY";
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
