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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "CUESTIONARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuestionario.findAll", query = "SELECT c FROM Cuestionario c"),
    @NamedQuery(name = "Cuestionario.findByIdCuestionario", query = "SELECT c FROM Cuestionario c WHERE c.idCuestionario = :idCuestionario"),
    @NamedQuery(name = "Cuestionario.findByCrTitulo", query = "SELECT c FROM Cuestionario c WHERE c.crTitulo = :crTitulo"),
    @NamedQuery(name = "Cuestionario.findByCrDescripcion", query = "SELECT c FROM Cuestionario c WHERE c.crDescripcion = :crDescripcion")})
public class Cuestionario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CUESTIONARIO")
    private BigDecimal idCuestionario;
    @Column(name = "CR_TITULO")
    private String crTitulo;
    @Column(name = "CR_DESCRIPCION")
    private String crDescripcion;
    @JoinColumn(name = "CR_ID_JUEGO", referencedColumnName = "ID_JUEGO")
    @ManyToOne
    private Juegos crIdJuego;
    @OneToMany(mappedBy = "prIdCuestionario")
    private List<Preguntas> preguntasList;
    @OneToMany(mappedBy = "pcIdCuestionario")
    private List<PuntajeCuestionario> puntajeCuestionarioList;

    public Cuestionario() {
    }

    public Cuestionario(BigDecimal idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public BigDecimal getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(BigDecimal idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public String getCrTitulo() {
        return crTitulo;
    }

    public void setCrTitulo(String crTitulo) {
        this.crTitulo = crTitulo;
    }

    public String getCrDescripcion() {
        return crDescripcion;
    }

    public void setCrDescripcion(String crDescripcion) {
        this.crDescripcion = crDescripcion;
    }

    public Juegos getCrIdJuego() {
        return crIdJuego;
    }

    public void setCrIdJuego(Juegos crIdJuego) {
        this.crIdJuego = crIdJuego;
    }

    @XmlTransient
    public List<Preguntas> getPreguntasList() {
        return preguntasList;
    }

    public void setPreguntasList(List<Preguntas> preguntasList) {
        this.preguntasList = preguntasList;
    }

    @XmlTransient
    public List<PuntajeCuestionario> getPuntajeCuestionarioList() {
        return puntajeCuestionarioList;
    }

    public void setPuntajeCuestionarioList(List<PuntajeCuestionario> puntajeCuestionarioList) {
        this.puntajeCuestionarioList = puntajeCuestionarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuestionario != null ? idCuestionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuestionario)) {
            return false;
        }
        Cuestionario other = (Cuestionario) object;
        if ((this.idCuestionario == null && other.idCuestionario != null) || (this.idCuestionario != null && !this.idCuestionario.equals(other.idCuestionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Cuestionario[ idCuestionario=" + idCuestionario + " ]";
    }
    
}
