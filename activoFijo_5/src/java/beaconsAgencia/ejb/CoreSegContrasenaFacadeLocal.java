/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreSegContrasena;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreSegContrasenaFacadeLocal {

    void create(CoreSegContrasena coreSegContrasena);

    void edit(CoreSegContrasena coreSegContrasena);

    void remove(CoreSegContrasena coreSegContrasena);

    CoreSegContrasena find(Object id);

    List<CoreSegContrasena> findAll();

    List<CoreSegContrasena> findRange(int[] range);

    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    
    String persistir(CoreSegContrasena seguridad,String tipo);
    
    String validaClave(String logIn, String pass, int opcion);
    
    String validaVigencia(Date fechaValida);
    
}
