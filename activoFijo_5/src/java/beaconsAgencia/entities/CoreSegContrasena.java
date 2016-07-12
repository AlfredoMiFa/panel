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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jgonzalezc
 */
@Entity
@Table(name = "core_seg_contrasena")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreSegContrasena.findAll", query = "SELECT c FROM CoreSegContrasena c"),
    @NamedQuery(name = "CoreSegContrasena.findByCoreSegContrasenaId", query = "SELECT c FROM CoreSegContrasena c WHERE c.coreSegContrasenaId = :coreSegContrasenaId"),
    @NamedQuery(name = "CoreSegContrasena.findByLongusuario", query = "SELECT c FROM CoreSegContrasena c WHERE c.longusuario = :longusuario"),
    @NamedQuery(name = "CoreSegContrasena.findByLongcontrasena", query = "SELECT c FROM CoreSegContrasena c WHERE c.longcontrasena = :longcontrasena"),
    @NamedQuery(name = "CoreSegContrasena.findByVigencia", query = "SELECT c FROM CoreSegContrasena c WHERE c.vigencia = :vigencia"),
    @NamedQuery(name = "CoreSegContrasena.findByRecnum", query = "SELECT c FROM CoreSegContrasena c WHERE c.recnum = :recnum"),
    @NamedQuery(name = "CoreSegContrasena.findByReccar", query = "SELECT c FROM CoreSegContrasena c WHERE c.reccar = :reccar")})
public class CoreSegContrasena implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_SEG_CONTRASENA_ID")
    private Integer coreSegContrasenaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGUSUARIO")
    private int longusuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGCONTRASENA")
    private int longcontrasena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VIGENCIA")
    private int vigencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECNUM")
    private boolean recnum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECCAR")
    private boolean reccar;

    public CoreSegContrasena() {
    }

    public CoreSegContrasena(Integer coreSegContrasena) {
        this.coreSegContrasenaId = coreSegContrasena;
    }

    public CoreSegContrasena(Integer coreSegContrasenaId, int longusuario, int longcontrasena, int vigencia, boolean recnum, boolean reccar) {
        this.coreSegContrasenaId = coreSegContrasenaId;
        this.longusuario = longusuario;
        this.longcontrasena = longcontrasena;
        this.vigencia = vigencia;
        this.recnum = recnum;
        this.reccar = reccar;
    }

    public Integer getCoreSegContrasenaId() {
        return coreSegContrasenaId;
    }

    public void setCoreSegContrasenaId(Integer coreSegContrasena) {
        this.coreSegContrasenaId = coreSegContrasena;
    }

    public int getLongusuario() {
        return longusuario;
    }

    public void setLongusuario(int longusuario) {
        this.longusuario = longusuario;
    }

    public int getLongcontrasena() {
        return longcontrasena;
    }

    public void setLongcontrasena(int longcontrasena) {
        this.longcontrasena = longcontrasena;
    }

    public int getVigencia() {
        return vigencia;
    }

    public void setVigencia(int vigencia) {
        this.vigencia = vigencia;
    }

    public boolean getRecnum() {
        return recnum;
    }

    public void setRecnum(boolean recnum) {
        this.recnum = recnum;
    }

    public boolean getReccar() {
        return reccar;
    }

    public void setReccar(boolean reccar) {
        this.reccar = reccar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreSegContrasenaId != null ? coreSegContrasenaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreSegContrasena)) {
            return false;
        }
        CoreSegContrasena other = (CoreSegContrasena) object;
        if ((this.coreSegContrasenaId == null && other.coreSegContrasenaId != null) || (this.coreSegContrasenaId != null && !this.coreSegContrasenaId.equals(other.coreSegContrasenaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreSegContrasenaId\":"+coreSegContrasenaId+"," +
            "\"longusuario\":"+longusuario+"," +
            "\"longcontrasena\":"+longcontrasena+"," +
            "\"vigencia\":"+vigencia+"," +
            "\"recnum\":"+recnum+"," +
            "\"reccar\":"+reccar ;                
        return devolver+"}";
    }
    
}
