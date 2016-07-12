/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Empresa;
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
public class EmpresaFacade extends AbstractFacade<Empresa> implements EmpresaFacadeLocal {

    @PersistenceContext(unitName="beaconsAgenciaPU")
    private EntityManager em;
    public EmpresaFacade() {
        super(Empresa.class);
    }

    public EmpresaFacade(Class<Empresa> entityClass) {
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
        return em;
        //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")


    //Método para mostrar los registros de la tabla empresa
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM empresa " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from empresa " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Empresa.class);

            List<Empresa> listPerfil = (List<Empresa>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (Empresa perfil: listPerfil) {
                json += perfil.toString()+",";
            }
            if (count > 0)
                json = json.substring(0, json.length() - 1);
            json += "],\"total\":"+count+"}";
        } catch (NumberFormatException e) {
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para guardar o actualizar según sea el caso
    @Override
    public String persistir(Empresa empresa, String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(empresa);
                    break;
                case "editar":
                    respuesta=mergeEntity(empresa);
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

    
    //Método para generar combo de la empresa
    @Override
    public String generaCombo(String estado, String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            if(tipo.equals("SUPER-ADMINISTRADOR")){
                Query query1 =em.createNativeQuery("select * from empresa ", Empresa.class);
                List<Empresa> listPersona = (List<Empresa>)query1.getResultList();
                for (Empresa estados: listPersona)
                    json += "{\"id\":\"" + estados.getIdEmpresa()+ "\",\"text\":\"" +estados.getDescripcion()+ "\"},";
                json = json.substring(0, json.length() - 1);
                json += "]";
            }
            Query query =em.createNativeQuery("select * from empresa where id_empresa = \""+estado+"\"", Empresa.class);
            List<Empresa> listPersonal = (List<Empresa>)query.getResultList();
            for (Empresa estados: listPersonal)
                json += "{\"id\":\"" + estados.getIdEmpresa()+ "\",\"text\":\"" +estados.getDescripcion()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String idEmpresa(String idEmpresa) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select empresa.id_empresa, empresa.descripcion FROM activo.empresa where descripcion = \""+idEmpresa+"\"", Empresa.class);
            List<Empresa> listHistorial = (List<Empresa>)query.getResultList();
                for (Empresa empresa: listHistorial)
                    json += empresa.getIdEmpresa();
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
