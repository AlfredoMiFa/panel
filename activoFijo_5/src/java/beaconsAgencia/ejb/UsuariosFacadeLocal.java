/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.entities.Usuarios;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface UsuariosFacadeLocal {
    
    void create(Usuarios usuarios);
    void edit(Usuarios usuarios);
    void remove(Usuarios usuarios);
    
    Usuarios find(Object id);
    
    List<Usuarios> findAll();
    
    List<Usuarios> findRange(int[] range);
    
    int count();
    
    List<Usuarios> queryUsuariosFindByUserName(Object usuario);
    
    List<Usuarios> queryUsuariosFindByEmail(Object email);
    
    ObjetoSesion queryUsuariosFindByUserContrasenia(Object usuario, Object contasenia, String cadenEncriptacion);
    
    String select(int rows, int page, String sort, String order, String search);
    
    String persistir(Usuarios usuarios, String tipo);
    
    String generaCombo(String estado, String tipo);
}
