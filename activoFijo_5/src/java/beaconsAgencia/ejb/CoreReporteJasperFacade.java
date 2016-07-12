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
import beaconsAgencia.entities.CoreReporteJasper;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreReporteJasperFacade extends AbstractFacade<CoreReporteJasper> implements CoreReporteJasperFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreReporteJasperFacade() {
        super(CoreReporteJasper.class);
    }

    public Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    public Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }
    
    @Override
    public String persistir(CoreReporteJasper reporte,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            if(tipo.equals("nuevo"))
                respuesta=persistEntity(reporte);
            else if(tipo.equals("editar"))
                respuesta=mergeEntity(reporte);
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Operación no encontrada.\",\"msg\":\"Esta tipo de operación no es valida.\"}";
        } catch (ConstraintViolationException e) {               
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    }
    @Override
    public String subirReporte(byte[] data,Object coreReporteJasperId, String nombreArchivo) {
        String mensaje;
        try {
            Object respuesta;
            CoreReporteJasper coreReporteJasper=em.find(CoreReporteJasper.class, coreReporteJasperId);
            coreReporteJasper.setData(data);
            coreReporteJasper.setNombreArchivo(nombreArchivo);
            respuesta=mergeEntity(coreReporteJasper);
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"Error al subir el archivo.\"}";
        } catch (Exception e) {
            //e.printStackTrace();                
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\""+e.getMessage()+"\"}";
        }
        return mensaje;
    }
   
    
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        int count;
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_reporte_jasper " + search);
            count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_reporte_jasper " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreReporteJasper.class);

            List<CoreReporteJasper> listReportes = (List<CoreReporteJasper>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreReporteJasper reporte: listReportes) {
                json += reporte.toString()+",";
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
