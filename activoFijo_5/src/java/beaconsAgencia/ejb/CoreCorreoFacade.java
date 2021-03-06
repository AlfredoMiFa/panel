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
import beaconsAgencia.entities.CoreCorreo;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreCorreoFacade extends AbstractFacade<CoreCorreo> implements CoreCorreoFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreCorreoFacade() {
        super(CoreCorreo.class);
    }

    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public String persistir(CoreCorreo correo,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(correo);
                    break;
                case "editar":
                    respuesta=mergeEntity(correo);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
        } catch (ConstraintViolationException e) {
            //e.printStackTrace();                
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    } 
    
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_correo " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_correo " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreCorreo.class);

            List<CoreCorreo> listCorreo = (List<CoreCorreo>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreCorreo correo: listCorreo) {
                json += correo.toString()+",";
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

    @Override
    public String generaCombo() {
        String json = "[{\"id\":-1,\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from core_correo where estatus='AC'", CoreCorreo.class);
            List<CoreCorreo> listCorreo = (List<CoreCorreo>)query.getResultList();
            for (CoreCorreo correo: listCorreo)
                json += "{\"id\":" + correo.getCoreCorreoId() + ",\"text\":\"" +correo.getUser() + "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":-1,\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
    }
    
}
