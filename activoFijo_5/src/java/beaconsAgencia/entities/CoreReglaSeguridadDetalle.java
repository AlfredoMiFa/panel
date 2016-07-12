/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jgonzalezc
 */
@Entity
@Table(name = "core_regla_seguridad_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreReglaSeguridadDetalle.findAll", query = "SELECT c FROM CoreReglaSeguridadDetalle c"),
    @NamedQuery(name = "CoreReglaSeguridadDetalle.findByCoreReglaSeguridadDetalleId", query = "SELECT c FROM CoreReglaSeguridadDetalle c WHERE c.coreReglaSeguridadDetalleId = :coreReglaSeguridadDetalleId"),
    @NamedQuery(name = "CoreReglaSeguridadDetalle.findByNombre", query = "SELECT c FROM CoreReglaSeguridadDetalle c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CoreReglaSeguridadDetalle.findByCatalogo", query = "SELECT c FROM CoreReglaSeguridadDetalle c WHERE c.catalogo = :catalogo"),
    @NamedQuery(name = "CoreReglaSeguridadDetalle.findByValorIni", query = "SELECT c FROM CoreReglaSeguridadDetalle c WHERE c.valorIni = :valorIni"),
    @NamedQuery(name = "CoreReglaSeguridadDetalle.findByValorFin", query = "SELECT c FROM CoreReglaSeguridadDetalle c WHERE c.valorFin = :valorFin"),
    @NamedQuery(name = "CoreReglaSeguridadDetalle.findByTipo", query = "SELECT c FROM CoreReglaSeguridadDetalle c WHERE c.tipo = :tipo")})
public class CoreReglaSeguridadDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CORE_REGLA_SEGURIDAD_DETALLE_ID")
    private Integer coreReglaSeguridadDetalleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CATALOGO")
    private String catalogo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "VALOR_INI")
    private String valorIni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "VALOR_FIN")
    private String valorFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumn(name = "CORE_REGLA_SEGURIDAD_ID", referencedColumnName = "CORE_REGLA_SEGURIDAD_ID")
    @ManyToOne(optional = false)
    private CoreReglaSeguridad coreReglaSeguridadId;

    public CoreReglaSeguridadDetalle() {
    }

    public CoreReglaSeguridadDetalle(Integer coreReglaSeguridadDetalleId) {
        this.coreReglaSeguridadDetalleId = coreReglaSeguridadDetalleId;
    }

    public CoreReglaSeguridadDetalle(Integer coreReglaSeguridadDetalleId, String nombre, String catalogo, String valorIni, String valorFin, String tipo) {
        this.coreReglaSeguridadDetalleId = coreReglaSeguridadDetalleId;
        this.nombre = nombre;
        this.catalogo = catalogo;
        this.valorIni = valorIni;
        this.valorFin = valorFin;
        this.tipo = tipo;
    }

    public Integer getCoreReglaSeguridadDetalleId() {
        return coreReglaSeguridadDetalleId;
    }

    public void setCoreReglaSeguridadDetalleId(Integer coreReglaSeguridadDetalleId) {
        this.coreReglaSeguridadDetalleId = coreReglaSeguridadDetalleId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }

    public String getValorIni() {
        return valorIni;
    }

    public void setValorIni(String valorIni) {
        this.valorIni = valorIni;
    }

    public String getValorFin() {
        return valorFin;
    }

    public void setValorFin(String valorFin) {
        this.valorFin = valorFin;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreReglaSeguridadDetalleId != null ? coreReglaSeguridadDetalleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreReglaSeguridadDetalle)) {
            return false;
        }
        CoreReglaSeguridadDetalle other = (CoreReglaSeguridadDetalle) object;
        if ((this.coreReglaSeguridadDetalleId == null && other.coreReglaSeguridadDetalleId != null) || (this.coreReglaSeguridadDetalleId != null && !this.coreReglaSeguridadDetalleId.equals(other.coreReglaSeguridadDetalleId))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        String devolver="{" +
            "\"coreReglaSeguridadDetalleId\":"+coreReglaSeguridadDetalleId+"," +
            "\"coreReglaSeguridadId\":"+coreReglaSeguridadId.getCoreReglaSeguridadId()+"," +
            "\"nombre\":\""+nombre+"\"," +
            "\"catalogo\":\""+catalogo+"\"," +
            "\"valorIni\":\""+valorIni+"\"," +
            "\"valorFin\":\""+valorFin+"\"," +
            "\"tipo\":\""+tipo+"\"";                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }
    public String toString2() {
        String devolver=coreReglaSeguridadDetalleId+"|" +
            getCoreReglaSeguridadId()+"|" +
            nombre+"|" +
            catalogo+"|" +
            valorIni+"|" +
            valorFin+"|" +
            tipo+"|" ;                
        return devolver;
    }

    /**
     * @return the coreReglaSeguridadId
     */
    public CoreReglaSeguridad getCoreReglaSeguridadId() {
        return coreReglaSeguridadId;
    }

    /**
     * @param coreReglaSeguridadId the coreReglaSeguridadId to set
     */
    public void setCoreReglaSeguridadId(CoreReglaSeguridad coreReglaSeguridadId) {
        this.coreReglaSeguridadId = coreReglaSeguridadId;
    }
    
}
