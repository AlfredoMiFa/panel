/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;


import beaconsAgencia.entities.Oficinas;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface OficinasFacadeLocal {
   
    void create(Oficinas oficinas);
    void remove(Oficinas oficinas);
    void edit(Oficinas oficinas);
    
    Oficinas find(Object id);
    List<Oficinas> findAll();
    List<Oficinas> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persistir(Oficinas oficinas, String tipo);
    String generaCombo(String estado, String tipo, String idEmpresa);
    String generaCpp(String cpp);
    String generaComboPorEmpresa(String ciudad, String tipo);
    String selectOficina(String idEmpresa);
    String nombreOficina(String idOficina);
    String oficinas(String idOficinas);
    String oficinaPorEmpresa(String idEmpresa);
    String idOficina(String nombre);
}
