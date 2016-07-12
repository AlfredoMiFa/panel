/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.Bitacora;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface BitacoraFacadeLocal {

    void create(Bitacora bitacora);

    void edit(Bitacora bitacora);

    void remove(Bitacora bitacora);

    Bitacora find(Object id);

    List<Bitacora> findAll();

    List<Bitacora> findRange(int[] range);

    int count();
    
    String persistir(Bitacora bitacora,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
}
