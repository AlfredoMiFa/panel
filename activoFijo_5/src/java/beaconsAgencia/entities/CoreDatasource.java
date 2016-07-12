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
@Table(name = "core_datasource")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreDatasource.findAll", query = "SELECT c FROM CoreDatasource c"),
    @NamedQuery(name = "CoreDatasource.findByCveDatasource", query = "SELECT c FROM CoreDatasource c WHERE c.cveDatasource = :cveDatasource"),
    @NamedQuery(name = "CoreDatasource.findByCampos", query = "SELECT c FROM CoreDatasource c WHERE c.campos = :campos"),
    @NamedQuery(name = "CoreDatasource.findByWherec", query = "SELECT c FROM CoreDatasource c WHERE c.wherec = :wherec"),
    @NamedQuery(name = "CoreDatasource.findByOrderBy", query = "SELECT c FROM CoreDatasource c WHERE c.orderBy = :orderBy"),
    @NamedQuery(name = "CoreDatasource.findByOrderc", query = "SELECT c FROM CoreDatasource c WHERE c.orderc = :orderc"),
    @NamedQuery(name = "CoreDatasource.findByLimitc", query = "SELECT c FROM CoreDatasource c WHERE c.limitc = :limitc"),
    @NamedQuery(name = "CoreDatasource.findByConexionDao", query = "SELECT c FROM CoreDatasource c WHERE c.conexionDao = :conexionDao")})
public class CoreDatasource implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "META_TAG")
    private String metaTag;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CVE_DATASOURCE")
    private String cveDatasource;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "CAMPOS")
    private String campos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
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
    @Size( max = 10)
    @Column(name = "LIMITC")
    private String limitc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONEXION_DAO")
    private Integer conexionDao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cveDatasource")
    private List<CoreDashboard> coreDashboardList;
    @Size(max = 45)
    @Column(name = "GROUP_BY")
    private String groupBy;

    public CoreDatasource() {
    }

    public CoreDatasource(String cveDatasource) {
        this.cveDatasource = cveDatasource;
    }

    public CoreDatasource(String cveDatasource, String campos, String from, String limitc) {
        this.cveDatasource = cveDatasource;
        this.campos = campos;
        this.tablas = from;
        this.limitc = limitc;
    }

    public String getCveDatasource() {
        return cveDatasource;
    }

    public void setCveDatasource(String cveDatasource) {
        this.cveDatasource = cveDatasource;
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

    public void setTablas(String from) {
        this.tablas = from;
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

    public Integer getConexionDao() {
        return conexionDao;
    }

    public void setConexionDao(Integer conexionDao) {
        this.conexionDao = conexionDao;
    }

    @XmlTransient
    public List<CoreDashboard> getCoreDashboardList() {
        return coreDashboardList;
    }

    public void setCoreDashboardList(List<CoreDashboard> coreDashboardList) {
        this.coreDashboardList = coreDashboardList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveDatasource != null ? cveDatasource.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreDatasource)) {
            return false;
        }
        CoreDatasource other = (CoreDatasource) object;
        if ((this.cveDatasource == null && other.cveDatasource != null) || (this.cveDatasource != null && !this.cveDatasource.equals(other.cveDatasource))) {
            return false;
        }
        return true;
    }

    public String getMetaTag() {
        return metaTag;
    }

    public void setMetaTag(String metaTag) {
        this.metaTag = metaTag;
    }
    @Override
    public String toString() {
        String devolver="{" +
            "\"cveDatasource\":\""+cveDatasource+"\"," +
            "\"campos\":\""+campos+"\"," +
            "\"tablas\":\""+tablas+"\"," +
            "\"orderc\":"+((orderc!=null)?"\""+orderc+"\",":"null,") +
            "\"orderBy\":"+((orderBy!=null)?"\""+orderBy+"\",":"null,") +
            "\"wherec\":"+((wherec!=null)?"\""+wherec+"\",":"null,") +
            "\"limitc\":"+((limitc!=null)?"\""+limitc+"\",":"null,") +
            "\"groupBy\":"+((groupBy!=null)?"\""+groupBy+"\",":"null,") +
            "\"metaTag\":"+((metaTag!=null)?"\""+metaTag+"\",":"null,") +
            "\"conexionDao\":"+conexionDao+"";           
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }

    /**
     * @return the groupBy
     */
    public String getGroupBy() {
        return groupBy;
    }

    /**
     * @param groupBy the groupBy to set
     */
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
    
}
