/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Imagenes;
import coreapp.clases.general.FCom;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Valar_Morgulis
 */
@Stateless
public class ImagenesFacade extends AbstractFacade<Imagenes> implements ImagenesFacadeLocal {
    @EJB
    ActivosFacadeLocal ActivoFaceLocal;
    @PersistenceContext(unitName="beaconsAgenciaPU")
    private EntityManager em;
    public ImagenesFacade() {
        super(Imagenes.class);
    }

    public ImagenesFacade(Class<Imagenes> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em; //To change body of generated methods, choose Tools | Templates.
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


    
    //Método para mostrar los registros de la tabla imágenes
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        int inicio = ((page - 1)* rows);
        try{
            Query queryTotal = em.createNativeQuery("SELECT COUNT(1) FROM IMAGENES " + search);
            int count = Integer.parseInt(queryTotal.getSingleResult().toString());
            String query = "select * from imagenes " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, Imagenes.class);
            List<Imagenes> listPerfil = (List<Imagenes>) queryRegistros.getResultList();
            json = "{\"data\":[";
            for(Imagenes imagenes: listPerfil) {
                json = json + imagenes.toString()+",";
            }
            if (count > 0)
                json = json.substring(0,json.length() - 1);
            json = json + "],\"total\":"+count+"}";
        }catch(NumberFormatException e) {
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Método para guardar o actualizar según sea el caso
    @Override
    public String persistir(Imagenes imagenes, String estado) {
        String mensaje;
        try {
            Object respuesta = null;
            switch (estado) {
                case "nuevo":
                    respuesta=persistEntity(imagenes);
                    break;
                case "editar":
                    respuesta=mergeEntity(imagenes);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
        }catch(ConstraintViolationException e){
            mensaje = "{\"success\":false,\"title\":\"Error en la persistencia de datos\",\"msg\":\""
                    + "Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    //Metodo para guardar las imágenes del activo en la tabla imágenes
    @Override
    public String subirImagen(byte[] data, Object idImg) {
        String mensaje;
        try {
            Object respuesta;
            Imagenes imagen = em.find(Imagenes.class, idImg);//new Imagenes();//=em.find(Imagenes.class, idImg);
            //imagen.setIdActivo ( ActivoFaceLocal.find(idImg) );
            imagen.setImagen(data);
            respuesta= mergeEntity(imagen);//persistir(imagen, "nuevo"); //mergeEntity(imagenes);
            if(respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
        }catch(Exception e){
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\""+e.getMessage()+"\"}";
        }
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

}
