/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ASUS TUF GAMING
 */
@Entity
@Table(name = "ANIO_LECTIVO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnioLectivo.findAll", query = "SELECT a FROM AnioLectivo a"),
    @NamedQuery(name = "AnioLectivo.findByIdAnioLectivo", query = "SELECT a FROM AnioLectivo a WHERE a.idAnioLectivo = :idAnioLectivo"),
    @NamedQuery(name = "AnioLectivo.findByAlNombre", query = "SELECT a FROM AnioLectivo a WHERE a.alNombre = :alNombre"),
    @NamedQuery(name = "AnioLectivo.findByAlFecInici", query = "SELECT a FROM AnioLectivo a WHERE a.alFecInici = :alFecInici"),
    @NamedQuery(name = "AnioLectivo.findByAlFecFin", query = "SELECT a FROM AnioLectivo a WHERE a.alFecFin = :alFecFin")})
public class AnioLectivo implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ANIO_LECTIVO")
    private BigDecimal idAnioLectivo;
    @Column(name = "AL_NOMBRE")
    private String alNombre;
    @Column(name = "AL_FEC_INICI")
    private String alFecInici;
    @Column(name = "AL_FEC_FIN")
    private String alFecFin;
    @OneToMany(mappedBy = "curIdA\u00f1oLectivo")
    private List<Curso> cursoList;

    public AnioLectivo() {
    }

    public AnioLectivo(BigDecimal idAnioLectivo) {
        this.idAnioLectivo = idAnioLectivo;
    }

    public BigDecimal getIdAnioLectivo() {
        return idAnioLectivo;
    }

    public void setIdAnioLectivo(BigDecimal idAnioLectivo) {
        this.idAnioLectivo = idAnioLectivo;
    }

    public String getAlNombre() {
        return alNombre;
    }

    public void setAlNombre(String alNombre) {
        this.alNombre = alNombre;
    }

    public String getAlFecInici() {
        return alFecInici;
    }

    public void setAlFecInici(String alFecInici) {
        this.alFecInici = alFecInici;
    }

    public String getAlFecFin() {
        return alFecFin;
    }

    public void setAlFecFin(String alFecFin) {
        this.alFecFin = alFecFin;
    }

    @XmlTransient
    public List<Curso> getCursoList() {
        return cursoList;
    }

    public void setCursoList(List<Curso> cursoList) {
        this.cursoList = cursoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnioLectivo != null ? idAnioLectivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnioLectivo)) {
            return false;
        }
        AnioLectivo other = (AnioLectivo) object;
        if ((this.idAnioLectivo == null && other.idAnioLectivo != null) || (this.idAnioLectivo != null && !this.idAnioLectivo.equals(other.idAnioLectivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.AnioLectivo[ idAnioLectivo=" + idAnioLectivo + " ]";
    }
    
}
