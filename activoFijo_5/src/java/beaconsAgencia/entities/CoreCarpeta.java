/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "core_carpeta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreCarpeta.findAll", query = "SELECT c FROM CoreCarpeta c"),
    @NamedQuery(name = "CoreCarpeta.findByCoreCarpetaId", query = "SELECT c FROM CoreCarpeta c WHERE c.coreCarpetaId = :coreCarpetaId"),
    @NamedQuery(name = "CoreCarpeta.findByNombre", query = "SELECT c FROM CoreCarpeta c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CoreCarpeta.findByDescripcion", query = "SELECT c FROM CoreCarpeta c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CoreCarpeta.findByEstatus", query = "SELECT c FROM CoreCarpeta c WHERE c.estatus = :estatus")})
public class CoreCarpeta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_CARPETA_ID")
    private Integer coreCarpetaId;
    @Size(max = 245)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 245)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @OneToMany(mappedBy = "coreCarpetaPadreId")
    private List<CoreCarpeta> coreCarpetaList;
    @JoinColumn(name = "CORE_CARPETA_PADRE_ID", referencedColumnName = "CORE_CARPETA_ID")
    @ManyToOne
    private CoreCarpeta coreCarpetaPadreId;
    @JoinColumn(name = "PROPIETARIO_ID", referencedColumnName = "CORE_USUARIO_ID")
    @ManyToOne
    private CoreUsuario propietarioId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coreCarpetaId")
    private List<CoreDocumento> coreDocumentoList;

    public CoreCarpeta() {
    }

    public CoreCarpeta(Integer coreCarpetaId) {
        this.coreCarpetaId = coreCarpetaId;
    }

    public CoreCarpeta(Integer coreCarpetaId, String estatus) {
        this.coreCarpetaId = coreCarpetaId;
        this.estatus = estatus;
    }

    public Integer getCoreCarpetaId() {
        return coreCarpetaId;
    }

    public void setCoreCarpetaId(Integer coreCarpetaId) {
        this.coreCarpetaId = coreCarpetaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @XmlTransient
    public List<CoreCarpeta> getCoreCarpetaList() {
        return coreCarpetaList;
    }

    public void setCoreCarpetaList(List<CoreCarpeta> coreCarpetaList) {
        this.coreCarpetaList = coreCarpetaList;
    }

    public CoreCarpeta getCoreCarpetaPadreId() {
        return coreCarpetaPadreId;
    }

    public void setCoreCarpetaPadreId(CoreCarpeta carpetaId) {
        this.coreCarpetaPadreId = carpetaId;
    }

    public CoreUsuario getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(CoreUsuario propietarioId) {
        this.propietarioId = propietarioId;
    }

    @XmlTransient
    public List<CoreDocumento> getCoreDocumentoList() {
        return coreDocumentoList;
    }

    public void setCoreDocumentoList(List<CoreDocumento> coreDocumentoList) {
        this.coreDocumentoList = coreDocumentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreCarpetaId != null ? coreCarpetaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreCarpeta)) {
            return false;
        }
        CoreCarpeta other = (CoreCarpeta) object;
        if ((this.coreCarpetaId == null && other.coreCarpetaId != null) || (this.coreCarpetaId != null && !this.coreCarpetaId.equals(other.coreCarpetaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tyren.entities.CoreCarpeta[ coreCarpetaId=" + coreCarpetaId + " ]";
    }
    
}
