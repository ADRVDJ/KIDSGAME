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
@Table(name = "ASIGNATURA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asignatura.findAll", query = "SELECT a FROM Asignatura a"),
    @NamedQuery(name = "Asignatura.findByIdAsignatura", query = "SELECT a FROM Asignatura a WHERE a.idAsignatura = :idAsignatura"),
    @NamedQuery(name = "Asignatura.findByAsNombre", query = "SELECT a FROM Asignatura a WHERE a.asNombre = :asNombre"),
    @NamedQuery(name = "Asignatura.findByAsDescripcion", query = "SELECT a FROM Asignatura a WHERE a.asDescripcion = :asDescripcion"),
    @NamedQuery(name = "Asignatura.findByIdUsuarioA", query = "SELECT a FROM Asignatura a WHERE a.idUsuarioA = :idUsuarioA")})
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
    @Column(name = "ID_USUARIO_A")
    private BigInteger idUsuarioA;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "asignatura")
    private AnioLectivo anioLectivo;
    @JoinColumn(name = "ID_ASIGNATURA", referencedColumnName = "ID_USUARIO_A", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private PersonaAsignatura personaAsignatura;

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

    public BigInteger getIdUsuarioA() {
        return idUsuarioA;
    }

    public void setIdUsuarioA(BigInteger idUsuarioA) {
        this.idUsuarioA = idUsuarioA;
    }

    public AnioLectivo getAnioLectivo() {
        return anioLectivo;
    }

    public void setAnioLectivo(AnioLectivo anioLectivo) {
        this.anioLectivo = anioLectivo;
    }

    public PersonaAsignatura getPersonaAsignatura() {
        return personaAsignatura;
    }

    public void setPersonaAsignatura(PersonaAsignatura personaAsignatura) {
        this.personaAsignatura = personaAsignatura;
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
