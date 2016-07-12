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
@Table(name = "core_correo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreCorreo.findAll", query = "SELECT c FROM CoreCorreo c"),
    @NamedQuery(name = "CoreCorreo.findByCoreCorreoId", query = "SELECT c FROM CoreCorreo c WHERE c.coreCorreoId = :coreCorreoId"),
    @NamedQuery(name = "CoreCorreo.findByUser", query = "SELECT c FROM CoreCorreo c WHERE c.user = :user"),
    @NamedQuery(name = "CoreCorreo.findByPassword", query = "SELECT c FROM CoreCorreo c WHERE c.password = :password"),
    @NamedQuery(name = "CoreCorreo.findByEmisor", query = "SELECT c FROM CoreCorreo c WHERE c.emisor = :emisor"),
    @NamedQuery(name = "CoreCorreo.findBySmtpServer", query = "SELECT c FROM CoreCorreo c WHERE c.smtpServer = :smtpServer"),
    @NamedQuery(name = "CoreCorreo.findBySmtpPort", query = "SELECT c FROM CoreCorreo c WHERE c.smtpPort = :smtpPort")})
public class CoreCorreo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_CORREO_ID")
    private Integer coreCorreoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER")
    private String user;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "EMISOR")
    private String emisor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "SMTP_SERVER")
    private String smtpServer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "SMTP_PORT")
    private String smtpPort;

    public CoreCorreo() {
    }

    public CoreCorreo(Integer coreCorreoId) {
        this.coreCorreoId = coreCorreoId;
    }

    public CoreCorreo(Integer coreCorreoId, String user, String password, String emisor, String smtpServer, String smtpPort) {
        this.coreCorreoId = coreCorreoId;
        this.user = user;
        this.password = password;
        this.emisor = emisor;
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
    }

    public Integer getCoreCorreoId() {
        return coreCorreoId;
    }

    public void setCoreCorreoId(Integer coreCorreoId) {
        this.coreCorreoId = coreCorreoId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreCorreoId != null ? coreCorreoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreCorreo)) {
            return false;
        }
        CoreCorreo other = (CoreCorreo) object;
        if ((this.coreCorreoId == null && other.coreCorreoId != null) || (this.coreCorreoId != null && !this.coreCorreoId.equals(other.coreCorreoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreCorreoId\":"+coreCorreoId+"," +
            "\"emisor\":\""+emisor+"\"," +
            "\"smtpPort\":\""+smtpPort+"\"," +
            "\"smtpServer\":\""+smtpServer+"\"," +
            "\"user\":\""+user+"\"," +
            "\"password\":\"SAME\"";                
        return devolver+"}";
    }
    
}
