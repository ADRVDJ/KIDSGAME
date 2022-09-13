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
 * @author ASUS TUF GAMING
 */
@Entity
@Table(name = "ASIGNATURA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asignatura.findAll", query = "SELECT a FROM Asignatura a"),
    @NamedQuery(name = "Asignatura.findByIdAsignatura", query = "SELECT a FROM Asignatura a WHERE a.idAsignatura = :idAsignatura"),
    @NamedQuery(name = "Asignatura.findByAsNombre", query = "SELECT a FROM Asignatura a WHERE a.asNombre = :asNombre"),
    @NamedQuery(name = "Asignatura.findByAsDescripcion", query = "SELECT a FROM Asignatura a WHERE a.asDescripcion = :asDescripcion")})
public class Asignatura implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ASIGNATURA")
    private BigDecimal idAsignatura;
    @Column(name = "AS_NOMBRE")
    private String asNombre;
    @Column(name = "AS_DESCRIPCION")
    private String asDescripcion;
    @OneToMany(mappedBy = "crIdAsignatura")
    private List<Cuestionario> cuestionarioList;
    @JoinColumn(name = "AS_IDCURSO", referencedColumnName = "ID_CURSO")
    @ManyToOne
    private Curso asIdcurso;
    @JoinColumn(name = "ID_PERSONA_A", referencedColumnName = "ID_PERSONA_A")
    @ManyToOne
    private PersonaAsignatura idPersonaA;

    public Asignatura() {
    }

    public Asignatura(BigDecimal idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public BigDecimal getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(BigDecimal idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public String getAsNombre() {
        return asNombre;
    }

    public void setAsNombre(String asNombre) {
        this.asNombre = asNombre;
    }

    public String getAsDescripcion() {
        return asDescripcion;
    }

    public void setAsDescripcion(String asDescripcion) {
        this.asDescripcion = asDescripcion;
    }

    @XmlTransient
    public List<Cuestionario> getCuestionarioList() {
        return cuestionarioList;
    }

    public void setCuestionarioList(List<Cuestionario> cuestionarioList) {
        this.cuestionarioList = cuestionarioList;
    }

    public Curso getAsIdcurso() {
        return asIdcurso;
    }

    public void setAsIdcurso(Curso asIdcurso) {
        this.asIdcurso = asIdcurso;
    }

    public PersonaAsignatura getIdPersonaA() {
        return idPersonaA;
    }

    public void setIdPersonaA(PersonaAsignatura idPersonaA) {
        this.idPersonaA = idPersonaA;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAsignatura != null ? idAsignatura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asignatura)) {
            return false;
        }
        Asignatura other = (Asignatura) object;
        if ((this.idAsignatura == null && other.idAsignatura != null) || (this.idAsignatura != null && !this.idAsignatura.equals(other.idAsignatura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Asignatura[ idAsignatura=" + idAsignatura + " ]";
    }
    
}
