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
public class AsignaturaJpaController implements Serializable {

    public AsignaturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignatura asignatura) throws PreexistingEntityException, Exception {
        if (asignatura.getCuestionarioList() == null) {
            asignatura.setCuestionarioList(new ArrayList<Cuestionario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso asIdcurso = asignatura.getAsIdcurso();
            if (asIdcurso != null) {
                asIdcurso = em.getReference(asIdcurso.getClass(), asIdcurso.getIdCurso());
                asignatura.setAsIdcurso(asIdcurso);
            }
            PersonaAsignatura idPersonaA = asignatura.getIdPersonaA();
            if (idPersonaA != null) {
                idPersonaA = em.getReference(idPersonaA.getClass(), idPersonaA.getIdPersonaA());
                asignatura.setIdPersonaA(idPersonaA);
            }
            List<Cuestionario> attachedCuestionarioList = new ArrayList<Cuestionario>();
            for (Cuestionario cuestionarioListCuestionarioToAttach : asignatura.getCuestionarioList()) {
                cuestionarioListCuestionarioToAttach = em.getReference(cuestionarioListCuestionarioToAttach.getClass(), cuestionarioListCuestionarioToAttach.getIdCuestionario());
                attachedCuestionarioList.add(cuestionarioListCuestionarioToAttach);
            }
            asignatura.setCuestionarioList(attachedCuestionarioList);
            em.persist(asignatura);
            if (asIdcurso != null) {
                asIdcurso.getAsignaturaList().add(asignatura);
                asIdcurso = em.merge(asIdcurso);
            }
            if (idPersonaA != null) {
                idPersonaA.getAsignaturaList().add(asignatura);
                idPersonaA = em.merge(idPersonaA);
            }
            for (Cuestionario cuestionarioListCuestionario : asignatura.getCuestionarioList()) {
                Asignatura oldCrIdAsignaturaOfCuestionarioListCuestionario = cuestionarioListCuestionario.getCrIdAsignatura();
                cuestionarioListCuestionario.setCrIdAsignatura(asignatura);
                cuestionarioListCuestionario = em.merge(cuestionarioListCuestionario);
                if (oldCrIdAsignaturaOfCuestionarioListCuestionario != null) {
                    oldCrIdAsignaturaOfCuestionarioListCuestionario.getCuestionarioList().remove(cuestionarioListCuestionario);
                    oldCrIdAsignaturaOfCuestionarioListCuestionario = em.merge(oldCrIdAsignaturaOfCuestionarioListCuestionario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAsignatura(asignatura.getIdAsignatura()) != null) {
                throw new PreexistingEntityException("Asignatura " + asignatura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asignatura asignatura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignatura persistentAsignatura = em.find(Asignatura.class, asignatura.getIdAsignatura());
            Curso asIdcursoOld = persistentAsignatura.getAsIdcurso();
            Curso asIdcursoNew = asignatura.getAsIdcurso();
            PersonaAsignatura idPersonaAOld = persistentAsignatura.getIdPersonaA();
            PersonaAsignatura idPersonaANew = asignatura.getIdPersonaA();
            List<Cuestionario> cuestionarioListOld = persistentAsignatura.getCuestionarioList();
            List<Cuestionario> cuestionarioListNew = asignatura.getCuestionarioList();
            if (asIdcursoNew != null) {
                asIdcursoNew = em.getReference(asIdcursoNew.getClass(), asIdcursoNew.getIdCurso());
                asignatura.setAsIdcurso(asIdcursoNew);
            }
            if (idPersonaANew != null) {
                idPersonaANew = em.getReference(idPersonaANew.getClass(), idPersonaANew.getIdPersonaA());
                asignatura.setIdPersonaA(idPersonaANew);
            }
            List<Cuestionario> attachedCuestionarioListNew = new ArrayList<Cuestionario>();
            for (Cuestionario cuestionarioListNewCuestionarioToAttach : cuestionarioListNew) {
                cuestionarioListNewCuestionarioToAttach = em.getReference(cuestionarioListNewCuestionarioToAttach.getClass(), cuestionarioListNewCuestionarioToAttach.getIdCuestionario());
                attachedCuestionarioListNew.add(cuestionarioListNewCuestionarioToAttach);
            }
            cuestionarioListNew = attachedCuestionarioListNew;
            asignatura.setCuestionarioList(cuestionarioListNew);
            asignatura = em.merge(asignatura);
            if (asIdcursoOld != null && !asIdcursoOld.equals(asIdcursoNew)) {
                asIdcursoOld.getAsignaturaList().remove(asignatura);
                asIdcursoOld = em.merge(asIdcursoOld);
            }
            if (asIdcursoNew != null && !asIdcursoNew.equals(asIdcursoOld)) {
                asIdcursoNew.getAsignaturaList().add(asignatura);
                asIdcursoNew = em.merge(asIdcursoNew);
            }
            if (idPersonaAOld != null && !idPersonaAOld.equals(idPersonaANew)) {
                idPersonaAOld.getAsignaturaList().remove(asignatura);
                idPersonaAOld = em.merge(idPersonaAOld);
            }
            if (idPersonaANew != null && !idPersonaANew.equals(idPersonaAOld)) {
                idPersonaANew.getAsignaturaList().add(asignatura);
                idPersonaANew = em.merge(idPersonaANew);
            }
            for (Cuestionario cuestionarioListOldCuestionario : cuestionarioListOld) {
                if (!cuestionarioListNew.contains(cuestionarioListOldCuestionario)) {
                    cuestionarioListOldCuestionario.setCrIdAsignatura(null);
                    cuestionarioListOldCuestionario = em.merge(cuestionarioListOldCuestionario);
                }
            }
            for (Cuestionario cuestionarioListNewCuestionario : cuestionarioListNew) {
                if (!cuestionarioListOld.contains(cuestionarioListNewCuestionario)) {
                    Asignatura oldCrIdAsignaturaOfCuestionarioListNewCuestionario = cuestionarioListNewCuestionario.getCrIdAsignatura();
                    cuestionarioListNewCuestionario.setCrIdAsignatura(asignatura);
                    cuestionarioListNewCuestionario = em.merge(cuestionarioListNewCuestionario);
                    if (oldCrIdAsignaturaOfCuestionarioListNewCuestionario != null && !oldCrIdAsignaturaOfCuestionarioListNewCuestionario.equals(asignatura)) {
                        oldCrIdAsignaturaOfCuestionarioListNewCuestionario.getCuestionarioList().remove(cuestionarioListNewCuestionario);
                        oldCrIdAsignaturaOfCuestionarioListNewCuestionario = em.merge(oldCrIdAsignaturaOfCuestionarioListNewCuestionario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = asignatura.getIdAsignatura();
                if (findAsignatura(id) == null) {
                    throw new NonexistentEntityException("The asignatura with id " + id + " no longer exists.");
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
            Asignatura asignatura;
            try {
                asignatura = em.getReference(Asignatura.class, id);
                asignatura.getIdAsignatura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignatura with id " + id + " no longer exists.", enfe);
            }
            Curso asIdcurso = asignatura.getAsIdcurso();
            if (asIdcurso != null) {
                asIdcurso.getAsignaturaList().remove(asignatura);
                asIdcurso = em.merge(asIdcurso);
            }
            PersonaAsignatura idPersonaA = asignatura.getIdPersonaA();
            if (idPersonaA != null) {
                idPersonaA.getAsignaturaList().remove(asignatura);
                idPersonaA = em.merge(idPersonaA);
            }
            List<Cuestionario> cuestionarioList = asignatura.getCuestionarioList();
            for (Cuestionario cuestionarioListCuestionario : cuestionarioList) {
                cuestionarioListCuestionario.setCrIdAsignatura(null);
                cuestionarioListCuestionario = em.merge(cuestionarioListCuestionario);
            }
            em.remove(asignatura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asignatura> findAsignaturaEntities() {
        return findAsignaturaEntities(true, -1, -1);
    }

    public List<Asignatura> findAsignaturaEntities(int maxResults, int firstResult) {
        return findAsignaturaEntities(false, maxResults, firstResult);
    }

    private List<Asignatura> findAsignaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asignatura.class));
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

    public Asignatura findAsignatura(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asignatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asignatura> rt = cq.from(Asignatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
