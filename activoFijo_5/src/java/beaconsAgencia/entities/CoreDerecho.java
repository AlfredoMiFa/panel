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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "core_derecho")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreDerecho.findAll", query = "SELECT c FROM CoreDerecho c"),
    @NamedQuery(name = "CoreDerecho.findByCoreDerechoId", query = "SELECT c FROM CoreDerecho c WHERE c.coreDerechoId = :coreDerechoId"),
    @NamedQuery(name = "CoreDerecho.findByDerecho", query = "SELECT c FROM CoreDerecho c WHERE c.derecho = :derecho"),
    @NamedQuery(name = "CoreDerecho.findByNombreMenu", query = "SELECT c FROM CoreDerecho c WHERE c.nombreMenu = :nombreMenu"),
    @NamedQuery(name = "CoreDerecho.findByRutaMenu", query = "SELECT c FROM CoreDerecho c WHERE c.rutaMenu = :rutaMenu"),
    @NamedQuery(name = "CoreDerecho.findByEsEnlace", query = "SELECT c FROM CoreDerecho c WHERE c.esEnlace = :esEnlace"),
    @NamedQuery(name = "CoreDerecho.findByEsVisible", query = "SELECT c FROM CoreDerecho c WHERE c.esVisible = :esVisible"),
    @NamedQuery(name = "CoreDerecho.findByEstatus", query = "SELECT c FROM CoreDerecho c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "CoreDerecho.findMenu",query = "select o from CoreDerecho o where o.estatus='AC' order by o.orden"),
    @NamedQuery(name = "CoreDerecho.findByOrden", query = "SELECT c FROM CoreDerecho c WHERE c.orden = :orden")})
public class CoreDerecho implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_DERECHO_ID")
    private Integer coreDerechoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "DERECHO")
    private String derecho;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_MENU")
    private String nombreMenu;
    @Size(max = 45)
    @Column(name = "RUTA_MENU")
    private String rutaMenu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ES_ENLACE")
    private String esEnlace;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ES_VISIBLE")
    private String esVisible;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDEN")
    private int orden;
    @ManyToMany(mappedBy = "coreDerechoList")
    private List<CorePerfil> corePerfilList;
    @OneToMany(mappedBy = "fkPadre")
    private List<CoreDerecho> coreDerechoList;
    @JoinColumn(name = "FK_PADRE", referencedColumnName = "CORE_DERECHO_ID")
    @ManyToOne
    private CoreDerecho fkPadre;
    @Basic(optional = false)
    @Size(max = 45)
    @Column(name = "ICON")
    private String icon;

    public CoreDerecho() {
    }

    public CoreDerecho(Integer coreDerechoId) {
        this.coreDerechoId = coreDerechoId;
    }

    public CoreDerecho(Integer coreDerechoId, String derecho, String nombreMenu, String esEnlace, String esVisible, String estatus, int orden) {
        this.coreDerechoId = coreDerechoId;
        this.derecho = derecho;
        this.nombreMenu = nombreMenu;
        this.esEnlace = esEnlace;
        this.esVisible = esVisible;
        this.estatus = estatus;
        this.orden = orden;
    }

    public Integer getCoreDerechoId() {
        return coreDerechoId;
    }

    public void setCoreDerechoId(Integer coreDerechoId) {
        this.coreDerechoId = coreDerechoId;
    }

    public String getDerecho() {
        return derecho;
    }

    public void setDerecho(String derecho) {
        this.derecho = derecho;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public String getRutaMenu() {
        return rutaMenu;
    }

    public void setRutaMenu(String rutaMenu) {
        this.rutaMenu = rutaMenu;
    }

    public String getEsEnlace() {
        return esEnlace;
    }

    public void setEsEnlace(String esEnlace) {
        this.esEnlace = esEnlace;
    }

    public String getEsVisible() {
        return esVisible;
    }

    public void setEsVisible(String esVisible) {
        this.esVisible = esVisible;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    @XmlTransient
    public List<CorePerfil> getCorePerfilList() {
        return corePerfilList;
    }

    public void setCorePerfilList(List<CorePerfil> corePerfilList) {
        this.corePerfilList = corePerfilList;
    }

    @XmlTransient
    public List<CoreDerecho> getCoreDerechoList() {
        return coreDerechoList;
    }

    public void setCoreDerechoList(List<CoreDerecho> coreDerechoList) {
        this.coreDerechoList = coreDerechoList;
    }

    public CoreDerecho getFkPadre() {
        return fkPadre;
    }

    public void setFkPadre(CoreDerecho fkPadre) {
        this.fkPadre = fkPadre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreDerechoId != null ? coreDerechoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreDerecho)) {
            return false;
        }
        CoreDerecho other = (CoreDerecho) object;
        if ((this.coreDerechoId == null && other.coreDerechoId != null) || (this.coreDerechoId != null && !this.coreDerechoId.equals(other.coreDerechoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreDerechoId\":"+coreDerechoId+"," +
            "\"derecho\":\""+derecho+"\"," +
            "\"nombreMenu\":\""+nombreMenu+"\"," ;
            if(rutaMenu!=null)
                devolver+="\"rutaMenu\":\""+rutaMenu+"\",";
            else
                devolver+="\"rutaMenu\":null,";   
            devolver+="\"esVisible\":\""+esVisible+"\"," +
            "\"esEnlace\":\""+esEnlace+"\"," +
            "\"estatus\":\""+estatus+"\"," +
            "\"icon\":"+(icon!=null?"\""+icon+"\"":null)+"," +
            "\"orden\":"+orden+",";            
        if(fkPadre!=null)
            devolver+="\"padre\":{\"id\":"+fkPadre.getCoreDerechoId()+",\"text\":\""+fkPadre.getDerecho()+"\"}";
        else
            devolver+="\"padre\":{\"id\":-1,\"text\":\"Seleccione una opción\"}";             
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
}
