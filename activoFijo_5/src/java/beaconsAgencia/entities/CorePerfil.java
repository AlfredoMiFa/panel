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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "core_perfil")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CorePerfil.findAll", query = "SELECT c FROM CorePerfil c"),
    @NamedQuery(name = "CorePerfil.findByCorePerfilId", query = "SELECT c FROM CorePerfil c WHERE c.corePerfilId = :corePerfilId"),
    @NamedQuery(name = "CorePerfil.findByPerfil", query = "SELECT c FROM CorePerfil c WHERE c.perfil = :perfil"),
    @NamedQuery(name = "CorePerfil.findByDescripcion", query = "SELECT c FROM CorePerfil c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CorePerfil.findByEstatus", query = "SELECT c FROM CorePerfil c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "CorePerfil.findByHomePage", query = "SELECT c FROM CorePerfil c WHERE c.homePage = :homePage")})
public class CorePerfil implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corePerfilId")
    private List<CoreDashboard> coreDashboardList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_PERFIL_ID")
    private Integer corePerfilId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PERFIL")
    private String perfil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "HOME_PAGE")
    private String homePage;    
    @JoinTable(name = "core_perfil_derecho", joinColumns = {
        @JoinColumn(name = "CORE_PERFIL_ID", referencedColumnName = "CORE_PERFIL_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CORE_DERECHO_ID", referencedColumnName = "CORE_DERECHO_ID")})  
    @ManyToMany
    @OrderBy("orden ASC")
    private List<CoreDerecho> coreDerechoList;
    @JoinTable(name = "core_perfil_derecho_sistema", joinColumns = {
        @JoinColumn(name = "CORE_PERFIL_ID", referencedColumnName = "CORE_PERFIL_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CORE_DERECHO_SISTEMA_ID", referencedColumnName = "CORE_DERECHO_SISTEMA_ID")})
    @ManyToMany
    private List<CoreDerechoSistema> coreDerechoSistemaList;
    
    @JoinTable(name = "core_perfil_regla_seguridad", joinColumns = {
        @JoinColumn(name = "CORE_PERFIL_ID", referencedColumnName = "CORE_PERFIL_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CORE_REGLA_SEGURIDAD_ID", referencedColumnName = "CORE_REGLA_SEGURIDAD_ID")})
    @ManyToMany
    private List<CoreReglaSeguridad> coreReglaSeguridadList;
    @ManyToMany(mappedBy = "corePerfilList")
    private List<CoreUsuario> coreUsuarioList;

    public CorePerfil() {
    }

    public CorePerfil(Integer corePerfilId) {
        this.corePerfilId = corePerfilId;
    }

    public CorePerfil(Integer corePerfilId, String perfil, String descripcion, String estatus, String homePage) {
        this.corePerfilId = corePerfilId;
        this.perfil = perfil;
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.homePage = homePage;
    }

    public Integer getCorePerfilId() {
        return corePerfilId;
    }

    public void setCorePerfilId(Integer corePerfilId) {
        this.corePerfilId = corePerfilId;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
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

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    @XmlTransient
    public List<CoreDerecho> getCoreDerechoList() {
        return coreDerechoList;
    }

    public void setCoreDerechoList(List<CoreDerecho> coreDerechoList) {
        this.coreDerechoList = coreDerechoList;
    }

    @XmlTransient
    public List<CoreDerechoSistema> getCoreDerechoSistemaList() {
        return coreDerechoSistemaList;
    }

    public void setCoreDerechoSistemaList(List<CoreDerechoSistema> coreDerechoSistemaList) {
        this.coreDerechoSistemaList = coreDerechoSistemaList;
    }

    @XmlTransient
    public List<CoreUsuario> getCoreUsuarioList() {
        return coreUsuarioList;
    }

    public void setCoreUsuarioList(List<CoreUsuario> coreUsuarioList) {
        this.coreUsuarioList = coreUsuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (corePerfilId != null ? corePerfilId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorePerfil)) {
            return false;
        }
        CorePerfil other = (CorePerfil) object;
        if ((this.corePerfilId == null && other.corePerfilId != null) || (this.corePerfilId != null && !this.corePerfilId.equals(other.corePerfilId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"corePerfilId\":"+corePerfilId+"," +
            "\"perfil\":\""+perfil+"\"," +
            "\"descripcion\":\""+descripcion+"\"," +
            "\"homePage\":\""+homePage+"\"," +
            "\"estatus\":\""+estatus+"\"" ;                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }

    @XmlTransient
    public List<CoreReglaSeguridad> getCoreReglaSeguridadList() {
        return coreReglaSeguridadList;
    }

    public void setCoreReglaSeguridadList(List<CoreReglaSeguridad> coreReglaSeguridadList) {
        this.coreReglaSeguridadList = coreReglaSeguridadList;
    }

    @XmlTransient
    public List<CoreDashboard> getCoreDashboardList() {
        return coreDashboardList;
    }

    public void setCoreDashboardList(List<CoreDashboard> coreDashboardList) {
        this.coreDashboardList = coreDashboardList;
    }
    
}
