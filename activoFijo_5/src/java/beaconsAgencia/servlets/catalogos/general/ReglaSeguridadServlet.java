/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import coreapp.clases.general.FCom;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CoreReglaSeguridadFacadeLocal;
import beaconsAgencia.entities.CoreReglaSeguridad;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "ReglaSeguridadServlet", urlPatterns = {"/catalogos/generals/reglaSeguridad.do"})
public class ReglaSeguridadServlet extends HttpServlet {

        @EJB
        CoreReglaSeguridadFacadeLocal reglaSeguridadEJBLocal;

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
                ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION");
                boolean isMultipart = ServletFileUpload.isMultipartContent( request );
                if ( !isMultipart )
                {
                    if(request.getParameter("modo")==null) {
                        int rows=10;
                        int page=1;   
                        String sort="CORE_REGLA_SEGURIDAD_ID"; 
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
                        
                        if(request.getParameter("exportar")==null)
                            response.getWriter().print(reglaSeguridadEJBLocal.select(rows,page,sort,order,filter));
                        else if(request.getParameter("exportar").equals("csv")){
                            try {
                                response.addHeader("Content-Disposition", "attachment; filename=reglaSeguridad.csv");
                                response.getOutputStream().write(reglaSeguridadEJBLocal.exportarCsv(rows,page,sort,order,filter).getBytes());
                            }catch (IOException e) {
                            }
                        }    
                    }else {
                        String modo=request.getParameter("modo");
                        String id=request.getParameter("coreReglaSeguridadId");
                        CoreReglaSeguridad reglaSeguridad=new CoreReglaSeguridad();
                        if(modo.equals("eliminar")) {
                            reglaSeguridadEJBLocal.remove(reglaSeguridadEJBLocal.find(Integer.parseInt(id)));
                            response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                        }else if(modo.equals("editar") || modo.equals("nuevo")) {
                            if(!id.equals(""))
                                reglaSeguridad=reglaSeguridadEJBLocal.find(Integer.parseInt(id));
                            else
                                reglaSeguridad.setCoreReglaSeguridadId(null);
                            reglaSeguridad.setNombre(request.getParameter("nombre").toUpperCase());
                            response.getWriter().print(reglaSeguridadEJBLocal.persistir(reglaSeguridad,modo));                          
                        }else if(modo.equals("combo")) {
                            response.getWriter().print(reglaSeguridadEJBLocal.generaCombo(request.getParameter("filtro"),"DOS"));
                        }
                    }                 
                }else {   
                    ServletFileUpload upload = new ServletFileUpload();
                    FileItemIterator iter = upload.getItemIterator( request );
                    while ( iter.hasNext() )
                    {
                        FileItemStream item = iter.next();
                        String fieldName = item.getFieldName();
                        if ( fieldName.equals( "archivo" ) )
                        {
                            response.getWriter().print(reglaSeguridadEJBLocal.cargarCsv(item.openStream()));
                            break;
                        }
                    }
                }
            }catch (IOException |FileUploadException | NumberFormatException e) {
                if(request.getParameter("modo")==null)
                    response.getWriter().print("{\"data\":[],\"total\":0}"); 
                else
                    response.getWriter().print("{\"errors\":\"Error al realizar la operacion.\"}");
            }
        }
    }
    private String fieldParse(String campo){
            switch (campo) {
                case "catalogoGeneralId":
                    return "CORE_REGLA_SEGURIDAD_ID";
                case "valorIni":
                    return "VALOR_INI";
                case "valorFin":
                    return "VALOR_FIN";
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
