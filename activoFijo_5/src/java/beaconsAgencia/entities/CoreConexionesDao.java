/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jgonzalezc
 */
@Entity
@Table(name = "core_conexiones_dao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreConexionesDao.findAll", query = "SELECT c FROM CoreConexionesDao c"),
    @NamedQuery(name = "CoreConexionesDao.findByCoreConexionesDaoId", query = "SELECT c FROM CoreConexionesDao c WHERE c.coreConexionesDaoId = :coreConexionesDaoId"),
    @NamedQuery(name = "CoreConexionesDao.findByNombre", query = "SELECT c FROM CoreConexionesDao c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CoreConexionesDao.findByDriver", query = "SELECT c FROM CoreConexionesDao c WHERE c.driver = :driver"),
    @NamedQuery(name = "CoreConexionesDao.findByServidor", query = "SELECT c FROM CoreConexionesDao c WHERE c.servidor = :servidor"),
    @NamedQuery(name = "CoreConexionesDao.findByUsuario", query = "SELECT c FROM CoreConexionesDao c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CoreConexionesDao.findByContrasena", query = "SELECT c FROM CoreConexionesDao c WHERE c.contrasena = :contrasena")})
public class CoreConexionesDao implements Serializable {
    @OneToMany(mappedBy = "conexionDao")
    private List<CoreValidador> coreValidadorList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_CONEXIONES_DAO_ID")
    private Integer coreConexionesDaoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DRIVER")
    private String driver;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "SERVIDOR")
    private String servidor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USUARIO")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CONTRASENA")
    private String contrasena;

    public CoreConexionesDao() {
    }

    public CoreConexionesDao(Integer coreConexionesDaoId) {
        this.coreConexionesDaoId = coreConexionesDaoId;
    }

    public CoreConexionesDao(Integer coreConexionesDaoId, String nombre, String driver, String servidor, String usuario, String contrasena) {
        this.coreConexionesDaoId = coreConexionesDaoId;
        this.nombre = nombre;
        this.driver = driver;
        this.servidor = servidor;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Integer getCoreConexionesDaoId() {
        return coreConexionesDaoId;
    }

    public void setCoreConexionesDaoId(Integer coreConexionesDaoId) {
        this.coreConexionesDaoId = coreConexionesDaoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreConexionesDaoId != null ? coreConexionesDaoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreConexionesDao)) {
            return false;
        }
        CoreConexionesDao other = (CoreConexionesDao) object;
        if ((this.coreConexionesDaoId == null && other.coreConexionesDaoId != null) || (this.coreConexionesDaoId != null && !this.coreConexionesDaoId.equals(other.coreConexionesDaoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreConexionesDaoId\":"+coreConexionesDaoId+"," +
            "\"nombre\":\""+nombre+"\"," +
            "\"driver\":\""+driver+"\"," +
            "\"servidor\":\""+servidor+"\"," +
            "\"usuario\":\""+usuario+"\"," +
            "\"contrasena\":\"SAME\"" ;                
        return devolver+"}";
    }

    @XmlTransient
    public List<CoreValidador> getCoreValidadorList() {
        return coreValidadorList;
    }

    public void setCoreValidadorList(List<CoreValidador> coreValidadorList) {
        this.coreValidadorList = coreValidadorList;
    }
    
}
