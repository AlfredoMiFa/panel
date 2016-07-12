/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreLayout;
import java.io.InputStream;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreLayoutFacadeLocal {

    void create(CoreLayout coreLayout);

    void edit(CoreLayout coreLayout);

    void remove(CoreLayout coreLayout);

    CoreLayout find(Object id);

    List<CoreLayout> findAll();

    List<CoreLayout> findRange(int[] range);

    int count();
    
    String persistir(CoreLayout layout,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    String probarLayout(InputStream inputStream,Integer idLayout);
    
}
