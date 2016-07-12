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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "core_dashboard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreDashboard.findAll", query = "SELECT c FROM CoreDashboard c"),
    @NamedQuery(name = "CoreDashboard.findByCoreDashboard", query = "SELECT c FROM CoreDashboard c WHERE c.coreDashboardId = :coreDashboardId"),
    @NamedQuery(name = "CoreDashboard.findByTipo", query = "SELECT c FROM CoreDashboard c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "CoreDashboard.findByParametros", query = "SELECT c FROM CoreDashboard c WHERE c.parametros = :parametros"),
    @NamedQuery(name = "CoreDashboard.findByColumnas", query = "SELECT c FROM CoreDashboard c WHERE c.columnas = :columnas"),
    @NamedQuery(name = "CoreDashboard.findByActivo", query = "SELECT c FROM CoreDashboard c WHERE c.activo = :activo")})
public class CoreDashboard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_DASHBOARD_ID")
    private Integer coreDashboardId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @Size(max = 200)
    @Column(name = "PARAMETROS")
    private String parametros;
    @Size(max = 1)
    @Column(name = "COLUMNAS")
    private String columnas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private boolean activo;
    @JoinColumn(name = "CVE_DATASOURCE", referencedColumnName = "CVE_DATASOURCE")
    @ManyToOne(optional = false)
    private CoreDatasource cveDatasource;
    @JoinColumn(name = "CORE_PERFIL_ID", referencedColumnName = "CORE_PERFIL_ID")
    @ManyToOne(optional = false)
    private CorePerfil corePerfilId;
    @Size(max = 3000)
    @Column(name = "CONTENIDO")
    private String contenido;
    @Size(max = 1)
    @Column(name = "BLOQUE")
    private String bloque;
    @Size(max = 100)
    @Column(name = "TITULO")
    private String titulo;

    public CoreDashboard() {
    }

    public CoreDashboard(Integer coreDashboardId) {
        this.coreDashboardId = coreDashboardId;
    }

    public CoreDashboard(Integer coreDashboardId, String tipo, boolean activo) {
        this.coreDashboardId = coreDashboardId;
        this.tipo = tipo;
        this.activo = activo;
    }

    public Integer getCoreDashboard() {
        return coreDashboardId;
    }

    public void setCoreDashboard(Integer coreDashboardId) {
        this.coreDashboardId = coreDashboardId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getColumnas() {
        return columnas;
    }

    public void setColumnas(String columnas) {
        this.columnas = columnas;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public CoreDatasource getCveDatasource() {
        return cveDatasource;
    }

    public void setCveDatasource(CoreDatasource cveDatasource) {
        this.cveDatasource = cveDatasource;
    }

    public CorePerfil getCorePerfilId() {
        return corePerfilId;
    }

    public void setCorePerfilId(CorePerfil corePerfilId) {
        this.corePerfilId = corePerfilId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreDashboardId != null ? coreDashboardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreDashboard)) {
            return false;
        }
        CoreDashboard other = (CoreDashboard) object;
        if ((this.coreDashboardId == null && other.coreDashboardId != null) || (this.coreDashboardId != null && !this.coreDashboardId.equals(other.coreDashboardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreDashboardId\":"+coreDashboardId+"," +
            "\"cveDatasource\":{\"id\":\""+cveDatasource.getCveDatasource()+"\",\"text\":\""+cveDatasource.getCveDatasource()+"\"}," +
            "\"bloque\":\""+bloque+"\"," +
            "\"corePerfilId\":{\"id\":"+corePerfilId.getCorePerfilId()+",\"text\":\""+corePerfilId.getPerfil()+"\"}," +
            "\"tipo\":\""+tipo+"\"," +
            "\"titulo\":"+((titulo!=null)?"\""+titulo+"\",":"null,") +
            "\"parametros\":"+((parametros!=null)?"\""+parametros+"\",":"null,") +
            "\"columnas\":"+((columnas!=null)?"\""+columnas+"\",":"null,") +
            "\"contenido\":"+((contenido!=null)?"\""+contenido.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("\"", "'")+"\",":"null,") +
            "\"activo\":"+activo;           
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }

    /**
     * @return the contenido
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * @return the bloque
     */
    public String getBloque() {
        return bloque;
    }

    /**
     * @param bloque the bloque to set
     */
    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
}
