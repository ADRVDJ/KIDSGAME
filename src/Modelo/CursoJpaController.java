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
public class CursoJpaController implements Serializable {

    public CursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Curso curso) throws PreexistingEntityException, Exception {
        if (curso.getAsignaturaList() == null) {
            curso.setAsignaturaList(new ArrayList<Asignatura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AnioLectivo curIdAñoLectivo = curso.getCurIdAñoLectivo();
            if (curIdAñoLectivo != null) {
                curIdAñoLectivo = em.getReference(curIdAñoLectivo.getClass(), curIdAñoLectivo.getIdAnioLectivo());
                curso.setCurIdAñoLectivo(curIdAñoLectivo);
            }
            List<Asignatura> attachedAsignaturaList = new ArrayList<Asignatura>();
            for (Asignatura asignaturaListAsignaturaToAttach : curso.getAsignaturaList()) {
                asignaturaListAsignaturaToAttach = em.getReference(asignaturaListAsignaturaToAttach.getClass(), asignaturaListAsignaturaToAttach.getIdAsignatura());
                attachedAsignaturaList.add(asignaturaListAsignaturaToAttach);
            }
            curso.setAsignaturaList(attachedAsignaturaList);
            em.persist(curso);
            if (curIdAñoLectivo != null) {
                curIdAñoLectivo.getCursoList().add(curso);
                curIdAñoLectivo = em.merge(curIdAñoLectivo);
            }
            for (Asignatura asignaturaListAsignatura : curso.getAsignaturaList()) {
                Curso oldAsIdcursoOfAsignaturaListAsignatura = asignaturaListAsignatura.getAsIdcurso();
                asignaturaListAsignatura.setAsIdcurso(curso);
                asignaturaListAsignatura = em.merge(asignaturaListAsignatura);
                if (oldAsIdcursoOfAsignaturaListAsignatura != null) {
                    oldAsIdcursoOfAsignaturaListAsignatura.getAsignaturaList().remove(asignaturaListAsignatura);
                    oldAsIdcursoOfAsignaturaListAsignatura = em.merge(oldAsIdcursoOfAsignaturaListAsignatura);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCurso(curso.getIdCurso()) != null) {
                throw new PreexistingEntityException("Curso " + curso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getIdCurso());
            AnioLectivo curIdAñoLectivoOld = persistentCurso.getCurIdAñoLectivo();
            AnioLectivo curIdAñoLectivoNew = curso.getCurIdAñoLectivo();
            List<Asignatura> asignaturaListOld = persistentCurso.getAsignaturaList();
            List<Asignatura> asignaturaListNew = curso.getAsignaturaList();
            if (curIdAñoLectivoNew != null) {
                curIdAñoLectivoNew = em.getReference(curIdAñoLectivoNew.getClass(), curIdAñoLectivoNew.getIdAnioLectivo());
                curso.setCurIdAñoLectivo(curIdAñoLectivoNew);
            }
            List<Asignatura> attachedAsignaturaListNew = new ArrayList<Asignatura>();
            for (Asignatura asignaturaListNewAsignaturaToAttach : asignaturaListNew) {
                asignaturaListNewAsignaturaToAttach = em.getReference(asignaturaListNewAsignaturaToAttach.getClass(), asignaturaListNewAsignaturaToAttach.getIdAsignatura());
                attachedAsignaturaListNew.add(asignaturaListNewAsignaturaToAttach);
            }
            asignaturaListNew = attachedAsignaturaListNew;
            curso.setAsignaturaList(asignaturaListNew);
            curso = em.merge(curso);
            if (curIdAñoLectivoOld != null && !curIdAñoLectivoOld.equals(curIdAñoLectivoNew)) {
                curIdAñoLectivoOld.getCursoList().remove(curso);
                curIdAñoLectivoOld = em.merge(curIdAñoLectivoOld);
            }
            if (curIdAñoLectivoNew != null && !curIdAñoLectivoNew.equals(curIdAñoLectivoOld)) {
                curIdAñoLectivoNew.getCursoList().add(curso);
                curIdAñoLectivoNew = em.merge(curIdAñoLectivoNew);
            }
            for (Asignatura asignaturaListOldAsignatura : asignaturaListOld) {
                if (!asignaturaListNew.contains(asignaturaListOldAsignatura)) {
                    asignaturaListOldAsignatura.setAsIdcurso(null);
                    asignaturaListOldAsignatura = em.merge(asignaturaListOldAsignatura);
                }
            }
            for (Asignatura asignaturaListNewAsignatura : asignaturaListNew) {
                if (!asignaturaListOld.contains(asignaturaListNewAsignatura)) {
                    Curso oldAsIdcursoOfAsignaturaListNewAsignatura = asignaturaListNewAsignatura.getAsIdcurso();
                    asignaturaListNewAsignatura.setAsIdcurso(curso);
                    asignaturaListNewAsignatura = em.merge(asignaturaListNewAsignatura);
                    if (oldAsIdcursoOfAsignaturaListNewAsignatura != null && !oldAsIdcursoOfAsignaturaListNewAsignatura.equals(curso)) {
                        oldAsIdcursoOfAsignaturaListNewAsignatura.getAsignaturaList().remove(asignaturaListNewAsignatura);
                        oldAsIdcursoOfAsignaturaListNewAsignatura = em.merge(oldAsIdcursoOfAsignaturaListNewAsignatura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = curso.getIdCurso();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getIdCurso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            AnioLectivo curIdAñoLectivo = curso.getCurIdAñoLectivo();
            if (curIdAñoLectivo != null) {
                curIdAñoLectivo.getCursoList().remove(curso);
                curIdAñoLectivo = em.merge(curIdAñoLectivo);
            }
            List<Asignatura> asignaturaList = curso.getAsignaturaList();
            for (Asignatura asignaturaListAsignatura : asignaturaList) {
                asignaturaListAsignatura.setAsIdcurso(null);
                asignaturaListAsignatura = em.merge(asignaturaListAsignatura);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
