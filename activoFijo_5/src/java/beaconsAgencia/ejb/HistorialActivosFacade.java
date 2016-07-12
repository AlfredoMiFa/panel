/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.HistorialActivos;
import beaconsAgencia.entities.Oficinas;
import coreapp.clases.general.FCom;
import java.util.Date;
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
public class HistorialActivosFacade extends AbstractFacade<HistorialActivos> implements HistorialActivosFacadeLocal {

    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;
    public HistorialActivosFacade() {
        super(HistorialActivos.class);
    }

    public HistorialActivosFacade(Class<HistorialActivos> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
        //To change body of generated methods, choose Tools | Templates.
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

    
    //Método para mostrar los registros de la tabla HistorialActivos
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1)* rows);
        try {
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM HISTORIAL_ACTIVOS " + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from historial_activos " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, HistorialActivos.class);
            List<HistorialActivos> listPerfil = (List<HistorialActivos>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for(HistorialActivos perfil: listPerfil){
                json += perfil.toString()+",";
            }
            if(count > 0)
                json = json.substring(0, json.length() - 1);
            json += "],\"total\":"+count+"}";
        }catch(NumberFormatException e){
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para agregar o actualizar según sea el caso
    @Override
    public String persistir(HistorialActivos historial, String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo){
                case "nuevo":
                    respuesta = persistEntity(historial);
                    break;
                case "editar":
                    respuesta = mergeEntity(historial);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
        } catch (ConstraintViolationException e){
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para guardar el historial de acuerdo a las actividades echas por el usuario
    @Override
    public String activo(String oficina, String departamento, int id_incidencia, String incidencia, int id_activo, String activo, int id_usuario, String usuario, 
            String actividad, Date fecha, String movimiento, String observaciones) {
        String mensaje="";
        if(incidencia != null){
            try {
            Object respuesta;
            HistorialActivos historial = new HistorialActivos();
            historial.setOficina(oficina);
            historial.setDepartamento(departamento);
            historial.setIdIncidencia(id_incidencia);
            historial.setIncidencia(incidencia);
            historial.setIdActivo(id_activo);
            historial.setActivo(activo);
            historial.setIdUsuario(id_usuario);
            historial.setUsuario(usuario);
            historial.setActividad(actividad);
            historial.setFecha(fecha);
            historial.setTipoMovimiento(movimiento);
            historial.setObservaciones(observaciones);
            respuesta = persistir(historial, "nuevo");
            if(respuesta != null) {
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            }else {
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            }
        }catch(Exception e) {
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\""+e.getMessage()+"\"}";
        }
    }
        
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener el nombre de la oficina
    @Override
    public String oficina(String nombre) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select oficinas.id_oficina, oficinas.nombre_oficina FROM activo.oficinas where nombre_oficina = \""+nombre+"\"", Oficinas.class);
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

    
    //Método para filtrar el id del historial de acuerdo a la oficina
    @Override
    public String idHistorial(String id) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_historial_activo, activo FROM beacons_agencias.historial_activos where oficina = \""+id+"\"", HistorialActivos.class);
            List<HistorialActivos> listHistorial = (List<HistorialActivos>)query.getResultList();
            int count = query.getResultList().size();
            //json = "{\"data\":[";
            for(HistorialActivos perfil: listHistorial){
                json += perfil.getIdHistorialActivo()+",";
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
 
}
