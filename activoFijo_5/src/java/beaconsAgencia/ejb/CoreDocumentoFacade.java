/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import coreapp.clases.general.FCom;
import beaconsAgencia.entities.CoreDocumento;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreDocumentoFacade extends AbstractFacade<CoreDocumento> implements CoreDocumentoFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    public CoreDocumentoFacade() {
        super(CoreDocumento.class);
    }
    @Override
    public CoreDocumento findDocumentoByCarpetaNombre(CoreDocumento documento){
        List<CoreDocumento> listaDocumento=em.createNativeQuery("select * from core_documento where core_carpeta_id="+documento.getCoreCarpetaId().getCoreCarpetaId()+" and nombre_archivo='"+documento.getNombreArchivo()+"'",CoreDocumento.class).getResultList();
        if(listaDocumento.size()>0)
            return listaDocumento.get(0);
        else
            return null;
    }
    
    @Override
    public String select(int rows, int page, String sort, String order, String search){
        String json;
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_documento " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_documento " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreDocumento.class);

            List<CoreDocumento> listDocumento = (List<CoreDocumento>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreDocumento documento: listDocumento) {
                json += documento.toString()+",";
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
    public String persistir(CoreDocumento documento,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(documento);
                    em.flush();
                    break;
                case "editar":
                    respuesta=mergeEntity(documento);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa\",\"msg\":\"\",\"id\":"+((CoreDocumento)respuesta).getCoreDocumentoId()+"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"Operación no encontrada.\"}";
        } catch (ConstraintViolationException e) {
            //e.printStackTrace();                
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"Error de persistencia de datos\"}";
        }
        return mensaje;
    }
    
}
