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
import beaconsAgencia.entities.CoreDerechoSistema;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreDerechoSistemaFacade extends AbstractFacade<CoreDerechoSistema> implements CoreDerechoSistemaFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreDerechoSistemaFacade() {
        super(CoreDerechoSistema.class);
    }
    
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    
    @Override
    public String persistir(CoreDerechoSistema derechoQv,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(derechoQv);
                    break;
                case "editar":
                    respuesta=mergeEntity(derechoQv);
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
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_derecho_sistema " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_derecho_sistema " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreDerechoSistema.class);

            List<CoreDerechoSistema> listPerfil = (List<CoreDerechoSistema>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreDerechoSistema perfil: listPerfil) {
                json += perfil.toString()+",";
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
    public boolean hasDerecho(Integer perfilId,String derechoSistema) {
        String queryPaginado = "select cs.* from core_perfil_derecho_sistema cs where cs.core_perfil_id="+perfilId+" and cs.core_derecho_sistema_id=(select der.core_derecho_sistema_id from core_derecho_sistema der where der.DERECHO='"+derechoSistema+"')";
        Query queryRegistros = em.createNativeQuery(queryPaginado, CoreDerechoSistema.class);
        List<CoreDerechoSistema> listCoreDerechoSistema = (List<CoreDerechoSistema>)queryRegistros.getResultList();
        if(listCoreDerechoSistema.size()>0){
            return true;
        }
        return false;
    }
    
}
