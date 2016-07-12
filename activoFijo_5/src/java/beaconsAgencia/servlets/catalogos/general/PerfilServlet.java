/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.servlets.catalogos.general;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import coreapp.clases.general.Arbol;
import coreapp.clases.general.FCom;
import coreapp.clases.general.Nodo;
import beaconsAgencia.ejb.CorePerfilFacadeLocal;
import beaconsAgencia.entities.CoreDerecho;
import beaconsAgencia.entities.CorePerfil;

/**
 *
 * @author jgonzalezc
 */
@WebServlet(name = "PerfilServlet", urlPatterns = {"/catalogos/generals/perfil.do"})
public class PerfilServlet extends HttpServlet {
    @EJB
    CorePerfilFacadeLocal perfilEJBLocal;

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
                    String sort="CORE_PERFIL_ID"; 
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
                    response.getWriter().print(perfilEJBLocal.select(rows,page,sort,order,filter));
                }else {
                    String modo=request.getParameter("modo");
                    String id=request.getParameter("corePerfilId");
                    CorePerfil perfil=new CorePerfil();
                    if(modo.equals("eliminar")) {
                        perfilEJBLocal.remove(perfilEJBLocal.find(Integer.parseInt(id)));
                        response.getWriter().print("{\"success\":true,\"title\":\"El registro se ha eliminado con exito.\",\"msg\":\"\"}");
                    }else if(modo.equals("editar") || modo.equals("nuevo")) {
                        if(!id.equals(""))
                            perfil=perfilEJBLocal.find(Integer.parseInt(id));
                        else
                            perfil.setCorePerfilId(null);
                        perfil.setPerfil(request.getParameter("perfil").toUpperCase());
                        perfil.setDescripcion(request.getParameter("descripcion"));
                        perfil.setEstatus(request.getParameter("estatus"));
                        perfil.setHomePage(request.getParameter("homePage"));
                        response.getWriter().print(perfilEJBLocal.persistir(perfil,modo));                          
                    }else if(modo.equals("perfilDerechos"))
                    {
                        response.getWriter().print(perfilEJBLocal.perfilDerechos(Integer.parseInt(id)));
                    }else if(modo.equals("getPerfilesUsuario"))
                    {
                        response.getWriter().print(perfilEJBLocal.usuarioPerfiles(Integer.parseInt(request.getParameter("coreUsuarioId"))));
                    }else if(modo.equals("actualizarPerfilDerechos"))
                    {
                        response.getWriter().print(perfilEJBLocal.actualizarPerfilDerechos(Integer.parseInt(id),request.getParameter("ids")));
                    }else if(modo.equals("actualizarPerfilesUsuario"))
                    {
                        response.getWriter().print(perfilEJBLocal.actualizarUsuarioPerfiles(Integer.parseInt(request.getParameter("coreUsuarioId")),request.getParameter("ids")));
                    }else if(modo.equals("getDerechosSistema"))
                    {
                        response.getWriter().print(perfilEJBLocal.getDerechosSistema(Integer.parseInt(request.getParameter("corePerfilId"))));
                    }else if(modo.equals("actualizarDerechosSistema"))
                    {
                        response.getWriter().print(perfilEJBLocal.actualizarDerechosSistema(Integer.parseInt(request.getParameter("corePerfilId")),request.getParameter("ids")));
                    }else if(modo.equals("getReglaSeguridad"))
                    {
                        response.getWriter().print(perfilEJBLocal.getReglaSeguridad(Integer.parseInt(request.getParameter("corePerfilId"))));
                    }else if(modo.equals("actualizarReglaSeguridad"))
                    {
                        response.getWriter().print(perfilEJBLocal.actualizarReglaSeguridad(Integer.parseInt(request.getParameter("corePerfilId")),request.getParameter("ids")));
                    }else if(modo.equals("combo")) {
                            response.getWriter().print(perfilEJBLocal.generaCombo());
                    }else if(modo.equals("combo2")) {
                            response.getWriter().print(perfilEJBLocal.generaCombo2());
                    }else if(modo.equals("arbolDerechos")) {      
                        List<CoreDerecho> listaDerechos=perfilEJBLocal.queryDerechoFindAll();
                        Arbol arbol=new Arbol(new Nodo(-1,-2,"MENU",null));
                        for(CoreDerecho derecho:listaDerechos){
                            if(derecho.getFkPadre()!=null)
                                arbol.agregarNodo(new Nodo(derecho.getCoreDerechoId(),derecho.getFkPadre().getCoreDerechoId(),derecho.getNombreMenu(),derecho));
                            else
                                arbol.agregarNodo(new Nodo(derecho.getCoreDerechoId(),-1,derecho.getNombreMenu(),derecho));
                        }
                        String html = "<ul  id='arbolDerechos'>";
                        for (int i = 1; i < arbol.getListaNodos().size(); i++) {
                                Nodo nodoTemp = new Nodo(arbol.getListaNodos().get(i));
                                if (nodoTemp.isNodoHoja()) {
                                    int ultimoHijo = 
                                        arbol.getListaNodos().get(arbol.posicionNodo(nodoTemp.getPadre())).getUltimoHijo();
                                    if (ultimoHijo == nodoTemp.getId()){
                                        if(!((CoreDerecho)nodoTemp.getObjeto()).getEsEnlace().equals("S"))
                                            html += "<li ><input type='checkbox' name='CH-"+((CoreDerecho)nodoTemp.getObjeto()).getCoreDerechoId()+"' /><span class='k-sprite folder'></span>" + nodoTemp.getTitulo() + 
                                                "</li>" + arbol.cerrarRama(nodoTemp, "", false); 
                                        else
                                            html += "<li ><input type='checkbox' name='CH-"+((CoreDerecho)nodoTemp.getObjeto()).getCoreDerechoId()+"'/><span class='k-sprite html'></span>" + nodoTemp.getTitulo() + 
                                                "</li>" + arbol.cerrarRama(nodoTemp, "", false); 
                                    }
                                    else{
                                        if(!((CoreDerecho)nodoTemp.getObjeto()).getEsEnlace().equals("S"))
                                            html += "<li ><input type='checkbox' name='CH-"+((CoreDerecho)nodoTemp.getObjeto()).getCoreDerechoId()+"'/><span class='k-sprite folder'></span>" + nodoTemp.getTitulo() + "</li>";
                                        else
                                            html += "<li><input type='checkbox' name='CH-"+((CoreDerecho)nodoTemp.getObjeto()).getCoreDerechoId()+"'/><span class='k-sprite html'></span>" + nodoTemp.getTitulo()+"</li>";
                                    }
                                } else{
                                    html += 
                                            "<li data-expanded='true'><input type='checkbox'  name='CH-"+((CoreDerecho)nodoTemp.getObjeto()).getCoreDerechoId()+"'/><span class='k-sprite rootfolder'></span>" + nodoTemp.getTitulo() +"<ul>";
                                }
                            //}
                        }
                        html += "</ul>";
                        response.getWriter().println(html);
                    }
                } 
            }catch (IOException | NumberFormatException e) {
                if(request.getParameter("modo")==null)
                    response.getWriter().print("{\"data\":[],\"total\":0}"); 
                else
                    response.getWriter().print("{\"success\":false,\"msg\":\"Error al realizar la operacion.\"}");
            }
        }
    }
    private String fieldParse(String campo){
        return campo;
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
