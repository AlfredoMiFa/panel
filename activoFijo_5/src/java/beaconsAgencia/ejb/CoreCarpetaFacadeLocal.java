/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreCarpeta;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreCarpetaFacadeLocal {

    void create(CoreCarpeta carpeta);

    void edit(CoreCarpeta carpeta);

    void remove(CoreCarpeta carpeta);

    CoreCarpeta find(Object id);

    List<CoreCarpeta> findAll();

    List<CoreCarpeta> findRange(int[] range);

    int count();
    
    List<CoreCarpeta> getListaCarpetas(Integer usuarioId);
    
    String persistir(CoreCarpeta derecho,String tipo);
    
}
