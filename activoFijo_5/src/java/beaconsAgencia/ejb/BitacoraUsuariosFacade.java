/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.BitacoraUsuarios;
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
public class BitacoraUsuariosFacade extends AbstractFacade<BitacoraUsuarios> implements BitacoraUsuariosFacadeLocal {

    @PersistenceContext (unitName = "beaconsAgenciaPU")
    private EntityManager em;
    
    public BitacoraUsuariosFacade() {
        super(BitacoraUsuarios.class);
    }

    public BitacoraUsuariosFacade(Class<BitacoraUsuarios> entityClass) {
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

    
    //Método para mostrar los registros de la tabla BitacoraUsuarios
    @Override
    public String select(int rows, int page, String sort, String order, String search){
        String json;
        int inicio = ((page - 1)* rows);
        try{
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM bitacora_usuarios " + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from bitacora_usuarios " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, BitacoraUsuarios.class);
            List<BitacoraUsuarios> listBitacora = (List<BitacoraUsuarios>) queryRegistros.getResultList();
            json = "{\"data\":[";
            for(BitacoraUsuarios bitacora: listBitacora){
                json = json + bitacora.toString()+",";
            }
            if(count > 0)
                json = json.substring(0, json.length() - 1);
            json = json + "],\"total\":"+count+"}";
        }catch(NumberFormatException e){
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
    }

    
    //Método para guardar o editar según sea el caso
    @Override
    public String persisitir(BitacoraUsuarios bitacora, String tipo) {
        String mensaje;
        try{
            Object respuesta = null;
            switch(tipo){
                case "nuevo":
                    respuesta = persistEntity(bitacora);
                    break;
                case "editar":
                    respuesta = mergeEntity(bitacora);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación  exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"Operación  exitosa.\",\"msg\":\"\"}";
        }catch(ConstraintViolationException e){
            mensaje = "{\"success\":false,\"title\":\"Error en la presistencia de datos\",\"msg\":\""
                    + "Favor de comunicarse con el administrador\"}";
        }
        return mensaje; //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public String bitacora(String fecha, String usuario, String actividad) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select bitacora_usuarios.id_bitacora_usuario, bitacora_usuarios.cantidad_activos FROM activo.bitacora_usuarios where fecha =\""+fecha+"\""+
                        " AND actividad = \""+actividad+"\""+ " AND usuario = \"" +usuario+"\"", BitacoraUsuarios.class); 
            List<BitacoraUsuarios> listHistorial = (List<BitacoraUsuarios>)query.getResultList();
            for (BitacoraUsuarios categoria: listHistorial)
                json += categoria.getCantidadActivos();
        } catch(Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String filtro(String fecha, String usuario, String actividad, String categoria, int id_usuario) {
        BitacoraUsuarios bitacora = new BitacoraUsuarios();
        String mensaje = "";
        Object respuesta;
        try {
            bitacora.setIdUsuario(id_usuario);
            bitacora.setUsuario(usuario);
            bitacora.setActividad(actividad);
            bitacora.setCantidadActivos(1);
            bitacora.setCategoria(categoria);
            bitacora.setFecha(new Date());
            respuesta = persisitir(bitacora, "nuevo");
            if(respuesta != null) {
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            }else {
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            }
        }catch (Exception e) {
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\""+e.getMessage()+"\"}";
        }
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public String idOficina(String idOficina) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_oficina from oficinas where id_usuario = \""+idOficina+"\"", Oficinas.class);
            List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
            int count = query.getResultList().size();
            int idOfic;
            for (Oficinas bitacora: listHistorial)
                idOfic = bitacora.getIdOficina();
            
            if(count > 0)
                json = json.substring(0, json.length() - 1);
            if(count == 0 )
                json+= 0;
            //json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

