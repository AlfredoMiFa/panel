/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import coreapp.clases.general.FCom;
import beaconsAgencia.entities.CoreSegContrasena;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreSegContrasenaFacade extends AbstractFacade<CoreSegContrasena> implements CoreSegContrasenaFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreSegContrasenaFacade() {
        super(CoreSegContrasena.class);
    }
    
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }
    
    @Override
    public String persistir(CoreSegContrasena seguridad,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(seguridad);
                    break;
                case "editar":
                    respuesta=mergeEntity(seguridad);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Operación no encontrada.\",\"msg\":\"Esta tipo de operación no es valida.\"}";
        } catch (ConstraintViolationException e) {               
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    }
    
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_seg_contrasena " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_seg_contrasena " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreSegContrasena.class);

            List<CoreSegContrasena> listPerfil = (List<CoreSegContrasena>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreSegContrasena perfil: listPerfil) {
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
    }
    @Override
    public String validaClave(String logIn, String pass, int opcion){
        // opcion = 0. solo verifica log pass
        // opcion = 1. verifica longitud pass y login
        List<CoreSegContrasena> coreSegContrasenaList=findAll();
        if(coreSegContrasenaList.size()>0){
            coreSegContrasenaList.get(0);
             int lonLogIn = coreSegContrasenaList.get(0).getLongusuario();
             int lonPass  = coreSegContrasenaList.get(0).getLongcontrasena();
             boolean requiereNum = coreSegContrasenaList.get(0).getRecnum();
             boolean requiereCar = coreSegContrasenaList.get(0).getReccar();
             int vigenciaPass = coreSegContrasenaList.get(0).getVigencia();
             if ((opcion != 0)&&(logIn.length() < lonLogIn)){
                 return "{\"success\":false,\"title\":\"\",\"msg\":\"Longitud mínima para login no satisfecha (minimo: "+ String.valueOf(lonLogIn) + ")...\"}";
             }
             if (pass.length() < lonPass) {
                 return "{\"success\":false,\"title\":\"\",\"msg\":\"Longitud mínima para password no satisfecha (mínimo: "+ String.valueOf(lonPass) + ")...\"}";
             }
             if (pass.toUpperCase().contains(logIn.toUpperCase())){
                 return "{\"success\":false,\"title\":\"\",\"msg\":\"El login no pude formar parte del password...\"}";
             }
             if (requiereNum){
                 if  (!( pass.toUpperCase().contains("0") || 
                    pass.toUpperCase().contains("1") || 
                    pass.toUpperCase().contains("2") ||
                    pass.toUpperCase().contains("3") ||
                    pass.toUpperCase().contains("4") ||
                    pass.toUpperCase().contains("5") ||
                    pass.toUpperCase().contains("6") ||
                    pass.toUpperCase().contains("7") ||
                    pass.toUpperCase().contains("8") ||
                    pass.toUpperCase().contains("9"))){
                    return "{\"success\":false,\"title\":\"\",\"msg\":\"El password debe contener por lo menos un número (0..9)\"}";
                 }
             }
             if (requiereCar){
                 if  (!( pass.toUpperCase().contains("$") || 
                    pass.toUpperCase().contains("%") || 
                    pass.toUpperCase().contains("!") ||
                    pass.toUpperCase().contains("?") ||
                    pass.toUpperCase().contains("@") ||
                    pass.toUpperCase().contains("#") ||
                    pass.toUpperCase().contains("/") ||
                    pass.toUpperCase().contains("¿") ||
                    pass.toUpperCase().contains("¡") ||
                    pass.toUpperCase().contains("&"))){
                    return "{\"success\":false,\"title\":\"\",\"msg\":\"El password debe contener por lo menos un caracter especial ($,%,!,?,¿,¡,@,#,/,&)\"}";
                 }
             }
             return "{\"success\":true\",\"title\":\"Operación exitosa\",\"msg\":\"\"}";
        }else
            return "{\"success\":false,\"title\":\"\",\"msg\":\"No existen politicas de seguridad de clave....\"}";
    }    
    
    @Override
    public String validaVigencia(Date fechaValida){
        List<CoreSegContrasena> coreSegContrasenaList=findAll();
        if(coreSegContrasenaList.size()>0){
            coreSegContrasenaList.get(0);
             int vigenciaPass = coreSegContrasenaList.get(0).getVigencia();
             Date now=new Date();
             long diffechas=(now.getTime()-fechaValida.getTime())/(24 * 60 * 60 * 1000);
             if(diffechas<vigenciaPass)
                return "{\"success\":true\",\"title\":\"Operación exitosa\",\"msg\":\"\"}";
             else
                return "{\"success\":false,\"title\":\"\",\"msg\":\"La vigencia ha sido vencida....\"}";
        }else
            return "{\"success\":false,\"title\":\"\",\"msg\":\"No existen politicas de seguridad de clave....\"}";
    }  
}
