/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Activo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface ActivosFacadeLocal {
    
    void create(Activo activo);
    void edit(Activo activo);
    void remove(Activo activo);
    
    Activo find(Object id);
    List<Activo> findAll();
    List<Activo> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persisitir(Activo activo, String tipo);
    String generaCombo(String estado, String tipo);
    String idActivo(String numeroSerie);
}
