/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreDashboard;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreDashboardFacadeLocal {

    void create(CoreDashboard coreDashboard);

    void edit(CoreDashboard coreDashboard);

    void remove(CoreDashboard coreDashboard);

    CoreDashboard find(Object id);

    List<CoreDashboard> findAll();

    List<CoreDashboard> findRange(int[] range);

    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    
    String persistir(CoreDashboard coreDashboard,String tipo);
    
    List<CoreDashboard> getDashboardList(Integer corePerfilId);
    
}
