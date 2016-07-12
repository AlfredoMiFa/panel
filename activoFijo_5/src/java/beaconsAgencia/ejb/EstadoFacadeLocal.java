/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Estado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface EstadoFacadeLocal {
    
    void create(Estado estado);

    void edit(Estado estado);

    void remove(Estado estado);

    Estado find(Object id);

    List<Estado> findAll();

    List<Estado> findRange(int[] range);

    int count();
    
    
    String select(int rows, int page, String sort, String order, String search);
    
    String persistir(Estado estado,String tipo);
    
    String generaCombo(String estado, String tipo);
    
    String generaComboPorCpp(String Cpp); 
    
    String findByDescripcion(String Descripcion);
}
