/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import beaconsAgencia.entities.CoreCatalogoGeneral;
import coreapp.clases.general.FCom;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreCatalogoGeneralFacade extends AbstractFacade<CoreCatalogoGeneral> implements CoreCatalogoGeneralFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreCatalogoGeneralFacade() {
        super(CoreCatalogoGeneral.class);
    }

    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    /**<code>select o fromId CoreCatalogoGeneral o</code>
     * @param catalogoGeneral
     * @param tipo
     * @return 
     */
    @Override
    public String persistir(CoreCatalogoGeneral catalogoGeneral,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(catalogoGeneral);
                    break;
                case "editar":
                    respuesta=mergeEntity(catalogoGeneral);
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
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_catalogo_general " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_catalogo_general " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreCatalogoGeneral.class);

            List<CoreCatalogoGeneral> listCatalogo = (List<CoreCatalogoGeneral>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreCatalogoGeneral catalogo: listCatalogo) {
                json += catalogo.toString()+",";
            }
            if (count > 0)
                json = json.substring(0, json.length() - 1);
            json += "],\"total\":"+count+",\"filter\":\""+search+"\"}";
        } catch (NumberFormatException e) {
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0,\"filter\":\"\"}";
        }
        return json;
    }

    @Override
    public String generaCombo(String dominio,String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from core_catalogo_general where estatus='AC' and DOMINIO='"+dominio+"'", CoreCatalogoGeneral.class);
            List<CoreCatalogoGeneral> listCatalogos = (List<CoreCatalogoGeneral>)query.getResultList();
            for (CoreCatalogoGeneral catalogo: listCatalogos)
                json += "{\"id\":\"" + catalogo.getCoreCatalogoGeneralId() + "\",\"text\":\"" +catalogo.getValor() + "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
    }    
    @Override
    public String generaCombo1(String dominio,String tipo) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from core_catalogo_general where estatus='AC' and DOMINIO='"+dominio+"'", CoreCatalogoGeneral.class);
            List<CoreCatalogoGeneral> listCatalogos = (List<CoreCatalogoGeneral>)query.getResultList();
            for (CoreCatalogoGeneral catalogo: listCatalogos)
                json += "{\"id\":\"" + catalogo.getValor()+ "\",\"text\":\"" +catalogo.getAtributo1()+ "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
    }  
    @Override
    public String generaComboS(String dominio,String tipo) {
        String json = "[";
        try {
            Query query =em.createNativeQuery("select * from core_catalogo_general where estatus='AC' and DOMINIO='"+dominio+"'", CoreCatalogoGeneral.class);
            List<CoreCatalogoGeneral> listCatalogos = (List<CoreCatalogoGeneral>)query.getResultList();
            for (CoreCatalogoGeneral catalogo: listCatalogos)
                json += "{\"id\":\"" + catalogo.getCoreCatalogoGeneralId() + "\",\"text\":\"" +catalogo.getValor() + "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[]";
        }
        return json;
    }
    
    @Override
    public CoreCatalogoGeneral getAtributo(String dominio,String valor) {
        try {
            Query query =em.createNativeQuery("select * from core_catalogo_general where estatus='AC' and DOMINIO='"+dominio+"' and VALOR='"+valor+"'", CoreCatalogoGeneral.class);
            List<CoreCatalogoGeneral> listCatalogos = (List<CoreCatalogoGeneral>)query.getResultList();
            if (listCatalogos.size()>0)
                return listCatalogos.get(0);
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
        }
        return null;
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
                    if(alinea.length>6 && alinea.length<9){
                        Boolean[] erroresCampos=new Boolean[7];
                        if(alinea[0].equals(""))
                            erroresCampos[0]=true;
                        else
                            erroresCampos[0]=alinea[0].matches("^[1-9 ]\\d*(\\.\\d+)?$");
                        erroresCampos[1]=alinea[1].matches("^[\\w_]{1,30}$");
                        erroresCampos[2]=alinea[2].matches("^[0-9a-zA-ZÁáÉéÍíÓóÚúÑñüÜ_,. ]{1,100}$");
                        erroresCampos[3]=alinea[3].matches("^[0-9a-zA-ZÁáÉéÍíÓóÚúÑñüÜ_,. ]{1,100}$");
                        erroresCampos[4]=alinea[4].matches("^[0-9a-zA-ZÁáÉéÍíÓóÚúÑñüÜ_,. ]{0,100}$");
                        erroresCampos[5]=alinea[5].matches("[SN]");
                        erroresCampos[6]=alinea[6].matches("[AI][CN]");
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
                            CoreCatalogoGeneral catalogoGeneral;
                            if(alinea[0].trim().equals("")){
                                catalogoGeneral=new CoreCatalogoGeneral();                                
                                catalogoGeneral.setDominio(alinea[1]);
                                catalogoGeneral.setValor(alinea[2]);
                                catalogoGeneral.setAtributo1(alinea[3]);
                                catalogoGeneral.setAtributo2(alinea[4]);
                                catalogoGeneral.setEsBorrable(alinea[5]);
                                catalogoGeneral.setEstatus(alinea[6]);
                                em.persist(catalogoGeneral);
                            }
                            else{
                                catalogoGeneral=find(Integer.parseInt(alinea[0])); 
                                if(catalogoGeneral!=null){
                                    catalogoGeneral.setDominio(alinea[1]);
                                    catalogoGeneral.setValor(alinea[2]);
                                    catalogoGeneral.setAtributo1(alinea[3]);
                                    catalogoGeneral.setAtributo2(alinea[4]);
                                    catalogoGeneral.setEsBorrable(alinea[5]);
                                    catalogoGeneral.setEstatus(alinea[6]);
                                    em.merge(catalogoGeneral);
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
            mensaje = "{\"success\":true,\"title\":\"Operación exitosa\",\"msg\":\""+errores.toString()+"\"}";
        } catch (Exception e) {
            //e.printStackTrace();                
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    }  
    
}
