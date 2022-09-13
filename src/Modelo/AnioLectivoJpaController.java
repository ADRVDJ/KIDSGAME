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
public class AnioLectivoJpaController implements Serializable {

    public AnioLectivoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AnioLectivo anioLectivo) throws PreexistingEntityException, Exception {
        if (anioLectivo.getCursoList() == null) {
            anioLectivo.setCursoList(new ArrayList<Curso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Curso> attachedCursoList = new ArrayList<Curso>();
            for (Curso cursoListCursoToAttach : anioLectivo.getCursoList()) {
                cursoListCursoToAttach = em.getReference(cursoListCursoToAttach.getClass(), cursoListCursoToAttach.getIdCurso());
                attachedCursoList.add(cursoListCursoToAttach);
            }
            anioLectivo.setCursoList(attachedCursoList);
            em.persist(anioLectivo);
            for (Curso cursoListCurso : anioLectivo.getCursoList()) {
                AnioLectivo oldCurIdAñoLectivoOfCursoListCurso = cursoListCurso.getCurIdAñoLectivo();
                cursoListCurso.setCurIdAñoLectivo(anioLectivo);
                cursoListCurso = em.merge(cursoListCurso);
                if (oldCurIdAñoLectivoOfCursoListCurso != null) {
                    oldCurIdAñoLectivoOfCursoListCurso.getCursoList().remove(cursoListCurso);
                    oldCurIdAñoLectivoOfCursoListCurso = em.merge(oldCurIdAñoLectivoOfCursoListCurso);
                }
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

    public void edit(AnioLectivo anioLectivo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AnioLectivo persistentAnioLectivo = em.find(AnioLectivo.class, anioLectivo.getIdAnioLectivo());
            List<Curso> cursoListOld = persistentAnioLectivo.getCursoList();
            List<Curso> cursoListNew = anioLectivo.getCursoList();
            List<Curso> attachedCursoListNew = new ArrayList<Curso>();
            for (Curso cursoListNewCursoToAttach : cursoListNew) {
                cursoListNewCursoToAttach = em.getReference(cursoListNewCursoToAttach.getClass(), cursoListNewCursoToAttach.getIdCurso());
                attachedCursoListNew.add(cursoListNewCursoToAttach);
            }
            cursoListNew = attachedCursoListNew;
            anioLectivo.setCursoList(cursoListNew);
            anioLectivo = em.merge(anioLectivo);
            for (Curso cursoListOldCurso : cursoListOld) {
                if (!cursoListNew.contains(cursoListOldCurso)) {
                    cursoListOldCurso.setCurIdAñoLectivo(null);
                    cursoListOldCurso = em.merge(cursoListOldCurso);
                }
            }
            for (Curso cursoListNewCurso : cursoListNew) {
                if (!cursoListOld.contains(cursoListNewCurso)) {
                    AnioLectivo oldCurIdAñoLectivoOfCursoListNewCurso = cursoListNewCurso.getCurIdAñoLectivo();
                    cursoListNewCurso.setCurIdAñoLectivo(anioLectivo);
                    cursoListNewCurso = em.merge(cursoListNewCurso);
                    if (oldCurIdAñoLectivoOfCursoListNewCurso != null && !oldCurIdAñoLectivoOfCursoListNewCurso.equals(anioLectivo)) {
                        oldCurIdAñoLectivoOfCursoListNewCurso.getCursoList().remove(cursoListNewCurso);
                        oldCurIdAñoLectivoOfCursoListNewCurso = em.merge(oldCurIdAñoLectivoOfCursoListNewCurso);
                    }
                }
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
            List<Curso> cursoList = anioLectivo.getCursoList();
            for (Curso cursoListCurso : cursoList) {
                cursoListCurso.setCurIdAñoLectivo(null);
                cursoListCurso = em.merge(cursoListCurso);
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
