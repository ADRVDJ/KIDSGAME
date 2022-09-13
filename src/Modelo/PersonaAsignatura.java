/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Shalva
 */
@Entity
@Table(name = "PERSONA_ASIGNATURA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonaAsignatura.findAll", query = "SELECT p FROM PersonaAsignatura p"),
    @NamedQuery(name = "PersonaAsignatura.findByIdUsuarioA", query = "SELECT p FROM PersonaAsignatura p WHERE p.idUsuarioA = :idUsuarioA"),
    @NamedQuery(name = "PersonaAsignatura.findByIdPersona", query = "SELECT p FROM PersonaAsignatura p WHERE p.idPersona = :idPersona")})
public class PersonaAsignatura implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_USUARIO_A")
    private BigDecimal idUsuarioA;
    @Column(name = "ID_PERSONA")
    private BigInteger idPersona;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personaAsignatura")
    private Asignatura asignatura;
    @JoinColumn(name = "ID_USUARIO_A", referencedColumnName = "ID_PERSONA", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Persona persona;

    public PersonaAsignatura() {
    }

    public PersonaAsignatura(BigDecimal idUsuarioA) {
        this.idUsuarioA = idUsuarioA;
    }

    public BigDecimal getIdUsuarioA() {
        return idUsuarioA;
    }

    public void setIdUsuarioA(BigDecimal idUsuarioA) {
        this.idUsuarioA = idUsuarioA;
    }

    public BigInteger getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(BigInteger idPersona) {
        this.idPersona = idPersona;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuarioA != null ? idUsuarioA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonaAsignatura)) {
            return false;
        }
        PersonaAsignatura other = (PersonaAsignatura) object;
        if ((this.idUsuarioA == null && other.idUsuarioA != null) || (this.idUsuarioA != null && !this.idUsuarioA.equals(other.idUsuarioA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.PersonaAsignatura[ idUsuarioA=" + idUsuarioA + " ]";
    }
    
}
