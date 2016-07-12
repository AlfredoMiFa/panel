/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Empresa;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface EmpresaFacadeLocal {
    
    void remove(Empresa empresa);
    void create(Empresa empresa);
    void edit(Empresa empresa);
    
    
    Empresa find(Object id);
    
    List<Empresa> findAll();
    
    List<Empresa> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persistir(Empresa empresa, String tipo);
    String generaCombo(String estado, String tipo);
    String idEmpresa(String idEmpresa);
}
