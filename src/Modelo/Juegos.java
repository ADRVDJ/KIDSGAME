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
 * @author Shalva
 */
@Entity
@Table(name = "JUEGOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Juegos.findAll", query = "SELECT j FROM Juegos j"),
    @NamedQuery(name = "Juegos.findByIdJuego", query = "SELECT j FROM Juegos j WHERE j.idJuego = :idJuego"),
    @NamedQuery(name = "Juegos.findByJgNombre", query = "SELECT j FROM Juegos j WHERE j.jgNombre = :jgNombre"),
    @NamedQuery(name = "Juegos.findByJgPuntajemax", query = "SELECT j FROM Juegos j WHERE j.jgPuntajemax = :jgPuntajemax"),
    @NamedQuery(name = "Juegos.findByJgPuntajemin", query = "SELECT j FROM Juegos j WHERE j.jgPuntajemin = :jgPuntajemin")})
public class Juegos implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_JUEGO")
    private BigDecimal idJuego;
    @Column(name = "JG_NOMBRE")
    private String jgNombre;
    @Column(name = "JG_PUNTAJEMAX")
    private String jgPuntajemax;
    @Column(name = "JG_PUNTAJEMIN")
    private String jgPuntajemin;
    @OneToMany(mappedBy = "crIdJuego")
    private List<Cuestionario> cuestionarioList;

    public Juegos() {
    }

    public Juegos(BigDecimal idJuego) {
        this.idJuego = idJuego;
    }

    public BigDecimal getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(BigDecimal idJuego) {
        this.idJuego = idJuego;
    }

    public String getJgNombre() {
        return jgNombre;
    }

    public void setJgNombre(String jgNombre) {
        this.jgNombre = jgNombre;
    }

    public String getJgPuntajemax() {
        return jgPuntajemax;
    }

    public void setJgPuntajemax(String jgPuntajemax) {
        this.jgPuntajemax = jgPuntajemax;
    }

    public String getJgPuntajemin() {
        return jgPuntajemin;
    }

    public void setJgPuntajemin(String jgPuntajemin) {
        this.jgPuntajemin = jgPuntajemin;
    }

    @XmlTransient
    public List<Cuestionario> getCuestionarioList() {
        return cuestionarioList;
    }

    public void setCuestionarioList(List<Cuestionario> cuestionarioList) {
        this.cuestionarioList = cuestionarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJuego != null ? idJuego.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Juegos)) {
            return false;
        }
        Juegos other = (Juegos) object;
        if ((this.idJuego == null && other.idJuego != null) || (this.idJuego != null && !this.idJuego.equals(other.idJuego))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Juegos[ idJuego=" + idJuego + " ]";
    }
    
}
