/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Shalva
 */
@Entity
@Table(name = "PERSONA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persona.findAll", query = "SELECT p FROM Persona p"),
    @NamedQuery(name = "Persona.findByIdPersona", query = "SELECT p FROM Persona p WHERE p.idPersona = :idPersona"),
    @NamedQuery(name = "Persona.findByPrNombre", query = "SELECT p FROM Persona p WHERE p.prNombre = :prNombre"),
    @NamedQuery(name = "Persona.findByPrApellido", query = "SELECT p FROM Persona p WHERE p.prApellido = :prApellido"),
    @NamedQuery(name = "Persona.findByPrEdad", query = "SELECT p FROM Persona p WHERE p.prEdad = :prEdad"),
    @NamedQuery(name = "Persona.findByPrGenero", query = "SELECT p FROM Persona p WHERE p.prGenero = :prGenero"),
    @NamedQuery(name = "Persona.findByPrCurso", query = "SELECT p FROM Persona p WHERE p.prCurso = :prCurso"),
    @NamedQuery(name = "Persona.findByPrTitulo", query = "SELECT p FROM Persona p WHERE p.prTitulo = :prTitulo"),
    @NamedQuery(name = "Persona.findByIdUsuario", query = "SELECT p FROM Persona p WHERE p.idUsuario = :idUsuario")})
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PERSONA")
    private BigDecimal idPersona;
    @Column(name = "PR_NOMBRE")
    private String prNombre;
    @Column(name = "PR_APELLIDO")
    private String prApellido;
    @Column(name = "PR_EDAD")
    private String prEdad;
    @Column(name = "PR_GENERO")
    private String prGenero;
    @Column(name = "PR_CURSO")
    private String prCurso;
    @Column(name = "PR_TITULO")
    private String prTitulo;
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;
    @OneToMany(mappedBy = "pcIdPersona")
    private List<PuntajeCuestionario> puntajeCuestionarioList;
    @JoinColumn(name = "ID_PERSONA", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuario usuario;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "persona")
    private PersonaAsignatura personaAsignatura;

    public Persona() {
    }

    public Persona(BigDecimal idPersona) {
        this.idPersona = idPersona;
    }

    public BigDecimal getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(BigDecimal idPersona) {
        this.idPersona = idPersona;
    }

    public String getPrNombre() {
        return prNombre;
    }

    public void setPrNombre(String prNombre) {
        this.prNombre = prNombre;
    }

    public String getPrApellido() {
        return prApellido;
    }

    public void setPrApellido(String prApellido) {
        this.prApellido = prApellido;
    }

    public String getPrEdad() {
        return prEdad;
    }

    public void setPrEdad(String prEdad) {
        this.prEdad = prEdad;
    }

    public String getPrGenero() {
        return prGenero;
    }

    public void setPrGenero(String prGenero) {
        this.prGenero = prGenero;
    }

    public String getPrCurso() {
        return prCurso;
    }

    public void setPrCurso(String prCurso) {
        this.prCurso = prCurso;
    }

    public String getPrTitulo() {
        return prTitulo;
    }

    public void setPrTitulo(String prTitulo) {
        this.prTitulo = prTitulo;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<PuntajeCuestionario> getPuntajeCuestionarioList() {
        return puntajeCuestionarioList;
    }

    public void setPuntajeCuestionarioList(List<PuntajeCuestionario> puntajeCuestionarioList) {
        this.puntajeCuestionarioList = puntajeCuestionarioList;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Persona[ idPersona=" + idPersona + " ]";
    }
    
}
