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
@Table(name = "core_permisos_ip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CorePermisosIp.findAll", query = "SELECT c FROM CorePermisosIp c"),
    @NamedQuery(name = "CorePermisosIp.findByCorePermisosIpId", query = "SELECT c FROM CorePermisosIp c WHERE c.corePermisosIpId = :corePermisosIpId"),
    @NamedQuery(name = "CorePermisosIp.findByAccion", query = "SELECT c FROM CorePermisosIp c WHERE c.accion = :accion"),
    @NamedQuery(name = "CorePermisosIp.findByIp", query = "SELECT c FROM CorePermisosIp c WHERE c.ip = :ip"),
    @NamedQuery(name = "CorePermisosIp.findByEstatus", query = "SELECT c FROM CorePermisosIp c WHERE c.estatus = :estatus")})
public class CorePermisosIp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CORE_PERMISOS_IP_ID")
    private Integer corePermisosIpId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ACCION")
    private String accion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "IP")
    private String ip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @JoinColumn(name = "CORE_USUARIO_ID", referencedColumnName = "CORE_USUARIO_ID")
    @ManyToOne(optional = false)
    private CoreUsuario coreUsuarioId;

    public CorePermisosIp() {
    }

    public CorePermisosIp(Integer corePermisosIpId) {
        this.corePermisosIpId = corePermisosIpId;
    }

    public CorePermisosIp(Integer corePermisosIpId, String accion, String ip, String estatus) {
        this.corePermisosIpId = corePermisosIpId;
        this.accion = accion;
        this.ip = ip;
        this.estatus = estatus;
    }

    public Integer getCorePermisosIpId() {
        return corePermisosIpId;
    }

    public void setCorePermisosIpId(Integer corePermisosIpId) {
        this.corePermisosIpId = corePermisosIpId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public CoreUsuario getCoreUsuarioId() {
        return coreUsuarioId;
    }

    public void setCoreUsuarioId(CoreUsuario coreUsuarioId) {
        this.coreUsuarioId = coreUsuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (corePermisosIpId != null ? corePermisosIpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorePermisosIp)) {
            return false;
        }
        CorePermisosIp other = (CorePermisosIp) object;
        if ((this.corePermisosIpId == null && other.corePermisosIpId != null) || (this.corePermisosIpId != null && !this.corePermisosIpId.equals(other.corePermisosIpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"corePermisosIpId\":"+corePermisosIpId+"," +
            "\"coreUsuarioId\":"+coreUsuarioId.getCoreUsuarioId()+"," +
            "\"accion\":\""+accion+"\"," +
            "\"ip\":\""+ip+"\"," +
            "\"estatus\":\""+estatus+"\"";                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }
    
}
