/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.Hashtable;
import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreParametrosGenerales;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreParametrosGeneralesFacadeLocal {

    void create(CoreParametrosGenerales coreParametrosGenerales);

    void edit(CoreParametrosGenerales coreParametrosGenerales);

    void remove(CoreParametrosGenerales coreParametrosGenerales);

    CoreParametrosGenerales find(Object id);

    List<CoreParametrosGenerales> findAll();

    List<CoreParametrosGenerales> findRange(int[] range);

    int count();
    
    String persistir(CoreParametrosGenerales catalogoGeneral,String tipo);
    
    String select(int rows, int page, String sort, String order, String search);
    
    Hashtable  getParametrosJasper();
    
    String getValor(String clave);
    
}
