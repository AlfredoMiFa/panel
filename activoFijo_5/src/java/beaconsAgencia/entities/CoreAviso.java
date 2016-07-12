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
import javax.persistence.Lob;
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
@Table(name = "core_aviso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreAviso.findAll", query = "SELECT c FROM CoreAviso c"),
    @NamedQuery(name = "CoreAviso.findByCoreAvisoId", query = "SELECT c FROM CoreAviso c WHERE c.coreAvisoId = :coreAvisoId"),
    @NamedQuery(name = "CoreAviso.findByTitulo", query = "SELECT c FROM CoreAviso c WHERE c.titulo = :titulo"),
    @NamedQuery(name = "CoreAviso.findByEstatus", query = "SELECT c FROM CoreAviso c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "CoreAviso.findByPrioridad", query = "SELECT c FROM CoreAviso c WHERE c.prioridad = :prioridad"),
    @NamedQuery(name = "CoreAviso.findByVigencia", query = "SELECT c FROM CoreAviso c WHERE c.vigencia = :vigencia")})
public class CoreAviso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_AVISO_ID")
    private Integer coreAvisoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TITULO")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "CONTENIDO")
    private String contenido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "PRIORIDAD")
    private String prioridad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VIGENCIA")
    private Integer vigencia;
    @Size( max = 10)
    @Column(name = "PERFIL_DESTINO")
    private String perfilDestino;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    public CoreAviso() {
    }

    public CoreAviso(Integer coreAvisoId) {
        this.coreAvisoId = coreAvisoId;
    }

    public CoreAviso(Integer coreAvisoId, String titulo, String contenido, String estatus, String prioridad, Integer vigencia) {
        this.coreAvisoId = coreAvisoId;
        this.titulo = titulo;
        this.contenido = contenido;
        this.estatus = estatus;
        this.prioridad = prioridad;
        this.vigencia = vigencia;
    }

    public Integer getCoreAvisoId() {
        return coreAvisoId;
    }

    public void setCoreAvisoId(Integer coreAvisoId) {
        this.coreAvisoId = coreAvisoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreAvisoId != null ? coreAvisoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreAviso)) {
            return false;
        }
        CoreAviso other = (CoreAviso) object;
        if ((this.coreAvisoId == null && other.coreAvisoId != null) || (this.coreAvisoId != null && !this.coreAvisoId.equals(other.coreAvisoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format=new SimpleDateFormat("kk:mm:ss");
        String devolver="{" +
            "\"coreAvisoId\":"+coreAvisoId+"," +
            "\"contenido\":\""+contenido.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("\"", "'")+"\"," +
            "\"titulo\":\""+titulo+"\"," +
            "\"estatus\":\""+estatus+"\"," +
            "\"vigencia\":"+vigencia+"," +
            "\"prioridad\":\""+prioridad+"\"," +
            "\"perfilDestino\":"+(perfilDestino!=null?"\""+perfilDestino+"\"":null)+"," +
            "\"fechaCreacion\":\""+format2.format(fechaCreacion)+"\"," +
            "\"fechaCreacionHora\":\""+format.format(fechaCreacion)+"\"" ;                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }

    /**
     * @return the perfilDestino
     */
    public String getPerfilDestino() {
        return perfilDestino;
    }

    /**
     * @param perfilDestino the perfilDestino to set
     */
    public void setPerfilDestino(String perfilDestino) {
        this.perfilDestino = perfilDestino;
    }

    /**
     * @return the fechaCreacion
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
}
