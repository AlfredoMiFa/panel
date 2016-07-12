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
 * @author jgonzalezc
 */
@Entity
@Table(name = "bitacora")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bitacora.findAll", query = "SELECT b FROM Bitacora b"),
    @NamedQuery(name = "Bitacora.findByBitacoraId", query = "SELECT b FROM Bitacora b WHERE b.bitacoraId = :bitacoraId"),
    @NamedQuery(name = "Bitacora.findByUsuarioId", query = "SELECT b FROM Bitacora b WHERE b.usuarioId = :usuarioId"),
    @NamedQuery(name = "Bitacora.findByTipo", query = "SELECT b FROM Bitacora b WHERE b.tipo = :tipo"),
    @NamedQuery(name = "Bitacora.findByOrigen", query = "SELECT b FROM Bitacora b WHERE b.origen = :origen"),
    @NamedQuery(name = "Bitacora.findByOperacion", query = "SELECT b FROM Bitacora b WHERE b.operacion = :operacion"),
    @NamedQuery(name = "Bitacora.findByFechaOperacion", query = "SELECT b FROM Bitacora b WHERE b.fechaOperacion = :fechaOperacion")})
public class Bitacora implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BITACORA_ID")
    private Integer bitacoraId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USUARIO_ID")
    private int usuarioId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size( max = 45)
    @Column(name = "ORIGEN")
    private String origen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "OPERACION")
    private String operacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_OPERACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOperacion;
    @Basic(optional = false)
    @Size( max = 100)
    @Column(name = "NOMBRE")
    private String nombre;

    public Bitacora() {
    }

    public Bitacora(Integer bitacoraId) {
        this.bitacoraId = bitacoraId;
    }

    public Bitacora(Integer bitacoraId, int usuarioId, String tipo, String origen, String operacion, Date fechaOperacion) {
        this.bitacoraId = bitacoraId;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.origen = origen;
        this.operacion = operacion;
        this.fechaOperacion = fechaOperacion;
    }

    public Integer getBitacoraId() {
        return bitacoraId;
    }

    public void setBitacoraId(Integer bitacoraId) {
        this.bitacoraId = bitacoraId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bitacoraId != null ? bitacoraId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bitacora)) {
            return false;
        }
        Bitacora other = (Bitacora) object;
        if ((this.bitacoraId == null && other.bitacoraId != null) || (this.bitacoraId != null && !this.bitacoraId.equals(other.bitacoraId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat=new SimpleDateFormat("kk:mm:ss");
        String devolver="{" +
            "\"bitacoraId\":"+bitacoraId+"," +
            "\"usuarioId\":\""+usuarioId+"\"," +
            "\"tipo\":\""+tipo+"\"," +
            "\"origen\":\""+origen+"\"," +
            "\"operacion\":\""+operacion+"\"," +
            "\"nombre\":"+(nombre!=null?"\""+nombre+"\"":null)+"," +
            "\"time\":\""+timeFormat.format(fechaOperacion)+"\"," +
            "\"fechaOperacion\":\""+dateFormat.format(fechaOperacion)+"\"" ;                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
