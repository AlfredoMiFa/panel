/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CorePermisosIp;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CorePermisosIpFacadeLocal {

    void create(CorePermisosIp corePermisosIp);

    void edit(CorePermisosIp corePermisosIp);

    void remove(CorePermisosIp corePermisosIp);

    CorePermisosIp find(Object id);

    List<CorePermisosIp> findAll();

    List<CorePermisosIp> findRange(int[] range);

    int count();
    
    String persistir(CorePermisosIp permisosIp,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    CorePermisosIp getPermisoIp(Integer coreUsuarioId,String ip);
    
}
