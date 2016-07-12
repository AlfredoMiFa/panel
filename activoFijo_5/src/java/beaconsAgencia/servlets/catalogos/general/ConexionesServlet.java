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
import beaconsAgencia.ejb.CoreConexionesDaoFacadeLocal;
import beaconsAgencia.entities.CoreConexionesDao;
import coreapp.clases.general.FCom;
import beaconsAgencia.ejb.CoreParametrosGeneralesFacadeLocal;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "ConexionesServlet", urlPatterns = {"/catalogos/generals/conexiones.do"})
public class ConexionesServlet extends HttpServlet {
    @EJB
    CoreConexionesDaoFacadeLocal coreConexionesEJBLocal;
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
                    String sort="CORE_CONEXIONES_DAO_ID"; 
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
                    response.getWriter().print(coreConexionesEJBLocal.select(rows,page,sort,order,filter));
                }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("coreConexionesDaoId");
                    CoreConexionesDao conexion=new CoreConexionesDao();
                    if(modo.equals("eliminar")) {
                        coreConexionesEJBLocal.remove(coreConexionesEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(id!=null && !id.equals(""))
                            conexion=coreConexionesEJBLocal.find(Integer.parseInt(id));
                        else
                            conexion.setCoreConexionesDaoId(null);
                        conexion.setNombre(request.getParameter("nombre").toUpperCase());
                        conexion.setDriver(request.getParameter("driver"));
                        conexion.setServidor(request.getParameter("servidor"));
                        conexion.setUsuario(request.getParameter("usuario"));
                        if(!request.getParameter("contrasena").equals("SAME")){
                            String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                            conexion.setContrasena(FCom.encrypt(request.getParameter("contrasena"),cadenaEncriptacion));
                        }
                        response.getWriter().print(coreConexionesEJBLocal.persistir(conexion,modo));                          
                    }else if(modo.equals("combo")) {
                        response.getWriter().print(coreConexionesEJBLocal.generaCombo());
                    }else if(modo.equals("probarConexion")) {
                        conexion=coreConexionesEJBLocal.find(Integer.parseInt(id));                        
                        Connection conn= null;
                        String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                        try{
                            Class.forName(conexion.getDriver());
                            try {
                                conn =  DriverManager.getConnection(conexion.getServidor(),conexion.getUsuario(),FCom.decrypt(conexion.getContrasena(),cadenaEncriptacion));
                                if(conn!=null)
                                    response.getWriter().print("{\"success\":true,\"title\":\"Conexión exitosa.\",\"msg\":\"\"}");
                                else
                                    response.getWriter().print("{\"success\":true,\"title\":\"Error de conexión.\",\"msg\":\"Credenciales incorrectas\"}");
                            }catch (Exception e) {                                
                                response.getWriter().print("{\"success\":false,\"title\":\"Error de conexión.\",\"msg\":\""+e.getMessage()+"\"}");
                            }finally{
                              if (conn != null) 
                                  conn.close();
                          }
                        }catch(Exception e1) {
                            response.getWriter().print("{\"success\":false,\"title\":\"Error de conexión.\",\"msg\":\""+e1.getMessage()+"\"}");
                        }
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
            case "coreConexionesDaoId":
                return "CORE_CONEXIONES_DAO_ID";
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
