/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreReglaSeguridad;
import java.io.InputStream;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreReglaSeguridadFacadeLocal {

    void create(CoreReglaSeguridad coreReglaSeguridad);

    void edit(CoreReglaSeguridad coreReglaSeguridad);

    void remove(CoreReglaSeguridad coreReglaSeguridad);

    CoreReglaSeguridad find(Object id);

    List<CoreReglaSeguridad> findAll();

    List<CoreReglaSeguridad> findRange(int[] range);

    int count();
    
    String persistir(CoreReglaSeguridad reglaSeguridad,String tipo) ;
    
    String select(int rows, int page, String sort, String order, String search);
    
    String generaCombo(String dominio,String tipo);
    
    String exportarCsv(int rows, int page, String sort, String order, String search);
    
    String cargarCsv(InputStream contenidoArchivo) ;
    
    
    
}
