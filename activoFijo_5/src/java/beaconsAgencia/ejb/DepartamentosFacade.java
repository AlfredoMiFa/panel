/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Departamento;
import coreapp.clases.general.FCom;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Servamp
 */
@Stateless
public class DepartamentosFacade extends AbstractFacade<Departamento> implements DepartamentosFacadeLocal {

    
    @PersistenceContext(unitName="beaconsAgenciaPU")
    private EntityManager em;
    
    public DepartamentosFacade() {
        super(Departamento.class);
    }

    public DepartamentosFacade(Class<Departamento> entityClass) {
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1) * rows);
        try{
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM DEPARTAMENTO " + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from departamento " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Departamento.class);
            List<Departamento> listPerfil = (List<Departamento>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for(Departamento departamento: listPerfil){
                json = json + departamento.toString()+",";
            }
            if (count > 0)
                json = json.substring(0, json.length() - 1);
            json = json + "],\"total\":"+count+"}";
        }catch(NumberFormatException e){
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String persistir(Departamento departamento, String tipo) {
        String mensaje;
        try{
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(departamento);
                    break;
                case "editar":
                    respuesta=mergeEntity(departamento);
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String generaCombo(String estado, String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from departamento where id_oficina = "+estado, Departamento.class);
            List<Departamento> listActivo = (List<Departamento>)query.getResultList();
            for (Departamento departamento: listActivo)
                json += "{\"id\":\"" + departamento.getIdDepartamento()+ "\",\"text\":\"" +departamento.getDescripcion()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String idDepartamento(String descripcion) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_departamento, descripcion FROM departamento where descripcion = \""+descripcion+"\"", Departamento.class);
            List<Departamento> listDepartamento = (List<Departamento>)query.getResultList();
            for (Departamento departamentos : listDepartamento)
                json += departamentos.getIdDepartamento();
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String idOficina(String idDepartamento) {
        String json = "";
        try {
            Query query = em.createNativeQuery("SELECT ID_DEPARTAMENTO, ID_OFICINA FROM DEPARTAMENTO WHERE ID_DEPARTAMENTO="+idDepartamento,Departamento.class);
            List<Departamento> listDepartamento = (List<Departamento>)query.getResultList();
            for (Departamento departamentos : listDepartamento)
                json += departamentos.getIdOficina().getIdOficina();
        } catch (Exception e){
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String filtro(String departamento, String oficina) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_departamento, descripcion FROM departamento where descripcion = \""+departamento+"\""+" AND ID_OFICINA=\""+oficina+"\"", Departamento.class);
            List<Departamento> listDepartamento = (List<Departamento>)query.getResultList();
            for (Departamento departamentos : listDepartamento)
                json += departamentos.getIdDepartamento();
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String nombre(String idDepartamento) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_departamento, descripcion FROM departamento where id_departamento = \""+idDepartamento+"\"", Departamento.class);
            List<Departamento> listDepartamento = (List<Departamento>)query.getResultList();
            for (Departamento departamentos : listDepartamento)
                json += departamentos.getDescripcion();
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
