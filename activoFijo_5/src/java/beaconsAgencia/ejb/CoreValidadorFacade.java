/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import coreapp.clases.general.FCom;
import beaconsAgencia.entities.CoreValidador;
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
public class CoreValidadorFacade extends AbstractFacade<CoreValidador> implements CoreValidadorFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreValidadorFacade() {
        super(CoreValidador.class);
    }
    
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }
    
    @Override
    public String persistir(CoreValidador validador,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevoValidador":
                    respuesta=persistEntity(validador);
                    break;
                case "editarValidador":
                    respuesta=mergeEntity(validador);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Operación no encontrada.\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        } catch (ConstraintViolationException e) {              
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }catch (Exception e) {              
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    }    
       
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_validador " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_validador " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreValidador.class);

            List<CoreValidador> listCatalogo = (List<CoreValidador>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreValidador catalogo: listCatalogo) {
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
    
    
    @Override
    public String subirClase(byte[] data,Object validadorId, String nombreArchivo) {
        String mensaje;
        try {
            Object respuesta;
            CoreValidador coreValidador=em.find(CoreValidador.class, validadorId);
            coreValidador.setClass1(data);
            respuesta=mergeEntity(coreValidador);
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"Operación no encontrada.\"}";
        } catch (Exception e) {
            //e.printStackTrace();                
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\""+e.getMessage()+"\"}";
        }
        return mensaje;
    }
    
}
