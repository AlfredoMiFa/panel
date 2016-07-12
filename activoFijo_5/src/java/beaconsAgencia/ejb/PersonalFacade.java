/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Estado;
import beaconsAgencia.entities.Personal;
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
public class PersonalFacade extends AbstractFacade<Personal> implements PersonalFacadeLocal {

    @PersistenceContext(unitName="beaconsAgenciaPU")
    private EntityManager em;
    public PersonalFacade() {
        super(Personal.class);
    }

    public PersonalFacade(Class<Personal> entityClass) {
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    //Método para mostrar los registros de la tabla personal
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1) * rows);
        try{
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM PERSONAL " + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from personal " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Personal.class);
            List<Personal> listPerfil = (List<Personal>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for(Personal personal: listPerfil){
                json = json + personal.toString()+",";
            }
            if (count > 0)
                json = json.substring(0, json.length() - 1);
            json = json + "],\"total\":"+count+"}";
        }catch(NumberFormatException e){
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para guardar o actualizar según sea el caso
    @Override
    public String persistir(Personal personal, String tipo) {
        String mensaje;
        try{
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(personal);
                    break;
                case "editar":
                    respuesta=mergeEntity(personal);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
        }catch(ConstraintViolationException e){
            mensaje = "{\"success\":false,\"title\":\"Error en persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para generar combo del personal de acuerdo a la oficina
    @Override
    public String generaCombo(String estado, String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from personal where id_departamento = "+estado, Personal.class);
            List<Personal> listActivos = (List<Personal>)query.getResultList();
            for (Personal personal: listActivos)
                json += "{\"id\":\"" + personal.getIdPersonal()+ "\",\"text\":\"" +personal.getNombre()+ " " +personal.getApellidoPaterno()+" "+ personal.getApellidoMaterno() + "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para consultar el id y descripción de la tabla estado
    @Override
    public String generaComboPorCpp(String cpp) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select estado.id_estado, estado.descripcion from estado "
                    + "Left outer join codigo_postales on (codigo_postales.estado = estado.descripcion) "+
                    " where codigo_postales.cpp=\""+cpp+"\"", Estado.class);                        
            List<Estado> listRegistros = (List<Estado>)query.getResultList();
            Estado estadoConsultado = listRegistros.get(0);
                json += "{\"id\":\"" + estadoConsultado.getIdEstado()+ "\",\"text\":\"" +estadoConsultado.getDescripcion()+ "\"},";            
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;  //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener el id del personal de acuerdo a la oficina
    @Override
    public String personal(String idOficina) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_personal, nombre FROM activo.personal where id_oficina = \""+idOficina+"\"", Personal.class);
            List<Personal> listHistorial = (List<Personal>)query.getResultList();
            int count = query.getResultList().size();
            //json = "{\"data\":[";
            for(Personal perfil: listHistorial){
                json += perfil.getIdPersonal()+",";
            }
            if(count > 0)
                json = json.substring(0, json.length() - 1);
            //json += "],\"total\":"+count+"}";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String idPersonal(String nombre) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_personal, nombre FROM activo.personal where nombre = \""+nombre+"\"", Personal.class);
            List<Personal> listHistorial = (List<Personal>)query.getResultList();
            for (Personal oficinas: listHistorial)
                json += oficinas.getIdPersonal();
            //json = json.substring(0, json.length() - 1);
            //json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
