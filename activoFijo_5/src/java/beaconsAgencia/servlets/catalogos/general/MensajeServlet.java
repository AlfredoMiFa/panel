/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import java.io.IOException;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beaconsAgencia.ejb.CoreMensajeFacadeLocal;
import beaconsAgencia.entities.CoreMensaje;
import beaconsAgencia.entities.CoreUsuario;
import coreapp.clases.general.FCom;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CoreUsuarioFacadeLocal;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "MensajeServlet", urlPatterns = {"/catalogos/generals/mensaje.do"})
public class MensajeServlet extends HttpServlet {
    @EJB
    CoreMensajeFacadeLocal coreMensajeEJBLocal;
    @EJB
    CoreUsuarioFacadeLocal coreUsuarioEJBLocal;

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
        CoreUsuario usuario;
        if(session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")==null){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                response.getWriter().print("{\"errors\":\"901\"}");
            else
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            usuario=((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario();
            try {
                if(request.getParameter("modo")==null) {
                    int rows=10;
                    int page=1;   
                    String sort="CORE_MENSAJE_ID"; 
                    String order="desc"; 
                    String filter=" where (TOV='"+usuario.getUsuario()+"' OR FROMV='"+usuario.getUsuario()+"' )";
                    
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
                    response.getWriter().print(coreMensajeEJBLocal.select(rows,page,sort,order,filter));
                }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("coreMensajeId");
                    CoreMensaje mensaje=new CoreMensaje();
                    if(modo.equals("eliminar")) {
                        coreMensajeEJBLocal.remove(coreMensajeEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(id!=null && !id.equals("")){
                            mensaje=coreMensajeEJBLocal.find(Integer.parseInt(id)); 
                            if(mensaje.getTo().equals(usuario.getUsuario())){
                                mensaje.setFromEstatus("RE");                          
                                mensaje.setToEstatus(request.getParameter("toEstatus"));
                                mensaje.setFechaRecepcion(new Date()); 
                                response.getWriter().print(coreMensajeEJBLocal.persistir(mensaje,modo));  
                            }else
                                response.getWriter().print("{\"success\":false,\"title\":\"Error\",\"msg\":\"Solo el desinatario puede actualizar el estado del mensaje.\"}");
                        }else{
                            mensaje.setCoreMensajeId(null);
                            mensaje.setFechaCreacion(new Date());                             
                            mensaje.setFrom(usuario.getUsuario());     
                            mensaje.setTexto(request.getParameter("texto").replaceAll("\"", "'"));
                            mensaje.setTitulo(request.getParameter("titulo").replaceAll("\"", "'"));
                            mensaje.setFromEstatus("AC");
                            mensaje.setToEstatus("AC");
                            mensaje.setGrupo(usuario.getGrupo());
                            CoreUsuario coreUsuario=coreUsuarioEJBLocal.find(Integer.parseInt(request.getParameter("tov")));
                            mensaje.setTo(coreUsuario.getUsuario());
                            response.getWriter().print(coreMensajeEJBLocal.persistir(mensaje,modo));  
                        }                        
                    }else if(modo.equals("nmensajes")) {
                        response.getWriter().print(coreMensajeEJBLocal.nmensajes(" where TOV='"+usuario.getUsuario()+"' and TO_ESTATUS='AC'"));
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
            case "coreMensajeId":
                return "CORE_MENSAJE_ID";
            case "fromEstatus":
                return "FROM_ESTATUS";
            case "toEstatus":
                return "TO_ESTATUS";
            case "fechaRecepcion":
                return "FECHA_RECEPCION";
            case "fechaCreacion":
                return "FECHA_CREACION";
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
