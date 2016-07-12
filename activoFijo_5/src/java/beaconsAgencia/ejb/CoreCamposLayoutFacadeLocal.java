/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreCamposLayout;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreCamposLayoutFacadeLocal {

    void create(CoreCamposLayout coreCamposLayout);

    void edit(CoreCamposLayout coreCamposLayout);

    void remove(CoreCamposLayout coreCamposLayout);

    CoreCamposLayout find(Object id);

    List<CoreCamposLayout> findAll();

    List<CoreCamposLayout> findRange(int[] range);

    int count();
    
    String persistir(CoreCamposLayout campoLayout,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
}
