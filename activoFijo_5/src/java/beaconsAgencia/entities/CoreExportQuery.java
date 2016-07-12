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
@Table(name = "core_export_query")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreExportQuery.findAll", query = "SELECT c FROM CoreExportQuery c"),
    @NamedQuery(name = "CoreExportQuery.findByCveExportQuery", query = "SELECT c FROM CoreExportQuery c WHERE c.cveExportQuery = :cveExportQuery"),
    @NamedQuery(name = "CoreExportQuery.findByCampos", query = "SELECT c FROM CoreExportQuery c WHERE c.campos = :campos"),
    @NamedQuery(name = "CoreExportQuery.findByTablas", query = "SELECT c FROM CoreExportQuery c WHERE c.tablas = :tablas"),
    @NamedQuery(name = "CoreExportQuery.findByWherec", query = "SELECT c FROM CoreExportQuery c WHERE c.wherec = :wherec"),
    @NamedQuery(name = "CoreExportQuery.findByOrderBy", query = "SELECT c FROM CoreExportQuery c WHERE c.orderBy = :orderBy"),
    @NamedQuery(name = "CoreExportQuery.findByOrderc", query = "SELECT c FROM CoreExportQuery c WHERE c.orderc = :orderc"),
    @NamedQuery(name = "CoreExportQuery.findByLimitc", query = "SELECT c FROM CoreExportQuery c WHERE c.limitc = :limitc"),
    @NamedQuery(name = "CoreExportQuery.findByAppendFilter", query = "SELECT c FROM CoreExportQuery c WHERE c.appendFilter = :appendFilter"),
    @NamedQuery(name = "CoreExportQuery.findByConexionDao", query = "SELECT c FROM CoreExportQuery c WHERE c.conexionDao = :conexionDao"),
    @NamedQuery(name = "CoreExportQuery.findByEstatus", query = "SELECT c FROM CoreExportQuery c WHERE c.estatus = :estatus")})
public class CoreExportQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CVE_EXPORT_QUERY")
    private String cveExportQuery;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "CAMPOS")
    private String campos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "TABLAS")
    private String tablas;
    @Size(max = 400)
    @Column(name = "WHEREC")
    private String wherec;
    @Size(max = 45)
    @Column(name = "ORDER_BY")
    private String orderBy;
    @Size(max = 45)
    @Column(name = "ORDERC")
    private String orderc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "LIMITC")
    private String limitc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "APPEND_FILTER")
    private boolean appendFilter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONEXION_DAO")
    private int conexionDao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @Size(max = 1000)
    @Column(name = "HEADER")
    private String header;

    public CoreExportQuery() {
    }

    public CoreExportQuery(String cveExportQuery) {
        this.cveExportQuery = cveExportQuery;
    }

    public CoreExportQuery(String cveExportQuery, String campos, String tablas, String limitc, boolean appendFilter, int conexionDao, String estatus) {
        this.cveExportQuery = cveExportQuery;
        this.campos = campos;
        this.tablas = tablas;
        this.limitc = limitc;
        this.appendFilter = appendFilter;
        this.conexionDao = conexionDao;
        this.estatus = estatus;
    }

    public String getCveExportQuery() {
        return cveExportQuery;
    }

    public void setCveExportQuery(String cveExportQuery) {
        this.cveExportQuery = cveExportQuery;
    }

    public String getCampos() {
        return campos;
    }

    public void setCampos(String campos) {
        this.campos = campos;
    }

    public String getTablas() {
        return tablas;
    }

    public void setTablas(String tablas) {
        this.tablas = tablas;
    }

    public String getWherec() {
        return wherec;
    }

    public void setWherec(String wherec) {
        this.wherec = wherec;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderc() {
        return orderc;
    }

    public void setOrderc(String orderc) {
        this.orderc = orderc;
    }

    public String getLimitc() {
        return limitc;
    }

    public void setLimitc(String limitc) {
        this.limitc = limitc;
    }

    public boolean getAppendFilter() {
        return appendFilter;
    }

    public void setAppendFilter(boolean appendFilter) {
        this.appendFilter = appendFilter;
    }

    public int getConexionDao() {
        return conexionDao;
    }

    public void setConexionDao(int conexionDao) {
        this.conexionDao = conexionDao;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveExportQuery != null ? cveExportQuery.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreExportQuery)) {
            return false;
        }
        CoreExportQuery other = (CoreExportQuery) object;
        if ((this.cveExportQuery == null && other.cveExportQuery != null) || (this.cveExportQuery != null && !this.cveExportQuery.equals(other.cveExportQuery))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        String devolver="{" +
            "\"cveExportQuery\":\""+cveExportQuery+"\"," +
            "\"campos\":\""+campos+"\"," +
            "\"tablas\":\""+tablas+"\"," +
            "\"orderc\":"+((orderc!=null)?"\""+orderc+"\",":"null,") +
            "\"orderBy\":"+((orderBy!=null)?"\""+orderBy+"\",":"null,") +
            "\"wherec\":"+((wherec!=null)?"\""+wherec+"\",":"null,") +
            "\"limitc\":"+((limitc!=null)?"\""+limitc+"\",":"null,") +
            "\"header\":"+((header!=null)?"\""+header+"\",":"null,") +
            "\"appendFilter\":"+appendFilter+"," +
            "\"estatus\":\""+estatus+"\","+   
            "\"conexionDao\":"+conexionDao+"";           
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }
    
}
