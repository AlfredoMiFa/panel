/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jgonzalezc
 */
@Entity
@Table(name = "core_regla_seguridad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreReglaSeguridad.findAll", query = "SELECT c FROM CoreReglaSeguridad c"),
    @NamedQuery(name = "CoreReglaSeguridad.findByCoreReglaSeguridadId", query = "SELECT c FROM CoreReglaSeguridad c WHERE c.coreReglaSeguridadId = :coreReglaSeguridadId"),
    @NamedQuery(name = "CoreReglaSeguridad.findByNombre", query = "SELECT c FROM CoreReglaSeguridad c WHERE c.nombre = :nombre")})
public class CoreReglaSeguridad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_REGLA_SEGURIDAD_ID")
    private Integer coreReglaSeguridadId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE")
    private String nombre;
    @ManyToMany(mappedBy = "coreReglaSeguridadList")
    private List<CorePerfil> corePerfilList;
    @OneToMany( mappedBy = "coreReglaSeguridadId")
    private List<CoreReglaSeguridadDetalle> coreReglaSeguridadList;

    public CoreReglaSeguridad() {
    }

    public CoreReglaSeguridad(Integer coreReglaSeguridadId) {
        this.coreReglaSeguridadId = coreReglaSeguridadId;
    }

    public CoreReglaSeguridad(Integer coreReglaSeguridadId, String nombre) {
        this.coreReglaSeguridadId = coreReglaSeguridadId;
        this.nombre = nombre;
    }

    public Integer getCoreReglaSeguridadId() {
        return coreReglaSeguridadId;
    }

    public void setCoreReglaSeguridadId(Integer coreReglaSeguridadId) {
        this.coreReglaSeguridadId = coreReglaSeguridadId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @XmlTransient
    public List<CorePerfil> getCorePerfilList() {
        return corePerfilList;
    }

    public void setCorePerfilList(List<CorePerfil> corePerfilList) {
        this.corePerfilList = corePerfilList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreReglaSeguridadId != null ? coreReglaSeguridadId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreReglaSeguridad)) {
            return false;
        }
        CoreReglaSeguridad other = (CoreReglaSeguridad) object;
        if ((this.coreReglaSeguridadId == null && other.coreReglaSeguridadId != null) || (this.coreReglaSeguridadId != null && !this.coreReglaSeguridadId.equals(other.coreReglaSeguridadId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreReglaSeguridadId\":"+coreReglaSeguridadId+"," +
            "\"nombre\":\""+nombre+"\"";                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }
    public String toString2() {
        String devolver=coreReglaSeguridadId+"|" +
            nombre+"|" ;                
        return devolver;
    }

    /**
     * @return the coreReglaSeguridadList
     */
    public List<CoreReglaSeguridadDetalle> getCoreReglaSeguridadList() {
        return coreReglaSeguridadList;
    }

    /**
     * @param coreReglaSeguridadList the coreReglaSeguridadList to set
     */
    public void setCoreReglaSeguridadList(List<CoreReglaSeguridadDetalle> coreReglaSeguridadList) {
        this.coreReglaSeguridadList = coreReglaSeguridadList;
    }
    
}
