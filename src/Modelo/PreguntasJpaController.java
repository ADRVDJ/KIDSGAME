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
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        if (preguntas.getRespuestasList() == null) {
            preguntas.setRespuestasList(new ArrayList<Respuestas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuestionario prIdCuestionario = preguntas.getPrIdCuestionario();
            if (prIdCuestionario != null) {
                prIdCuestionario = em.getReference(prIdCuestionario.getClass(), prIdCuestionario.getIdCuestionario());
                preguntas.setPrIdCuestionario(prIdCuestionario);
            }
            List<Respuestas> attachedRespuestasList = new ArrayList<Respuestas>();
            for (Respuestas respuestasListRespuestasToAttach : preguntas.getRespuestasList()) {
                respuestasListRespuestasToAttach = em.getReference(respuestasListRespuestasToAttach.getClass(), respuestasListRespuestasToAttach.getIdRespuesta());
                attachedRespuestasList.add(respuestasListRespuestasToAttach);
            }
            preguntas.setRespuestasList(attachedRespuestasList);
            em.persist(preguntas);
            if (prIdCuestionario != null) {
                prIdCuestionario.getPreguntasList().add(preguntas);
                prIdCuestionario = em.merge(prIdCuestionario);
            }
            for (Respuestas respuestasListRespuestas : preguntas.getRespuestasList()) {
                Preguntas oldRsIdPreguntaOfRespuestasListRespuestas = respuestasListRespuestas.getRsIdPregunta();
                respuestasListRespuestas.setRsIdPregunta(preguntas);
                respuestasListRespuestas = em.merge(respuestasListRespuestas);
                if (oldRsIdPreguntaOfRespuestasListRespuestas != null) {
                    oldRsIdPreguntaOfRespuestasListRespuestas.getRespuestasList().remove(respuestasListRespuestas);
                    oldRsIdPreguntaOfRespuestasListRespuestas = em.merge(oldRsIdPreguntaOfRespuestasListRespuestas);
                }
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
            List<Respuestas> respuestasListOld = persistentPreguntas.getRespuestasList();
            List<Respuestas> respuestasListNew = preguntas.getRespuestasList();
            if (prIdCuestionarioNew != null) {
                prIdCuestionarioNew = em.getReference(prIdCuestionarioNew.getClass(), prIdCuestionarioNew.getIdCuestionario());
                preguntas.setPrIdCuestionario(prIdCuestionarioNew);
            }
            List<Respuestas> attachedRespuestasListNew = new ArrayList<Respuestas>();
            for (Respuestas respuestasListNewRespuestasToAttach : respuestasListNew) {
                respuestasListNewRespuestasToAttach = em.getReference(respuestasListNewRespuestasToAttach.getClass(), respuestasListNewRespuestasToAttach.getIdRespuesta());
                attachedRespuestasListNew.add(respuestasListNewRespuestasToAttach);
            }
            respuestasListNew = attachedRespuestasListNew;
            preguntas.setRespuestasList(respuestasListNew);
            preguntas = em.merge(preguntas);
            if (prIdCuestionarioOld != null && !prIdCuestionarioOld.equals(prIdCuestionarioNew)) {
                prIdCuestionarioOld.getPreguntasList().remove(preguntas);
                prIdCuestionarioOld = em.merge(prIdCuestionarioOld);
            }
            if (prIdCuestionarioNew != null && !prIdCuestionarioNew.equals(prIdCuestionarioOld)) {
                prIdCuestionarioNew.getPreguntasList().add(preguntas);
                prIdCuestionarioNew = em.merge(prIdCuestionarioNew);
            }
            for (Respuestas respuestasListOldRespuestas : respuestasListOld) {
                if (!respuestasListNew.contains(respuestasListOldRespuestas)) {
                    respuestasListOldRespuestas.setRsIdPregunta(null);
                    respuestasListOldRespuestas = em.merge(respuestasListOldRespuestas);
                }
            }
            for (Respuestas respuestasListNewRespuestas : respuestasListNew) {
                if (!respuestasListOld.contains(respuestasListNewRespuestas)) {
                    Preguntas oldRsIdPreguntaOfRespuestasListNewRespuestas = respuestasListNewRespuestas.getRsIdPregunta();
                    respuestasListNewRespuestas.setRsIdPregunta(preguntas);
                    respuestasListNewRespuestas = em.merge(respuestasListNewRespuestas);
                    if (oldRsIdPreguntaOfRespuestasListNewRespuestas != null && !oldRsIdPreguntaOfRespuestasListNewRespuestas.equals(preguntas)) {
                        oldRsIdPreguntaOfRespuestasListNewRespuestas.getRespuestasList().remove(respuestasListNewRespuestas);
                        oldRsIdPreguntaOfRespuestasListNewRespuestas = em.merge(oldRsIdPreguntaOfRespuestasListNewRespuestas);
                    }
                }
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
            List<Respuestas> respuestasList = preguntas.getRespuestasList();
            for (Respuestas respuestasListRespuestas : respuestasList) {
                respuestasListRespuestas.setRsIdPregunta(null);
                respuestasListRespuestas = em.merge(respuestasListRespuestas);
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
