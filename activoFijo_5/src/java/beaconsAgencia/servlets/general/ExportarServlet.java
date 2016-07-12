/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.general;

import coreapp.clases.general.FCom;
import beaconsAgencia.ejb.CoreConexionesDaoFacadeLocal;
import beaconsAgencia.ejb.CoreExportQueryFacadeLocal;
import beaconsAgencia.ejb.CoreParametrosGeneralesFacadeLocal;
import beaconsAgencia.entities.CoreConexionesDao;
import beaconsAgencia.entities.CoreExportQuery;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "ExportarServlet", urlPatterns = {"/generals/exportar.do"})
public class ExportarServlet extends HttpServlet {
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
            String modo=request.getParameter("modo");
            if(modo!=null){
                Connection conn= null;
                Statement st=null;
                ResultSet rs = null;
                try{
                    CoreExportQuery coreExportQuery=coreExportQuerysEJBLocal.find(modo);
                    if(coreExportQuery!=null){
                        String tipo=request.getParameter("cmbTipoTool");
                        if(tipo==null)
                            tipo="XLSX";
                        String filtro=request.getParameter("filtro");
                        String query=request.getParameter("querystring");
                        String cadenaEncriptacion=coreParametrosGeneralesEJBLocal.getValor("SEC_ENCRYP");
                        CoreConexionesDao conexionDB=coreConexionesEJBLocal.find(coreExportQuery.getConexionDao());
                        try{
                            if(!coreExportQuery.getAppendFilter())
                                filtro=(coreExportQuery.getWherec()!=null?coreExportQuery.getWherec():"");
                            if(query==null)
                                query="select "+coreExportQuery.getCampos()+" from "+coreExportQuery.getTablas()+" "+filtro+" "+(coreExportQuery.getOrderBy()!=null?" order by "+coreExportQuery.getOrderBy():"")+" "+(coreExportQuery.getOrderc()!=null?coreExportQuery.getOrderc():"")+" "+(coreExportQuery.getLimitc()!=null?" limit "+coreExportQuery.getLimitc():"");
                            conn =  DriverManager.getConnection(conexionDB.getServidor(),conexionDB.getUsuario(),FCom.decrypt(conexionDB.getContrasena(),cadenaEncriptacion));
                            st = conn.createStatement();
                            rs=st.executeQuery(query);
                            ResultSetMetaData metaData = rs.getMetaData();
                            int count = metaData.getColumnCount();
                            response.setHeader("Content-Disposition", "attachment; filename="+modo+"."+tipo);
                            if(tipo.equals("XLS") || tipo.equals("XLSX")){ 
                                SimpleDateFormat dateFormat=new SimpleDateFormat("dd/mm/yyyy");
                                SXSSFWorkbook workbook= new SXSSFWorkbook(10000); 
                                Sheet  sheet = workbook.createSheet(modo); 
                                int rownum = 0;  
                                Row row = sheet.createRow(rownum++);
                                if(!coreExportQuery.getHeader().equals("")){                                                     
                                    CellStyle style = workbook.createCellStyle();   
                                    style.setAlignment(CellStyle.ALIGN_CENTER);
                                    Font font = workbook.createFont();
                                    font.setFontHeightInPoints((short)14);
                                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                                    style.setFont(font);                                           
                                    String[] headerRows=coreExportQuery.getHeader().replaceAll("#NOW#", dateFormat.format(new Date())).split("\\|");
                                    for(int i=0;i<headerRows.length;i++){
                                        row = sheet.createRow(rownum++);         
                                        row.createCell(3).setCellValue(headerRows[i]); 
                                        row.getCell(3).setCellStyle(style); 
                                    }
                                    row = sheet.createRow(rownum++); 
                                }        
                                CellStyle stylec = workbook.createCellStyle();                                  
                                stylec.setBorderBottom(CellStyle.BORDER_THIN);
                                stylec.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                                Font fontc = workbook.createFont();
                                fontc.setBoldweight(Font.BOLDWEIGHT_BOLD);
                                stylec.setFont(fontc);
                                for (int i = 1; i <= count; i++){
                                    row.createCell(i).setCellValue(metaData.getColumnName(i)); 
                                    row.getCell(i).setCellStyle(stylec); 
                                }
                                while(rs.next()){
                                    Row rowh = sheet.createRow(rownum++);
                                    for (int i = 1; i <= count; i++){
                                        if(metaData.getColumnTypeName(i).equalsIgnoreCase("INT") || metaData.getColumnTypeName(i).equalsIgnoreCase("INT UNSIGNED"))
                                            rowh.createCell(i).setCellValue(rs.getInt(i));
                                        else if(metaData.getColumnTypeName(i).equalsIgnoreCase("DOUBLE"))
                                            rowh.createCell(i).setCellValue(rs.getDouble(i));
                                        else
                                            rowh.createCell(i).setCellValue(rs.getString(i));
                                    }
                                }         
                                if(rownum<5000){
                                    for (int i = 1; i <= count; i++)
                                        sheet.autoSizeColumn(i); 
                                }
                                try {
                                    workbook.write(response.getOutputStream());
                                } catch (FileNotFoundException e) {
                                } catch (IOException e) {
                                }
                            }else if(tipo.equals("CSV") ){  
                                StringBuilder csv=new StringBuilder();
                                for (int i = 1; i <= count; i++)
                                    csv.append(metaData.getColumnName(i)).append("|");
                                csv.append("E\n");
                                while(rs.next()){
                                    for (int i = 1; i <= count; i++)
                                        csv.append(rs.getString(i)).append("|");
                                    csv.append("E\n");    
                                }     
                                response.setContentType("text/xml;charset=UTF-8");                        
                                response.getOutputStream().write(csv.toString().getBytes()); 
                            }else if(tipo.equals("HTML") ){  
                                StringBuilder html=new StringBuilder();                                    
                                html.append("<table>");                                  
                                html.append("<tr>");
                                for (int i = 1; i <= count; i++)
                                    html.append("<th>").append(metaData.getColumnName(i)).append("</th>");                                  
                                html.append("</tr>");
                                while(rs.next()){                                  
                                    html.append("<tr>");
                                    for (int i = 1; i <= count; i++)
                                        html.append("<td>").append(rs.getString(i)).append("</td>");         
                                    html.append("</tr>"); 
                                }                                                                         
                                html.append("</table>");
                                response.setContentType("text/html;charset=UTF-8");                        
                                response.getOutputStream().write(html.toString().getBytes()); 
                            }
                        }catch (SQLException e) {
                            response.setHeader("Content-Disposition", "attachment; filename="+modo+".error");
                            response.getOutputStream().write(e.getMessage().getBytes()); 
                        }finally{
                            if (st != null)
                                st.close();
                            if (conn != null) 
                                conn.close();
                        }
                    }else if(modo.equals("proxy")){
                        response.setHeader("Content-Disposition", "attachment;filename=" + request.getParameter("fileName"));
                        response.setContentType(request.getParameter("contentType"));
                        byte[] data = DatatypeConverter.parseBase64Binary(request.getParameter("base64"));
                        response.setContentLength(data.length);
                        response.getOutputStream().write(data);
                        response.flushBuffer();
                    }
                }catch(Exception e1) {
                    response.setHeader("Content-Disposition", "attachment; filename="+modo+".error");
                    response.getOutputStream().write(e1.getMessage().getBytes()); 
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
