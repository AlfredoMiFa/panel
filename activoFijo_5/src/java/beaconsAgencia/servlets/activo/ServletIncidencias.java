/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.servlets.activo;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.ActivosFacadeLocal;
import beaconsAgencia.ejb.CoreUsuarioFacadeLocal;
import beaconsAgencia.ejb.DepartamentosFacadeLocal;
import beaconsAgencia.ejb.HistorialActivosFacadeLocal;
import beaconsAgencia.ejb.IncidenciasFacadeLocal;
import beaconsAgencia.ejb.OficinasFacadeLocal;
import beaconsAgencia.ejb.UsuariosFacadeLocal;
import beaconsAgencia.entities.CoreUsuario;
import beaconsAgencia.entities.Incidencias;
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
@WebServlet(name = "ServletIncidencias", urlPatterns = {"/beaconsAgencias/Incidencias.do"})
public class ServletIncidencias extends HttpServlet {

    @EJB
    UsuariosFacadeLocal UsuariosEJBLocal;
    @EJB
    ActivosFacadeLocal ActivosEJBLocal;
    @EJB
    OficinasFacadeLocal OficinasEJBLocal;
    @EJB
    IncidenciasFacadeLocal IncidenciasEJBLocal;
    @EJB
    HistorialActivosFacadeLocal HistorialEJB;
    @EJB
    CoreUsuarioFacadeLocal CoreUsuarioEJB;
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
                    String sort="ID_INCIDENCIA"; 
                    String order="desc"; 
                    String filter="where ";
                    String idEmpresa= String.valueOf(usuario.getIdEmpresa().getIdEmpresa());
                    String idOficina = String.valueOf(usuario.getIdOficina().getIdOficina());
                    String ids = OficinasEJBLocal.oficinaPorEmpresa(idEmpresa);
                    if(usuario.getGrupo().equals("ADMINISTRADOR")) {
                        filter += " ID_OFICINA IN("+ids+ ") ";
                        
                    }
                    if(usuario.getGrupo().equals("SUPERVISOR")) {
                        for(int i=0;i<1;i++) {
                            filter += " ID_OFICINA ="+idOficina;
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
                                String field = fieldParse(request.getParameter("filter[filters]["+i+"][field]"));
                                String valor = request.getParameter("filter[filters]["+i+"][value]");
                                if(field.equals("ID_ACTIVO")) {
                                    filter += FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),ActivosEJBLocal.idActivo(valor));
                                }if(field.equals("ID_OFICINA")) {
                                    filter += FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),OficinasEJBLocal.idOficina(valor));
                                }if(field.equals("ID_DEPARTAMENTO")) {
                                    filter += FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),DepartamentoEJB.idDepartamento(valor));
                                }if(field.equals("ID_USUARIO") || field.equals("TIPO_INCIDENCIA") || field.equals("DESCRIPCION") || field.equals("FECHA")) {
                                   filter += FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]")); 
                                }
                            }

                            if(i<mFiltroDoble.length-1)
                                filter += " "+request.getParameter("filter[logic]")+" ";  
                        }
                    }
                    if(filter.equals("where ")) { filter = " ";}
                    response.getWriter().print(IncidenciasEJBLocal.select(rows,page,sort,order,filter));
                 }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("idIncidencia");
                    Incidencias incidencia = new Incidencias();
                    String usur = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario().getUsuario();
                    Integer idusur = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario().getCoreUsuarioId();
                    String numeroSerie, nombreOficina;
                    numeroSerie = IncidenciasEJBLocal.activo(request.getParameter("idActivo[id]"));
                    String serie = IncidenciasEJBLocal.activo(request.getParameter("idActivo"));
                    String nomOfi = IncidenciasEJBLocal.oficina(request.getParameter("idOficina"));
                    nombreOficina = IncidenciasEJBLocal.oficina(request.getParameter("idOficina[id]"));
                    String departamento = DepartamentoEJB.nombre(request.getParameter("idDepartamento"));
                    String nombDepa= DepartamentoEJB.nombre(request.getParameter("idDepartamento[id]"));
                    if(modo.equals("eliminar")) {
                        IncidenciasEJBLocal.remove(IncidenciasEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                        HistorialEJB.activo(nombreOficina,nombDepa, Integer.parseInt(id), request.getParameter("tipoIncidencia"),
                                Integer.parseInt(request.getParameter("idActivo[id]")),
                                numeroSerie,
                                idusur, usur, "INCIIDENCIA", 
                                new Date(), "ELIMINACION DE INCIDENCIA", "Incidencia corregida del activo");
                    }else if(modo.equals("editar")) {
                        if(!id.equals(""))
                            incidencia.setIdIncidencia(Integer.parseInt(id));
                        incidencia.setIdUsuario(usur);
                        //incidencia.setIdUsuario(CoreUsuarioEJB.find(Integer.parseInt(request.getParameter("idUsuario[id]"))));
                        incidencia.setIdOficina(OficinasEJBLocal.find(Integer.parseInt(request.getParameter("idOficina[id]"))));
                        incidencia.setIdDepartamento(DepartamentoEJB.find(Integer.parseInt(request.getParameter("idDepartamento[id]"))));
                        incidencia.setIdActivo(ActivosEJBLocal.find(Integer.parseInt(request.getParameter("idActivo[id]"))));
                        incidencia.setTipoIncidencia(request.getParameter("tipoIncidencia"));
                        incidencia.setDescripcion(request.getParameter("descripcion"));
                        incidencia.setFecha(new Date());
                        response.getWriter().print(IncidenciasEJBLocal.persistir(incidencia,modo));
                        HistorialEJB.activo(nombreOficina,nombDepa, Integer.parseInt(id), request.getParameter("tipoIncidencia"),
                                Integer.parseInt(request.getParameter("idActivo[id]")),
                                numeroSerie,
                                idusur, usur, "INCIDENCIA", new Date(), "EDICION DE INCIDENCIA", 
                                "Incidencia editada del activo");
                    }else if(modo.equals("nuevo")){
                        if(!id.equals(""))
                            incidencia.setIdIncidencia(Integer.parseInt(id));
                        incidencia.setIdUsuario(usur);
                        //incidencia.setIdUsuario(UsuariosEJBLocal.find(Integer.parseInt(request.getParameter("idUsuario"))));
                        incidencia.setIdOficina(OficinasEJBLocal.find(Integer.parseInt(request.getParameter("idOficina"))));
                        incidencia.setIdDepartamento(DepartamentoEJB.find(Integer.parseInt(request.getParameter("idDepartamento"))));
                        incidencia.setIdActivo(ActivosEJBLocal.find(Integer.parseInt(request.getParameter("idActivo"))));
                        incidencia.setTipoIncidencia(request.getParameter("tipoIncidencia"));
                        incidencia.setDescripcion(request.getParameter("descripcion"));
                        incidencia.setFecha(new Date());
                        response.getWriter().print(IncidenciasEJBLocal.persistir(incidencia,modo));
                        HistorialEJB.activo(nomOfi,departamento, Integer.parseInt(id), request.getParameter("tipoIncidencia"),
                                Integer.parseInt(request.getParameter("idActivo")),
                                serie,
                                idusur, usur, "INCIDENCIA", new Date(), "CREACION DE INCIDENCIA", 
                                "Incidencia encontrada del activo");
                    }else if(modo.equals("combo")){    
                       response.getWriter().print(IncidenciasEJBLocal.generaCombo("",""));
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
            case "idIncidencia":
                return "ID_INCIDENCIA";
            case "idUsuario":
                return "ID_USUARIO";
            case "idActivo":
                return "ID_ACTIVO";
            case "idOficina":
                return "ID_OFICINA";
            case "tipoIncidencia":
                return "TIPO_INCIDENCIA";
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
