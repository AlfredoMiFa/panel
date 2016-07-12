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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Valar_Morgulis
 */
@Entity
@Table(name = "codigo_postales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CodigoPostales.findAll", query = "SELECT c FROM CodigoPostales c"),
    @NamedQuery(name = "CodigoPostales.findByIdCpp", query = "SELECT c FROM CodigoPostales c WHERE c.idCpp = :idCpp"),
    @NamedQuery(name = "CodigoPostales.findByCpp", query = "SELECT c FROM CodigoPostales c WHERE c.cpp = :cpp"),
    @NamedQuery(name = "CodigoPostales.findByAsentamiento", query = "SELECT c FROM CodigoPostales c WHERE c.asentamiento = :asentamiento"),
    @NamedQuery(name = "CodigoPostales.findByTipoasentamiento", query = "SELECT c FROM CodigoPostales c WHERE c.tipoasentamiento = :tipoasentamiento"),
    @NamedQuery(name = "CodigoPostales.findByMunicipio", query = "SELECT c FROM CodigoPostales c WHERE c.municipio = :municipio"),
    @NamedQuery(name = "CodigoPostales.findByEstado", query = "SELECT c FROM CodigoPostales c WHERE c.estado = :estado"),
    @NamedQuery(name = "CodigoPostales.findByTipozona", query = "SELECT c FROM CodigoPostales c WHERE c.tipozona = :tipozona")})
public class CodigoPostales implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CPP")
    private Integer idCpp;
    @Size(max = 5)
    @Column(name = "CPP")
    private String cpp;
    @Size(max = 150)
    @Column(name = "ASENTAMIENTO")
    private String asentamiento;
    @Size(max = 150)
    @Column(name = "TIPOASENTAMIENTO")
    private String tipoasentamiento;
    @Size(max = 100)
    @Column(name = "MUNICIPIO")
    private String municipio;
    @Size(max = 100)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 20)
    @Column(name = "TIPOZONA")
    private String tipozona;

    public CodigoPostales() {
    }

    public CodigoPostales(Integer idCpp) {
        this.idCpp = idCpp;
    }

    public Integer getIdCpp() {
        return idCpp;
    }

    public void setIdCpp(Integer idCpp) {
        this.idCpp = idCpp;
    }

    public String getCpp() {
        return cpp;
    }

    public void setCpp(String cpp) {
        this.cpp = cpp;
    }

    public String getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(String asentamiento) {
        this.asentamiento = asentamiento;
    }

    public String getTipoasentamiento() {
        return tipoasentamiento;
    }

    public void setTipoasentamiento(String tipoasentamiento) {
        this.tipoasentamiento = tipoasentamiento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipozona() {
        return tipozona;
    }

    public void setTipozona(String tipozona) {
        this.tipozona = tipozona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCpp != null ? idCpp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CodigoPostales)) {
            return false;
        }
        CodigoPostales other = (CodigoPostales) object;
        if ((this.idCpp == null && other.idCpp != null) || (this.idCpp != null && !this.idCpp.equals(other.idCpp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver = "{" +
                "\"idCpp\":"+idCpp+","+
                "\"cpp\":\""+cpp+"\","+
                "\"asentamiento\":\""+asentamiento+"\","+
                "\"tipoasentamiento\":\""+tipoasentamiento+"\","+
                "\"municipio\":\""+municipio+"\","+
                "\"estado\":\""+estado+"\","+
                "\"tipozona\":\""+tipozona+"\"}";
        return devolver;
    }
    
}
