/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ASUS TUF GAMING
 */
@Entity
@Table(name = "USUARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuario.findByUsUsuario", query = "SELECT u FROM Usuario u WHERE u.usUsuario = :usUsuario"),
    @NamedQuery(name = "Usuario.findByUsContrase\u00f1a", query = "SELECT u FROM Usuario u WHERE u.usContrase\u00f1a = :usContrase\u00f1a"),
    @NamedQuery(name = "Usuario.findByUsPermisos", query = "SELECT u FROM Usuario u WHERE u.usPermisos = :usPermisos")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private BigDecimal idUsuario;
    @Column(name = "US_USUARIO")
    private String usUsuario;
    @Column(name = "US_CONTRASE\u00d1A")
    private String usContraseña;
    @Column(name = "US_PERMISOS")
    private String usPermisos;
    @JoinColumn(name = "US_ID_PERSONA", referencedColumnName = "ID_PERSONA")
    @ManyToOne
    private Persona usIdPersona;
    @JoinColumn(name = "US_ID_ROL", referencedColumnName = "ID_ROL")
    @ManyToOne
    private Rol usIdRol;

    public Usuario() {
    }

    public Usuario(BigDecimal idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigDecimal idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsUsuario() {
        return usUsuario;
    }

    public void setUsUsuario(String usUsuario) {
        this.usUsuario = usUsuario;
    }

    public String getUsContraseña() {
        return usContraseña;
    }

    public void setUsContraseña(String usContraseña) {
        this.usContraseña = usContraseña;
    }

    public String getUsPermisos() {
        return usPermisos;
    }

    public void setUsPermisos(String usPermisos) {
        this.usPermisos = usPermisos;
    }

    public Persona getUsIdPersona() {
        return usIdPersona;
    }

    public void setUsIdPersona(Persona usIdPersona) {
        this.usIdPersona = usIdPersona;
    }

    public Rol getUsIdRol() {
        return usIdRol;
    }

    public void setUsIdRol(Rol usIdRol) {
        this.usIdRol = usIdRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Usuario[ idUsuario=" + idUsuario + " ]";
    }
    
}
