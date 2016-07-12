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
 * @author Servamp
 */
@Entity
@Table(name = "activo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activo.findAll", query = "SELECT a FROM Activo a"),
    @NamedQuery(name = "Activo.findByIdActivo", query = "SELECT a FROM Activo a WHERE a.idActivo = :idActivo"),
    @NamedQuery(name = "Activo.findByNumeroInventario", query = "SELECT a FROM Activo a WHERE a.numeroInventario = :numeroInventario"),
    @NamedQuery(name = "Activo.findByDenominacion", query = "SELECT a FROM Activo a WHERE a.denominacion = :denominacion"),
    @NamedQuery(name = "Activo.findByNumeroSerie", query = "SELECT a FROM Activo a WHERE a.numeroSerie = :numeroSerie"),
    @NamedQuery(name = "Activo.findByDescripcion", query = "SELECT a FROM Activo a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Activo.findByMarca", query = "SELECT a FROM Activo a WHERE a.marca = :marca"),
    @NamedQuery(name = "Activo.findByModelo", query = "SELECT a FROM Activo a WHERE a.modelo = :modelo"),
    @NamedQuery(name = "Activo.findByPrecio", query = "SELECT a FROM Activo a WHERE a.precio = :precio"),
    @NamedQuery(name = "Activo.findByNotaCredito", query = "SELECT a FROM Activo a WHERE a.notaCredito = :notaCredito"),
    @NamedQuery(name = "Activo.findByEstado", query = "SELECT a FROM Activo a WHERE a.estado = :estado")})
public class Activo implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idActivo")
    private List<Incidencias> incidenciasList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ACTIVO", nullable = false)
    private Integer idActivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NUMERO_INVENTARIO", nullable = false, length = 45)
    private String numeroInventario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "DENOMINACION", nullable = false, length = 60)
    private String denominacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "NUMERO_SERIE", nullable = false, length = 60)
    private String numeroSerie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPCION", nullable = false, length = 100)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "MARCA", nullable = false, length = 30)
    private String marca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "MODELO", nullable = false, length = 30)
    private String modelo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRECIO", nullable = false)
    private float precio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NOTA_CREDITO", nullable = false, length = 20)
    private String notaCredito;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ESTADO", nullable = false, length = 2)
    private String estado;
    @JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID_CATEGORIA", nullable = false)
    @ManyToOne(optional = false)
    private Categoria idCategoria;
    @JoinColumn(name = "ID_OFICINA", referencedColumnName = "ID_OFICINA", nullable = false)
    @ManyToOne(optional = false)
    private Oficinas idOficina;
    @JoinColumn(name = "ID_PERSONAL", referencedColumnName = "ID_PERSONAL", nullable = false)
    @ManyToOne(optional = false)
    private Personal idPersonal;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO", nullable = false)
    @ManyToOne(optional = false)
    private Departamento idDepartamento;

    public Activo() {
    }

    public Activo(Integer idActivo) {
        this.idActivo = idActivo;
    }

    public Activo(Integer idActivo, String numeroInventario, String denominacion, String numeroSerie, String descripcion, String marca, String modelo, float precio, String notaCredito, String estado) {
        this.idActivo = idActivo;
        this.numeroInventario = numeroInventario;
        this.denominacion = denominacion;
        this.numeroSerie = numeroSerie;
        this.descripcion = descripcion;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.notaCredito = notaCredito;
        this.estado = estado;
    }

    public Integer getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(Integer idActivo) {
        this.idActivo = idActivo;
    }

    public String getNumeroInventario() {
        return numeroInventario;
    }

    public void setNumeroInventario(String numeroInventario) {
        this.numeroInventario = numeroInventario;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(String notaCredito) {
        this.notaCredito = notaCredito;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Oficinas getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Oficinas idOficina) {
        this.idOficina = idOficina;
    }

    public Personal getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(Personal idPersonal) {
        this.idPersonal = idPersonal;
    }

    public Departamento getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Departamento idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActivo != null ? idActivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Activo)) {
            return false;
        }
        Activo other = (Activo) object;
        if ((this.idActivo == null && other.idActivo != null) || (this.idActivo != null && !this.idActivo.equals(other.idActivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver = "{"+
                "\"idActivo\":"+idActivo+","+
                "\"idOficina\":{\"id\":"+idOficina.getIdOficina()+",\"text\":\""+idOficina.getNombreOficina()+"\"},"+
                "\"idDepartamento\":{\"id\":"+idDepartamento.getIdDepartamento()+",\"text\":\""+idDepartamento.getDescripcion()+"\"},"+
                "\"idPersonal\":{\"id\":"+idPersonal.getIdPersonal()+",\"text\":\""+idPersonal.getNombre()+"\"},"+
                "\"idCategoria\":{\"id\":"+idCategoria.getIdCategoria()+",\"text\":\""+idCategoria.getDescripcion()+"\"},"+
                "\"numeroInventario\":\""+numeroInventario+"\","+
                "\"denominacion\":\""+denominacion+"\","+
                "\"numeroSerie\":\""+numeroSerie+"\","+
                "\"descripcion\":\""+descripcion+"\","+
                "\"marca\":\""+marca+"\","+
                "\"modelo\":\""+modelo+"\","+
                "\"precio\":"+precio+","+
                "\"notaCredito\":\""+notaCredito+"\","+
                "\"estado\":\""+estado+"\"}";
        return devolver;
        //return "beaconsAgencia.entities.Activo[ idActivo=" + idActivo + " ]";
    }

    @XmlTransient
    public List<Incidencias> getIncidenciasList() {
        return incidenciasList;
    }

    public void setIncidenciasList(List<Incidencias> incidenciasList) {
        this.incidenciasList = incidenciasList;
    }
    
}
