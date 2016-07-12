/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beaconsAgencia.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Servamp
 */
@Entity
@Table(name = "incidencias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Incidencias.findAll", query = "SELECT i FROM Incidencias i"),
    @NamedQuery(name = "Incidencias.findByIdIncidencia", query = "SELECT i FROM Incidencias i WHERE i.idIncidencia = :idIncidencia"),
    @NamedQuery(name = "Incidencias.findByIdUsuario", query = "SELECT i FROM Incidencias i WHERE i.idUsuario = :idUsuario"),
    @NamedQuery(name = "Incidencias.findByIdDepartamento", query = "SELECT i FROM Incidencias i WHERE i.idDepartamento = :idDepartamento"),
    @NamedQuery(name = "Incidencias.findByTipoIncidencia", query = "SELECT i FROM Incidencias i WHERE i.tipoIncidencia = :tipoIncidencia"),
    @NamedQuery(name = "Incidencias.findByDescripcion", query = "SELECT i FROM Incidencias i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "Incidencias.findByFecha", query = "SELECT i FROM Incidencias i WHERE i.fecha = :fecha")})
public class Incidencias implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_INCIDENCIA", nullable = false)
    private Integer idIncidencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ID_USUARIO", nullable = false, length = 100)
    private String idUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TIPO_INCIDENCIA", nullable = false, length = 45)
    private String tipoIncidencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DESCRIPCION", nullable = false, length = 45)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "ID_ACTIVO", referencedColumnName = "ID_ACTIVO", nullable = false)
    @ManyToOne(optional = false)
    private Activo idActivo;
    @JoinColumn(name = "ID_OFICINA", referencedColumnName = "ID_OFICINA", nullable = false)
    @ManyToOne(optional = false)
    private Oficinas idOficina;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO", nullable = false)
    @ManyToOne(optional = false)
    private Departamento idDepartamento;

    public Incidencias() {
    }

    public Incidencias(Integer idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public Incidencias(Integer idIncidencia, String idUsuario, String tipoIncidencia, String descripcion, Date fecha) {
        this.idIncidencia = idIncidencia;
        this.idUsuario = idUsuario;
        this.tipoIncidencia = tipoIncidencia;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Integer getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Integer idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Departamento getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Departamento idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setTipoIncidencia(String tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Activo getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(Activo idActivo) {
        this.idActivo = idActivo;
    }

    public Oficinas getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Oficinas idOficina) {
        this.idOficina = idOficina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIncidencia != null ? idIncidencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Incidencias)) {
            return false;
        }
        Incidencias other = (Incidencias) object;
        if ((this.idIncidencia == null && other.idIncidencia != null) || (this.idIncidencia != null && !this.idIncidencia.equals(other.idIncidencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat fec = new SimpleDateFormat("yyyy-MM-dd");
        String devolver = "{" +
                "\"idIncidencia\":"+idIncidencia+","+
                "\"idUsuario\":\""+idUsuario+"\","+
                "\"idOficina\":{\"id\":"+idOficina.getIdOficina()+",\"text\":\""+idOficina.getNombreOficina()+"\"},"+
                "\"idDepartamento\":{\"id\":"+idDepartamento.getIdDepartamento()+",\"text\":\""+idDepartamento.getDescripcion()+"\"},"+
                "\"idActivo\":{\"id\":"+idActivo.getIdActivo()+",\"text\":\""+idActivo.getNumeroSerie()+"\"},"+
                "\"tipoIncidencia\":\""+tipoIncidencia+"\","+
                "\"descripcion\":\""+descripcion+"\","+
                "\"fecha\":\""+fec.format(fecha)+"\"}";
        return devolver;
        //return "beaconsAgencia.entities.Incidencias[ idIncidencia=" + idIncidencia + " ]";
    }
    
}
