/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Incidencias;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface IncidenciasFacadeLocal {
    
    void create(Incidencias incidencias);
    void edit(Incidencias incidencias);
    void remove(Incidencias incidencias);
    
    Incidencias find(Object id);
    List<Incidencias> findAll();
    List<Incidencias> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persistir(Incidencias incidencias, String tipo);
    String generaCombo(String estado, String tipo);
    String activo(String id);
    String oficina(String idActivo);
}
