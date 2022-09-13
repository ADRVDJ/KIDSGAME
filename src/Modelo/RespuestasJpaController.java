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
public class RespuestasJpaController implements Serializable {

    public RespuestasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Respuestas respuestas) throws PreexistingEntityException, Exception {
        if (respuestas.getPuntajeCuestionarioList() == null) {
            respuestas.setPuntajeCuestionarioList(new ArrayList<PuntajeCuestionario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Preguntas rsIdPregunta = respuestas.getRsIdPregunta();
            if (rsIdPregunta != null) {
                rsIdPregunta = em.getReference(rsIdPregunta.getClass(), rsIdPregunta.getIdPregunta());
                respuestas.setRsIdPregunta(rsIdPregunta);
            }
            List<PuntajeCuestionario> attachedPuntajeCuestionarioList = new ArrayList<PuntajeCuestionario>();
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionarioToAttach : respuestas.getPuntajeCuestionarioList()) {
                puntajeCuestionarioListPuntajeCuestionarioToAttach = em.getReference(puntajeCuestionarioListPuntajeCuestionarioToAttach.getClass(), puntajeCuestionarioListPuntajeCuestionarioToAttach.getIdPuntaje());
                attachedPuntajeCuestionarioList.add(puntajeCuestionarioListPuntajeCuestionarioToAttach);
            }
            respuestas.setPuntajeCuestionarioList(attachedPuntajeCuestionarioList);
            em.persist(respuestas);
            if (rsIdPregunta != null) {
                rsIdPregunta.getRespuestasList().add(respuestas);
                rsIdPregunta = em.merge(rsIdPregunta);
            }
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionario : respuestas.getPuntajeCuestionarioList()) {
                Respuestas oldPcIdRespuestaOfPuntajeCuestionarioListPuntajeCuestionario = puntajeCuestionarioListPuntajeCuestionario.getPcIdRespuesta();
                puntajeCuestionarioListPuntajeCuestionario.setPcIdRespuesta(respuestas);
                puntajeCuestionarioListPuntajeCuestionario = em.merge(puntajeCuestionarioListPuntajeCuestionario);
                if (oldPcIdRespuestaOfPuntajeCuestionarioListPuntajeCuestionario != null) {
                    oldPcIdRespuestaOfPuntajeCuestionarioListPuntajeCuestionario.getPuntajeCuestionarioList().remove(puntajeCuestionarioListPuntajeCuestionario);
                    oldPcIdRespuestaOfPuntajeCuestionarioListPuntajeCuestionario = em.merge(oldPcIdRespuestaOfPuntajeCuestionarioListPuntajeCuestionario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRespuestas(respuestas.getIdRespuesta()) != null) {
                throw new PreexistingEntityException("Respuestas " + respuestas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Respuestas respuestas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Respuestas persistentRespuestas = em.find(Respuestas.class, respuestas.getIdRespuesta());
            Preguntas rsIdPreguntaOld = persistentRespuestas.getRsIdPregunta();
            Preguntas rsIdPreguntaNew = respuestas.getRsIdPregunta();
            List<PuntajeCuestionario> puntajeCuestionarioListOld = persistentRespuestas.getPuntajeCuestionarioList();
            List<PuntajeCuestionario> puntajeCuestionarioListNew = respuestas.getPuntajeCuestionarioList();
            if (rsIdPreguntaNew != null) {
                rsIdPreguntaNew = em.getReference(rsIdPreguntaNew.getClass(), rsIdPreguntaNew.getIdPregunta());
                respuestas.setRsIdPregunta(rsIdPreguntaNew);
            }
            List<PuntajeCuestionario> attachedPuntajeCuestionarioListNew = new ArrayList<PuntajeCuestionario>();
            for (PuntajeCuestionario puntajeCuestionarioListNewPuntajeCuestionarioToAttach : puntajeCuestionarioListNew) {
                puntajeCuestionarioListNewPuntajeCuestionarioToAttach = em.getReference(puntajeCuestionarioListNewPuntajeCuestionarioToAttach.getClass(), puntajeCuestionarioListNewPuntajeCuestionarioToAttach.getIdPuntaje());
                attachedPuntajeCuestionarioListNew.add(puntajeCuestionarioListNewPuntajeCuestionarioToAttach);
            }
            puntajeCuestionarioListNew = attachedPuntajeCuestionarioListNew;
            respuestas.setPuntajeCuestionarioList(puntajeCuestionarioListNew);
            respuestas = em.merge(respuestas);
            if (rsIdPreguntaOld != null && !rsIdPreguntaOld.equals(rsIdPreguntaNew)) {
                rsIdPreguntaOld.getRespuestasList().remove(respuestas);
                rsIdPreguntaOld = em.merge(rsIdPreguntaOld);
            }
            if (rsIdPreguntaNew != null && !rsIdPreguntaNew.equals(rsIdPreguntaOld)) {
                rsIdPreguntaNew.getRespuestasList().add(respuestas);
                rsIdPreguntaNew = em.merge(rsIdPreguntaNew);
            }
            for (PuntajeCuestionario puntajeCuestionarioListOldPuntajeCuestionario : puntajeCuestionarioListOld) {
                if (!puntajeCuestionarioListNew.contains(puntajeCuestionarioListOldPuntajeCuestionario)) {
                    puntajeCuestionarioListOldPuntajeCuestionario.setPcIdRespuesta(null);
                    puntajeCuestionarioListOldPuntajeCuestionario = em.merge(puntajeCuestionarioListOldPuntajeCuestionario);
                }
            }
            for (PuntajeCuestionario puntajeCuestionarioListNewPuntajeCuestionario : puntajeCuestionarioListNew) {
                if (!puntajeCuestionarioListOld.contains(puntajeCuestionarioListNewPuntajeCuestionario)) {
                    Respuestas oldPcIdRespuestaOfPuntajeCuestionarioListNewPuntajeCuestionario = puntajeCuestionarioListNewPuntajeCuestionario.getPcIdRespuesta();
                    puntajeCuestionarioListNewPuntajeCuestionario.setPcIdRespuesta(respuestas);
                    puntajeCuestionarioListNewPuntajeCuestionario = em.merge(puntajeCuestionarioListNewPuntajeCuestionario);
                    if (oldPcIdRespuestaOfPuntajeCuestionarioListNewPuntajeCuestionario != null && !oldPcIdRespuestaOfPuntajeCuestionarioListNewPuntajeCuestionario.equals(respuestas)) {
                        oldPcIdRespuestaOfPuntajeCuestionarioListNewPuntajeCuestionario.getPuntajeCuestionarioList().remove(puntajeCuestionarioListNewPuntajeCuestionario);
                        oldPcIdRespuestaOfPuntajeCuestionarioListNewPuntajeCuestionario = em.merge(oldPcIdRespuestaOfPuntajeCuestionarioListNewPuntajeCuestionario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = respuestas.getIdRespuesta();
                if (findRespuestas(id) == null) {
                    throw new NonexistentEntityException("The respuestas with id " + id + " no longer exists.");
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
            Respuestas respuestas;
            try {
                respuestas = em.getReference(Respuestas.class, id);
                respuestas.getIdRespuesta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The respuestas with id " + id + " no longer exists.", enfe);
            }
            Preguntas rsIdPregunta = respuestas.getRsIdPregunta();
            if (rsIdPregunta != null) {
                rsIdPregunta.getRespuestasList().remove(respuestas);
                rsIdPregunta = em.merge(rsIdPregunta);
            }
            List<PuntajeCuestionario> puntajeCuestionarioList = respuestas.getPuntajeCuestionarioList();
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionario : puntajeCuestionarioList) {
                puntajeCuestionarioListPuntajeCuestionario.setPcIdRespuesta(null);
                puntajeCuestionarioListPuntajeCuestionario = em.merge(puntajeCuestionarioListPuntajeCuestionario);
            }
            em.remove(respuestas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Respuestas> findRespuestasEntities() {
        return findRespuestasEntities(true, -1, -1);
    }

    public List<Respuestas> findRespuestasEntities(int maxResults, int firstResult) {
        return findRespuestasEntities(false, maxResults, firstResult);
    }

    private List<Respuestas> findRespuestasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Respuestas.class));
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

    public Respuestas findRespuestas(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Respuestas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRespuestasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Respuestas> rt = cq.from(Respuestas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
