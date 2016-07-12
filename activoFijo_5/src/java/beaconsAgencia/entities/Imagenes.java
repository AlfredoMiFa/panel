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

/**
 *
 * @author Valar_Morgulis
 */
@Entity
@Table(name = "imagenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Imagenes.findAll", query = "SELECT i FROM Imagenes i"),
    @NamedQuery(name = "Imagenes.findByIdImagenes", query = "SELECT i FROM Imagenes i WHERE i.idImagenes = :idImagenes"),
    @NamedQuery(name = "Imagenes.findByDescripcion", query = "SELECT i FROM Imagenes i WHERE i.descripcion = :descripcion")})
public class Imagenes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_IMAGENES")
    private Integer idImagenes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Lob
    @Column(name = "IMAGEN")
    private byte[] imagen;
    @JoinColumn(name = "ID_ACTIVO", referencedColumnName = "ID_ACTIVO")
    @ManyToOne(optional = false)
    private Activo idActivo;

    public Imagenes() {
    }

    public Imagenes(Integer idImagenes) {
        this.idImagenes = idImagenes;
    }

    public Imagenes(Integer idImagenes, String descripcion) {
        this.idImagenes = idImagenes;
        this.descripcion = descripcion;
    }

    public Integer getIdImagenes() {
        return idImagenes;
    }

    public void setIdImagenes(Integer idImagenes) {
        this.idImagenes = idImagenes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Activo getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(Activo idActivo) {
        this.idActivo = idActivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idImagenes != null ? idImagenes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Imagenes)) {
            return false;
        }
        Imagenes other = (Imagenes) object;
        if ((this.idImagenes == null && other.idImagenes != null) || (this.idImagenes != null && !this.idImagenes.equals(other.idImagenes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String devolver = "{" +
                "\"idImagenes\":"+idImagenes+","+
                "\"idActivo\":"+idActivo.getIdActivo()+","+
                "\"descripcion\":\""+descripcion+"\"}";
        return devolver;
    }
    
}
