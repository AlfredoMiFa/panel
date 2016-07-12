/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreReglaSeguridadDetalle;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreReglaSeguridadDetalleFacadeLocal {

    void create(CoreReglaSeguridadDetalle coreReglaSeguridadDetalle);

    void edit(CoreReglaSeguridadDetalle coreReglaSeguridadDetalle);

    void remove(CoreReglaSeguridadDetalle coreReglaSeguridadDetalle);

    CoreReglaSeguridadDetalle find(Object id);

    List<CoreReglaSeguridadDetalle> findAll();

    List<CoreReglaSeguridadDetalle> findRange(int[] range);

    int count();
    
    String persistir(CoreReglaSeguridadDetalle reglaSeguridad,String tipo);
    
    String select(int rows, int page, String sort, String order, String search) ;
    
    String getFiltroSeguridad(String ids);;
    
}
