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
 * @author Valar_Morgulis
 */
@Entity
@Table(name = "oficinas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Oficinas.findAll", query = "SELECT o FROM Oficinas o"),
    @NamedQuery(name = "Oficinas.findByIdOficina", query = "SELECT o FROM Oficinas o WHERE o.idOficina = :idOficina"),
    @NamedQuery(name = "Oficinas.findByIdEstado", query = "SELECT o FROM Oficinas o WHERE o.idEstado = :idEstado"),
    @NamedQuery(name = "Oficinas.findByIdCiudad", query = "SELECT o FROM Oficinas o WHERE o.idCiudad = :idCiudad"),
    @NamedQuery(name = "Oficinas.findByNombreOficina", query = "SELECT o FROM Oficinas o WHERE o.nombreOficina = :nombreOficina"),
    @NamedQuery(name = "Oficinas.findByCalle", query = "SELECT o FROM Oficinas o WHERE o.calle = :calle"),
    @NamedQuery(name = "Oficinas.findByNumeroOficina", query = "SELECT o FROM Oficinas o WHERE o.numeroOficina = :numeroOficina"),
    @NamedQuery(name = "Oficinas.findByColonia", query = "SELECT o FROM Oficinas o WHERE o.colonia = :colonia"),
    @NamedQuery(name = "Oficinas.findByCpp", query = "SELECT o FROM Oficinas o WHERE o.cpp = :cpp"),
    @NamedQuery(name = "Oficinas.findByNumeroTelefono", query = "SELECT o FROM Oficinas o WHERE o.numeroTelefono = :numeroTelefono"),
    @NamedQuery(name = "Oficinas.findByRfc", query = "SELECT o FROM Oficinas o WHERE o.rfc = :rfc")})
public class Oficinas implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOficina")
    private List<Incidencias> incidenciasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOficina")
    private List<Personal> personalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOficina")
    private List<Activo> activoList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_OFICINA", nullable = false)
    private Integer idOficina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ID_ESTADO", nullable = false, length = 100)
    private String idEstado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ID_CIUDAD", nullable = false, length = 100)
    private String idCiudad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_OFICINA", nullable = false, length = 45)
    private String nombreOficina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CALLE", nullable = false, length = 45)
    private String calle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NUMERO_OFICINA", nullable = false, length = 20)
    private String numeroOficina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COLONIA", nullable = false, length = 45)
    private String colonia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "CPP", nullable = false, length = 5)
    private String cpp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "NUMERO_TELEFONO", nullable = false, length = 15)
    private String numeroTelefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "RFC", nullable = false, length = 13)
    private String rfc;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_EMPRESA", nullable = false)
    @ManyToOne(optional = false)
    private Empresa idEmpresa;

    public Oficinas() {
    }

    public Oficinas(Integer idOficina) {
        this.idOficina = idOficina;
    }

    public Oficinas(Integer idOficina, String idEstado, String idCiudad, String nombreOficina, String calle, String numeroOficina, String colonia, String cpp, String numeroTelefono, String rfc) {
        this.idOficina = idOficina;
        this.idEstado = idEstado;
        this.idCiudad = idCiudad;
        this.nombreOficina = nombreOficina;
        this.calle = calle;
        this.numeroOficina = numeroOficina;
        this.colonia = colonia;
        this.cpp = cpp;
        this.numeroTelefono = numeroTelefono;
        this.rfc = rfc;
    }

    public Integer getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Integer idOficina) {
        this.idOficina = idOficina;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombreOficina() {
        return nombreOficina;
    }

    public void setNombreOficina(String nombreOficina) {
        this.nombreOficina = nombreOficina;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroOficina() {
        return numeroOficina;
    }

    public void setNumeroOficina(String numeroOficina) {
        this.numeroOficina = numeroOficina;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCpp() {
        return cpp;
    }

    public void setCpp(String cpp) {
        this.cpp = cpp;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOficina != null ? idOficina.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oficinas)) {
            return false;
        }
        Oficinas other = (Oficinas) object;
        if ((this.idOficina == null && other.idOficina != null) || (this.idOficina != null && !this.idOficina.equals(other.idOficina))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver = "{" +
            "\"idOficina\":"+idOficina+","+
            "\"idEmpresa\":{\"id\":"+idEmpresa.getIdEmpresa()+",\"text\":\""+idEmpresa.getDescripcion()+"\"},"+
            "\"idEstado\":\""+idEstado+"\","+
            "\"idCiudad\":\""+idCiudad+"\","+
            //"\"idEstado\":{\"id\":"+idEstado.getIdEstado()+",\"text\":\""+idEstado.getDescripcion()+"\"},"+
            //"\"idCiudad\":{\"id\":"+idCiudad.getIdCiudad()+",\"text\":\""+idCiudad.getDescripcion()+"\"},"+
            "\"nombreOficina\":\""+nombreOficina+"\","+
            "\"calle\":\""+calle+"\","+
            "\"numeroOficina\":\""+numeroOficina+"\","+
            "\"colonia\":\""+colonia+"\","+
            "\"cpp\":\""+cpp+"\","+
            "\"numeroTelefono\":\""+numeroTelefono+"\","+
            "\"rfc\":\""+rfc+"\"}";
        return devolver;
    }

    @XmlTransient
    public List<Personal> getPersonalList() {
        return personalList;
    }

    public void setPersonalList(List<Personal> personalList) {
        this.personalList = personalList;
    }

    @XmlTransient
    public List<Activo> getActivoList() {
        return activoList;
    }

    public void setActivoList(List<Activo> activoList) {
        this.activoList = activoList;
    }

    @XmlTransient
    public List<Incidencias> getIncidenciasList() {
        return incidenciasList;
    }

    public void setIncidenciasList(List<Incidencias> incidenciasList) {
        this.incidenciasList = incidenciasList;
    }
    
}
