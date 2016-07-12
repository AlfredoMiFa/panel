/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Activo;
import beaconsAgencia.entities.Incidencias;
import beaconsAgencia.entities.Oficinas;
import coreapp.clases.general.FCom;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Valar_Morgulis
 */
@Stateless
public class IncidenciasFacade extends AbstractFacade<Incidencias> implements IncidenciasFacadeLocal {

    @PersistenceContext(unitName="beaconsAgenciaPU")
    private EntityManager em;
    public IncidenciasFacade() {
        super(Incidencias.class);
    }

    public IncidenciasFacade(Class<Incidencias> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em; //To change body of generated methods, choose Tools | Templates.
    }
    
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }
    
    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    
    //Método para mostrar los registros de la tabla incidencias
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1)* rows);
        try{
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM INCIDENCIAS " + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from incidencias " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Incidencias.class);
            List<Incidencias> listPerfil = (List<Incidencias>) queryRegistros.getResultList();
            json = "{\"data\":[";
            for(Incidencias incidencia: listPerfil){
                json = json + incidencia.toString()+",";
            }
            if (count > 0)
                json = json.substring(0,json.length() - 1);
            json = json + "],\"total\":"+count+"}";
        }catch(NumberFormatException e){
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para gusrdar o actualizar las incidencias según sea el caso
    @Override
    public String persistir(Incidencias incidencias, String tipo) {
        String mensaje;
        try{
            Object respuesta = null;
            switch(tipo){
                case "nuevo":
                    respuesta=persistEntity(incidencias);
                    break;
                case "editar":
                    respuesta=mergeEntity(incidencias);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
        }catch(ConstraintViolationException e){
            mensaje = "{\"success\":false,\"title\":\"Error en la persistencia de datos\",\"msg\":\""
                    + "Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para generar comobo de las incidencias
    @Override
    public String generaCombo(String estado, String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from incidencias", Incidencias.class);
            List<Incidencias> listHistorial = (List<Incidencias>)query.getResultList();
            for (Incidencias incidencias: listHistorial)
                json += "{\"id\":\"" + incidencias.getIdIncidencia()+ "\",\"text\":\"" +incidencias.getTipoIncidencia()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    //Método para obtener el número de serie del activo de acuerdo a su id
    @Override
    public String activo(String id) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select activo.id_activo, activo.numero_serie FROM activo.activo where id_activo = \""+id+"\"", Activo.class);
            List<Activo> listHistorial = (List<Activo>)query.getResultList();
            for (Activo activo: listHistorial)
                json += activo.getNumeroSerie();
            //json = json.substring(0, json.length() - 1);
            //json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener el nombre de la oficina de acuerdo a su id
    @Override
    public String oficina(String idOficina) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_oficina, nombre_oficina FROM oficinas where id_oficina = \""+idOficina+"\"", Oficinas.class);
            List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
            for (Oficinas oficinas: listHistorial)
                json += oficinas.getNombreOficina();
            //json = json.substring(0, json.length() - 1);
            //json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }
}
