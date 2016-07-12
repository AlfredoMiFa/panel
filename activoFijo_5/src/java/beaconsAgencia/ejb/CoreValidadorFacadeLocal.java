/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import beaconsAgencia.entities.CoreValidador;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreValidadorFacadeLocal {

    void create(CoreValidador coreValidador);

    void edit(CoreValidador coreValidador);

    void remove(CoreValidador coreValidador);

    CoreValidador find(Object id);

    List<CoreValidador> findAll();

    List<CoreValidador> findRange(int[] range);

    int count();
    
    String persistir(CoreValidador validador,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    String subirClase(byte[] data,Object validadorId, String nombreArchivo);
    
}
