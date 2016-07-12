/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import coreapp.clases.general.FCom;
import beaconsAgencia.entities.CoreReglaSeguridad;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreReglaSeguridadFacade extends AbstractFacade<CoreReglaSeguridad> implements CoreReglaSeguridadFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreReglaSeguridadFacade() {
        super(CoreReglaSeguridad.class);
    }

    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    /**<code>select o fromId CoreCatalogoGeneral o</code>
     * @param reglaSeguridad
     * @param tipo
     * @return 
     */
    @Override
    public String persistir(CoreReglaSeguridad reglaSeguridad,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(reglaSeguridad);
                    break;
                case "editar":
                    respuesta=mergeEntity(reglaSeguridad);
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
    }  
    
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_regla_seguridad " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_regla_seguridad " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreReglaSeguridad.class);

            List<CoreReglaSeguridad> listCoreReglaSeguridad = (List<CoreReglaSeguridad>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreReglaSeguridad reglaSeguridad: listCoreReglaSeguridad) {
                json += reglaSeguridad.toString()+",";
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
    public String generaCombo(String dominio,String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from core_regla_seguridad ", CoreReglaSeguridad.class);
            List<CoreReglaSeguridad> listReglaSeguridad = (List<CoreReglaSeguridad>)query.getResultList();
            for (CoreReglaSeguridad reglaSeguridad: listReglaSeguridad)
                json += "{\"id\":" + reglaSeguridad.getCoreReglaSeguridadId()+ ",\"text\":\"" +reglaSeguridad.getNombre()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
    }  
    @Override
    public String exportarCsv(int rows, int page, String sort, String order, String search) {
        String txt;
        try {
            String query = "select * from core_regla_seguridad " + search + " order by " + sort + " " +order;
            Query queryRegistros = em.createNativeQuery(query, CoreReglaSeguridad.class);

            List<CoreReglaSeguridad> listCatalogo = (List<CoreReglaSeguridad>)queryRegistros.getResultList();
            txt = "";
            for (CoreReglaSeguridad catalogo: listCatalogo) {
                txt += catalogo.toString2()+"\t\n";
            }
        } catch (NumberFormatException e) {
            //FCom.printDebug(e.getMessage());
            txt = "e.getMessage()";
        }
        return txt;
    }
    @Override
    public String cargarCsv(InputStream contenidoArchivo) {
        String mensaje;
        String linea=null;
        int noLinea=0;
        StringBuilder errores=new StringBuilder();
        try {               
            BufferedReader br = new BufferedReader(new InputStreamReader( contenidoArchivo ));
            linea = br.readLine();
            while (linea != null) {
                noLinea++;
                linea = linea.replaceAll("\n\r", "").replaceAll("\n", "").replaceAll("\r", "");
                if (linea.length() > 0)
                {
                    String[] alinea=linea.split("\\|");
                    if(alinea.length>5 && alinea.length<8){
                        Boolean[] erroresCampos=new Boolean[6];
                        if(alinea[0].equals(""))
                            erroresCampos[0]=true;
                        else
                            erroresCampos[0]=alinea[0].matches("^[1-9 ]\\d*(\\.\\d+)?$");
                        erroresCampos[1]=alinea[1].matches("^[0-9a-zA-Z_,. ]{1,45}$");
                        boolean hasErrors=false;
                        String mensajeErrorCampos="";
                        for(int i=0;i<erroresCampos.length;i++)
                        {
                            if(!erroresCampos[i]){
                                hasErrors=true;
                                mensajeErrorCampos+= "Columna "+(i+1)+", ";
                            }
                        }
                        if(!hasErrors){
                            CoreReglaSeguridad coreReglaSeguridad;
                            if(alinea[0].trim().equals("")){
                                coreReglaSeguridad=new CoreReglaSeguridad();                                
                                coreReglaSeguridad.setNombre(alinea[1]);
                                em.persist(coreReglaSeguridad);
                            }
                            else{
                                coreReglaSeguridad=find(Integer.parseInt(alinea[0])); 
                                if(coreReglaSeguridad!=null){                            
                                    coreReglaSeguridad.setNombre(alinea[1]);
                                    em.merge(coreReglaSeguridad);
                                }else
                                    errores.append("Línea ").append(noLinea).append(": error no existe el id.");
                            }
                        }else
                            errores.append("Línea ").append(noLinea).append(": error de tipo en columnas(").append(mensajeErrorCampos).append(")</br>");
                    }else
                        errores.append("Línea ").append(noLinea).append(": error en número de columnas.</br>");
                }
                linea = br.readLine();
            }
            mensaje = "{\"success\":true,\"title\":\"\",\"msg\":\""+errores.toString()+"\"}";
        } catch (Exception e) {
            //e.printStackTrace();                
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    }  
    
}
