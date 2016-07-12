/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.entities.CodigoPostales;
import beaconsAgencia.entities.Oficinas;
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
public class OficinasFacade extends AbstractFacade<Oficinas> implements OficinasFacadeLocal {

    @PersistenceContext(unitName="beaconsAgenciaPU")
    private EntityManager em;
    public OficinasFacade() {
        super(Oficinas.class);
    }

    public OficinasFacade(Class<Oficinas> entityClass) {
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

    
    //Método para mostrar los registros de la tabla oficinas
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1) * rows);
        try{
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM OFICINAS " + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from oficinas " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Oficinas.class);
            List<Oficinas> listPerfil = (List<Oficinas>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for(Oficinas oficinas: listPerfil){
                json = json + oficinas.toString()+",";
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
    public String persistir(Oficinas oficinas, String tipo) {
        String mensaje;
        try{
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(oficinas);
                    break;
                case "editar":
                    respuesta=mergeEntity(oficinas);
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

    
    //Método para generar combo de las oficinas
    @Override
    public String generaCombo(String idOficina, String grupoUsuario, String idEmpresa) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            if(grupoUsuario.equals("SUPERVISOR")){
                Query query =em.createNativeQuery("select * from oficinas where id_oficina= "+idOficina, Oficinas.class);
                List<Oficinas> listActivos = (List<Oficinas>)query.getResultList();
                for (Oficinas oficinas: listActivos)
                    json += "{\"id\":\"" + oficinas.getIdOficina()+ "\",\"text\":\"" +oficinas.getNombreOficina()+"\"},";
                json = json.substring(0, json.length() - 1);
                json += "]";
            }if(grupoUsuario.equals("ADMINISTRADOR")) {
                Query query =em.createNativeQuery("select * from oficinas where id_empresa= "+idEmpresa, Oficinas.class);
                List<Oficinas> listActivos = (List<Oficinas>)query.getResultList();
                for (Oficinas oficinas: listActivos)
                    json += "{\"id\":\"" + oficinas.getIdOficina()+ "\",\"text\":\"" +oficinas.getNombreOficina()+"\"},";
                json = json.substring(0, json.length() - 1);
                json += "]";
            }if(grupoUsuario.equals("SUPER-ADMINISTRADOR")) {
                Query query =em.createNativeQuery("select * from oficinas", Oficinas.class);
                List<Oficinas> listActivos = (List<Oficinas>)query.getResultList();
                for (Oficinas oficinas: listActivos)
                    json += "{\"id\":\"" + oficinas.getIdOficina()+ "\",\"text\":\"" +oficinas.getNombreOficina()+"\"},";
                json = json.substring(0, json.length() - 1);
                json += "]";
            }
            
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener el estado y ciudad de acuerdo al código postal
    @Override
    public String generaCpp(String cpp) {
        String mensaje;
        try {
            Query query =em.createNativeQuery(" SELECT codigo_postales.id_cpp,codigo_postales.estado, codigo_postales.municipio  FROM activo.codigo_postales WHERE CPP =\""+cpp+"\"", CodigoPostales.class );
            List <CodigoPostales> listRegistros = (List<CodigoPostales>)query.getResultList();
            if (listRegistros.size()== 0)
                mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"El codigo postal seleccionado no existe.\"}";
            else {
                CodigoPostales cppConsultado = listRegistros.get(0);
                mensaje = "{\"success\":true,\"title\":\"\",\"msg\":\"Codigo postal existente \" ,\"estado\":\""+ cppConsultado .getEstado()+"\", \"ciudad\":\""+ cppConsultado .getMunicipio()+"\"}";
            }
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Error al verificar el dato\"}";
        }
        return mensaje;        
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para generar combo de acuerdo a la empresa
    @Override
    public String generaComboPorEmpresa(String ciudad, String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from oficinas where id_empresa = "+ciudad, Oficinas.class);
            List<Oficinas> listActivos = (List<Oficinas>)query.getResultList();
            for (Oficinas oficinas: listActivos)
                json += "{\"id\":\"" + oficinas.getIdOficina()+ "\",\"text\":\"" +oficinas.getNombreOficina()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener los ids de las oficinas de acuerdo a la empresa del usuario
    @Override
    public String selectOficina(String idEmpresa) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select oficinas.id_oficina, oficinas.nombre_oficina FROM activo.oficinas where id_empresa = \""+idEmpresa+"\"", Oficinas.class);
            List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
            for(int i=0; i<query.getResultList().size();i++){
                for (Oficinas oficinas: listHistorial)
                    json += oficinas.getIdOficina();
            }
            //json = json.substring(0, json.length() - 1);
            //json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para obtener el nombre de la oficina de acuerdo a su id
    @Override
    public String nombreOficina(String idOficina) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select oficinas.id_oficina, oficinas.nombre_oficina FROM activo.oficinas where id_oficina = \""+idOficina+"\"", Oficinas.class);
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

    
    //Método para obtener el id de la oficina de acuerdo a la empresa
    @Override
    public String oficinas(String idOficinas) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select id_oficina, nombre_oficina FROM activo.oficinas where id_empresa = \""+idOficinas+"\"", Oficinas.class);
            List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
            int count = query.getResultList().size();
            //json = "{\"data\":[";
            for(Oficinas perfil: listHistorial){
                json += perfil.getIdOficina()+",";
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

    
    //Método para obtener el los ids de las oficinas de acuerdo a la empresa del usuario
    @Override
    public String oficinaPorEmpresa(String idEmpresa) {
        String json = "";
        try {
            Query query = em.createNativeQuery("SELECT ID_OFICINA FROM OFICINAS WHERE ID_EMPRESA ="+idEmpresa, Oficinas.class);
            List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
            int count = query.getResultList().size();
            for(Oficinas perfil: listHistorial){
                json += perfil.getIdOficina()+",";
            }
            if(count > 0)
                json = json.substring(0, json.length() - 1);
            
        }catch(Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*@Override
    public String idOficina(String filtro, String valor, String operador, String empresa) {
        String json = "";
        int count;
        try {
            if(operador.equals("neq")){
                operador="!=";
                json +=" IN(";
                Query query =em.createNativeQuery("SELECT * FROM OFICINAS WHERE ID_EMPRESA ="+empresa+" AND NOMBRE_OFICINA "+operador+" \""+valor+"\"", Oficinas.class);
                List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
                count = query.getResultList().size();
                for (Oficinas oficinas: listHistorial)
                    json += oficinas.getIdOficina()+",";
                if(count > 0)
                    json = json.substring(0, json.length() - 1);
                json +=") ";
            }
            if(operador.equals("eq")){
                operador="=";
                json +=" IN(";
                Query query =em.createNativeQuery("SELECT * FROM OFICINAS WHERE NOMBRE_OFICINA "+operador+" \""+valor+"\"", Oficinas.class);
                List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
                count = query.getResultList().size();
                for (Oficinas oficinas: listHistorial)
                    json += oficinas.getIdOficina()+",";
                if(count > 0)
                    json = json.substring(0, json.length() - 1);
                json +=") ";
            }
            if(operador.equals("startswith") || operador.equals("contains") || operador.equals("endswith")){
                operador="like";
                json +=" IN(";
                Query query =em.createNativeQuery("SELECT * FROM OFICINAS WHERE NOMBRE_OFICINA "+operador+" \""+valor+"\"", Oficinas.class);
                List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
                count = query.getResultList().size();
                for (Oficinas oficinas: listHistorial)
                    json += oficinas.getIdOficina()+",";
                if(count > 0)
                    json = json.substring(0, json.length() - 1);
                json +=") ";
            }
            if(operador.equals("doesnotcontain")){
                operador="not like";
                json +=" IN(";
                Query query =em.createNativeQuery("SELECT * FROM OFICINAS WHERE NOMBRE_OFICINA "+operador+" \""+valor+"\"", Oficinas.class);
                List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
                count = query.getResultList().size();
                for (Oficinas oficinas: listHistorial)
                    json += oficinas.getIdOficina()+",";
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
    public String idOficina(String nombre) {
        String json = "";
        try {
            Query query =em.createNativeQuery("select oficinas.id_oficina, oficinas.nombre_oficina FROM activo.oficinas where nombre_oficina = \""+nombre+"\"", Oficinas.class);
            List<Oficinas> listHistorial = (List<Oficinas>)query.getResultList();
            for(int i=0; i<query.getResultList().size();i++){
                for (Oficinas oficinas: listHistorial)
                    json += oficinas.getIdOficina();
            }
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
