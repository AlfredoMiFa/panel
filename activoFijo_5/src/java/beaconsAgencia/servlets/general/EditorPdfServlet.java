/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.general;

import coreapp.clases.general.Arbol;
import coreapp.clases.general.FCom;
import coreapp.clases.general.Nodo;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CoreDocumentoFacadeLocal;
import beaconsAgencia.entities.CoreCarpeta;
import beaconsAgencia.entities.CoreDocumento;
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
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "EditorPdfServlet", urlPatterns = {"/general/editorPdf.do"})
public class EditorPdfServlet extends HttpServlet {
    @EJB
    CoreDocumentoFacadeLocal documentoEJBLocal;

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
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent( request );
            if ( !isMultipart )
            {
                String modo=request.getParameter("modo");                
                if(modo.equals("abrirPdf")) {
                    //if(objetoSesion.isSuperAdministrador() || objetoSesion.getPerfilSelected().getCoreDerechoSistemaList().toString().indexOf("D_VER_DEPOSITO")>-1){                        
                        CoreDocumento documento=documentoEJBLocal.find(Integer.parseInt(request.getParameter("coreDocumentoId")));
                        response.setContentType("application/pdf");   
                        response.setHeader("Content-Disposition","attachment; filename=\"" + documento.getNombreArchivo() + "\""); 
                        response.getOutputStream().write(documento.getDocumento());
                    //}else
                    //    response.getWriter().println("{\"success\":false,\"msg\":\"Usted no cuenta con los permisos necesarios.\"}");
                }
            }else{                        
                ServletFileUpload upload = new ServletFileUpload();                
                try
                {
                    byte[] bytes=null;
                    String name=null;
                    Integer documentoId=null;
                    FileItemIterator iter = upload.getItemIterator( request );
                    while ( iter.hasNext() )
                    {
                        FileItemStream item = iter.next();
                        String fieldName = item.getFieldName();
                        if ( fieldName.equals( "PDFFile" ) )
                        {
                            name=item.getName();
                            String[] aname=name.split("Id=");
                            bytes = IOUtils.toByteArray( item.openStream() );                            
                            documentoId=Integer.parseInt(aname[1]);
                        }else if(fieldName.equals( "coreDocumentoId" ))
                            documentoId=Integer.parseInt(Streams.asString(item.openStream()));
                    }
                    if(documentoId!=null){
                        CoreDocumento documento=documentoEJBLocal.find(documentoId);
                        documento.setDocumento(bytes);
                        response.getWriter().print(documentoEJBLocal.persistir(documento,"editar"));
                    }
                }
                catch ( IOException ex )
                {
                    throw ex;
                }
                catch ( Exception ex )
                {
                    throw new ServletException( ex );
                }

            }
        }catch (Exception e) {
            if(request.getParameter("modo")==null || request.getParameter("modo").equals("listaDocumentos"))
                response.getWriter().print("{\"data\":[],\"total\":0}"); 
            else
                response.getWriter().print("{\"success\":false,\"msg\":\"Error al realizar la operacion.\"}");
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
