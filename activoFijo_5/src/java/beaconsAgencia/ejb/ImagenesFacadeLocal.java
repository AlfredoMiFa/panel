/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Imagenes;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface ImagenesFacadeLocal {
    
    void remove(Imagenes imagenes);
    void create(Imagenes imagenes);
    void edit(Imagenes imagenes);
    
    Imagenes find(Object id);
    List<Imagenes> findAll();
    List<Imagenes> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persistir(Imagenes imagenes,String estado);
    String subirImagen(byte[] data, Object coreReportJasperId);
}
