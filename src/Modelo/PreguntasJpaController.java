/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.exceptions.NonexistentEntityException;
import Modelo.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ASUS TUF GAMING
 */
public class PreguntasJpaController implements Serializable {

    public PreguntasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Preguntas preguntas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuestionario prIdCuestionario = preguntas.getPrIdCuestionario();
            if (prIdCuestionario != null) {
                prIdCuestionario = em.getReference(prIdCuestionario.getClass(), prIdCuestionario.getIdCuestionario());
                preguntas.setPrIdCuestionario(prIdCuestionario);
            }
            Respuestas prIdRespuestas = preguntas.getPrIdRespuestas();
            if (prIdRespuestas != null) {
                prIdRespuestas = em.getReference(prIdRespuestas.getClass(), prIdRespuestas.getIdRespuesta());
                preguntas.setPrIdRespuestas(prIdRespuestas);
            }
            em.persist(preguntas);
            if (prIdCuestionario != null) {
                prIdCuestionario.getPreguntasList().add(preguntas);
                prIdCuestionario = em.merge(prIdCuestionario);
            }
            if (prIdRespuestas != null) {
                prIdRespuestas.getPreguntasList().add(preguntas);
                prIdRespuestas = em.merge(prIdRespuestas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPreguntas(preguntas.getIdPregunta()) != null) {
                throw new PreexistingEntityException("Preguntas " + preguntas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Preguntas preguntas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Preguntas persistentPreguntas = em.find(Preguntas.class, preguntas.getIdPregunta());
            Cuestionario prIdCuestionarioOld = persistentPreguntas.getPrIdCuestionario();
            Cuestionario prIdCuestionarioNew = preguntas.getPrIdCuestionario();
            Respuestas prIdRespuestasOld = persistentPreguntas.getPrIdRespuestas();
            Respuestas prIdRespuestasNew = preguntas.getPrIdRespuestas();
            if (prIdCuestionarioNew != null) {
                prIdCuestionarioNew = em.getReference(prIdCuestionarioNew.getClass(), prIdCuestionarioNew.getIdCuestionario());
                preguntas.setPrIdCuestionario(prIdCuestionarioNew);
            }
            if (prIdRespuestasNew != null) {
                prIdRespuestasNew = em.getReference(prIdRespuestasNew.getClass(), prIdRespuestasNew.getIdRespuesta());
                preguntas.setPrIdRespuestas(prIdRespuestasNew);
            }
            preguntas = em.merge(preguntas);
            if (prIdCuestionarioOld != null && !prIdCuestionarioOld.equals(prIdCuestionarioNew)) {
                prIdCuestionarioOld.getPreguntasList().remove(preguntas);
                prIdCuestionarioOld = em.merge(prIdCuestionarioOld);
            }
            if (prIdCuestionarioNew != null && !prIdCuestionarioNew.equals(prIdCuestionarioOld)) {
                prIdCuestionarioNew.getPreguntasList().add(preguntas);
                prIdCuestionarioNew = em.merge(prIdCuestionarioNew);
            }
            if (prIdRespuestasOld != null && !prIdRespuestasOld.equals(prIdRespuestasNew)) {
                prIdRespuestasOld.getPreguntasList().remove(preguntas);
                prIdRespuestasOld = em.merge(prIdRespuestasOld);
            }
            if (prIdRespuestasNew != null && !prIdRespuestasNew.equals(prIdRespuestasOld)) {
                prIdRespuestasNew.getPreguntasList().add(preguntas);
                prIdRespuestasNew = em.merge(prIdRespuestasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = preguntas.getIdPregunta();
                if (findPreguntas(id) == null) {
                    throw new NonexistentEntityException("The preguntas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Preguntas preguntas;
            try {
                preguntas = em.getReference(Preguntas.class, id);
                preguntas.getIdPregunta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The preguntas with id " + id + " no longer exists.", enfe);
            }
            Cuestionario prIdCuestionario = preguntas.getPrIdCuestionario();
            if (prIdCuestionario != null) {
                prIdCuestionario.getPreguntasList().remove(preguntas);
                prIdCuestionario = em.merge(prIdCuestionario);
            }
            Respuestas prIdRespuestas = preguntas.getPrIdRespuestas();
            if (prIdRespuestas != null) {
                prIdRespuestas.getPreguntasList().remove(preguntas);
                prIdRespuestas = em.merge(prIdRespuestas);
            }
            em.remove(preguntas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Preguntas> findPreguntasEntities() {
        return findPreguntasEntities(true, -1, -1);
    }

    public List<Preguntas> findPreguntasEntities(int maxResults, int firstResult) {
        return findPreguntasEntities(false, maxResults, firstResult);
    }

    private List<Preguntas> findPreguntasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Preguntas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Preguntas findPreguntas(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Preguntas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPreguntasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Preguntas> rt = cq.from(Preguntas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
