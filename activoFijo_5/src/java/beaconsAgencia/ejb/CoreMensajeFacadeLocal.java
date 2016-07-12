/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreMensaje;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreMensajeFacadeLocal {

    void create(CoreMensaje coreMensaje);

    void edit(CoreMensaje coreMensaje);

    void remove(CoreMensaje coreMensaje);

    CoreMensaje find(Object id);

    List<CoreMensaje> findAll();

    List<CoreMensaje> findRange(int[] range);

    int count();
    
    String persistir(CoreMensaje mensaje,String tipo);
    
    String nmensajes(String search);
    
    String select(int rows, int page, String sort, String order, String search) ;
    
}
