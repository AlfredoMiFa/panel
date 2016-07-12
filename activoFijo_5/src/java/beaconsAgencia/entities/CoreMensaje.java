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
@Table(name = "core_mensaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreMensaje.findAll", query = "SELECT c FROM CoreMensaje c"),
    @NamedQuery(name = "CoreMensaje.findByCoreMensajeId", query = "SELECT c FROM CoreMensaje c WHERE c.coreMensajeId = :coreMensajeId"),
    @NamedQuery(name = "CoreMensaje.findByTitulo", query = "SELECT c FROM CoreMensaje c WHERE c.titulo = :titulo"),
    @NamedQuery(name = "CoreMensaje.findByTexto", query = "SELECT c FROM CoreMensaje c WHERE c.texto = :texto"),
    @NamedQuery(name = "CoreMensaje.findByFechaCreacion", query = "SELECT c FROM CoreMensaje c WHERE c.fechaCreacion = :fechaCreacion")})
public class CoreMensaje implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "TOV")
    private String tov;
    @Size(max = 20)
    @Column(name = "FROMV")
    private String fromv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "TO_ESTATUS")
    private String toEstatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "FROM_ESTATUS")
    private String fromEstatus;
    @Size(max = 45)
    @Column(name = "GRUPO")
    private String grupo;
    @Column(name = "FECHA_RECEPCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecepcion;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_MENSAJE_ID")
    private Integer coreMensajeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TITULO")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "TEXTO")
    private String texto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    public CoreMensaje() {
    }

    public CoreMensaje(Integer coreMensajeId) {
        this.coreMensajeId = coreMensajeId;
    }

    public CoreMensaje(Integer coreMensajeId, String titulo, String texto, Date fechaCreacion) {
        this.coreMensajeId = coreMensajeId;
        this.titulo = titulo;
        this.texto = texto;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getCoreMensajeId() {
        return coreMensajeId;
    }

    public void setCoreMensajeId(Integer coreMensajeId) {
        this.coreMensajeId = coreMensajeId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreMensajeId != null ? coreMensajeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreMensaje)) {
            return false;
        }
        CoreMensaje other = (CoreMensaje) object;
        if ((this.coreMensajeId == null && other.coreMensajeId != null) || (this.coreMensajeId != null && !this.coreMensajeId.equals(other.coreMensajeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format=new SimpleDateFormat("kk:mm:ss");
        SimpleDateFormat format3=new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
        String devolver="{" +
            "\"coreMensajeId\":"+coreMensajeId+"," +
            "\"fromEstatus\":\""+fromEstatus+"\"," +
            "\"toEstatus\":\""+toEstatus+"\"," +
            "\"grupo\":\""+grupo+"\"," +
            "\"fechaCreacion\":\""+format2.format(fechaCreacion)+"\"," +
            "\"fechaCreacionHora\":\""+format.format(fechaCreacion)+"\"," +
            "\"fechaRecepcion\":"+(fechaRecepcion!=null?"\""+format3.format(fechaRecepcion)+"\"":null)+"," +
            "\"titulo\":\""+titulo+"\"," +
            "\"texto\":\""+texto.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("\"", "'")+"\"," +
            "\"fromv\":\""+fromv+"\"," +
            "\"tov\":\""+tov+"\"";                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }

    public String getTo() {
        return tov;
    }

    public void setTo(String to) {
        this.tov = to;
    }

    public String getFrom() {
        return fromv;
    }

    public void setFrom(String from) {
        this.fromv = from;
    }

    public String getToEstatus() {
        return toEstatus;
    }

    public void setToEstatus(String toEstatus) {
        this.toEstatus = toEstatus;
    }

    public String getFromEstatus() {
        return fromEstatus;
    }

    public void setFromEstatus(String fromEstatus) {
        this.fromEstatus = fromEstatus;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }
    
}
