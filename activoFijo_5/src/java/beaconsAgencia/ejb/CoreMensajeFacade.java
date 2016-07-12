/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import coreapp.clases.general.FCom;
import beaconsAgencia.entities.CoreMensaje;
import java.text.SimpleDateFormat;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreMensajeFacade extends AbstractFacade<CoreMensaje> implements CoreMensajeFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreMensajeFacade() {
        super(CoreMensaje.class);
    }

    public Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    public Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    /**<code>select o fromId CoreMensaje o</code>
     * @param mensaje
     * @param tipo
     * @return 
     */
    @Override
    public String persistir(CoreMensaje mensaje,String tipo) {
        String rmensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(mensaje);
                    break;
                case "editar":
                    respuesta=mergeEntity(mensaje);
                    break;
            }
            if (respuesta != null)
                rmensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                rmensaje = "{\"success\":false,\"title\":\"Operación no encontrada.\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        } catch (ConstraintViolationException e) {              
            rmensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return rmensaje;
    }
 
    @Override
    public String nmensajes(String search){
        String json;
        SimpleDateFormat simpleFormat=new SimpleDateFormat("dd-MM-yyyy");
        int count=0;
        try{
            Query queryRegistros =em.createNativeQuery("SELECT * FROM core_mensaje " + search+" limit 5", CoreMensaje.class);
            List<CoreMensaje> listMensaje = (List<CoreMensaje>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreMensaje mensaje: listMensaje) {
                json += "{\"titulo\":\""+mensaje.getTitulo()+"\",\"fecha\":\""+simpleFormat.format(mensaje.getFechaCreacion())+"\"},";
                count++;
            }
            if (count > 0)
                json = json.substring(0, json.length() - 1);
            json += "],\"total\":"+count+",\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
        }catch(Exception e) {
            FCom.printDebug(e.getMessage());
            json = "{\"success\":false,\"title\":\"Error al cargar los mensajes.\",\"msg\":\"Favor de comunicarse con el administrador.\",\"data\":[],\"total\":0}";
        }
        return json;
    }
    
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_mensaje " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_mensaje " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreMensaje.class);

            List<CoreMensaje> listMensaje = (List<CoreMensaje>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreMensaje mensaje: listMensaje) {
                json += mensaje.toString()+",";
            }
            if (count > 0)
                json = json.substring(0, json.length() - 1);
            json += "],\"total\":"+count+"}";
        } catch (NumberFormatException e) {
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
    }
    
}
