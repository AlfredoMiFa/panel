/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import coreapp.clases.general.FCom;
import beaconsAgencia.entities.CoreCamposLayout;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreCamposLayoutFacade extends AbstractFacade<CoreCamposLayout> implements CoreCamposLayoutFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public CoreCamposLayoutFacade() {
        super(CoreCamposLayout.class);
    }
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }
    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }
    
    @Override
    public String persistir(CoreCamposLayout campoLayout,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevoCamposLayout":
                    respuesta=persistEntity(campoLayout);
                    break;
                case "editarCamposLayout":
                    respuesta=mergeEntity(campoLayout);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Operación no encontrada.\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        } catch (ConstraintViolationException e) {              
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    }    
       
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_campos_layout " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_campos_layout " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreCamposLayout.class);

            List<CoreCamposLayout> listCatalogo = (List<CoreCamposLayout>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreCamposLayout catalogo: listCatalogo) {
                json += catalogo.toString()+",";
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
