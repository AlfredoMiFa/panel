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
@Table(name = "core_derecho_sistema")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreDerechoSistema.findAll", query = "SELECT c FROM CoreDerechoSistema c"),
    @NamedQuery(name = "CoreDerechoSistema.findByCoreDerechoSistemaId", query = "SELECT c FROM CoreDerechoSistema c WHERE c.coreDerechoSistemaId = :coreDerechoSistemaId"),
    @NamedQuery(name = "CoreDerechoSistema.findByDerecho", query = "SELECT c FROM CoreDerechoSistema c WHERE c.derecho = :derecho"),
    @NamedQuery(name = "CoreDerechoSistema.findByDescripcion", query = "SELECT c FROM CoreDerechoSistema c WHERE c.descripcion = :descripcion")})
public class CoreDerechoSistema implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_DERECHO_SISTEMA_ID")
    private Integer coreDerechoSistemaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DERECHO")
    private String derecho;
    @Size(max = 100)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @ManyToMany(mappedBy = "coreDerechoSistemaList")
    private List<CorePerfil> corePerfilList;

    public CoreDerechoSistema() {
    }

    public CoreDerechoSistema(Integer coreDerechoSistemaId) {
        this.coreDerechoSistemaId = coreDerechoSistemaId;
    }

    public CoreDerechoSistema(Integer coreDerechoSistemaId, String derecho) {
        this.coreDerechoSistemaId = coreDerechoSistemaId;
        this.derecho = derecho;
    }

    public Integer getCoreDerechoSistemaId() {
        return coreDerechoSistemaId;
    }

    public void setCoreDerechoSistemaId(Integer coreDerechoSistemaId) {
        this.coreDerechoSistemaId = coreDerechoSistemaId;
    }

    public String getDerecho() {
        return derecho;
    }

    public void setDerecho(String derecho) {
        this.derecho = derecho;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        hash += (coreDerechoSistemaId != null ? coreDerechoSistemaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreDerechoSistema)) {
            return false;
        }
        CoreDerechoSistema other = (CoreDerechoSistema) object;
        if ((this.coreDerechoSistemaId == null && other.coreDerechoSistemaId != null) || (this.coreDerechoSistemaId != null && !this.coreDerechoSistemaId.equals(other.coreDerechoSistemaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreDerechoSistemaId\":"+coreDerechoSistemaId+"," +
            "\"derecho\":\""+derecho+"\"," +
            "\"descripcion\":\""+descripcion+"\"" ;                
        return devolver+"}";
    }
    
}
