/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.clases.general;

import java.io.Serializable;
import beaconsAgencia.entities.CorePerfil;
import beaconsAgencia.entities.CoreUsuario;

/**
 *
 * @author jgonzalezc
 */
public class ObjetoSesion implements Serializable{
    private CorePerfil perfilSelected;
    private CorePerfil superAdmin;
    private CoreUsuario usuario;
    private boolean superAdministrador;

    /**
     * @return the perfilSelected
     */
    public CorePerfil getPerfilSelected() {
        return perfilSelected;
    }
    
    
    
    public int getPerfil() {
        if(superAdministrador)
            return 0;
        else
            return perfilSelected.getCorePerfilId();
    }

    /**
     * @param perfilSelected the perfilSelected to set
     */
    public void setPerfilSelected(CorePerfil perfilSelected) {
        this.perfilSelected = perfilSelected;
    }

    /**
     * @return the usuario
     */
    public CoreUsuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(CoreUsuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the superAdmin
     */
    public CorePerfil getSuperAdmin() {
        return superAdmin;
    }

    /**
     * @param superAdmin the superAdmin to set
     */
    public void setSuperAdmin(CorePerfil superAdmin) {
        this.superAdmin = superAdmin;
    }

    /**
     * @return the superAdministrador
     */
    public boolean isSuperAdministrador() {
        return superAdministrador;
    }

    /**
     * @param superAdministrador the superAdministrador to set
     */
    public void setSuperAdministrador(boolean superAdministrador) {
        this.superAdministrador = superAdministrador;
    }


}
