/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.exceptions.IllegalOrphanException;
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
public class AnioLectivoJpaController implements Serializable {

    public AnioLectivoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AnioLectivo anioLectivo) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Asignatura asignaturaOrphanCheck = anioLectivo.getAsignatura();
        if (asignaturaOrphanCheck != null) {
            AnioLectivo oldAnioLectivoOfAsignatura = asignaturaOrphanCheck.getAnioLectivo();
            if (oldAnioLectivoOfAsignatura != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Asignatura " + asignaturaOrphanCheck + " already has an item of type AnioLectivo whose asignatura column cannot be null. Please make another selection for the asignatura field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignatura asignatura = anioLectivo.getAsignatura();
            if (asignatura != null) {
                asignatura = em.getReference(asignatura.getClass(), asignatura.getIdAsignatura());
                anioLectivo.setAsignatura(asignatura);
            }
            em.persist(anioLectivo);
            if (asignatura != null) {
                asignatura.setAnioLectivo(anioLectivo);
                asignatura = em.merge(asignatura);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnioLectivo(anioLectivo.getIdAnioLectivo()) != null) {
                throw new PreexistingEntityException("AnioLectivo " + anioLectivo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AnioLectivo anioLectivo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AnioLectivo persistentAnioLectivo = em.find(AnioLectivo.class, anioLectivo.getIdAnioLectivo());
            Asignatura asignaturaOld = persistentAnioLectivo.getAsignatura();
            Asignatura asignaturaNew = anioLectivo.getAsignatura();
            List<String> illegalOrphanMessages = null;
            if (asignaturaNew != null && !asignaturaNew.equals(asignaturaOld)) {
                AnioLectivo oldAnioLectivoOfAsignatura = asignaturaNew.getAnioLectivo();
                if (oldAnioLectivoOfAsignatura != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Asignatura " + asignaturaNew + " already has an item of type AnioLectivo whose asignatura column cannot be null. Please make another selection for the asignatura field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (asignaturaNew != null) {
                asignaturaNew = em.getReference(asignaturaNew.getClass(), asignaturaNew.getIdAsignatura());
                anioLectivo.setAsignatura(asignaturaNew);
            }
            anioLectivo = em.merge(anioLectivo);
            if (asignaturaOld != null && !asignaturaOld.equals(asignaturaNew)) {
                asignaturaOld.setAnioLectivo(null);
                asignaturaOld = em.merge(asignaturaOld);
            }
            if (asignaturaNew != null && !asignaturaNew.equals(asignaturaOld)) {
                asignaturaNew.setAnioLectivo(anioLectivo);
                asignaturaNew = em.merge(asignaturaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = anioLectivo.getIdAnioLectivo();
                if (findAnioLectivo(id) == null) {
                    throw new NonexistentEntityException("The anioLectivo with id " + id + " no longer exists.");
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
            AnioLectivo anioLectivo;
            try {
                anioLectivo = em.getReference(AnioLectivo.class, id);
                anioLectivo.getIdAnioLectivo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The anioLectivo with id " + id + " no longer exists.", enfe);
            }
            Asignatura asignatura = anioLectivo.getAsignatura();
            if (asignatura != null) {
                asignatura.setAnioLectivo(null);
                asignatura = em.merge(asignatura);
            }
            em.remove(anioLectivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AnioLectivo> findAnioLectivoEntities() {
        return findAnioLectivoEntities(true, -1, -1);
    }

    public List<AnioLectivo> findAnioLectivoEntities(int maxResults, int firstResult) {
        return findAnioLectivoEntities(false, maxResults, firstResult);
    }

    private List<AnioLectivo> findAnioLectivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AnioLectivo.class));
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

    public AnioLectivo findAnioLectivo(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AnioLectivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnioLectivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AnioLectivo> rt = cq.from(AnioLectivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
