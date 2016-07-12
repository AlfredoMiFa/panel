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
import javax.validation.ConstraintViolationException;
import beaconsAgencia.entities.CoreCarpeta;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreCarpetaFacade extends AbstractFacade<CoreCarpeta> implements CoreCarpetaFacadeLocal {
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

    public CoreCarpetaFacade() {
        super(CoreCarpeta.class);
    }
    
    @Override
    public List<CoreCarpeta> getListaCarpetas(Integer usuarioId){
        List<CoreCarpeta> listaCarpetas=em.createNativeQuery("select * from core_carpeta where propietario_id is null or propietario_id="+usuarioId,CoreCarpeta.class).getResultList();
        return listaCarpetas;
    }
    
    @Override
    public String persistir(CoreCarpeta carpeta,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(carpeta);
                    em.flush();
                    break;
                case "editar":
                    respuesta=mergeEntity(carpeta);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"id\":"+((CoreCarpeta)respuesta).getCoreCarpetaId()+"}";
            else
                mensaje = "{\"success\":false,\"msg\":\"Operación no encontrada.\"}";
        } catch (ConstraintViolationException e) {
            //e.printStackTrace();                
            mensaje = "{\"success\":false,\"msg\":\"Error de persistencia de datos\"}";
        }
        return mensaje;
    }
    
}
