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
public class PersonaAsignaturaJpaController implements Serializable {

    public PersonaAsignaturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PersonaAsignatura personaAsignatura) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Persona personaOrphanCheck = personaAsignatura.getPersona();
        if (personaOrphanCheck != null) {
            PersonaAsignatura oldPersonaAsignaturaOfPersona = personaOrphanCheck.getPersonaAsignatura();
            if (oldPersonaAsignaturaOfPersona != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Persona " + personaOrphanCheck + " already has an item of type PersonaAsignatura whose persona column cannot be null. Please make another selection for the persona field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignatura asignatura = personaAsignatura.getAsignatura();
            if (asignatura != null) {
                asignatura = em.getReference(asignatura.getClass(), asignatura.getIdAsignatura());
                personaAsignatura.setAsignatura(asignatura);
            }
            Persona persona = personaAsignatura.getPersona();
            if (persona != null) {
                persona = em.getReference(persona.getClass(), persona.getIdPersona());
                personaAsignatura.setPersona(persona);
            }
            em.persist(personaAsignatura);
            if (asignatura != null) {
                PersonaAsignatura oldPersonaAsignaturaOfAsignatura = asignatura.getPersonaAsignatura();
                if (oldPersonaAsignaturaOfAsignatura != null) {
                    oldPersonaAsignaturaOfAsignatura.setAsignatura(null);
                    oldPersonaAsignaturaOfAsignatura = em.merge(oldPersonaAsignaturaOfAsignatura);
                }
                asignatura.setPersonaAsignatura(personaAsignatura);
                asignatura = em.merge(asignatura);
            }
            if (persona != null) {
                persona.setPersonaAsignatura(personaAsignatura);
                persona = em.merge(persona);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonaAsignatura(personaAsignatura.getIdUsuarioA()) != null) {
                throw new PreexistingEntityException("PersonaAsignatura " + personaAsignatura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PersonaAsignatura personaAsignatura) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PersonaAsignatura persistentPersonaAsignatura = em.find(PersonaAsignatura.class, personaAsignatura.getIdUsuarioA());
            Asignatura asignaturaOld = persistentPersonaAsignatura.getAsignatura();
            Asignatura asignaturaNew = personaAsignatura.getAsignatura();
            Persona personaOld = persistentPersonaAsignatura.getPersona();
            Persona personaNew = personaAsignatura.getPersona();
            List<String> illegalOrphanMessages = null;
            if (asignaturaOld != null && !asignaturaOld.equals(asignaturaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Asignatura " + asignaturaOld + " since its personaAsignatura field is not nullable.");
            }
            if (personaNew != null && !personaNew.equals(personaOld)) {
                PersonaAsignatura oldPersonaAsignaturaOfPersona = personaNew.getPersonaAsignatura();
                if (oldPersonaAsignaturaOfPersona != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Persona " + personaNew + " already has an item of type PersonaAsignatura whose persona column cannot be null. Please make another selection for the persona field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (asignaturaNew != null) {
                asignaturaNew = em.getReference(asignaturaNew.getClass(), asignaturaNew.getIdAsignatura());
                personaAsignatura.setAsignatura(asignaturaNew);
            }
            if (personaNew != null) {
                personaNew = em.getReference(personaNew.getClass(), personaNew.getIdPersona());
                personaAsignatura.setPersona(personaNew);
            }
            personaAsignatura = em.merge(personaAsignatura);
            if (asignaturaNew != null && !asignaturaNew.equals(asignaturaOld)) {
                PersonaAsignatura oldPersonaAsignaturaOfAsignatura = asignaturaNew.getPersonaAsignatura();
                if (oldPersonaAsignaturaOfAsignatura != null) {
                    oldPersonaAsignaturaOfAsignatura.setAsignatura(null);
                    oldPersonaAsignaturaOfAsignatura = em.merge(oldPersonaAsignaturaOfAsignatura);
                }
                asignaturaNew.setPersonaAsignatura(personaAsignatura);
                asignaturaNew = em.merge(asignaturaNew);
            }
            if (personaOld != null && !personaOld.equals(personaNew)) {
                personaOld.setPersonaAsignatura(null);
                personaOld = em.merge(personaOld);
            }
            if (personaNew != null && !personaNew.equals(personaOld)) {
                personaNew.setPersonaAsignatura(personaAsignatura);
                personaNew = em.merge(personaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = personaAsignatura.getIdUsuarioA();
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

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PersonaAsignatura personaAsignatura;
            try {
                personaAsignatura = em.getReference(PersonaAsignatura.class, id);
                personaAsignatura.getIdUsuarioA();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personaAsignatura with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Asignatura asignaturaOrphanCheck = personaAsignatura.getAsignatura();
            if (asignaturaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PersonaAsignatura (" + personaAsignatura + ") cannot be destroyed since the Asignatura " + asignaturaOrphanCheck + " in its asignatura field has a non-nullable personaAsignatura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona persona = personaAsignatura.getPersona();
            if (persona != null) {
                persona.setPersonaAsignatura(null);
                persona = em.merge(persona);
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
