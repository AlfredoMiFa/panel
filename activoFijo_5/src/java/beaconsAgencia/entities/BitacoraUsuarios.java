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
 * @author Valar_Morgulis
 */
@Entity
@Table(name = "bitacora_usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BitacoraUsuarios.findAll", query = "SELECT b FROM BitacoraUsuarios b"),
    @NamedQuery(name = "BitacoraUsuarios.findByIdBitacoraUsuario", query = "SELECT b FROM BitacoraUsuarios b WHERE b.idBitacoraUsuario = :idBitacoraUsuario"),
    @NamedQuery(name = "BitacoraUsuarios.findByIdUsuario", query = "SELECT b FROM BitacoraUsuarios b WHERE b.idUsuario = :idUsuario"),
    @NamedQuery(name = "BitacoraUsuarios.findByUsuario", query = "SELECT b FROM BitacoraUsuarios b WHERE b.usuario = :usuario"),
    @NamedQuery(name = "BitacoraUsuarios.findByActividad", query = "SELECT b FROM BitacoraUsuarios b WHERE b.actividad = :actividad"),
    @NamedQuery(name = "BitacoraUsuarios.findByCantidadActivos", query = "SELECT b FROM BitacoraUsuarios b WHERE b.cantidadActivos = :cantidadActivos"),
    @NamedQuery(name = "BitacoraUsuarios.findByCategoria", query = "SELECT b FROM BitacoraUsuarios b WHERE b.categoria = :categoria"),
    @NamedQuery(name = "BitacoraUsuarios.findByFecha", query = "SELECT b FROM BitacoraUsuarios b WHERE b.fecha = :fecha")})
public class BitacoraUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_BITACORA_USUARIO")
    private Integer idBitacoraUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private int idUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USUARIO")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ACTIVIDAD")
    private String actividad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_ACTIVOS")
    private int cantidadActivos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CATEGORIA")
    private String categoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    public BitacoraUsuarios() {
    }

    public BitacoraUsuarios(Integer idBitacoraUsuario) {
        this.idBitacoraUsuario = idBitacoraUsuario;
    }

    public BitacoraUsuarios(Integer idBitacoraUsuario, int idUsuario, String usuario, String actividad, int cantidadActivos, String categoria, Date fecha) {
        this.idBitacoraUsuario = idBitacoraUsuario;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.actividad = actividad;
        this.cantidadActivos = cantidadActivos;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    public Integer getIdBitacoraUsuario() {
        return idBitacoraUsuario;
    }

    public void setIdBitacoraUsuario(Integer idBitacoraUsuario) {
        this.idBitacoraUsuario = idBitacoraUsuario;
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

    public int getCantidadActivos() {
        return cantidadActivos;
    }

    public void setCantidadActivos(int cantidadActivos) {
        this.cantidadActivos = cantidadActivos;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBitacoraUsuario != null ? idBitacoraUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BitacoraUsuarios)) {
            return false;
        }
        BitacoraUsuarios other = (BitacoraUsuarios) object;
        if ((this.idBitacoraUsuario == null && other.idBitacoraUsuario != null) || (this.idBitacoraUsuario != null && !this.idBitacoraUsuario.equals(other.idBitacoraUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat fec = new SimpleDateFormat("yyyy-MM-dd");
        String devolver = "{" +
              "\"idBitacoraUsuario\":"+idBitacoraUsuario+","  +
              "\"idUsuario\":"+idUsuario+"," +
              "\"usuario\":\""+usuario+"\"," +
              "\"actividad\":\""+actividad+"\"," +
              "\"cantidadActivos\":"+cantidadActivos+"," +
              "\"categoria\":\""+categoria+"\","+
              "\"fecha\":\""+fec.format(fecha)+"\"}";
        return devolver;
    }
    
}
