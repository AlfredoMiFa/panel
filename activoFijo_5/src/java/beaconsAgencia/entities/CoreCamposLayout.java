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
import javax.persistence.JoinColumn;
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
@Table(name = "core_campos_layout")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreCamposLayout.findAll", query = "SELECT c FROM CoreCamposLayout c"),
    @NamedQuery(name = "CoreCamposLayout.findByCoreCamposLayoutId", query = "SELECT c FROM CoreCamposLayout c WHERE c.coreCamposLayoutId = :coreCamposLayoutId"),
    @NamedQuery(name = "CoreCamposLayout.findByNombreCampo", query = "SELECT c FROM CoreCamposLayout c WHERE c.nombreCampo = :nombreCampo"),
    @NamedQuery(name = "CoreCamposLayout.findByNombreVariable", query = "SELECT c FROM CoreCamposLayout c WHERE c.nombreVariable = :nombreVariable"),
    @NamedQuery(name = "CoreCamposLayout.findByTipoDato", query = "SELECT c FROM CoreCamposLayout c WHERE c.tipoDato = :tipoDato"),
    @NamedQuery(name = "CoreCamposLayout.findByOrden", query = "SELECT c FROM CoreCamposLayout c WHERE c.orden = :orden"),
    @NamedQuery(name = "CoreCamposLayout.findByValidar", query = "SELECT c FROM CoreCamposLayout c WHERE c.validar = :validar")})
public class CoreCamposLayout implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_CAMPOS_LAYOUT_ID")
    private Integer coreCamposLayoutId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_CAMPO")
    private String nombreCampo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_VARIABLE")
    private String nombreVariable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TIPO_DATO")
    private String tipoDato;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDEN")
    private int orden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALIDAR")
    private boolean validar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NULLABLE")
    private boolean nullable;
    @JoinColumn(name = "CORE_LAYOUT_ID", referencedColumnName = "CORE_LAYOUT_ID")
    @ManyToOne(optional = false)
    private CoreLayout coreLayoutId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coreCamposLayoutId")
    private List<CoreValidador> coreValidadorList;

    public CoreCamposLayout() {
    }

    public CoreCamposLayout(Integer coreCamposLayoutId) {
        this.coreCamposLayoutId = coreCamposLayoutId;
    }

    public CoreCamposLayout(Integer coreCamposLayoutId, String nombreCampo, String nombreVariable, String tipoDato, int orden, boolean validar) {
        this.coreCamposLayoutId = coreCamposLayoutId;
        this.nombreCampo = nombreCampo;
        this.nombreVariable = nombreVariable;
        this.tipoDato = tipoDato;
        this.orden = orden;
        this.validar = validar;
    }

    public Integer getCoreCamposLayoutId() {
        return coreCamposLayoutId;
    }

    public void setCoreCamposLayoutId(Integer coreCamposLayoutId) {
        this.coreCamposLayoutId = coreCamposLayoutId;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public String getNombreVariable() {
        return nombreVariable;
    }

    public void setNombreVariable(String nombreVariable) {
        this.nombreVariable = nombreVariable;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public boolean getValidar() {
        return validar;
    }

    public void setValidar(boolean validar) {
        this.validar = validar;
    }

    public CoreLayout getCoreLayoutId() {
        return coreLayoutId;
    }

    public void setCoreLayoutId(CoreLayout coreLayoutId) {
        this.coreLayoutId = coreLayoutId;
    }

    @XmlTransient
    public List<CoreValidador> getCoreValidadorList() {
        return coreValidadorList;
    }

    public void setCoreValidadorList(List<CoreValidador> coreValidadorList) {
        this.coreValidadorList = coreValidadorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreCamposLayoutId != null ? coreCamposLayoutId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreCamposLayout)) {
            return false;
        }
        CoreCamposLayout other = (CoreCamposLayout) object;
        if ((this.coreCamposLayoutId == null && other.coreCamposLayoutId != null) || (this.coreCamposLayoutId != null && !this.coreCamposLayoutId.equals(other.coreCamposLayoutId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreCamposLayoutId\":"+coreCamposLayoutId+"," +
            "\"coreLayoutId\":"+coreLayoutId.getCoreLayoutId()+"," +
            "\"orden\":"+orden+"," +
            "\"nombreCampo\":\""+nombreCampo+"\"," +
            "\"nombreVariable\":\""+nombreVariable+"\"," +
            "\"validar\":"+validar+"," +
            "\"nullable\":"+nullable+"," +
            "\"tipoDato\":\""+tipoDato+"\"" ;     
        return devolver+"}";
    }

    /**
     * @return the nullable
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * @param nullable the nullable to set
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
}
