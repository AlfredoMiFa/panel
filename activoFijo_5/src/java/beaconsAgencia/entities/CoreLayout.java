/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "core_layout")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreLayout.findAll", query = "SELECT c FROM CoreLayout c"),
    @NamedQuery(name = "CoreLayout.findByCoreLayoutId", query = "SELECT c FROM CoreLayout c WHERE c.coreLayoutId = :coreLayoutId"),
    @NamedQuery(name = "CoreLayout.findByTabla", query = "SELECT c FROM CoreLayout c WHERE c.tabla = :tabla"),
    @NamedQuery(name = "CoreLayout.findByNombre", query = "SELECT c FROM CoreLayout c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CoreLayout.findByActivo", query = "SELECT c FROM CoreLayout c WHERE c.activo = :activo")})
public class CoreLayout implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_LAYOUT_ID")
    private Integer coreLayoutId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "TABLA")
    private String tabla;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coreLayoutId")
    @OrderBy("orden ASC")
    private List<CoreCamposLayout> coreCamposLayoutList;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COLUMNA_INICIO")
    private Integer columnaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COLUMNA_FIN")
    private Integer columnaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RENGLON_INICIO")
    private Integer renglonInicio;

    public CoreLayout() {
    }

    public CoreLayout(Integer coreLayoutId) {
        this.coreLayoutId = coreLayoutId;
    }

    public CoreLayout(Integer coreLayoutId, String tabla, String nombre, boolean activo) {
        this.coreLayoutId = coreLayoutId;
        this.tabla = tabla;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Integer getCoreLayoutId() {
        return coreLayoutId;
    }

    public void setCoreLayoutId(Integer coreLayoutId) {
        this.coreLayoutId = coreLayoutId;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<CoreCamposLayout> getCoreCamposLayoutList() {
        return coreCamposLayoutList;
    }

    public void setCoreCamposLayoutList(List<CoreCamposLayout> coreCamposLayoutList) {
        this.coreCamposLayoutList = coreCamposLayoutList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreLayoutId != null ? coreLayoutId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreLayout)) {
            return false;
        }
        CoreLayout other = (CoreLayout) object;
        if ((this.coreLayoutId == null && other.coreLayoutId != null) || (this.coreLayoutId != null && !this.coreLayoutId.equals(other.coreLayoutId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreLayoutId\":"+coreLayoutId+"," +
            "\"tabla\":\""+tabla+"\"," +
            "\"nombre\":\""+nombre+"\"," +
            "\"columnaInicio\":"+columnaInicio+"," +
            "\"columnaFin\":"+columnaFin+"," +
            "\"renglonInicio\":"+renglonInicio+"," +
            "\"activo\":"+activo+"";                
        return devolver+"}";
    }

    /**
     * @return the columnaInicio
     */
    public Integer getColumnaInicio() {
        return columnaInicio;
    }

    /**
     * @param columnaInicio the columnaInicio to set
     */
    public void setColumnaInicio(Integer columnaInicio) {
        this.columnaInicio = columnaInicio;
    }

    /**
     * @return the columnaFin
     */
    public Integer getColumnaFin() {
        return columnaFin;
    }

    /**
     * @param columnaFin the columnaFin to set
     */
    public void setColumnaFin(Integer columnaFin) {
        this.columnaFin = columnaFin;
    }

    /**
     * @return the renglonFin
     */
    public Integer getRenglonInicio() {
        return renglonInicio;
    }

    /**
     * @param renglonFin the renglonFin to set
     */
    public void setRenglonInicio(Integer renglonFin) {
        this.renglonInicio = renglonFin;
    }
    
}
