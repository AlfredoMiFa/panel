/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import coreapp.clases.general.FCom;
import beaconsAgencia.entities.CorePermisosIp;
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
public class CorePermisosIpFacade extends AbstractFacade<CorePermisosIp> implements CorePermisosIpFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CorePermisosIpFacade() {
        super(CorePermisosIp.class);
    }
    
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public String persistir(CorePermisosIp permisosIp,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(permisosIp);
                    break;
                case "editar":
                    respuesta=mergeEntity(permisosIp);
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
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_permisos_ip " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_permisos_ip " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CorePermisosIp.class);

            List<CorePermisosIp> listCorePermisosIp = (List<CorePermisosIp>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CorePermisosIp corePermisosIp: listCorePermisosIp) {
                json += corePermisosIp.toString()+",";
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
    public CorePermisosIp getPermisoIp(Integer coreUsuarioId,String ip){
        String query="select * from core_permisos_ip where core_usuario_id="+coreUsuarioId+" and ip='"+ip+"' and estatus='AC'";
        Query queryRegistros = em.createNativeQuery(query, CorePermisosIp.class);
        List<CorePermisosIp> listCorePermisosIp = (List<CorePermisosIp>)queryRegistros.getResultList();
        if(listCorePermisosIp.size()>0){
            return listCorePermisosIp.get(0);
        }else
            return null;
    }
    
}
