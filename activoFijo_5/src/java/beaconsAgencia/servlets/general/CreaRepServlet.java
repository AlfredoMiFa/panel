/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.general;

import coreapp.clases.general.FCom;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beaconsAgencia.ejb.CoreConexionesDaoFacadeLocal;
import beaconsAgencia.ejb.CoreParametrosGeneralesFacadeLocal;
import beaconsAgencia.entities.CoreConexionesDao;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "CreaRepServlet", urlPatterns = {"/creaReporte.view"})
public class CreaRepServlet extends HttpServlet {
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
        response.setContentType("text/html;charset=UTF-8");if(request.getParameter("id")!=null) {
             String modo=request.getParameter("modo");
              Map parameters = new HashMap();          
              if(request.getParameter("nomParametros")!=null && request.getParameter("valParametros")!=null) {
                  String[] anomParametros=request.getParameter("nomParametros").split(",");
                  String[] avalParametros=request.getParameter("valParametros").split(",");
                  if(anomParametros.length==avalParametros.length)
                  {
                      for (int i=0;i<anomParametros.length;i++) 
                          parameters.put(anomParametros[i], avalParametros[i]);
                  }     
                  parameters.put(JRParameter.REPORT_LOCALE, new Locale("es","MX"));
              }
              Connection conn= null;
              ResultSet rs = null;
              Statement st = null;
              try{
                  CoreConexionesDao conexionDB=coreConexionesEJBLocal.getConexion("DB_CONF");
                  String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                  Class.forName(conexionDB.getDriver());
                  try {
                      String sql="select DATA  from CORE_REPORTE_JASPER where ESTATUS='AC' AND CORE_REPORTE_JASPER_ID="+request.getParameter("id");
                      conn =  DriverManager.getConnection(conexionDB.getServidor(),conexionDB.getUsuario(),FCom.decrypt(conexionDB.getContrasena(),cadenaEncriptacion));
                      st = conn.createStatement();
                      rs = st.executeQuery(sql); 
                      InputStream inputStream=null;
                      if(rs.next())            
                          inputStream=(rs.getBinaryStream(1));
                      if(inputStream!=null) { 
                          if(modo!=null && modo.equals("stream")){                              
                            int tamanoInput = inputStream.available();
                            byte[] datosArc = new byte[tamanoInput];
                            inputStream.read(datosArc, 0, tamanoInput);
                            response.getOutputStream().write(datosArc);
                          }else{
                            response.setContentType("application/pdf");
                            JasperPrint jasperPrint =JasperFillManager.fillReport(inputStream,parameters,conn );
                            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
                          }
                      }
                  }catch (JRException e) { 
                      throw e;
                  } catch (SQLException e) {
                      throw e;
                  }catch (Exception e) {
                      throw e;
                  }finally{
                    if (rs != null)
                        rs.close();
                    if (st != null)
                        st.close();
                    if (conn != null) 
                        conn.close();
                }
              }catch(Exception e1) {
                  System.out.println(e1.getMessage());
              }
          }else {
              response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
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
