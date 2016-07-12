/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreExportQuery;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreExportQueryFacadeLocal {

    void create(CoreExportQuery coreExportQuery);

    void edit(CoreExportQuery coreExportQuery);

    void remove(CoreExportQuery coreExportQuery);

    CoreExportQuery find(Object id);

    List<CoreExportQuery> findAll();

    List<CoreExportQuery> findRange(int[] range);

    int count();
    
    String persistir(CoreExportQuery coreExportQuery,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
}
