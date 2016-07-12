/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.HistorialActivos;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface HistorialActivosFacadeLocal {
    
    void create(HistorialActivos historial);
    void edit(HistorialActivos historial);
    void remove(HistorialActivos historial);
    
    HistorialActivos find(Object id);
    List<HistorialActivos> findAll();
    List<HistorialActivos> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persistir(HistorialActivos historial, String tipo);
    String activo(String oficina, String departamento, int id_incidencia, String incidencia, int id_activo, String activo, int id_usuario, String usuario, 
            String actividad, Date fecha, String movimiento, String observaciones);
    String oficina(String nombre);
    String idHistorial(String id);
}
