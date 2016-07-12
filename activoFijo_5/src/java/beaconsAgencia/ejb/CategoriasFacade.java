/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Categoria;
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
public class CategoriasFacade extends AbstractFacade<Categoria> implements CategoriasFacadeLocal {

    @PersistenceContext(unitName="beaconsAgenciaPU")
    private EntityManager em;
    
    public CategoriasFacade() {
        super(Categoria.class);
    }

    public CategoriasFacade(Class<Categoria> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
        //To change body of generated methods, choose Tools | Templates.
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

    
    //Método para mostrar los registros de la tabla Categorias
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM categoria " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from categoria " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Categoria.class);

            List<Categoria> listPerfil = (List<Categoria>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (Categoria perfil: listPerfil) {
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

    
    //Método para gusradr o actualizar según sea el caso
    @Override
    public String persistir(Categoria categoria, String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(categoria);
                    break;
                case "editar":
                    respuesta=mergeEntity(categoria);
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
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Metodo para generar combo de las categorias desponibles
    @Override
    public String combo(String tipo, String estado) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from categoria", Categoria.class);
            List<Categoria> listPersonal = (List<Categoria>)query.getResultList();
            for (Categoria estados: listPersonal)
                json += "{\"id\":\"" + estados.getIdCategoria()+ "\",\"text\":\"" +estados.getDescripcion()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener el nombre de la categoria
    @Override
    public String filtro(String id) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select categoria.id_categoria, categoria.descripcion FROM activo.categoria where id_categoria = \""+id+"\"", Categoria.class);
            List<Categoria> listHistorial = (List<Categoria>)query.getResultList();
            for (Categoria categoria: listHistorial)
                json += categoria.getDescripcion();
            //json = json.substring(0, json.length() - 1);
            //json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    /*@Override
    public String idCategoria(String filtro, String valor, String operador) {
        String json = "";
        int count;
        try {
            if(operador.equals("neq")){
                operador="!=";
                json +=" IN(";
                Query query =em.createNativeQuery("SELECT * FROM CATEGORIA WHERE DESCRIPCION "+operador+" "+"\""+valor+"\"", Categoria.class);
                List<Categoria> listHistorial = (List<Categoria>)query.getResultList();
                count = query.getResultList().size();
                for (Categoria oficinas: listHistorial)
                    json += oficinas.getIdCategoria()+",";
                if(count > 0)
                    json = json.substring(0, json.length() - 1);
                json +=") ";
            }
            if(operador.equals("eq")){
                operador="=";
                json +=" IN(";
                Query query =em.createNativeQuery("SELECT * FROM CATEGORIA WHERE DESCRIPCION "+operador+" "+"\""+valor+"\"", Categoria.class);
                List<Categoria> listHistorial = (List<Categoria>)query.getResultList();
                count = query.getResultList().size();
                for (Categoria oficinas: listHistorial)
                    json += oficinas.getIdCategoria()+",";
                if(count > 0)
                    json = json.substring(0, json.length() - 1);
                json +=") ";
            }
            if(operador.equals("startswith") || operador.equals("contains") || operador.equals("endswith")){
                operador="like";
                json +=" IN(";
                Query query =em.createNativeQuery("SELECT * FROM CATEGORIA WHERE DESCRIPCION "+operador+" "+"\""+valor+"\"", Categoria.class);
                List<Categoria> listHistorial = (List<Categoria>)query.getResultList();
                count = query.getResultList().size();
                for (Categoria oficinas: listHistorial)
                    json += oficinas.getIdCategoria()+",";
                if(count > 0)
                    json = json.substring(0, json.length() - 1);
                json +=") ";
            }
            if(operador.equals("doesnotcontain")){
                operador="not like";
                json +=" IN(";
                Query query =em.createNativeQuery("SELECT * FROM CATEGORIA WHERE DESCRIPCION "+operador+" "+"\""+valor+"\"", Categoria.class);
                List<Categoria> listHistorial = (List<Categoria>)query.getResultList();
                count = query.getResultList().size();
                for (Categoria oficinas: listHistorial)
                    json += oficinas.getIdCategoria()+",";
                if(count > 0)
                    json = json.substring(0, json.length() - 1);
                json +=") ";
            }
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = " ";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    @Override
    public String idCategoria(String descripcion) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select categoria.id_categoria, categoria.descripcion FROM activo.categoria where descripcion = \""+descripcion+"\"", Categoria.class);
            List<Categoria> listHistorial = (List<Categoria>)query.getResultList();
            for (Categoria categoria: listHistorial)
                json += categoria.getIdCategoria();
            //json = json.substring(0, json.length() - 1);
            //json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }
}
