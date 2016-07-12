/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.servlets.activo;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.BitacoraUsuariosFacadeLocal;
import beaconsAgencia.ejb.CoreUsuarioFacadeLocal;
import beaconsAgencia.ejb.UsuariosFacadeLocal;
import beaconsAgencia.entities.BitacoraUsuarios;
import beaconsAgencia.entities.CoreUsuario;
import coreapp.clases.general.FCom;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Valar_Morgulis
 */
@WebServlet(name = "ServletBitacoraUsuarios", urlPatterns = {"/beaconsAgencias/Bitacora.do"})
public class ServletBitacoraUsuarios extends HttpServlet {

    
    @EJB
    UsuariosFacadeLocal UsuariosEJBLocal;
    @EJB
    BitacoraUsuariosFacadeLocal BitacoraEJBLocal;
    @EJB
    CoreUsuarioFacadeLocal CoreUsuarioEJB;
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
            usuario = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario();
            try {
                if(request.getParameter("modo")==null) {
                    int rows=10;
                    int page=1;   
                    String sort="ID_BITACORA_USUARIO"; 
                    String order="desc"; 
                    String filter="where ";
                    String usur = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario().getUsuario();//UsuarioEJB.generaComboUsuario(objetoSesion.getUsuario());
                    Integer idusur = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario().getCoreUsuarioId();
                    String idEmpresa= String.valueOf(usuario.getIdEmpresa().getIdEmpresa());
                    String idOficina = String.valueOf(usuario.getIdOficina().getIdOficina());
                    String ids = CoreUsuarioEJB.idUsuarios(idEmpresa);
                    if(usuario.getGrupo().equals("ADMINISTRADOR")) {
                        filter += " ID_USUARIO IN("+ids+ ") ";
                        
                    }
                    if(usuario.getGrupo().equals("SUPERVISOR")) {
                        String nom =  CoreUsuarioEJB.idUsuariosPorOficina(idOficina);
                        for(int i=0;i<1;i++) {
                            filter += " ID_USUARIO IN("+nom+") ";
                            i++;
                        }
                    }
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
                        //filter=" where ";
                        for(int i=0;i<mFiltroDoble.length;i++)
                        {                            
                            if(mFiltroDoble[i]){                                
                                filter += "("+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][0][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][0][field]")),request.getParameter("filter[filters]["+i+"][filters][0][value]"))
                                        +" "+request.getParameter("filter[filters]["+i+"][logic]")+" "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][1][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][1][field]")),request.getParameter("filter[filters]["+i+"][filters][1][value]"))+")";
                            }
                            else{
                                if(usuario.getGrupo().equals("ADMINISTRADOR"))
                                    filter += " AND "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));
                                if(usuario.getGrupo().equals("SUPERVISOR"))
                                    filter += " AND "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));
                                if(usuario.getGrupo().equals("SUPER-ADMINISTRADOR"))
                                    filter += FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));
                            }
                            if(i<mFiltroDoble.length-1)
                                filter += " "+request.getParameter("filter[logic]")+" ";  
                        }
                    }
                    if(filter.equals("where ")) { filter = " ";}
                    response.getWriter().print(BitacoraEJBLocal.select(rows,page,sort,order,filter));
                 }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("idBitacoraUsuario");
                    BitacoraUsuarios bitacora = new BitacoraUsuarios();
                    if(modo.equals("eliminar")) {
                        BitacoraEJBLocal.remove(BitacoraEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(!id.equals(""))
                            bitacora.setIdBitacoraUsuario(Integer.parseInt(id));
                        bitacora.setIdUsuario(Integer.parseInt(request.getParameter("idUsuario")));
                        bitacora.setUsuario(request.getParameter("usuario"));
                        bitacora.setActividad(request.getParameter("actividad"));
                        bitacora.setCantidadActivos(Integer.parseInt(request.getParameter("cantidadActivos")));
                        bitacora.setCategoria(request.getParameter("categoria"));
                        bitacora.setFecha(new Date());
                        response.getWriter().print(BitacoraEJBLocal.persisitir(bitacora, modo));
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
            case "idBitacoraUsuario":
                return "ID_BITACORA_USUARIO";
            case "idUsuario":
                return "ID_USUARIO";
            case "cantidadActivos":
                return "CANTIDAD_ACTIVOS";
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
