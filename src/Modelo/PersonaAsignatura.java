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
@Table(name = "PERSONA_ASIGNATURA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonaAsignatura.findAll", query = "SELECT p FROM PersonaAsignatura p"),
    @NamedQuery(name = "PersonaAsignatura.findByIdPersonaA", query = "SELECT p FROM PersonaAsignatura p WHERE p.idPersonaA = :idPersonaA")})
public class PersonaAsignatura implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PERSONA_A")
    private BigDecimal idPersonaA;
    @OneToMany(mappedBy = "idPersonaA")
    private List<Asignatura> asignaturaList;
    @JoinColumn(name = "PA_ID_PERSONA", referencedColumnName = "ID_PERSONA")
    @ManyToOne
    private Persona paIdPersona;

    public PersonaAsignatura() {
    }

    public PersonaAsignatura(BigDecimal idPersonaA) {
        this.idPersonaA = idPersonaA;
    }

    public BigDecimal getIdPersonaA() {
        return idPersonaA;
    }

    public void setIdPersonaA(BigDecimal idPersonaA) {
        this.idPersonaA = idPersonaA;
    }

    @XmlTransient
    public List<Asignatura> getAsignaturaList() {
        return asignaturaList;
    }

    public void setAsignaturaList(List<Asignatura> asignaturaList) {
        this.asignaturaList = asignaturaList;
    }

    public Persona getPaIdPersona() {
        return paIdPersona;
    }

    public void setPaIdPersona(Persona paIdPersona) {
        this.paIdPersona = paIdPersona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersonaA != null ? idPersonaA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonaAsignatura)) {
            return false;
        }
        PersonaAsignatura other = (PersonaAsignatura) object;
        if ((this.idPersonaA == null && other.idPersonaA != null) || (this.idPersonaA != null && !this.idPersonaA.equals(other.idPersonaA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.PersonaAsignatura[ idPersonaA=" + idPersonaA + " ]";
    }
    
}
