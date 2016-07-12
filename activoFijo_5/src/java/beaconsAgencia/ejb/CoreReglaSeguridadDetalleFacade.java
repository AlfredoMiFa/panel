/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import coreapp.clases.general.FCom;
import beaconsAgencia.entities.CoreReglaSeguridadDetalle;
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
public class CoreReglaSeguridadDetalleFacade extends AbstractFacade<CoreReglaSeguridadDetalle> implements CoreReglaSeguridadDetalleFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreReglaSeguridadDetalleFacade() {
        super(CoreReglaSeguridadDetalle.class);
    }
    
    

    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    /**<code>select o fromId CoreCatalogoGeneral o</code>
     * @param reglaSeguridad
     * @param tipo
     * @return 
     */
    @Override
    public String persistir(CoreReglaSeguridadDetalle reglaSeguridad,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(reglaSeguridad);
                    break;
                case "editar":
                    respuesta=mergeEntity(reglaSeguridad);
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
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_regla_seguridad_detalle " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_regla_seguridad_detalle " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreReglaSeguridadDetalle.class);

            List<CoreReglaSeguridadDetalle> listCoreReglaSeguridad = (List<CoreReglaSeguridadDetalle>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreReglaSeguridadDetalle reglaSeguridad: listCoreReglaSeguridad) {
                json += reglaSeguridad.toString()+",";
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
    public String getFiltroSeguridad(String ids) {
        String filtro = "";
        try {
            Query query =em.createNativeQuery("select * from core_regla_seguridad_detalle where core_regla_seguridad_detalle_id in("+ids+")", CoreReglaSeguridadDetalle.class);
            List<CoreReglaSeguridadDetalle> listReglaSeguridad = (List<CoreReglaSeguridadDetalle>)query.getResultList();
            for (CoreReglaSeguridadDetalle reglaSeguridad: listReglaSeguridad){
                if(reglaSeguridad.getTipo().equals("PERMITIR"))
                    filtro += " ("+reglaSeguridad.getNombre()+">="+reglaSeguridad.getValorIni()+" and "+reglaSeguridad.getNombre()+"<="+reglaSeguridad.getValorFin()+") ";
                else
                    filtro += " not ("+reglaSeguridad.getNombre()+">="+reglaSeguridad.getValorIni()+" and "+reglaSeguridad.getNombre()+"<="+reglaSeguridad.getValorFin()+") ";
            }
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            filtro = "";
        }
        return filtro;
    }  
}
