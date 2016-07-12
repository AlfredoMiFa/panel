/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Ciudad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface CiudadFacadeLocal {
    
    void create(Ciudad ciudad);

    void edit(Ciudad ciudad);

    void remove(Ciudad ciudad);

    Ciudad find(Object id);

    List<Ciudad> findAll();
    
     
    
    List<Ciudad> findRange(int[] range);

    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    
    String persistir(Ciudad estado,String tipo);  
    
    String generaCombo(String dominio,String tipo);
    
    String generaComboPorCpp(String Cpp);
    
    String ColoniasCpp(String cpp) ;
    
    String findByDescripcion(String Descripcion);
    
    String ciudad(String descripcion);
}
