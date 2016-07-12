/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreConexionesDao;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreConexionesDaoFacadeLocal {

    void create(CoreConexionesDao coreConexionesDao);

    void edit(CoreConexionesDao coreConexionesDao);

    void remove(CoreConexionesDao coreConexionesDao);

    CoreConexionesDao find(Object id);

    List<CoreConexionesDao> findAll();

    List<CoreConexionesDao> findRange(int[] range);

    int count();
    
    String persistir(CoreConexionesDao conexion,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    String generaCombo();
    
    CoreConexionesDao getConexion(String clave);
}
