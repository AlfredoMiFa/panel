/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreDerechoSistema;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreDerechoSistemaFacadeLocal {

    void create(CoreDerechoSistema coreDerechoQv);

    void edit(CoreDerechoSistema coreDerechoQv);

    void remove(CoreDerechoSistema coreDerechoQv);

    CoreDerechoSistema find(Object id);

    List<CoreDerechoSistema> findAll();

    List<CoreDerechoSistema> findRange(int[] range);

    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    
    String persistir(CoreDerechoSistema derechoQv,String tipo);
    
    boolean hasDerecho(Integer perfilId,String derechoSistema);
    
}
