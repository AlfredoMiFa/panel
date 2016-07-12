/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Estado;
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
public class EstadoFacade extends AbstractFacade<Estado> implements EstadoFacadeLocal {

    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    public EstadoFacade() {
        super(Estado.class);
    }

    public EstadoFacade(Class<Estado> entityClass) {
        super(entityClass);
    }

    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    } 
    @Override
    protected EntityManager getEntityManager() {
        return em; //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    //Método para mostrar los registros de la tabla estado
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM estado " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from estado " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Estado.class);

            List<Estado> listPerfil = (List<Estado>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (Estado perfil: listPerfil) {
                json += perfil.toString()+",";
            }
            if (count > 0)
                json = json.substring(0, json.length() - 1);
            json += "],\"total\":"+count+"}";
        } catch (NumberFormatException e) {
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json; //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para guardar o actualizar según sea el caso
    @Override
    public String persistir(Estado estado, String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(estado);
                    break;
                case "editar":
                    respuesta=mergeEntity(estado);
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
        return mensaje; //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para generar combo de acuerdo al código postal
    @Override
    public String generaComboPorCpp(String Cpp) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select estado.id_estado from estado Left outer join codigo_postales on (codigo_postales.estado = estado.descripcion) "+
                    " where codigo_postales.cpp=\""+Cpp+"\"", Estado.class);                        
            List<Estado> listRegistros = (List<Estado>)query.getResultList();
            Estado estadoConsultado = listRegistros.get(0);
                json += "{\"id\":\"" + estadoConsultado.getIdEstado()+ "\",\"text\":\"" +estadoConsultado.getDescripcion()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json; //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener el id del estado de acuerdo a su descripción
    @Override
    public String findByDescripcion(String Descripcion) {
        String IdCiudad= "";
        String query = "SELECT Estado.id_Estado FROM Estado where descripcion=\""+Descripcion+"\"";
        Query queryRegistros = em.createNativeQuery(query, Estado.class);
        List<Estado> listEstado = (List<Estado>)queryRegistros.getResultList();
        //count = 0;
        for (Estado estado: listEstado) {
            IdCiudad = estado.getIdEstado().toString();
        }
        return IdCiudad; //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para generar combo de la tabla estado
    @Override
    public String generaCombo(String estado, String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from estado", Estado.class);
            List<Estado> listPersonal = (List<Estado>)query.getResultList();
            for (Estado estados: listPersonal)
                json += "{\"id\":\"" + estados.getIdEstado()+ "\",\"text\":\"" +estados.getDescripcion()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json; //To change body of generated methods, choose Tools | Templates.
    }
}
