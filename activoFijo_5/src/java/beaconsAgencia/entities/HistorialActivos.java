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
@Table(name = "historial_activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistorialActivos.findAll", query = "SELECT h FROM HistorialActivos h"),
    @NamedQuery(name = "HistorialActivos.findByIdHistorialActivo", query = "SELECT h FROM HistorialActivos h WHERE h.idHistorialActivo = :idHistorialActivo"),
    @NamedQuery(name = "HistorialActivos.findByOficina", query = "SELECT h FROM HistorialActivos h WHERE h.oficina = :oficina"),
    @NamedQuery(name = "HistorialActivos.findByDepartamento", query = "SELECT h FROM HistorialActivos h WHERE h.departamento = :departamento"),
    @NamedQuery(name = "HistorialActivos.findByIdIncidencia", query = "SELECT h FROM HistorialActivos h WHERE h.idIncidencia = :idIncidencia"),
    @NamedQuery(name = "HistorialActivos.findByIncidencia", query = "SELECT h FROM HistorialActivos h WHERE h.incidencia = :incidencia"),
    @NamedQuery(name = "HistorialActivos.findByIdActivo", query = "SELECT h FROM HistorialActivos h WHERE h.idActivo = :idActivo"),
    @NamedQuery(name = "HistorialActivos.findByActivo", query = "SELECT h FROM HistorialActivos h WHERE h.activo = :activo"),
    @NamedQuery(name = "HistorialActivos.findByIdUsuario", query = "SELECT h FROM HistorialActivos h WHERE h.idUsuario = :idUsuario"),
    @NamedQuery(name = "HistorialActivos.findByUsuario", query = "SELECT h FROM HistorialActivos h WHERE h.usuario = :usuario"),
    @NamedQuery(name = "HistorialActivos.findByActividad", query = "SELECT h FROM HistorialActivos h WHERE h.actividad = :actividad"),
    @NamedQuery(name = "HistorialActivos.findByFecha", query = "SELECT h FROM HistorialActivos h WHERE h.fecha = :fecha"),
    @NamedQuery(name = "HistorialActivos.findByTipoMovimiento", query = "SELECT h FROM HistorialActivos h WHERE h.tipoMovimiento = :tipoMovimiento"),
    @NamedQuery(name = "HistorialActivos.findByObservaciones", query = "SELECT h FROM HistorialActivos h WHERE h.observaciones = :observaciones")})
public class HistorialActivos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_HISTORIAL_ACTIVO", nullable = false)
    private Integer idHistorialActivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "OFICINA", nullable = false, length = 45)
    private String oficina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DEPARTAMENTO", nullable = false, length = 100)
    private String departamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_INCIDENCIA", nullable = false)
    private int idIncidencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INCIDENCIA", nullable = false, length = 45)
    private String incidencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ACTIVO", nullable = false)
    private int idActivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ACTIVO", nullable = false, length = 50)
    private String activo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO", nullable = false)
    private int idUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USUARIO", nullable = false, length = 45)
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ACTIVIDAD", nullable = false, length = 45)
    private String actividad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TIPO_MOVIMIENTO", nullable = false, length = 45)
    private String tipoMovimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "OBSERVACIONES", nullable = false, length = 45)
    private String observaciones;

    public HistorialActivos() {
    }

    public HistorialActivos(Integer idHistorialActivo) {
        this.idHistorialActivo = idHistorialActivo;
    }

    public HistorialActivos(Integer idHistorialActivo, String oficina, String departamento, int idIncidencia, String incidencia, int idActivo, String activo, int idUsuario, String usuario, String actividad, Date fecha, String tipoMovimiento, String observaciones) {
        this.idHistorialActivo = idHistorialActivo;
        this.oficina = oficina;
        this.departamento = departamento;
        this.idIncidencia = idIncidencia;
        this.incidencia = incidencia;
        this.idActivo = idActivo;
        this.activo = activo;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.actividad = actividad;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.observaciones = observaciones;
    }

    public Integer getIdHistorialActivo() {
        return idHistorialActivo;
    }

    public void setIdHistorialActivo(Integer idHistorialActivo) {
        this.idHistorialActivo = idHistorialActivo;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(String incidencia) {
        this.incidencia = incidencia;
    }

    public int getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(int idActivo) {
        this.idActivo = idActivo;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorialActivo != null ? idHistorialActivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistorialActivos)) {
            return false;
        }
        HistorialActivos other = (HistorialActivos) object;
        if ((this.idHistorialActivo == null && other.idHistorialActivo != null) || (this.idHistorialActivo != null && !this.idHistorialActivo.equals(other.idHistorialActivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat fec = new SimpleDateFormat("yyyy-MM-dd");
        String devolver = "{" +
                "\"idHistorialActivo\":"+idHistorialActivo+","+
                "\"oficina\":\""+oficina+"\","+
                "\"departamento\":\""+departamento+"\","+
                "\"idIncidencia\":"+idIncidencia+","+
                "\"incidencia\":\""+incidencia+"\","+
                "\"idActivo\":"+idActivo+","+
                "\"activo\":\""+activo+"\","+
                "\"idUsuario\":"+idUsuario+","+
                "\"usuario\":\""+usuario+"\","+
                "\"actividad\":\""+actividad+"\","+
                "\"fecha\":\""+fec.format(fecha)+"\","+
                "\"tipoMovimiento\":\""+tipoMovimiento+"\","+
                "\"observaciones\":\""+observaciones+"\"}";
        return devolver;
        //return "beaconsAgencia.entities.HistorialActivos[ idHistorialActivo=" + idHistorialActivo + " ]";
    }
    
}
