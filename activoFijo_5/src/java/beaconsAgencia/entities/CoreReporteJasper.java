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
import javax.persistence.Lob;
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
@Table(name = "core_reporte_jasper")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoreReporteJasper.findAll", query = "SELECT c FROM CoreReporteJasper c"),
    @NamedQuery(name = "CoreReporteJasper.findByCoreReporteJasperId", query = "SELECT c FROM CoreReporteJasper c WHERE c.coreReporteJasperId = :coreReporteJasperId"),
    @NamedQuery(name = "CoreReporteJasper.findByNombre", query = "SELECT c FROM CoreReporteJasper c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CoreReporteJasper.findByDescripcion", query = "SELECT c FROM CoreReporteJasper c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CoreReporteJasper.findByEstatus", query = "SELECT c FROM CoreReporteJasper c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "CoreReporteJasper.findByNombreArchivo", query = "SELECT c FROM CoreReporteJasper c WHERE c.nombreArchivo = :nombreArchivo"),
    @NamedQuery(name = "CoreReporteJasper.findByCvesServicios", query = "SELECT c FROM CoreReporteJasper c WHERE c.cvesServicios = :cvesServicios"),
    @NamedQuery(name = "CoreReporteJasper.findByCategoria", query = "SELECT c FROM CoreReporteJasper c WHERE c.categoria = :categoria"),
    @NamedQuery(name = "CoreReporteJasper.findByParametros", query = "SELECT c FROM CoreReporteJasper c WHERE c.parametros = :parametros"),
    @NamedQuery(name = "CoreReporteJasper.findByConexionDao", query = "SELECT c FROM CoreReporteJasper c WHERE c.conexionDao = :conexionDao")})
public class CoreReporteJasper implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CORE_REPORTE_JASPER_ID")
    private Integer coreReporteJasperId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Lob
    @Column(name = "DATA")
    private byte[] data;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @Basic(optional = false)
    @Size(max = 45)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    @Size(max = 45)
    @Column(name = "CVES_SERVICIOS")
    private String cvesServicios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "CATEGORIA")
    private String categoria;
    @Size(max = 300)
    @Column(name = "PARAMETROS")
    private String parametros;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONEXION_DAO")
    private int conexionDao;

    public CoreReporteJasper() {
    }

    public CoreReporteJasper(Integer coreReporteJasperId) {
        this.coreReporteJasperId = coreReporteJasperId;
    }

    public CoreReporteJasper(Integer coreReporteJasperId, String nombre, String estatus, String nombreArchivo, String categoria, int conexionDao) {
        this.coreReporteJasperId = coreReporteJasperId;
        this.nombre = nombre;
        this.estatus = estatus;
        this.nombreArchivo = nombreArchivo;
        this.categoria = categoria;
        this.conexionDao = conexionDao;
    }

    public Integer getCoreReporteJasperId() {
        return coreReporteJasperId;
    }

    public void setCoreReporteJasperId(Integer coreReporteJasperId) {
        this.coreReporteJasperId = coreReporteJasperId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getCvesServicios() {
        return cvesServicios;
    }

    public void setCvesServicios(String cvesServicios) {
        this.cvesServicios = cvesServicios;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public int getConexionDao() {
        return conexionDao;
    }

    public void setConexionDao(int conexionDao) {
        this.conexionDao = conexionDao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coreReporteJasperId != null ? coreReporteJasperId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoreReporteJasper)) {
            return false;
        }
        CoreReporteJasper other = (CoreReporteJasper) object;
        if ((this.coreReporteJasperId == null && other.coreReporteJasperId != null) || (this.coreReporteJasperId != null && !this.coreReporteJasperId.equals(other.coreReporteJasperId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        String devolver="{" +
            "\"coreReporteJasperId\":"+coreReporteJasperId+"," +
            "\"nombre\":\""+nombre+"\"," +
            "\"nombreArchivo\":\""+nombreArchivo+"\"," +
            "\"descripcion\":\""+descripcion+"\"," +
            "\"cvesServicios\":"+((cvesServicios!=null)?"\""+cvesServicios+"\",":"null,") +
            "\"parametros\":\""+parametros+"\"," +
            "\"estatus\":\""+estatus+"\","+   
            "\"categoria\":\""+categoria+"\","+   
            "\"conexionDao\":"+conexionDao+"";           
        return devolver.replaceAll("(\r\n|\n|\r|\t)", "")+"}";
    }
    
}
