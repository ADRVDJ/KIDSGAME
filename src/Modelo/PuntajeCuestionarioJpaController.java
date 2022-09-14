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
public class PuntajeCuestionarioJpaController implements Serializable {

    public PuntajeCuestionarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PuntajeCuestionario puntajeCuestionario) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuestionario pcIdCuestionario = puntajeCuestionario.getPcIdCuestionario();
            if (pcIdCuestionario != null) {
                pcIdCuestionario = em.getReference(pcIdCuestionario.getClass(), pcIdCuestionario.getIdCuestionario());
                puntajeCuestionario.setPcIdCuestionario(pcIdCuestionario);
            }
            Persona pcIdPersona = puntajeCuestionario.getPcIdPersona();
            if (pcIdPersona != null) {
                pcIdPersona = em.getReference(pcIdPersona.getClass(), pcIdPersona.getIdPersona());
                puntajeCuestionario.setPcIdPersona(pcIdPersona);
            }
            em.persist(puntajeCuestionario);
            if (pcIdCuestionario != null) {
                pcIdCuestionario.getPuntajeCuestionarioList().add(puntajeCuestionario);
                pcIdCuestionario = em.merge(pcIdCuestionario);
            }
            if (pcIdPersona != null) {
                pcIdPersona.getPuntajeCuestionarioList().add(puntajeCuestionario);
                pcIdPersona = em.merge(pcIdPersona);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPuntajeCuestionario(puntajeCuestionario.getIdPuntaje()) != null) {
                throw new PreexistingEntityException("PuntajeCuestionario " + puntajeCuestionario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PuntajeCuestionario puntajeCuestionario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PuntajeCuestionario persistentPuntajeCuestionario = em.find(PuntajeCuestionario.class, puntajeCuestionario.getIdPuntaje());
            Cuestionario pcIdCuestionarioOld = persistentPuntajeCuestionario.getPcIdCuestionario();
            Cuestionario pcIdCuestionarioNew = puntajeCuestionario.getPcIdCuestionario();
            Persona pcIdPersonaOld = persistentPuntajeCuestionario.getPcIdPersona();
            Persona pcIdPersonaNew = puntajeCuestionario.getPcIdPersona();
            if (pcIdCuestionarioNew != null) {
                pcIdCuestionarioNew = em.getReference(pcIdCuestionarioNew.getClass(), pcIdCuestionarioNew.getIdCuestionario());
                puntajeCuestionario.setPcIdCuestionario(pcIdCuestionarioNew);
            }
            if (pcIdPersonaNew != null) {
                pcIdPersonaNew = em.getReference(pcIdPersonaNew.getClass(), pcIdPersonaNew.getIdPersona());
                puntajeCuestionario.setPcIdPersona(pcIdPersonaNew);
            }
            puntajeCuestionario = em.merge(puntajeCuestionario);
            if (pcIdCuestionarioOld != null && !pcIdCuestionarioOld.equals(pcIdCuestionarioNew)) {
                pcIdCuestionarioOld.getPuntajeCuestionarioList().remove(puntajeCuestionario);
                pcIdCuestionarioOld = em.merge(pcIdCuestionarioOld);
            }
            if (pcIdCuestionarioNew != null && !pcIdCuestionarioNew.equals(pcIdCuestionarioOld)) {
                pcIdCuestionarioNew.getPuntajeCuestionarioList().add(puntajeCuestionario);
                pcIdCuestionarioNew = em.merge(pcIdCuestionarioNew);
            }
            if (pcIdPersonaOld != null && !pcIdPersonaOld.equals(pcIdPersonaNew)) {
                pcIdPersonaOld.getPuntajeCuestionarioList().remove(puntajeCuestionario);
                pcIdPersonaOld = em.merge(pcIdPersonaOld);
            }
            if (pcIdPersonaNew != null && !pcIdPersonaNew.equals(pcIdPersonaOld)) {
                pcIdPersonaNew.getPuntajeCuestionarioList().add(puntajeCuestionario);
                pcIdPersonaNew = em.merge(pcIdPersonaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = puntajeCuestionario.getIdPuntaje();
                if (findPuntajeCuestionario(id) == null) {
                    throw new NonexistentEntityException("The puntajeCuestionario with id " + id + " no longer exists.");
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
            PuntajeCuestionario puntajeCuestionario;
            try {
                puntajeCuestionario = em.getReference(PuntajeCuestionario.class, id);
                puntajeCuestionario.getIdPuntaje();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puntajeCuestionario with id " + id + " no longer exists.", enfe);
            }
            Cuestionario pcIdCuestionario = puntajeCuestionario.getPcIdCuestionario();
            if (pcIdCuestionario != null) {
                pcIdCuestionario.getPuntajeCuestionarioList().remove(puntajeCuestionario);
                pcIdCuestionario = em.merge(pcIdCuestionario);
            }
            Persona pcIdPersona = puntajeCuestionario.getPcIdPersona();
            if (pcIdPersona != null) {
                pcIdPersona.getPuntajeCuestionarioList().remove(puntajeCuestionario);
                pcIdPersona = em.merge(pcIdPersona);
            }
            em.remove(puntajeCuestionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PuntajeCuestionario> findPuntajeCuestionarioEntities() {
        return findPuntajeCuestionarioEntities(true, -1, -1);
    }

    public List<PuntajeCuestionario> findPuntajeCuestionarioEntities(int maxResults, int firstResult) {
        return findPuntajeCuestionarioEntities(false, maxResults, firstResult);
    }

    private List<PuntajeCuestionario> findPuntajeCuestionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PuntajeCuestionario.class));
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

    public PuntajeCuestionario findPuntajeCuestionario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PuntajeCuestionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuntajeCuestionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PuntajeCuestionario> rt = cq.from(PuntajeCuestionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
