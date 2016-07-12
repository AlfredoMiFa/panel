/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import coreapp.clases.general.FCom;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CoreConexionesDaoFacadeLocal;
import beaconsAgencia.ejb.CoreDatasourceFacadeLocal;
import beaconsAgencia.ejb.CoreParametrosGeneralesFacadeLocal;
import beaconsAgencia.ejb.CoreReglaSeguridadDetalleFacadeLocal;
import beaconsAgencia.entities.CoreConexionesDao;
import beaconsAgencia.entities.CoreDatasource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
@WebServlet(name = "DatasourceServlet", urlPatterns = {"/catalogos/generals/datasource.do"})
public class DatasourceServlet extends HttpServlet {
    @EJB
    CoreConexionesDaoFacadeLocal coreConexionesEJBLocal;
    @EJB
    CoreDatasourceFacadeLocal coreDatasourceEJBLocal;
    @EJB
    CoreParametrosGeneralesFacadeLocal coreParametrosGeneralesEJBLocal;
    @EJB
    CoreReglaSeguridadDetalleFacadeLocal reglaSeguridadDetalleEJBLocal;


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
                    String sort="CVE_DATASOURCE"; 
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
                    response.getWriter().print(coreDatasourceEJBLocal.select(rows,page,sort,order,filter));
                }else {
                    String modo=request.getParameter("modo");
                    String clave=request.getParameter("cveDatasource");
                    CoreDatasource coreDatasource=new CoreDatasource();
                    ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(session.getServletContext().getInitParameter("vsi")+"SESION");  
                    if(modo.equals("eliminar")) {
                        coreDatasourceEJBLocal.remove(coreDatasourceEJBLocal.find(clave));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(modo.equals("editar"))
                            coreDatasource=coreDatasourceEJBLocal.find(clave);
                        else
                            coreDatasource.setCveDatasource(clave);
                        coreDatasource.setMetaTag(request.getParameter("metaTag"));
                        coreDatasource.setCampos(request.getParameter("campos"));
                            if(request.getParameter("conexionDao[id]")!=null)
                                coreDatasource.setConexionDao(Integer.parseInt(request.getParameter("conexionDao[id]")));
                            else
                                coreDatasource.setConexionDao(Integer.parseInt(request.getParameter("conexionDao")));
                        coreDatasource.setLimitc(request.getParameter("limitc"));
                        coreDatasource.setOrderc(request.getParameter("orderc"));
                        coreDatasource.setOrderBy(request.getParameter("orderBy"));
                        coreDatasource.setTablas(request.getParameter("tablas"));
                        coreDatasource.setWherec(request.getParameter("wherec"));
                        coreDatasource.setGroupBy(request.getParameter("groupBy"));
                        response.getWriter().print(coreDatasourceEJBLocal.persistir(coreDatasource,modo));                          
                    }else if(modo.equals("combo")){                        
                        response.getWriter().print(coreDatasourceEJBLocal.generaCombo()); 
                    }else if(modo.equals("probarQuery")){
                        Connection conn= null;
                        Statement st=null;
                        ResultSet rs = null;
                        try{
                            coreDatasource=coreDatasourceEJBLocal.find(clave);
                            if(coreDatasource!=null){
                                String query=request.getParameter("query");
                                if(query==null)
                                {
                                    String filter="";
                                    if(coreDatasource.getWherec()!=null)
                                    {
                                        if(!coreDatasource.getWherec().trim().equals(""))
                                            filter=" WHERE "+coreDatasource.getWherec();
                                    }
                                    if(coreDatasource.getMetaTag().indexOf("[USUARIO]")>-1){
                                        if(filter.equals(""))
                                            filter=" WHERE CORE_USUARIO_ID="+objetoSesion.getUsuario().getCoreUsuarioId();
                                        else
                                            filter=" AND CORE_USUARIO_ID="+objetoSesion.getUsuario().getCoreUsuarioId();
                                    }
                                    if(coreDatasource.getMetaTag().indexOf("[PERFIL]")>-1){
                                        if(filter.equals(""))
                                            filter=" WHERE CORE_PERFIL_ID="+objetoSesion.getUsuario().getCoreUsuarioId(); 
                                        else
                                            filter=" AND CORE_USUARIO_ID="+objetoSesion.getUsuario().getCoreUsuarioId();                                       
                                    }
                                    if(coreDatasource.getMetaTag().indexOf("[REGLA[")>-1 && coreDatasource.getMetaTag().indexOf("]]")>-1){
                                        String ids=coreDatasource.getMetaTag().substring(coreDatasource.getMetaTag().indexOf("[REGLA[")+7,coreDatasource.getMetaTag().indexOf("]]")-2);
                                        if(filter.equals(""))
                                            filter= " WHERE "+reglaSeguridadDetalleEJBLocal.getFiltroSeguridad(ids); 
                                        else
                                            filter= " AND "+reglaSeguridadDetalleEJBLocal.getFiltroSeguridad(ids);                                      
                                    }
                                    query="SELECT "+coreDatasource.getCampos()+" FROM "+coreDatasource.getTablas()+filter+
                                    (coreDatasource.getOrderBy()!=null?(!coreDatasource.getOrderBy().trim().equals("")?" ORDER BY "+coreDatasource.getOrderBy():""):"")+(coreDatasource.getOrderc()!=null?(!coreDatasource.getOrderc().trim().equals("")?" "+coreDatasource.getOrderc():""):"")+
                                    (coreDatasource.getGroupBy()!=null?(!coreDatasource.getGroupBy().trim().equals("")?" GROUP BY "+coreDatasource.getGroupBy():""):"")+(coreDatasource.getLimitc()!=null?(!coreDatasource.getLimitc().trim().equals("")?" limit "+coreDatasource.getLimitc():" limit 1000"):"");
                                }
                                CoreConexionesDao conexionDB=coreConexionesEJBLocal.find(coreDatasource.getConexionDao());
                                try{
                                    String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                                    String html="<table id='grid2'>";
                                    if(request.getParameter("IDC")!=null)
                                        html="<table id='gridTAB"+request.getParameter("IDC")+"'>";
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
                    }else if(modo.equals("graficar")){
                        Connection conn= null;
                        Statement st=null;
                        ResultSet rs = null;
                        try{
                            coreDatasource=coreDatasourceEJBLocal.find(clave);
                            if(coreDatasource!=null){
                                String query=request.getParameter("query");
                                if(query==null)
                                {
                                    String filter="";
                                    if(coreDatasource.getWherec()!=null)
                                    {
                                        if(!coreDatasource.getWherec().trim().equals(""))
                                            filter=" WHERE "+coreDatasource.getWherec();
                                    }
                                    if(coreDatasource.getMetaTag().indexOf("[USUARIO]")>-1){
                                        if(filter.equals(""))
                                            filter=" WHERE CORE_USUARIO_ID="+objetoSesion.getUsuario().getCoreUsuarioId();
                                        else
                                            filter=" AND CORE_USUARIO_ID="+objetoSesion.getUsuario().getCoreUsuarioId();
                                    }
                                    if(coreDatasource.getMetaTag().indexOf("[PERFIL]")>-1){
                                        if(filter.equals(""))
                                            filter=" WHERE CORE_PERFIL_ID="+objetoSesion.getUsuario().getCoreUsuarioId(); 
                                        else
                                            filter=" AND CORE_USUARIO_ID="+objetoSesion.getUsuario().getCoreUsuarioId();                                       
                                    }
                                    if(coreDatasource.getMetaTag().indexOf("[REGLA[")>-1 && coreDatasource.getMetaTag().indexOf("]]")>-1){
                                        String ids=coreDatasource.getMetaTag().substring(coreDatasource.getMetaTag().indexOf("[REGLA[")+7,coreDatasource.getMetaTag().indexOf("]]"));
                                        if(filter.equals(""))
                                            filter= " WHERE "+reglaSeguridadDetalleEJBLocal.getFiltroSeguridad(ids); 
                                        else
                                            filter= " AND "+reglaSeguridadDetalleEJBLocal.getFiltroSeguridad(ids);                                      
                                    }
                                    query="SELECT "+coreDatasource.getCampos()+" FROM "+coreDatasource.getTablas()+filter+
                                    (coreDatasource.getOrderBy()!=null?(!coreDatasource.getOrderBy().trim().equals("")?" ORDER BY "+coreDatasource.getOrderBy():""):"")+(coreDatasource.getOrderc()!=null?(!coreDatasource.getOrderc().trim().equals("")?" "+coreDatasource.getOrderc():""):"")+
                                    (coreDatasource.getGroupBy()!=null?(!coreDatasource.getGroupBy().trim().equals("")?" GROUP BY "+coreDatasource.getGroupBy():""):"")+(coreDatasource.getLimitc()!=null?(!coreDatasource.getLimitc().trim().equals("")?" limit "+coreDatasource.getLimitc():" limit 1000"):"");
                                }
                                CoreConexionesDao conexionDB=coreConexionesEJBLocal.find(coreDatasource.getConexionDao());
                                try{
                                    String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                                    String json="";
                                    conn =  DriverManager.getConnection(conexionDB.getServidor(),conexionDB.getUsuario(),FCom.decrypt(conexionDB.getContrasena(),cadenaEncriptacion));
                                    st = conn.createStatement();
                                    rs=st.executeQuery(query);
                                    ResultSetMetaData metaData = rs.getMetaData();
                                    int count = metaData.getColumnCount();
                                    int rows=0;
                                    while(rs.next()){
                                        String json2="{";
                                        for (int i = 1; i <= count; i++){
                                            String type = metaData.getColumnClassName(i);
                                            if (type.equals("java.lang.Long") || type.equals("java.lang.Integer") || type.equals("java.lang.Double")) 
                                                json2+="\""+metaData.getColumnName(i)+"\":"+rs.getString(i)+",";
                                            else
                                                json2+="\""+metaData.getColumnName(i)+"\":\""+rs.getString(i)+"\",";
                                        }
                                        json2=json2.substring(0, json2.length() - 1)+"}"; 
                                        json+=json2+",";
                                        rows++;
                                    }  
                                    if (rows > 0)
                                        json=json.substring(0, json.length() - 1); 
                                    String parametros=request.getParameter("parametros");
                                    String[] aparametros=parametros.split("\\|");
                                    String categoria=aparametros[0].split(":")[1]; 
                                    String field=aparametros[1].split(":")[1]; 
                                    String group=aparametros[2].split(":")[1];
                                    String sort=aparametros[3].split(":")[1];  
                                    String tipo=aparametros[4].split(":")[1]; 
                                    String titulo=aparametros[5].split(":")[1]; 
                                    response.getWriter().print("{\"data\":["+json+"],\"success\":true,\"sort\":\""+sort+"\",\"categoria\":\""+categoria+"\",\"group\":\""+group+"\",\"field\":\""+field+"\",\"tipoGrafica\":\""+tipo+"\",\"titulo\":\""+titulo+"\",\"count\":"+rows+"}"); 
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
    private String filterToken(String operator,String campo,String value){
        String campoparse=fieldParse(campo);
        switch (campo) {
           default:
                return FCom.ToFilterOperator(operator,campoparse,value);
        }
    }
    private String fieldParse(String campo){
        switch (campo) {
            case "cveDatasource":
                return "CVE_DATASOURCE";
            case "orderBy":
                campo= "ORDER_BY";
            case "groupBy":
                campo= "GROUP_BY";
            case "userFilter":
                campo= "USER_FILTER";
            case "roleFilter":
                campo= "ROLE_FILTER";
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
