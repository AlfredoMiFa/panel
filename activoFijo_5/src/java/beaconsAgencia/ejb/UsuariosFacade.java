/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.entities.Usuarios;
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
public class UsuariosFacade extends AbstractFacade<Usuarios> implements UsuariosFacadeLocal {

    @PersistenceContext (unitName = "beaconsAgenciaPU")
    private EntityManager em;
    public UsuariosFacade() {
        super(Usuarios.class);
    }

    public UsuariosFacade(Class<Usuarios> entityClass) {
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

    

    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1)* rows);
        try{
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM usuarios" + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from usuarios " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Usuarios.class);
            List<Usuarios> listUsuarios = (List<Usuarios>) queryRegistros.getResultList();
            json = "{\"data\":[";
            for(Usuarios usuarios: listUsuarios){
                json = json + usuarios.toString()+",";
            }
            if(count > 0)
                json = json.substring(0, json.length() - 1);
            json = json + "],\"total\":"+count+"}";
        }catch(NumberFormatException e){
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String persistir(Usuarios usuarios, String tipo) {
        String mensaje;
        try{
            Object respuesta = null;
            switch(tipo){
                case "nuevo":
                    respuesta=persistEntity(usuarios);
                    break;
                case "editar":
                    respuesta=mergeEntity(usuarios);
                    break;
            }
            if(respuesta != null)
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
    public String generaCombo(String estado, String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from usuarios", Usuarios.class);
            List<Usuarios> listHistorial = (List<Usuarios>)query.getResultList();
            for (Usuarios usuarios: listHistorial)
                json += "{\"id\":\"" + usuarios.getIdUsuario()+ "\",\"text\":\"" +usuarios.getUsuario()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Usuarios> queryUsuariosFindByUserName(Object usuario) {
        return em.createNamedQuery("Usuarios.findByUsuario").setParameter("usuario", usuario).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Usuarios> queryUsuariosFindByEmail(Object email) {
        return em.createNamedQuery("Usuarios.findByEmail").setParameter("email", email).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObjetoSesion queryUsuariosFindByUserContrasenia(Object usuario, Object contasenia, String cadenEncriptacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
