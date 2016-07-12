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
import beaconsAgencia.entities.CoreConexionesDao;
import beaconsAgencia.entities.CoreParametrosGenerales;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreConexionesDaoFacade extends AbstractFacade<CoreConexionesDao> implements CoreConexionesDaoFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreConexionesDaoFacade() {
        super(CoreConexionesDao.class);
    }
    
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }
    
    @Override
    public String persistir(CoreConexionesDao conexion,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(conexion);
                    break;
                case "editar":
                    respuesta=mergeEntity(conexion);
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
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_conexiones_dao " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_conexiones_dao " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreConexionesDao.class);

            List<CoreConexionesDao> listCatalogo = (List<CoreConexionesDao>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreConexionesDao catalogo: listCatalogo) {
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
    public String generaCombo() {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from core_conexiones_dao ", CoreConexionesDao.class);
            List<CoreConexionesDao> listCatalogos = (List<CoreConexionesDao>)query.getResultList();
            for (CoreConexionesDao catalogo: listCatalogos)
                json += "{\"id\":\"" + catalogo.getCoreConexionesDaoId() + "\",\"text\":\"" +catalogo.getNombre() + "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
    }
    
    @Override
    public CoreConexionesDao getConexion(String clave) {
        CoreParametrosGenerales parametrosGenerales=em.find(CoreParametrosGenerales.class,clave);
        if(parametrosGenerales!=null){        
            List<CoreConexionesDao> listaCoreConexiones=(List<CoreConexionesDao>)em.createNamedQuery("CoreConexionesDao.findByNombre").setParameter("nombre",parametrosGenerales.getValor()).getResultList();
            if(listaCoreConexiones.size()>0){
                return listaCoreConexiones.get(0);
            }
        }
        return null;
    }
    
}
