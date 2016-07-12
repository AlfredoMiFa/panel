/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.io.InputStream;
import java.util.List;
import javax.ejb.Local;
import beaconsAgencia.entities.CoreCatalogoGeneral;

/**
 *
 * @author jgonzalezc
 */
@Local
public interface CoreCatalogoGeneralFacadeLocal {

    void create(CoreCatalogoGeneral coreCatalogoGeneral);

    void edit(CoreCatalogoGeneral coreCatalogoGeneral);

    void remove(CoreCatalogoGeneral coreCatalogoGeneral);

    CoreCatalogoGeneral find(Object id);

    List<CoreCatalogoGeneral> findAll();

    List<CoreCatalogoGeneral> findRange(int[] range);

    int count();
    
    String persistir(CoreCatalogoGeneral catalogoGeneral,String tipo);
    
    String select(int rows, int page, String sort, String order, String search) ;
    
    String generaCombo(String dominio,String tipo);
    
    String generaCombo1(String dominio,String tipo);
    
    String generaComboS(String dominio,String tipo);
    
    CoreCatalogoGeneral getAtributo(String dominio,String valor);
        
    String cargarCsv(InputStream contenidoArchivo);
    
}
