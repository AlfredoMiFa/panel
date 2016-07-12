/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.ejb;

import beaconsAgencia.entities.Categoria;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Valar_Morgulis
 */
@Local
public interface CategoriasFacadeLocal {
    
    void create(Categoria categoria);
    void edit(Categoria categoria);
    void remove(Categoria categoria);
    
    Categoria find(Object id);
    List<Categoria> findAll();
    List<Categoria> findRange(int[] range);
    
    int count();
    
    String select(int rows, int page, String sort, String order, String search);
    String persistir(Categoria categoria, String tipo);
    String combo(String tipo, String estado);
    String filtro(String id);
    String idCategoria(String descripcion);
}
