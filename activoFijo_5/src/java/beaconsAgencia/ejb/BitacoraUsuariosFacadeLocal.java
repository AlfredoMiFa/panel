/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.BitacoraUsuarios;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface BitacoraUsuariosFacadeLocal {
    void create(BitacoraUsuarios bitacora);
    void edit(BitacoraUsuarios bitacora);
    void remove(BitacoraUsuarios bitacora);
    BitacoraUsuarios find(Object id);
    List<BitacoraUsuarios> findAll();
    List<BitacoraUsuarios> findRange(int[] range);
    int count();
    String select(int rows, int page, String sort, String order, String search);
    String persisitir(BitacoraUsuarios bitacora, String tipo);
    String bitacora(String fecha, String usuario, String actividad);
    String filtro(String fecha, String usuario, String actividad, String categoria, int id_usuario);
    String idOficina(String idOficina);
}
