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
public class PersonaAsignaturaJpaController implements Serializable {

    public PersonaAsignaturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PersonaAsignatura personaAsignatura) throws PreexistingEntityException, Exception {
        if (personaAsignatura.getAsignaturaList() == null) {
            personaAsignatura.setAsignaturaList(new ArrayList<Asignatura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona paIdPersona = personaAsignatura.getPaIdPersona();
            if (paIdPersona != null) {
                paIdPersona = em.getReference(paIdPersona.getClass(), paIdPersona.getIdPersona());
                personaAsignatura.setPaIdPersona(paIdPersona);
            }
            List<Asignatura> attachedAsignaturaList = new ArrayList<Asignatura>();
            for (Asignatura asignaturaListAsignaturaToAttach : personaAsignatura.getAsignaturaList()) {
                asignaturaListAsignaturaToAttach = em.getReference(asignaturaListAsignaturaToAttach.getClass(), asignaturaListAsignaturaToAttach.getIdAsignatura());
                attachedAsignaturaList.add(asignaturaListAsignaturaToAttach);
            }
            personaAsignatura.setAsignaturaList(attachedAsignaturaList);
            em.persist(personaAsignatura);
            if (paIdPersona != null) {
                paIdPersona.getPersonaAsignaturaList().add(personaAsignatura);
                paIdPersona = em.merge(paIdPersona);
            }
            for (Asignatura asignaturaListAsignatura : personaAsignatura.getAsignaturaList()) {
                PersonaAsignatura oldIdPersonaAOfAsignaturaListAsignatura = asignaturaListAsignatura.getIdPersonaA();
                asignaturaListAsignatura.setIdPersonaA(personaAsignatura);
                asignaturaListAsignatura = em.merge(asignaturaListAsignatura);
                if (oldIdPersonaAOfAsignaturaListAsignatura != null) {
                    oldIdPersonaAOfAsignaturaListAsignatura.getAsignaturaList().remove(asignaturaListAsignatura);
                    oldIdPersonaAOfAsignaturaListAsignatura = em.merge(oldIdPersonaAOfAsignaturaListAsignatura);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonaAsignatura(personaAsignatura.getIdPersonaA()) != null) {
                throw new PreexistingEntityException("PersonaAsignatura " + personaAsignatura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PersonaAsignatura personaAsignatura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PersonaAsignatura persistentPersonaAsignatura = em.find(PersonaAsignatura.class, personaAsignatura.getIdPersonaA());
            Persona paIdPersonaOld = persistentPersonaAsignatura.getPaIdPersona();
            Persona paIdPersonaNew = personaAsignatura.getPaIdPersona();
            List<Asignatura> asignaturaListOld = persistentPersonaAsignatura.getAsignaturaList();
            List<Asignatura> asignaturaListNew = personaAsignatura.getAsignaturaList();
            if (paIdPersonaNew != null) {
                paIdPersonaNew = em.getReference(paIdPersonaNew.getClass(), paIdPersonaNew.getIdPersona());
                personaAsignatura.setPaIdPersona(paIdPersonaNew);
            }
            List<Asignatura> attachedAsignaturaListNew = new ArrayList<Asignatura>();
            for (Asignatura asignaturaListNewAsignaturaToAttach : asignaturaListNew) {
                asignaturaListNewAsignaturaToAttach = em.getReference(asignaturaListNewAsignaturaToAttach.getClass(), asignaturaListNewAsignaturaToAttach.getIdAsignatura());
                attachedAsignaturaListNew.add(asignaturaListNewAsignaturaToAttach);
            }
            asignaturaListNew = attachedAsignaturaListNew;
            personaAsignatura.setAsignaturaList(asignaturaListNew);
            personaAsignatura = em.merge(personaAsignatura);
            if (paIdPersonaOld != null && !paIdPersonaOld.equals(paIdPersonaNew)) {
                paIdPersonaOld.getPersonaAsignaturaList().remove(personaAsignatura);
                paIdPersonaOld = em.merge(paIdPersonaOld);
            }
            if (paIdPersonaNew != null && !paIdPersonaNew.equals(paIdPersonaOld)) {
                paIdPersonaNew.getPersonaAsignaturaList().add(personaAsignatura);
                paIdPersonaNew = em.merge(paIdPersonaNew);
            }
            for (Asignatura asignaturaListOldAsignatura : asignaturaListOld) {
                if (!asignaturaListNew.contains(asignaturaListOldAsignatura)) {
                    asignaturaListOldAsignatura.setIdPersonaA(null);
                    asignaturaListOldAsignatura = em.merge(asignaturaListOldAsignatura);
                }
            }
            for (Asignatura asignaturaListNewAsignatura : asignaturaListNew) {
                if (!asignaturaListOld.contains(asignaturaListNewAsignatura)) {
                    PersonaAsignatura oldIdPersonaAOfAsignaturaListNewAsignatura = asignaturaListNewAsignatura.getIdPersonaA();
                    asignaturaListNewAsignatura.setIdPersonaA(personaAsignatura);
                    asignaturaListNewAsignatura = em.merge(asignaturaListNewAsignatura);
                    if (oldIdPersonaAOfAsignaturaListNewAsignatura != null && !oldIdPersonaAOfAsignaturaListNewAsignatura.equals(personaAsignatura)) {
                        oldIdPersonaAOfAsignaturaListNewAsignatura.getAsignaturaList().remove(asignaturaListNewAsignatura);
                        oldIdPersonaAOfAsignaturaListNewAsignatura = em.merge(oldIdPersonaAOfAsignaturaListNewAsignatura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = personaAsignatura.getIdPersonaA();
                if (findPersonaAsignatura(id) == null) {
                    throw new NonexistentEntityException("The personaAsignatura with id " + id + " no longer exists.");
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
            PersonaAsignatura personaAsignatura;
            try {
                personaAsignatura = em.getReference(PersonaAsignatura.class, id);
                personaAsignatura.getIdPersonaA();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personaAsignatura with id " + id + " no longer exists.", enfe);
            }
            Persona paIdPersona = personaAsignatura.getPaIdPersona();
            if (paIdPersona != null) {
                paIdPersona.getPersonaAsignaturaList().remove(personaAsignatura);
                paIdPersona = em.merge(paIdPersona);
            }
            List<Asignatura> asignaturaList = personaAsignatura.getAsignaturaList();
            for (Asignatura asignaturaListAsignatura : asignaturaList) {
                asignaturaListAsignatura.setIdPersonaA(null);
                asignaturaListAsignatura = em.merge(asignaturaListAsignatura);
            }
            em.remove(personaAsignatura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PersonaAsignatura> findPersonaAsignaturaEntities() {
        return findPersonaAsignaturaEntities(true, -1, -1);
    }

    public List<PersonaAsignatura> findPersonaAsignaturaEntities(int maxResults, int firstResult) {
        return findPersonaAsignaturaEntities(false, maxResults, firstResult);
    }

    private List<PersonaAsignatura> findPersonaAsignaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PersonaAsignatura.class));
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

    public PersonaAsignatura findPersonaAsignatura(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PersonaAsignatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaAsignaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PersonaAsignatura> rt = cq.from(PersonaAsignatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
