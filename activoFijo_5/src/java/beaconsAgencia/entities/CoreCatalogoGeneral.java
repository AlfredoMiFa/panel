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
@Table(name = "core_catalogo_general")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreCatalogoGeneral.findAll", query = "SELECT c FROM CoreCatalogoGeneral c"),
    @NamedQuery(name = "CoreCatalogoGeneral.findByCatalogoGeneralId", query = "SELECT c FROM CoreCatalogoGeneral c WHERE c.coreCatalogoGeneralId = :coreCatalogoGeneralId"),
    @NamedQuery(name = "CoreCatalogoGeneral.findByDominio", query = "SELECT c FROM CoreCatalogoGeneral c WHERE c.dominio = :dominio"),
    @NamedQuery(name = "CoreCatalogoGeneral.findByValor", query = "SELECT c FROM CoreCatalogoGeneral c WHERE c.valor = :valor"),
    @NamedQuery(name = "CoreCatalogoGeneral.findByAtributo1", query = "SELECT c FROM CoreCatalogoGeneral c WHERE c.atributo1 = :atributo1"),
    @NamedQuery(name = "CoreCatalogoGeneral.findByAtributo2", query = "SELECT c FROM CoreCatalogoGeneral c WHERE c.atributo2 = :atributo2"),
    @NamedQuery(name = "CoreCatalogoGeneral.findByEstatus", query = "SELECT c FROM CoreCatalogoGeneral c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "CoreCatalogoGeneral.findByEsBorrable", query = "SELECT c FROM CoreCatalogoGeneral c WHERE c.esBorrable = :esBorrable")})
public class CoreCatalogoGeneral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_CATALOGO_GENERAL_ID")
    private Integer coreCatalogoGeneralId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "DOMINIO")
    private String dominio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "VALOR")
    private String valor;
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "ATRIBUTO1")
    private String atributo1;
    @Size( max = 100)
    @Column(name = "ATRIBUTO2")
    private String atributo2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ES_BORRABLE")
    private String esBorrable;

    public CoreCatalogoGeneral() {
    }

    public CoreCatalogoGeneral(Integer catalogoGeneralId) {
        this.coreCatalogoGeneralId = catalogoGeneralId;
    }

    public CoreCatalogoGeneral(Integer catalogoGeneralId, String dominio, String valor, String atributo2, String estatus, String esBorrable) {
        this.coreCatalogoGeneralId = catalogoGeneralId;
        this.dominio = dominio;
        this.valor = valor;
        this.atributo2 = atributo2;
        this.estatus = estatus;
        this.esBorrable = esBorrable;
    }

    public Integer getCoreCatalogoGeneralId() {
        return coreCatalogoGeneralId;
    }

    public void setCoreCatalogoGeneralId(Integer catalogoGeneralId) {
        this.coreCatalogoGeneralId = catalogoGeneralId;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getAtributo1() {
        return atributo1;
    }

    public void setAtributo1(String atributo1) {
        this.atributo1 = atributo1;
    }

    public String getAtributo2() {
        return atributo2;
    }

    public void setAtributo2(String atributo2) {
        this.atributo2 = atributo2;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEsBorrable() {
        return esBorrable;
    }

    public void setEsBorrable(String esBorrable) {
        this.esBorrable = esBorrable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreCatalogoGeneralId != null ? coreCatalogoGeneralId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreCatalogoGeneral)) {
            return false;
        }
        CoreCatalogoGeneral other = (CoreCatalogoGeneral) object;
        if ((this.coreCatalogoGeneralId == null && other.coreCatalogoGeneralId != null) || (this.coreCatalogoGeneralId != null && !this.coreCatalogoGeneralId.equals(other.coreCatalogoGeneralId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        String devolver="{" +
            "\"coreCatalogoGeneralId\":"+coreCatalogoGeneralId+"," +
            "\"dominio\":\""+dominio+"\"," +
            "\"valor\":\""+valor+"\"," +
            "\"atributo1\":\""+atributo1+"\"," +
            "\"atributo2\":\""+atributo2+"\"," +
            "\"esBorrable\":\""+esBorrable+"\"," +
            "\"estatus\":\""+estatus+"\"" ;                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }
    public String toString2() {
        String devolver=coreCatalogoGeneralId+"|" +
            dominio+"|" +
            valor+"|" +
            atributo1+"|" +
            atributo2+"|" +
            esBorrable+"|" +
            estatus+"|" ;                
        return devolver;
    }
    
}
