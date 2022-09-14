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
@Table(name = "PREGUNTAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Preguntas.findAll", query = "SELECT p FROM Preguntas p"),
    @NamedQuery(name = "Preguntas.findByIdPregunta", query = "SELECT p FROM Preguntas p WHERE p.idPregunta = :idPregunta"),
    @NamedQuery(name = "Preguntas.findByPrPregunta", query = "SELECT p FROM Preguntas p WHERE p.prPregunta = :prPregunta")})
public class Preguntas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PREGUNTA")
    private BigDecimal idPregunta;
    @Column(name = "PR_PREGUNTA")
    private String prPregunta;
    @JoinColumn(name = "PR_ID_CUESTIONARIO", referencedColumnName = "ID_CUESTIONARIO")
    @ManyToOne
    private Cuestionario prIdCuestionario;
    @JoinColumn(name = "PR_ID_RESPUESTAS", referencedColumnName = "ID_RESPUESTA")
    @ManyToOne
    private Respuestas prIdRespuestas;

    public Preguntas() {
    }

    public Preguntas(BigDecimal idPregunta) {
        this.idPregunta = idPregunta;
    }

    public BigDecimal getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(BigDecimal idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getPrPregunta() {
        return prPregunta;
    }

    public void setPrPregunta(String prPregunta) {
        this.prPregunta = prPregunta;
    }

    public Cuestionario getPrIdCuestionario() {
        return prIdCuestionario;
    }

    public void setPrIdCuestionario(Cuestionario prIdCuestionario) {
        this.prIdCuestionario = prIdCuestionario;
    }

    public Respuestas getPrIdRespuestas() {
        return prIdRespuestas;
    }

    public void setPrIdRespuestas(Respuestas prIdRespuestas) {
        this.prIdRespuestas = prIdRespuestas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPregunta != null ? idPregunta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Preguntas)) {
            return false;
        }
        Preguntas other = (Preguntas) object;
        if ((this.idPregunta == null && other.idPregunta != null) || (this.idPregunta != null && !this.idPregunta.equals(other.idPregunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Preguntas[ idPregunta=" + idPregunta + " ]";
    }
    
}
