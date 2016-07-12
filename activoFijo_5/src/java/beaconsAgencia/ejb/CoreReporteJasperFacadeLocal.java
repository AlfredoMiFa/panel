/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreReporteJasper;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreReporteJasperFacadeLocal {

    void create(CoreReporteJasper coreReporteJasper);

    void edit(CoreReporteJasper coreReporteJasper);

    void remove(CoreReporteJasper coreReporteJasper);

    CoreReporteJasper find(Object id);

    List<CoreReporteJasper> findAll();

    List<CoreReporteJasper> findRange(int[] range);

    int count();
    
    String persistir(CoreReporteJasper reporte,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    String subirReporte(byte[] data,Object coreReporteJasperId, String nombreArchivo);
    
}
