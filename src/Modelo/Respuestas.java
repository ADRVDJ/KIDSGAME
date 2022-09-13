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
@Table(name = "RESPUESTAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Respuestas.findAll", query = "SELECT r FROM Respuestas r"),
    @NamedQuery(name = "Respuestas.findByIdRespuesta", query = "SELECT r FROM Respuestas r WHERE r.idRespuesta = :idRespuesta"),
    @NamedQuery(name = "Respuestas.findByRsRespuestac", query = "SELECT r FROM Respuestas r WHERE r.rsRespuestac = :rsRespuestac"),
    @NamedQuery(name = "Respuestas.findByRsRespuestai", query = "SELECT r FROM Respuestas r WHERE r.rsRespuestai = :rsRespuestai")})
public class Respuestas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_RESPUESTA")
    private BigDecimal idRespuesta;
    @Column(name = "RS_RESPUESTAC")
    private String rsRespuestac;
    @Column(name = "RS_RESPUESTAI")
    private String rsRespuestai;
    @OneToMany(mappedBy = "pcIdRespuesta")
    private List<PuntajeCuestionario> puntajeCuestionarioList;
    @JoinColumn(name = "RS_ID_PREGUNTA", referencedColumnName = "ID_PREGUNTA")
    @ManyToOne
    private Preguntas rsIdPregunta;

    public Respuestas() {
    }

    public Respuestas(BigDecimal idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public BigDecimal getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(BigDecimal idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getRsRespuestac() {
        return rsRespuestac;
    }

    public void setRsRespuestac(String rsRespuestac) {
        this.rsRespuestac = rsRespuestac;
    }

    public String getRsRespuestai() {
        return rsRespuestai;
    }

    public void setRsRespuestai(String rsRespuestai) {
        this.rsRespuestai = rsRespuestai;
    }

    @XmlTransient
    public List<PuntajeCuestionario> getPuntajeCuestionarioList() {
        return puntajeCuestionarioList;
    }

    public void setPuntajeCuestionarioList(List<PuntajeCuestionario> puntajeCuestionarioList) {
        this.puntajeCuestionarioList = puntajeCuestionarioList;
    }

    public Preguntas getRsIdPregunta() {
        return rsIdPregunta;
    }

    public void setRsIdPregunta(Preguntas rsIdPregunta) {
        this.rsIdPregunta = rsIdPregunta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRespuesta != null ? idRespuesta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Respuestas)) {
            return false;
        }
        Respuestas other = (Respuestas) object;
        if ((this.idRespuesta == null && other.idRespuesta != null) || (this.idRespuesta != null && !this.idRespuesta.equals(other.idRespuesta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Respuestas[ idRespuesta=" + idRespuesta + " ]";
    }
    
}
