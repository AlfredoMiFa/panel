/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Departamento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Servamp
 */
@Local
public interface DepartamentosFacadeLocal {
    
    void remove(Departamento departamento);
    void create(Departamento departamento);
    void edit(Departamento departamento);
    
    Departamento find(Object id);
    
    List<Departamento> findAll();
    
    List<Departamento> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persistir(Departamento departamento, String tipo);
    String generaCombo(String estado, String tipo);
    String idDepartamento(String departamento);
    String idOficina(String idDepartamento);
    String filtro(String departamento, String oficina);
    String nombre(String idDepartamento);
}
