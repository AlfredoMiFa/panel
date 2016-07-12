/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.servlets.activo;

import beaconsAgencia.clases.general.ObjetoSesion;
import java.util.Calendar;
import beaconsAgencia.ejb.ActivosFacadeLocal;
import beaconsAgencia.ejb.BitacoraUsuariosFacadeLocal;
import beaconsAgencia.ejb.CategoriasFacadeLocal;
import beaconsAgencia.ejb.CoreUsuarioFacadeLocal;
import beaconsAgencia.ejb.DepartamentosFacadeLocal;
import beaconsAgencia.ejb.EmpresaFacadeLocal;
import beaconsAgencia.ejb.HistorialActivosFacadeLocal;
import beaconsAgencia.ejb.OficinasFacadeLocal;
import beaconsAgencia.ejb.PersonalFacadeLocal;
import beaconsAgencia.entities.Activo;
import beaconsAgencia.entities.CoreUsuario;
import coreapp.clases.general.FCom;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.GregorianCalendar;
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

@WebServlet(name = "ServletActivo", urlPatterns = {"/beaconsAgencias/Activos.do"})
public class ServletActivo extends HttpServlet {

    @EJB
    ActivosFacadeLocal ActivosEJBLocal;
    @EJB
    EmpresaFacadeLocal EmpresaEJB;
    @EJB
    OficinasFacadeLocal OficinasEJBLocal;
    @EJB
    PersonalFacadeLocal PersonalEJBLocal;
    @EJB
    CategoriasFacadeLocal CategoriaEJB;
    @EJB
    HistorialActivosFacadeLocal HistorialEJB;
    @EJB
    CoreUsuarioFacadeLocal UsuarioEJB;
    @EJB
    BitacoraUsuariosFacadeLocal BitacoraEJB;
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
                    String sort="ID_ACTIVO"; 
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
                        for(int i=0;i<mFiltroDoble.length;i++)
                        {                            
                            if(mFiltroDoble[i]){                           
                                filter += "("+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][0][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][0][field]")),request.getParameter("filter[filters]["+i+"][filters][0][value]"))
                                        +" "+request.getParameter("filter[filters]["+i+"][logic]")+" "+FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][filters][1][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][filters][1][field]")),request.getParameter("filter[filters]["+i+"][filters][1][value]"))+")";
                            
                            }else{
                                String field = fieldParse(request.getParameter("filter[filters]["+i+"][field]"));
                                String valor = request.getParameter("filter[filters]["+i+"][value]");
                                if(field.equals("ID_OFICINA")){
                                    filter = "where "+ FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),OficinasEJBLocal.idOficina(valor));
                                }
                                if(field.equals("ID_PERSONAL")){
                                    filter = "where "+ FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),PersonalEJBLocal.idPersonal(valor));
                                }
                                if(field.equals("ID_CATEGORIA")){
                                    filter = "where "+ FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),CategoriaEJB.idCategoria(valor));
                                }
                                if(field.equals("ID_DEPARTAMENTO")){
                                    filter = "where "+ FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),DepartamentoEJB.idDepartamento(valor));
                                }
                                if(field.equals("NUMERO_INVENTARIO") || field.equals("denominacion") || field.equals("NUMERO_SERIE") || field.equals("descripcion") || field.equals("marca")
                                        || field.equals("modelo") || field.equals("precio") || field.equals("NOTA_CREDITO") || field.equals("estado")){
                                    filter += " AND "+ FCom.ToFilterOperator(request.getParameter("filter[filters]["+i+"][operator]"),fieldParse(request.getParameter("filter[filters]["+i+"][field]")),request.getParameter("filter[filters]["+i+"][value]"));
                                }
                            }
                            if(i<mFiltroDoble.length-1)
                                filter += " "+request.getParameter("filter[logic]")+" ";
                        }
                    }
                    if(filter.equals("where ")) { filter = " ";}
                    response.getWriter().print(ActivosEJBLocal.select(rows,page,sort,order,filter));
                 }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("idActivo");
                    Activo activo = new Activo();
                    Calendar fecha = new GregorianCalendar();
                    int anio,mes,dia;
                    anio = fecha.get(Calendar.YEAR);
                    mes = fecha.get(Calendar.MONTH);
                    dia = fecha.get(Calendar.DAY_OF_MONTH);
                    String fechaActual = String.valueOf(anio+"-"+(mes+1)+"-"+dia);
                    String usur = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario().getUsuario();//UsuarioEJB.generaComboUsuario(objetoSesion.getUsuario());
                    Integer idusur = ((ObjetoSesion)session.getAttribute(getServletContext().getInitParameter("vsi")+"SESION")).getUsuario().getCoreUsuarioId();
                    String actividad = request.getParameter("estado");//request.getParameter("idAcategoria");//CategoriaEJB.filtro(request.getParameter("idCategoria"));
                    String categoria = CategoriaEJB.filtro(request.getParameter("idCategoria"));
                    String nomOfi = OficinasEJBLocal.nombreOficina(request.getParameter("idOficina[id]"));
                    String nombreOficina = OficinasEJBLocal.nombreOficina(request.getParameter("idOficina"));
                    String departamento = DepartamentoEJB.nombre(request.getParameter("idDepartamento"));
                    String nombDepa= DepartamentoEJB.nombre(request.getParameter("idDepartamento[id]"));
                    //String idDepartamento = request.getParameter("idDepartamento");
                    if(modo.equals("eliminar")) {
                        ActivosEJBLocal.remove(ActivosEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                        HistorialEJB.activo(nomOfi,nombDepa,0, "0", Integer.parseInt(id), request.getParameter("numeroSerie"), 
                                idusur, usur, "ELIMINACION", new Date(), "ELIMINACION", "Se elimino un activo");
                    }else if(modo.equals("editar")) {
                        if(!id.equals(""))
                            activo.setIdActivo(Integer.parseInt(id));
                        activo.setIdOficina(OficinasEJBLocal.find(Integer.parseInt(request.getParameter("idOficina[id]"))));
                        activo.setIdDepartamento(DepartamentoEJB.find(Integer.parseInt(request.getParameter("idDepartamento[id]"))));
                        activo.setIdPersonal(PersonalEJBLocal.find(Integer.parseInt(request.getParameter("idPersonal[id]"))));
                        activo.setIdCategoria(CategoriaEJB.find(Integer.parseInt(request.getParameter("idCategoria[id]"))));
                        activo.setNumeroInventario(request.getParameter("numeroInventario"));
                        activo.setDescripcion(request.getParameter("descripcion"));
                        activo.setDenominacion(request.getParameter("denominacion"));
                        activo.setNumeroSerie(request.getParameter("numeroSerie"));
                        activo.setMarca(request.getParameter("marca"));
                        activo.setModelo(request.getParameter("modelo"));
                        activo.setPrecio(Float.parseFloat(request.getParameter("precio")));
                        activo.setNotaCredito(request.getParameter("notaCredito"));
                        activo.setEstado(request.getParameter("estado"));
                        HistorialEJB.activo(nomOfi,nombDepa,0, "0", Integer.parseInt(id), request.getParameter("numeroSerie"), 
                                idusur, usur, "EDICIÓN", new Date(), "EDICIÓN DE ACTIVO", "SE ACTUALIZO EL ACTIVO");
                        response.getWriter().print(ActivosEJBLocal.persisitir(activo,modo));
                    }else if(modo.equals("nuevo")){
                        if(!id.equals(""))
                            activo.setIdActivo(Integer.parseInt(id));
                        activo.setIdOficina(OficinasEJBLocal.find(Integer.parseInt(request.getParameter("idOficina"))));
                        activo.setIdDepartamento(DepartamentoEJB.find(Integer.parseInt(request.getParameter("idDepartamento"))));
                        activo.setIdPersonal(PersonalEJBLocal.find(Integer.parseInt(request.getParameter("idPersonal"))));
                        activo.setIdCategoria(CategoriaEJB.find(Integer.parseInt(request.getParameter("idCategoria"))));
                        activo.setNumeroInventario(request.getParameter("numeroInventario"));
                        activo.setDenominacion(request.getParameter("denominacion"));
                        activo.setNumeroSerie(request.getParameter("numeroSerie"));
                        activo.setDescripcion(request.getParameter("descripcion"));
                        activo.setMarca(request.getParameter("marca"));
                        activo.setModelo(request.getParameter("modelo"));
                        activo.setPrecio(Float.parseFloat(request.getParameter("precio")));
                        activo.setNotaCredito(request.getParameter("notaCredito"));
                        activo.setEstado(request.getParameter("estado"));
                        response.getWriter().print(ActivosEJBLocal.persisitir(activo,modo));
                        BitacoraEJB.filtro(fechaActual, usur, actividad, categoria, idusur);
                        HistorialEJB.activo(nombreOficina,departamento,0, "0", Integer.parseInt(id), request.getParameter("numeroSerie"), 
                                idusur, usur, "ALTA", new Date(), "ALTA DE ACTIVO", "Se dio de alta el activo");
                    }else if(modo.equals("combo")){
                        String departam = request.getParameter("idDepartamento");
                        response.getWriter().print(ActivosEJBLocal.generaCombo(departam,""));
                    }
                        //int oficina = Integer.parseInt(request.getParameter("idOficina"));
                        //String ofi = String.valueOf(oficina);
                       
                    
                } 
            }catch (IOException | NumberFormatException e) {
                if(request.getParameter("modo")==null)
                    response.getWriter().print("{\"data\":[],\"total\":0}"); 
                else
                    response.getWriter().print("{\"errors\":\"Error al realizar la operacion.\"}");
            }
        }
    }
    
    private String filtro(String filtro, String valor, String operador, String empresa) {
        switch (filtro) {
            case "ID_OFICINA":
                return "";//OficinasEJBLocal.idOficina(filtro,valor,operador,empresa);
            case "ID_PERSONAL":
                return PersonalEJBLocal.idPersonal(valor);
            case "ID_CATEGORIA":
                return "";//CategoriaEJB.idCategoria(filtro,valor,operador);
            default:
                return valor;
        }
    }
    private String fieldParse(String campo){
        switch (campo) {
            case "idActivo":
                return "ID_ACTIVO";
            case "idOficina":
                return "ID_OFICINA";
            case "idPersonal":
                return "ID_PERSONAL";
            case "numeroSerie":
                return "NUMERO_SERIE";
            case "idCategoria":
                return "ID_CATEGORIA";
            case "numeroInventario":
                return "NUMERO_INVENTARIO";
            case "notaCredito":
                return "NOTA_CREDITO";
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
