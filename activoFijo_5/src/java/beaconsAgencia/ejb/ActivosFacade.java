/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Activo;
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
public class ActivosFacade extends AbstractFacade<Activo> implements ActivosFacadeLocal {

    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;
    public ActivosFacade() {
        super(Activo.class);
    }

    public ActivosFacade(Class<Activo> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em; //To change body of generated methods, choose Tools | Templates.
    }
    
    private Object mergeEntity(Object entity){
        return em.merge(entity);
    }
    
    private Object persistEntity(Object entity){
        em.persist(entity);
        return entity;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    //Método para guardar o editar el activo según sea el caso
    @Override
    public String persisitir(Activo activo, String tipo) {
        String mensaje;
        try{
            Object respuesta = null;
            switch(tipo){
                case "nuevo":
                    respuesta=persistEntity(activo);
                    break;
                case "editar":
                    respuesta=mergeEntity(activo);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
        }catch(ConstraintViolationException e){
            mensaje = "{\"success\":false,\"title\":\"Error en la persistencia de datos\",\"msg\":\""
                    + "Favor de comuniarse con el administrador\"}";
        }
        return mensaje;//To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para generar combo del activo de acuerdo a la oficina
    
    @Override
    public String generaCombo(String estado, String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from activo where id_departamento = "+estado, Activo.class);
            List<Activo> listActivo = (List<Activo>)query.getResultList();
            for (Activo activo: listActivo)
                json += "{\"id\":\"" + activo.getIdActivo()+ "\",\"text\":\"" +activo.getNumeroSerie()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para mostrar los activos
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1)*rows);
        try{
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM activo " + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from activo " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Activo.class);
            List<Activo> listPerfil = (List<Activo>) queryRegistros.getResultList();
            json = "{\"data\":[";
            for(Activo activo: listPerfil){
                json = json + activo.toString()+",";
            }
            if(count > 0)
                json = json.substring(0, json.length() - 1);
            json = json + "],\"total\":"+count+"}";
        }catch(NumberFormatException e){
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String idActivo(String numeroSerie) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select * from activo where numero_serie = \""+numeroSerie+"\"", Activo.class);
            List<Activo> listActivo = (List<Activo>)query.getResultList();
            for (Activo activo: listActivo)
                json += activo.getIdActivo();
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "no";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
