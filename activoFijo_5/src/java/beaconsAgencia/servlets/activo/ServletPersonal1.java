/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.servlets.activo;

import beaconsAgencia.ejb.CiudadFacadeLocal;
import beaconsAgencia.ejb.DepartamentosFacadeLocal;
import beaconsAgencia.ejb.EstadoFacadeLocal;
import beaconsAgencia.ejb.OficinasFacadeLocal;
import beaconsAgencia.ejb.PersonalFacadeLocal;
import beaconsAgencia.entities.Personal;
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
@WebServlet(name = "ServletPersonal1", urlPatterns = {"/beaconsAgencias/personal.do"})
public class ServletPersonal1 extends HttpServlet {

    @EJB
    PersonalFacadeLocal PersonalEJBLocal;
    @EJB
    OficinasFacadeLocal OficinasEJB;
    @EJB
    CiudadFacadeLocal CiudadEJB;
    @EJB
    EstadoFacadeLocal EstadoEJB;
    @EJB
    DepartamentosFacadeLocal DepartamentoEJB;
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
        if(session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")==null){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                response.getWriter().print("{\"errors\":\"901\"}");
            else
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {        
            try {
                if(request.getParameter("modo")==null) {
                    int rows=10;
                    int page=1;   
                    String sort="ID_PERSONAL"; 
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
                    response.getWriter().print(PersonalEJBLocal.select(rows,page,sort,order,filter));
                 }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("idPersonal");
                    Personal personal = new Personal();
                    String idciu = (CiudadEJB.ciudad(request.getParameter("idCiudad")));
                    //String idOfi = OficinasEJB.generaComboPorCiudad(request.getParameter("idCiudad"));
                    if(modo.equals("eliminar")) {
                        PersonalEJBLocal.remove(PersonalEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar")) {
                        if(!id.equals(""))
                            personal.setIdPersonal(Integer.parseInt(id));
                        personal.setIdOficina(OficinasEJB.find(Integer.parseInt(request.getParameter("idOficina[id]"))));
                        personal.setIdDepartamento(DepartamentoEJB.find(Integer.parseInt(request.getParameter("idDepartamento[id]"))));
                        personal.setIdEstado(request.getParameter("idEstado"));
                        personal.setIdCiudad(request.getParameter("idCiudad"));
                        personal.setNombre(request.getParameter("nombre"));
                        personal.setApellidoPaterno(request.getParameter("apellidoPaterno"));
                        personal.setApellidoMaterno(request.getParameter("apellidoMaterno"));
                        personal.setEmail(request.getParameter("email"));
                        personal.setCalle(request.getParameter("calle"));
                        personal.setColonia(request.getParameter("colonia"));
                        personal.setCpp(request.getParameter("cpp"));
                        personal.setNumeroTelefono(request.getParameter("numeroTelefono"));
                        response.getWriter().print(PersonalEJBLocal.persistir(personal,modo));
                    }else if(modo.equals("nuevo")){
                        if(!id.equals(""))
                            personal.setIdPersonal(Integer.parseInt(id));
                        personal.setIdOficina(OficinasEJB.find(Integer.parseInt(request.getParameter("idOficina"))));
                        personal.setIdDepartamento(DepartamentoEJB.find(Integer.parseInt(request.getParameter("idDepartamento"))));
                        personal.setIdEstado(request.getParameter("idEstado"));
                        personal.setIdCiudad(request.getParameter("idCiudad"));
                        personal.setNombre(request.getParameter("nombre"));
                        personal.setApellidoPaterno(request.getParameter("apellidoPaterno"));
                        personal.setApellidoMaterno(request.getParameter("apellidoMaterno"));
                        personal.setEmail(request.getParameter("email"));
                        personal.setCalle(request.getParameter("calle"));
                        personal.setColonia(request.getParameter("colonia"));
                        personal.setCpp(request.getParameter("cpp"));
                        personal.setNumeroTelefono(request.getParameter("numeroTelefono"));
                        response.getWriter().print(PersonalEJBLocal.persistir(personal,modo));
                        
                    }else if(modo.equals("combo")){    
                        response.getWriter().print(PersonalEJBLocal.generaCombo(request.getParameter("idOficina"),""));
                    }else if(modo.equals("validaCpp")){
                        String CPP = request.getParameter("cpp");
                        response.getWriter().print(OficinasEJB.generaCpp(CPP));
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
            case "idPersonal":
                return "ID_PERSONAL";
            case "apellidoPaterno":
                return "APELLIDO_PATERNO";
            case "apellidoMaterno":
                return "APELLIDO_MATERNO";
            case "idEstado":
                return "ID_ESTADO";
            case "numeroTelefono":
                return "NUMERO_TELEFONO";
            case "idOficina":
                return "ID_OFICINA";
            case "idCiudad":
                return "ID_CIUDAD";
            case "idDepartamento":
                return "ID_DEPARTAMENTO";
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
