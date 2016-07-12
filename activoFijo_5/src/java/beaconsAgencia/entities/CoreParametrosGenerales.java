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
@Table(name = "core_parametros_generales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreParametrosGenerales.findAll", query = "SELECT c FROM CoreParametrosGenerales c"),
    @NamedQuery(name = "CoreParametrosGenerales.findByClave", query = "SELECT c FROM CoreParametrosGenerales c WHERE c.clave = :clave"),
    @NamedQuery(name = "CoreParametrosGenerales.findByDescripcion", query = "SELECT c FROM CoreParametrosGenerales c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CoreParametrosGenerales.findByTipoDato", query = "SELECT c FROM CoreParametrosGenerales c WHERE c.tipoDato = :tipoDato"),
    @NamedQuery(name = "CoreParametrosGenerales.findByValor", query = "SELECT c FROM CoreParametrosGenerales c WHERE c.valor = :valor"),
    @NamedQuery(name = "CoreParametrosGenerales.findByEstatus", query = "SELECT c FROM CoreParametrosGenerales c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "CoreParametrosGenerales.findByEsBorrable", query = "SELECT c FROM CoreParametrosGenerales c WHERE c.esBorrable = :esBorrable")})
public class CoreParametrosGenerales implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "CLAVE")
    private String clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "TIPO_DATO")
    private String tipoDato;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "VALOR")
    private String valor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ES_BORRABLE")
    private String esBorrable;

    public CoreParametrosGenerales() {
    }

    public CoreParametrosGenerales(String clave) {
        this.clave = clave;
    }

    public CoreParametrosGenerales(String clave, String descripcion, String tipoDato, String valor, String estatus, String esBorrable) {
        this.clave = clave;
        this.descripcion = descripcion;
        this.tipoDato = tipoDato;
        this.valor = valor;
        this.estatus = estatus;
        this.esBorrable = esBorrable;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreParametrosGenerales)) {
            return false;
        }
        CoreParametrosGenerales other = (CoreParametrosGenerales) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"clave\":\""+clave+"\"," +
            "\"descripcion\":\""+descripcion+"\"," +
            "\"tipoDato\":\""+tipoDato+"\"," +
            "\"valor\":\""+valor+"\"," +
            "\"esBorrable\":\"" + esBorrable + "\"," +
            "\"estatus\":\""+estatus+"\"" ;                
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }
    
}
