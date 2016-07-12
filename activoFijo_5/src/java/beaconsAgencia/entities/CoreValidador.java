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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;

/**
 *
 * @author jgonzalezc
 */
@Entity
@Table(name = "core_validador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreValidador.findAll", query = "SELECT c FROM CoreValidador c"),
    @NamedQuery(name = "CoreValidador.findByCoreValidadorId", query = "SELECT c FROM CoreValidador c WHERE c.coreValidadorId = :coreValidadorId"),
    @NamedQuery(name = "CoreValidador.findByTipo", query = "SELECT c FROM CoreValidador c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "CoreValidador.findByNombreClase", query = "SELECT c FROM CoreValidador c WHERE c.nombreClase = :nombreClase"),
    @NamedQuery(name = "CoreValidador.findByNombreMetodo", query = "SELECT c FROM CoreValidador c WHERE c.nombreMetodo = :nombreMetodo"),
    @NamedQuery(name = "CoreValidador.findByRegex", query = "SELECT c FROM CoreValidador c WHERE c.regex = :regex"),
    @NamedQuery(name = "CoreValidador.findByValorInicial", query = "SELECT c FROM CoreValidador c WHERE c.valorInicial = :valorInicial"),
    @NamedQuery(name = "CoreValidador.findByValorFinal", query = "SELECT c FROM CoreValidador c WHERE c.valorFinal = :valorFinal"),
    @NamedQuery(name = "CoreValidador.findBySqlSentence", query = "SELECT c FROM CoreValidador c WHERE c.sqlSentence = :sqlSentence"),
    @NamedQuery(name = "CoreValidador.findByParametros", query = "SELECT c FROM CoreValidador c WHERE c.parametros = :parametros"),
    @NamedQuery(name = "CoreValidador.findByConexionDao", query = "SELECT c FROM CoreValidador c WHERE c.conexionDao = :conexionDao"),
    @NamedQuery(name = "CoreValidador.findByActivo", query = "SELECT c FROM CoreValidador c WHERE c.activo = :activo")})
public class CoreValidador implements Serializable {
    @Lob
    @Column(name = "CLASS1")
    private byte[] class1;
    @JoinColumn(name = "CONEXION_DAO", referencedColumnName = "CORE_CONEXIONES_DAO_ID")
    @ManyToOne
    private CoreConexionesDao conexionDao;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_VALIDADOR_ID")
    private Integer coreValidadorId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TIPO")
    private String tipo;
    @Size(max = 45)
    @Column(name = "NOMBRE_CLASE")
    private String nombreClase;
    @Size(max = 45)
    @Column(name = "NOMBRE_METODO")
    private String nombreMetodo;
    @Size(max = 200)
    @Column(name = "REGEX")
    private String regex;
    @Size(max = 20)
    @Column(name = "VALOR_INICIAL")
    private String valorInicial;
    @Size(max = 20)
    @Column(name = "VALOR_FINAL")
    private String valorFinal;
    @Size(max = 200)
    @Column(name = "SQL_SENTENCE")
    private String sqlSentence;
    @Size(max = 100)
    @Column(name = "PARAMETROS")
    private String parametros;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private boolean activo;
    @JoinColumn(name = "CORE_CAMPOS_LAYOUT_ID", referencedColumnName = "CORE_CAMPOS_LAYOUT_ID")
    @ManyToOne(optional = false)
    private CoreCamposLayout coreCamposLayoutId;
    @Basic(optional = false)
    @NotNull
    @Size(max = 45)
    @Column(name = "MENSAJE_ERROR")
    private String mensajeError;

    public CoreValidador() {
    }

    public CoreValidador(Integer coreValidadorId) {
        this.coreValidadorId = coreValidadorId;
    }

    public CoreValidador(Integer coreValidadorId, String tipo, boolean activo) {
        this.coreValidadorId = coreValidadorId;
        this.tipo = tipo;
        this.activo = activo;
    }

    public Integer getCoreValidadorId() {
        return coreValidadorId;
    }

    public void setCoreValidadorId(Integer coreValidadorId) {
        this.coreValidadorId = coreValidadorId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getClass1() {
        return class1;
    }

    public void setClass1(byte[] class1) {
        this.class1 = class1;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getNombreMetodo() {
        return nombreMetodo;
    }

    public void setNombreMetodo(String nombreMetodo) {
        this.nombreMetodo = nombreMetodo;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(String valorInicial) {
        this.valorInicial = valorInicial;
    }

    public String getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getSqlSentence() {
        return sqlSentence;
    }

    public void setSqlSentence(String sqlSentence) {
        this.sqlSentence = sqlSentence;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public CoreCamposLayout getCoreCamposLayoutId() {
        return coreCamposLayoutId;
    }

    public void setCoreCamposLayoutId(CoreCamposLayout coreCamposLayoutId) {
        this.coreCamposLayoutId = coreCamposLayoutId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreValidadorId != null ? coreValidadorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreValidador)) {
            return false;
        }
        CoreValidador other = (CoreValidador) object;
        if ((this.coreValidadorId == null && other.coreValidadorId != null) || (this.coreValidadorId != null && !this.coreValidadorId.equals(other.coreValidadorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver="{" +
            "\"coreCamposLayoutId\":"+coreCamposLayoutId.getCoreCamposLayoutId()+"," +
            "\"coreValidadorId\":"+coreValidadorId+"," +
            "\"tipo\":\""+tipo+"\"," +
            "\"nombreClase\":\""+nombreClase+"\"," +
            "\"nombreMetodo\":\""+nombreMetodo+"\"," +
            "\"regex\":\""+JSONObject.escape(regex)+"\"," +
            "\"valorInicial\":\""+valorInicial+"\"," +
            "\"valorFinal\":\""+valorFinal+"\"," +
            "\"sqlSentence\":\""+sqlSentence+"\"," +
            "\"conexionDao\":"+(conexionDao!=null?"{\"id\":"+conexionDao.getCoreConexionesDaoId()+",\"text\":\""+conexionDao.getNombre()+"\"}":null)+"," +
            "\"parametros\":\""+parametros+"\"," +
            "\"mensajeError\":\""+mensajeError+"\"," +
            "\"activo\":"+activo+"";                
        return devolver+"}";
    }

    /**
     * @return the mensajeError
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * @param mensajeError the mensajeError to set
     */
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public CoreConexionesDao getConexionDao() {
        return conexionDao;
    }

    public void setConexionDao(CoreConexionesDao conexionDao) {
        this.conexionDao = conexionDao;
    }
    
}
