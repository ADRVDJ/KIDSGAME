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
        if (respuestas.getPreguntasList() == null) {
            respuestas.setPreguntasList(new ArrayList<Preguntas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Preguntas> attachedPreguntasList = new ArrayList<Preguntas>();
            for (Preguntas preguntasListPreguntasToAttach : respuestas.getPreguntasList()) {
                preguntasListPreguntasToAttach = em.getReference(preguntasListPreguntasToAttach.getClass(), preguntasListPreguntasToAttach.getIdPregunta());
                attachedPreguntasList.add(preguntasListPreguntasToAttach);
            }
            respuestas.setPreguntasList(attachedPreguntasList);
            em.persist(respuestas);
            for (Preguntas preguntasListPreguntas : respuestas.getPreguntasList()) {
                Respuestas oldPrIdRespuestasOfPreguntasListPreguntas = preguntasListPreguntas.getPrIdRespuestas();
                preguntasListPreguntas.setPrIdRespuestas(respuestas);
                preguntasListPreguntas = em.merge(preguntasListPreguntas);
                if (oldPrIdRespuestasOfPreguntasListPreguntas != null) {
                    oldPrIdRespuestasOfPreguntasListPreguntas.getPreguntasList().remove(preguntasListPreguntas);
                    oldPrIdRespuestasOfPreguntasListPreguntas = em.merge(oldPrIdRespuestasOfPreguntasListPreguntas);
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
            List<Preguntas> preguntasListOld = persistentRespuestas.getPreguntasList();
            List<Preguntas> preguntasListNew = respuestas.getPreguntasList();
            List<Preguntas> attachedPreguntasListNew = new ArrayList<Preguntas>();
            for (Preguntas preguntasListNewPreguntasToAttach : preguntasListNew) {
                preguntasListNewPreguntasToAttach = em.getReference(preguntasListNewPreguntasToAttach.getClass(), preguntasListNewPreguntasToAttach.getIdPregunta());
                attachedPreguntasListNew.add(preguntasListNewPreguntasToAttach);
            }
            preguntasListNew = attachedPreguntasListNew;
            respuestas.setPreguntasList(preguntasListNew);
            respuestas = em.merge(respuestas);
            for (Preguntas preguntasListOldPreguntas : preguntasListOld) {
                if (!preguntasListNew.contains(preguntasListOldPreguntas)) {
                    preguntasListOldPreguntas.setPrIdRespuestas(null);
                    preguntasListOldPreguntas = em.merge(preguntasListOldPreguntas);
                }
            }
            for (Preguntas preguntasListNewPreguntas : preguntasListNew) {
                if (!preguntasListOld.contains(preguntasListNewPreguntas)) {
                    Respuestas oldPrIdRespuestasOfPreguntasListNewPreguntas = preguntasListNewPreguntas.getPrIdRespuestas();
                    preguntasListNewPreguntas.setPrIdRespuestas(respuestas);
                    preguntasListNewPreguntas = em.merge(preguntasListNewPreguntas);
                    if (oldPrIdRespuestasOfPreguntasListNewPreguntas != null && !oldPrIdRespuestasOfPreguntasListNewPreguntas.equals(respuestas)) {
                        oldPrIdRespuestasOfPreguntasListNewPreguntas.getPreguntasList().remove(preguntasListNewPreguntas);
                        oldPrIdRespuestasOfPreguntasListNewPreguntas = em.merge(oldPrIdRespuestasOfPreguntasListNewPreguntas);
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
            List<Preguntas> preguntasList = respuestas.getPreguntasList();
            for (Preguntas preguntasListPreguntas : preguntasList) {
                preguntasListPreguntas.setPrIdRespuestas(null);
                preguntasListPreguntas = em.merge(preguntasListPreguntas);
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
