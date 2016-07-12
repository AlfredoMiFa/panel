/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreAviso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreAvisoFacadeLocal {

    void create(CoreAviso coreAviso);

    void edit(CoreAviso coreAviso);

    void remove(CoreAviso coreAviso);

    CoreAviso find(Object id);

    List<CoreAviso> findAll();

    List<CoreAviso> findRange(int[] range);

    int count();
    
    String persistir(CoreAviso mensaje,String tipo);
    
    String navisos(String search);
    
    String select(int rows, int page, String sort, String order, String search);
    
}
