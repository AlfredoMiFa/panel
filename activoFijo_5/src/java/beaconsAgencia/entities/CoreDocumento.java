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
import javax.persistence.Lob;
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
@Table(name = "core_documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreDocumento.findAll", query = "SELECT c FROM CoreDocumento c"),
    @NamedQuery(name = "CoreDocumento.findByCoreDocumentoId", query = "SELECT c FROM CoreDocumento c WHERE c.coreDocumentoId = :coreDocumentoId"),
    @NamedQuery(name = "CoreDocumento.findByNombreArchivo", query = "SELECT c FROM CoreDocumento c WHERE c.nombreArchivo = :nombreArchivo"),
    @NamedQuery(name = "CoreDocumento.findByDescripcion", query = "SELECT c FROM CoreDocumento c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CoreDocumento.findByTipomime", query = "SELECT c FROM CoreDocumento c WHERE c.tipomime = :tipomime"),
    @NamedQuery(name = "CoreDocumento.findByEstatus", query = "SELECT c FROM CoreDocumento c WHERE c.estatus = :estatus")})
public class CoreDocumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_DOCUMENTO_ID")
    private Integer coreDocumentoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 245)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    @Basic(optional = false)
    @NotNull
    @Size( max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "TIPOMIME")
    private String tipomime;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "DOCUMENTO")
    private byte[] documento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @JoinColumn(name = "CORE_CARPETA_ID", referencedColumnName = "CORE_CARPETA_ID")
    @ManyToOne(optional = false)
    private CoreCarpeta coreCarpetaId;

    public CoreDocumento() {
    }

    public CoreDocumento(Integer coreDocumentoId) {
        this.coreDocumentoId = coreDocumentoId;
    }

    public CoreDocumento(Integer coreDocumentoId, String nombreArchivo, String descripcion, String tipomime, byte[] documento, String estatus) {
        this.coreDocumentoId = coreDocumentoId;
        this.nombreArchivo = nombreArchivo;
        this.descripcion = descripcion;
        this.tipomime = tipomime;
        this.documento = documento;
        this.estatus = estatus;
    }

    public Integer getCoreDocumentoId() {
        return coreDocumentoId;
    }

    public void setCoreDocumentoId(Integer coreDocumentoId) {
        this.coreDocumentoId = coreDocumentoId;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipomime() {
        return tipomime;
    }

    public void setTipomime(String tipomime) {
        this.tipomime = tipomime;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public CoreCarpeta getCoreCarpetaId() {
        return coreCarpetaId;
    }

    public void setCoreCarpetaId(CoreCarpeta coreCarpetaId) {
        this.coreCarpetaId = coreCarpetaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreDocumentoId != null ? coreDocumentoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreDocumento)) {
            return false;
        }
        CoreDocumento other = (CoreDocumento) object;
        if ((this.coreDocumentoId == null && other.coreDocumentoId != null) || (this.coreDocumentoId != null && !this.coreDocumentoId.equals(other.coreDocumentoId))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        String devolver="{" +
            "\"nombreArchivo\":\""+this.nombreArchivo+"\"," +
            "\"coreDocumentoId\":"+this.coreDocumentoId+"," +
            "\"tipomime\":\""+this.tipomime+"\"," +
            "\"descripcion\":\""+this.descripcion+"\"," +
            "\"coreCarpetaId\":"+this.coreCarpetaId.getCoreCarpetaId()+"";                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }
    
}
