/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.servlets.activo;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.CiudadFacadeLocal;
import beaconsAgencia.ejb.EmpresaFacadeLocal;
import beaconsAgencia.ejb.EstadoFacadeLocal;
import beaconsAgencia.ejb.OficinasFacadeLocal;
import beaconsAgencia.entities.CoreUsuario;
import beaconsAgencia.entities.Oficinas;
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
 * @author Valar_Morgulis
 */
@WebServlet(name = "ServletOficinas", urlPatterns = {"/beaconsAgencias/oficinas.do"})
public class ServletOficinas extends HttpServlet {
    @EJB
    OficinasFacadeLocal OficinasEJBLocal;
    @EJB
    EstadoFacadeLocal EstadosEJBLocal;
    @EJB
    CiudadFacadeLocal CiudadEJB;   
    @EJB
    EmpresaFacadeLocal EmpresaEJB;
    String idEmpr;
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
                    String sort="ID_OFICINA"; 
                    String order="desc"; 
                    String filter="where ";
                    if(usuario.getGrupo().equals("ADMINISTRADOR")) {
                        filter += " ID_EMPRESA = "+usuario.getIdEmpresa().getIdEmpresa().toString() + " ";
                    }else if(usuario.getGrupo().equals("SUPERVISOR")){
                        filter += " ID_OFICINA = "+usuario.getIdOficina().getIdOficina().toString() + " ";
                    }
                    idEmpr = usuario.getIdEmpresa().getIdEmpresa().toString();
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
                            if(mFiltroDoble[i]) {                              
                                filter += "("+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][0][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][0][field]")),request.getParameter("filter[filters]["+i+"][filters][0][value]"))
                                        +" "+request.getParameter("filter[filters]["+i+"][logic]")+" "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][1][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][1][field]")),request.getParameter("filter[filters]["+i+"][filters][1][value]"))+")";
                            }
                            else {
                                String field = fieldParse(request.getParameter("filter[filters]["+i+"][field]"));
                                String valor = request.getParameter("filter[filters]["+i+"][value]");
                                if(field.equals("ID_EMPRESA"))
                                    filter = " where "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),EmpresaEJB.idEmpresa(valor));
                                if(field.equals("ID_OFICINA") || field.equals("ID_ESTADO") || field.equals("ID_CIUDAD") || field.equals("NOMBRE_OFICINA")
                                        || field.equals("calle") || field.equals("NUMERO_OFICINA") || field.equals("colonia") || field.equals("cpp")
                                        || field.equals("NUMERO_TELEFONO") || field.equals("rfc"))
                                    filter += " AND "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));
                            }
                            if(i<mFiltroDoble.length-1)
                                filter += " "+request.getParameter("filter[logic]")+" ";  
                        }
                    }
                    if(filter.equals("where ")) { filter = " ";}
                    response.getWriter().print(OficinasEJBLocal.select(rows,page,sort,order,filter));
                 }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("idOficina");
                    Oficinas oficinas = new Oficinas();
                    String idc = CiudadEJB.ciudad(request.getParameter("idCiudad"));
                    if(modo.equals("eliminar")) {
                        OficinasEJBLocal.remove(OficinasEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if( modo.equals("nuevo")) {
                        if(!id.equals(""))
                            oficinas.setIdOficina(Integer.parseInt(id));
                        oficinas.setIdEmpresa(EmpresaEJB.find(Integer.parseInt(request.getParameter("idEmpresa"))));
                        oficinas.setNombreOficina(request.getParameter("nombreOficina"));
                        oficinas.setCpp(request.getParameter("cpp"));
                        oficinas.setIdEstado(request.getParameter("idEstado"));
                        oficinas.setIdCiudad(request.getParameter("idCiudad"));
                        oficinas.setCalle(request.getParameter("calle"));
                        oficinas.setNumeroOficina(request.getParameter("numeroOficina"));
                        oficinas.setColonia(request.getParameter("colonia"));
                        oficinas.setNumeroTelefono(request.getParameter("numeroTelefono"));
                        oficinas.setRfc(request.getParameter("rfc"));
                        response.getWriter().print(OficinasEJBLocal.persistir(oficinas,modo));
                    }else if (modo.equals("editar")){
                        if(!id.equals(""))
                            oficinas.setIdOficina(Integer.parseInt(id));
                        oficinas.setIdEmpresa(EmpresaEJB.find(Integer.parseInt(request.getParameter("idEmpresa[id]"))));
                        oficinas.setNombreOficina(request.getParameter("nombreOficina"));
                        oficinas.setCpp(request.getParameter("cpp"));
                        oficinas.setIdEstado(request.getParameter("idEstado"));
                        oficinas.setIdCiudad(request.getParameter("idCiudad"));
                        oficinas.setCalle(request.getParameter("calle"));
                        oficinas.setNumeroOficina(request.getParameter("numeroOficina"));
                        oficinas.setColonia(request.getParameter("colonia"));
                        oficinas.setNumeroTelefono(request.getParameter("numeroTelefono"));
                        oficinas.setRfc(request.getParameter("rfc"));
                        response.getWriter().print(OficinasEJBLocal.persistir(oficinas,modo));
                    }else if(modo.equals("combo")){
                        String idEmpresa = String.valueOf(usuario.getIdEmpresa().getIdEmpresa());
                        String idOficina = String.valueOf(usuario.getIdOficina().getIdOficina());
                        if(usuario.getGrupo().equals("SUPERVISOR")) {
                            response.getWriter().print(OficinasEJBLocal.generaCombo(idOficina,"SUPERVISOR",""));
                        }if (usuario.getGrupo().equals("ADMINISTRADOR")) {
                            response.getWriter().print(OficinasEJBLocal.generaCombo(idOficina,"ADMINISTRADOR",idEmpresa));
                        }if(usuario.getGrupo().equals("SUPER-ADMINISTRADOR")) {
                            response.getWriter().print(OficinasEJBLocal.generaCombo("","SUPER-ADMINISTRADOR",""));
                        }
                    }else if(modo.equals("validaCpp")){
                        String cpp = request.getParameter("cpp");
                        response.getWriter().print(OficinasEJBLocal.generaCpp(cpp));
                    }else if(modo.equals("combos")) {
                        response.getWriter().print(OficinasEJBLocal.generaComboPorEmpresa(request.getParameter("idEmpresa"),""));
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
            case "idEmpresa":
                return "ID_EMPRESA";
            case "idOficina":
                return "ID_OFICINA";
            case "idPersonal":
                return "ID_PERSONAL";
            case "idEstado":
                return "ID_ESTADO";
            case "nombreOficina":
                return "NOMBRE_OFICINA";
            case "idCiudad":
                return "ID_CIUDAD";
            case "numeroOficina":
                return "NUMERO_OFICINA";
            case "numeroTelefono":
                return "NUMERO_TELEFONO";
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
