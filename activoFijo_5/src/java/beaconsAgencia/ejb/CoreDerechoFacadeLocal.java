/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreDerecho;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreDerechoFacadeLocal {

    void create(CoreDerecho coreDerecho);

    void edit(CoreDerecho coreDerecho);

    void remove(CoreDerecho coreDerecho);

    CoreDerecho find(Object id);

    List<CoreDerecho> findAll();

    List<CoreDerecho> findRange(int[] range);

    int count();
    
    String persistir(CoreDerecho derecho,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    String generaComboPadre(String filtro);
    
    String verificarUnico(String campo, String textoBuscar,String id);
    
}
