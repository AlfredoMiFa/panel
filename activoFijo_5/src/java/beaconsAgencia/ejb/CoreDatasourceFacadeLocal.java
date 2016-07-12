/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreDatasource;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreDatasourceFacadeLocal {

    void create(CoreDatasource coreDatasource);

    void edit(CoreDatasource coreDatasource);

    void remove(CoreDatasource coreDatasource);

    CoreDatasource find(Object id);

    List<CoreDatasource> findAll();

    List<CoreDatasource> findRange(int[] range);

    int count();
    
    String persistir(CoreDatasource coreDatasource,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    String generaCombo();
    
}
