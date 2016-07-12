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
import beaconsAgencia.entities.CoreDerecho;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreDerechoFacade extends AbstractFacade<CoreDerecho> implements CoreDerechoFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreDerechoFacade() {
        super(CoreDerecho.class);
    }
    
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    
    @Override
    public String persistir(CoreDerecho derecho,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(derecho);
                    break;
                case "editar":
                    respuesta=mergeEntity(derecho);
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
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_derecho " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());        
            String query = "select * from core_derecho " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreDerecho.class);

            List<CoreDerecho> listDerecho = (List<CoreDerecho>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreDerecho derecho: listDerecho) {
                json += derecho.toString()+",";
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
    public String generaComboPadre(String filtro) {
        String json = "[{\"id\":-1,\"text\":\"SELECCIONE UNA OPCION\"},";
        if(filtro.equals("null"))
            filtro="-1";
        try {
            Query query =em.createNativeQuery("select * from core_derecho where  ES_ENLACE='N'and CORE_DERECHO_ID!="+filtro, CoreDerecho.class);
            List<CoreDerecho> listDerechos = 
                (List<CoreDerecho>)query.getResultList();
            for (CoreDerecho tbDerecho: listDerechos)
                json += "{\"id\":" + tbDerecho.getCoreDerechoId() + ",\"text\":\"" +tbDerecho.getNombreMenu()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":-1,\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
    }
    
    @Override
    public String verificarUnico(String campo, String textoBuscar,String id) {
        String mensaje;
        try {
            if (id == null || id.equals(""))
                id = "-1";
            Query query = 
                em.createNativeQuery("select "+campo+" FROM core_derecho WHERE  core_derecho_id!=" + 
                                     id + " and " + campo + "='" +textoBuscar + "'");
            List listRegistros = query.getResultList();
            if (listRegistros.size()>0)
                mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"Ya existe, favor de cambiar.\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"\",\"msg\":\"Disponible\"}";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            mensaje = "{\"success\":false,\"title\":\"Error: \",\"msg\":\"Error al verificar dato\"}";
        }
        return mensaje;
    }
    
}
