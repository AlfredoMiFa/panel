/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Personal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface PersonalFacadeLocal {
    
    void create(Personal personal);
    void edit(Personal personal);
    void remove(Personal personal);
    
    Personal find(Object id);
    List<Personal> findAll();
    List<Personal> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persistir(Personal personal, String tipo);
    String generaCombo(String estado, String tipo);
    String generaComboPorCpp(String cpp);
    String personal(String idOficina);
    String idPersonal(String nombre);
}
