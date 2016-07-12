/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreDocumento;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreDocumentoFacadeLocal {

    void create(CoreDocumento documento);

    void edit(CoreDocumento documento);

    void remove(CoreDocumento documento);

    CoreDocumento find(Object id);

    List<CoreDocumento> findAll();

    List<CoreDocumento> findRange(int[] range);

    int count();
    
     String persistir(CoreDocumento documento,String tipo) ;
     
     CoreDocumento findDocumentoByCarpetaNombre(CoreDocumento documento);
     
     String select(int rows, int page, String sort, String order, String search);
    
}
