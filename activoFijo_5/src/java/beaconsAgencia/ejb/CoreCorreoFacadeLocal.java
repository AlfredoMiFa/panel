/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreCorreo;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreCorreoFacadeLocal {

     void create(CoreCorreo coreCorreo);

    void edit(CoreCorreo coreCorreo);

    void remove(CoreCorreo coreCorreo);

    CoreCorreo find(Object id);

    List<CoreCorreo> findAll();

    List<CoreCorreo> findRange(int[] range);

    int count();
    
    String persistir(CoreCorreo correo,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    String generaCombo();
    
}
