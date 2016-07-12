/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Ciudad;
import beaconsAgencia.entities.CodigoPostales;
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
public class CiudadFacade extends AbstractFacade<Ciudad> implements CiudadFacadeLocal {

    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;
    public CiudadFacade() {
        super(Ciudad.class);
    }

    public CiudadFacade(Class<Ciudad> entityClass) {
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
    
    private String idCiudad = "";

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    //Método para mostrar los registros de la tabla ciudad
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM ciudad" + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from ciudad " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Ciudad.class);

            List<Ciudad> listPerfil = (List<Ciudad>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (Ciudad perfil: listPerfil) {
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
    public String persistir(Ciudad ciudad, String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(ciudad);
                    break;
                case "editar":
                    respuesta=mergeEntity(ciudad);
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

    
    //Método para generar combo de ciudad
    @Override
    public String generaCombo(String dominio, String tipo) {
         String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from ciudad", Ciudad.class);
            List<Ciudad> listCiudad = (List<Ciudad>)query.getResultList();
            for (Ciudad ciudad: listCiudad)
                json += "{\"id\":\"" + ciudad.getIdCiudad() + "\",\"text\":\"" +ciudad.getDescripcion() + "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json; //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para generar combo de acuerdo al municipio
    @Override
    public String generaComboPorCpp(String cpp) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select ciudad.id_ciudad from ciudad Left outer join codigo_postales on (codigo_postales.municipio = ciudad.descripcion) "+
                    " where codigo_postales.cpp=\""+cpp+"\"", Ciudad.class);                        
            List<Ciudad> listRegistros = (List<Ciudad>)query.getResultList();
            Ciudad estadoCiudad = listRegistros.get(0);
            //for (Beacons beacon: listBeacons)
                json += "{\"id\":\"" + estadoCiudad.getIdCiudad()+ "\",\"text\":\"" +estadoCiudad.getDescripcion()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json; //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtner el tipo de asentamiento de la tabla codigo_postales
    @Override
    public String ColoniasCpp(String cpp) {
        String json = "[{\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery(" SELECT * FROM codigo_postales" , CodigoPostales.class);                        
            List<CodigoPostales> listRegistros = (List<CodigoPostales>)query.getResultList();
            //CodigoPostales codigos = listRegistros.get(0);
            for (CodigoPostales codigos: listRegistros)
                json += "{\"text\":\"" +codigos.getAsentamiento()+ "\"},";            
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json; //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtner el id de la ciudad de la tabla ciudad
    @Override
    public String findByDescripcion(String Descripcion) {
        String IdCiudad= "";
        String query = "SELECT ciudad.id_Ciudad, ciudad.descripcion FROM ciudad where descripcion="+Descripcion;
        Query queryRegistros = em.createNativeQuery(query, Ciudad.class);
        List<Ciudad> listCiuda = (List<Ciudad>)queryRegistros.getResultList();
        //count = 0;
        for (Ciudad sucursal: listCiuda) {
            IdCiudad = sucursal.getIdCiudad().toString();
        }
        return IdCiudad; //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener el id de la ciudad
    @Override
    public String ciudad(String descripcion) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select ciudad.id_ciudad, ciudad.descripcion FROM activo.ciudad where descripcion = \""+descripcion+"\"", Ciudad.class);
            List<Ciudad> listHistorial = (List<Ciudad>)query.getResultList();
            for (Ciudad ciudad: listHistorial)
                json += ciudad.getIdCiudad();
            setIdCiudad(json);
            //json = json.substring(0, json.length() - 1);
            //json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the idCiudad
     */
    public String getIdCiudad() {
        return idCiudad;
    }

    /**
     * @param idCiudad the idCiudad to set
     */
    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }
}
