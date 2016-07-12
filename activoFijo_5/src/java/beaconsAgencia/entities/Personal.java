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
 * @author Servamp
 */
@Entity
@Table(name = "personal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personal.findAll", query = "SELECT p FROM Personal p"),
    @NamedQuery(name = "Personal.findByIdPersonal", query = "SELECT p FROM Personal p WHERE p.idPersonal = :idPersonal"),
    @NamedQuery(name = "Personal.findByIdOficina", query = "SELECT p FROM Personal p WHERE p.idOficina = :idOficina"),
    @NamedQuery(name = "Personal.findByIdEstado", query = "SELECT p FROM Personal p WHERE p.idEstado = :idEstado"),
    @NamedQuery(name = "Personal.findByIdCiudad", query = "SELECT p FROM Personal p WHERE p.idCiudad = :idCiudad"),
    @NamedQuery(name = "Personal.findByNombre", query = "SELECT p FROM Personal p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Personal.findByApellidoPaterno", query = "SELECT p FROM Personal p WHERE p.apellidoPaterno = :apellidoPaterno"),
    @NamedQuery(name = "Personal.findByApellidoMaterno", query = "SELECT p FROM Personal p WHERE p.apellidoMaterno = :apellidoMaterno"),
    @NamedQuery(name = "Personal.findByEmail", query = "SELECT p FROM Personal p WHERE p.email = :email"),
    @NamedQuery(name = "Personal.findByCalle", query = "SELECT p FROM Personal p WHERE p.calle = :calle"),
    @NamedQuery(name = "Personal.findByColonia", query = "SELECT p FROM Personal p WHERE p.colonia = :colonia"),
    @NamedQuery(name = "Personal.findByCpp", query = "SELECT p FROM Personal p WHERE p.cpp = :cpp"),
    @NamedQuery(name = "Personal.findByNumeroTelefono", query = "SELECT p FROM Personal p WHERE p.numeroTelefono = :numeroTelefono")})
public class Personal implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersonal")
    private List<Activo> activoList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PERSONAL", nullable = false)
    private Integer idPersonal;
    @JoinColumn(name = "ID_OFICINA", referencedColumnName = "ID_OFICINA")
    @ManyToOne(optional = false)
    private Oficinas idOficina;
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
    @Size(min = 1, max = 20)
    @Column(name = "NOMBRE", nullable = false, length = 20)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 20)
    private String apellidoPaterno;
    @Size(max = 20)
    @Column(name = "APELLIDO_MATERNO", length = 20)
    private String apellidoMaterno;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "EMAIL", nullable = false, length = 30)
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CALLE", nullable = false, length = 45)
    private String calle;
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
    @Size(min = 1, max = 10)
    @Column(name = "NUMERO_TELEFONO", nullable = false, length = 10)
    private String numeroTelefono;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO", nullable = false)
    @ManyToOne(optional = false)
    private Departamento idDepartamento;

    public Personal() {
    }

    public Personal(Integer idPersonal) {
        this.idPersonal = idPersonal;
    }

    public Personal(Integer idPersonal, String idEstado, String idCiudad, String nombre, String apellidoPaterno, String email, String calle, String colonia, String cpp, String numeroTelefono) {
        this.idPersonal = idPersonal;
        this.idEstado = idEstado;
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.email = email;
        this.calle = calle;
        this.colonia = colonia;
        this.cpp = cpp;
        this.numeroTelefono = numeroTelefono;
    }

    public Integer getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(Integer idPersonal) {
        this.idPersonal = idPersonal;
    }

    public Oficinas getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Oficinas idOficina) {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
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

    public Departamento getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Departamento idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersonal != null ? idPersonal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personal)) {
            return false;
        }
        Personal other = (Personal) object;
        if ((this.idPersonal == null && other.idPersonal != null) || (this.idPersonal != null && !this.idPersonal.equals(other.idPersonal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver = "{" +
                "\"idPersonal\":"+idPersonal+","+
                "\"idOficina\":{\"id\":"+idOficina.getIdOficina()+",\"text\":\""+idOficina.getNombreOficina()+"\"},"+
                "\"idDepartamento\":{\"id\":"+idDepartamento.getIdDepartamento()+",\"text\":\""+idDepartamento.getDescripcion()+"\"},"+
                //"\"idCiudad\":{\"id\":"+idCiudad.getIdCiudad()+",\"text\":\""+idCiudad.getDescripcion()+"\"},"+
                "\"idEstado\":\""+idEstado+"\","+
                "\"idCiudad\":\""+idCiudad+"\","+
                "\"nombre\":\""+nombre+"\","+
                "\"apellidoPaterno\":\""+apellidoPaterno+"\","+
                "\"apellidoMaterno\":\""+apellidoMaterno+"\","+
                "\"email\":\""+email+"\","+
                "\"calle\":\""+calle+"\","+
                "\"colonia\":\""+colonia+"\","+
                "\"cpp\":\""+cpp+"\","+
                "\"numeroTelefono\":\""+numeroTelefono+"\"}";
        return devolver;
        //return "beaconsAgencia.entities.Personal[ idPersonal=" + idPersonal + " ]";
    }
    
    public String toStringCombo() {
        String devolver = "{" +
                "\"idPersonal\":"+idPersonal+","+
                "\"nombre\":\""+nombre+"\","+
                "\"apellidoPaterno\":\""+apellidoPaterno+"\","+
                "\"apellidoMaterno\":\""+apellidoMaterno+"\"}";
        return devolver;
    }

    @XmlTransient
    public List<Activo> getActivoList() {
        return activoList;
    }

    public void setActivoList(List<Activo> activoList) {
        this.activoList = activoList;
    }
    
}
