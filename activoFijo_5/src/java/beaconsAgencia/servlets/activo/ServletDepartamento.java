/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.servlets.activo;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.DepartamentosFacadeLocal;
import beaconsAgencia.ejb.OficinasFacadeLocal;
import beaconsAgencia.entities.CoreUsuario;
import beaconsAgencia.entities.Departamento;
import coreapp.clases.general.FCom;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Servamp
 */
@WebServlet(name = "ServletDepartamento", urlPatterns = {"/beaconsAgencias/Departamentos.do"})
public class ServletDepartamento extends HttpServlet {

    @EJB
    DepartamentosFacadeLocal DepartamentoEJBLocal;
    @EJB
    OficinasFacadeLocal OficinasEJBLocal;
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
                    String sort="ID_DEPARTAMENTO"; 
                    String order="desc"; 
                    String filter="where ";
                    String idEmpr = usuario.getIdEmpresa().getIdEmpresa().toString();
                    String oficinas = OficinasEJBLocal.oficinaPorEmpresa(idEmpr);
                    if(usuario.getGrupo().equals("ADMINISTRADOR")) {
                        filter += " ID_OFICINA IN("+oficinas+ ")";
                    }if(usuario.getGrupo().equals("SUPERVISOR")) {
                            filter += " ID_OFICINA = "+usuario.getIdOficina().getIdOficina().toString() + " ";
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
                                String field = fieldParse(request.getParameter("filter[filters]["+i+"][field]"));
                                String valor = request.getParameter("filter[filters]["+i+"][value]");
                                if(field.equals("ID_OFICINA")){
                                    filter = "where "+ FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),OficinasEJBLocal.idOficina(valor));
                                }if(field.equals("DESCRIPCION")){
                                    filter += " AND "+ FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));
                                }
                            }
                            if(i<mFiltroDoble.length-1)
                                filter += " "+request.getParameter("filter[logic]")+" ";  
                        }
                    }
                    if(filter.equals("where ")) { filter = " ";}
                    response.getWriter().print(DepartamentoEJBLocal.select(rows,page,sort,order,filter));
                 }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("idDepartamento");
                    Departamento departamento = new Departamento();
                    if(modo.equals("eliminar")) {
                        DepartamentoEJBLocal.remove(DepartamentoEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("nuevo") ) {
                        if(!id.equals(""))
                            departamento.setIdDepartamento(Integer.parseInt(id));
                        departamento.setIdOficina(OficinasEJBLocal.find(Integer.parseInt(request.getParameter("idOficina[id]"))));
                        departamento.setDescripcion(request.getParameter("descripcion"));
                        response.getWriter().print(DepartamentoEJBLocal.persistir(departamento, modo));
                    }else if(modo.equals("editar")) {
                        if(!id.equals(""))
                            departamento.setIdDepartamento(Integer.parseInt(id));
                        departamento.setIdOficina(OficinasEJBLocal.find(Integer.parseInt(request.getParameter("idOficina[id]"))));
                        departamento.setDescripcion(request.getParameter("descripcion"));
                        response.getWriter().print(DepartamentoEJBLocal.persistir(departamento, modo));
                    }else if(modo.equals("combo")){
                        response.getWriter().print(DepartamentoEJBLocal.generaCombo(request.getParameter("idOficina"), ""));
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
            case "idDepartamento":
                return "ID_DEPARTAMENTO";
            case "idOficina":
                return "ID_OFICINA";
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
