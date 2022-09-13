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
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws PreexistingEntityException, Exception {
        if (persona.getPuntajeCuestionarioList() == null) {
            persona.setPuntajeCuestionarioList(new ArrayList<PuntajeCuestionario>());
        }
        if (persona.getPersonaAsignaturaList() == null) {
            persona.setPersonaAsignaturaList(new ArrayList<PersonaAsignatura>());
        }
        if (persona.getUsuarioList() == null) {
            persona.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PuntajeCuestionario> attachedPuntajeCuestionarioList = new ArrayList<PuntajeCuestionario>();
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionarioToAttach : persona.getPuntajeCuestionarioList()) {
                puntajeCuestionarioListPuntajeCuestionarioToAttach = em.getReference(puntajeCuestionarioListPuntajeCuestionarioToAttach.getClass(), puntajeCuestionarioListPuntajeCuestionarioToAttach.getIdPuntaje());
                attachedPuntajeCuestionarioList.add(puntajeCuestionarioListPuntajeCuestionarioToAttach);
            }
            persona.setPuntajeCuestionarioList(attachedPuntajeCuestionarioList);
            List<PersonaAsignatura> attachedPersonaAsignaturaList = new ArrayList<PersonaAsignatura>();
            for (PersonaAsignatura personaAsignaturaListPersonaAsignaturaToAttach : persona.getPersonaAsignaturaList()) {
                personaAsignaturaListPersonaAsignaturaToAttach = em.getReference(personaAsignaturaListPersonaAsignaturaToAttach.getClass(), personaAsignaturaListPersonaAsignaturaToAttach.getIdPersonaA());
                attachedPersonaAsignaturaList.add(personaAsignaturaListPersonaAsignaturaToAttach);
            }
            persona.setPersonaAsignaturaList(attachedPersonaAsignaturaList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : persona.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            persona.setUsuarioList(attachedUsuarioList);
            em.persist(persona);
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionario : persona.getPuntajeCuestionarioList()) {
                Persona oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario = puntajeCuestionarioListPuntajeCuestionario.getPcIdPersona();
                puntajeCuestionarioListPuntajeCuestionario.setPcIdPersona(persona);
                puntajeCuestionarioListPuntajeCuestionario = em.merge(puntajeCuestionarioListPuntajeCuestionario);
                if (oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario != null) {
                    oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario.getPuntajeCuestionarioList().remove(puntajeCuestionarioListPuntajeCuestionario);
                    oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario = em.merge(oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario);
                }
            }
            for (PersonaAsignatura personaAsignaturaListPersonaAsignatura : persona.getPersonaAsignaturaList()) {
                Persona oldPaIdPersonaOfPersonaAsignaturaListPersonaAsignatura = personaAsignaturaListPersonaAsignatura.getPaIdPersona();
                personaAsignaturaListPersonaAsignatura.setPaIdPersona(persona);
                personaAsignaturaListPersonaAsignatura = em.merge(personaAsignaturaListPersonaAsignatura);
                if (oldPaIdPersonaOfPersonaAsignaturaListPersonaAsignatura != null) {
                    oldPaIdPersonaOfPersonaAsignaturaListPersonaAsignatura.getPersonaAsignaturaList().remove(personaAsignaturaListPersonaAsignatura);
                    oldPaIdPersonaOfPersonaAsignaturaListPersonaAsignatura = em.merge(oldPaIdPersonaOfPersonaAsignaturaListPersonaAsignatura);
                }
            }
            for (Usuario usuarioListUsuario : persona.getUsuarioList()) {
                Persona oldUsIdPersonaOfUsuarioListUsuario = usuarioListUsuario.getUsIdPersona();
                usuarioListUsuario.setUsIdPersona(persona);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldUsIdPersonaOfUsuarioListUsuario != null) {
                    oldUsIdPersonaOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldUsIdPersonaOfUsuarioListUsuario = em.merge(oldUsIdPersonaOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersona(persona.getIdPersona()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getIdPersona());
            List<PuntajeCuestionario> puntajeCuestionarioListOld = persistentPersona.getPuntajeCuestionarioList();
            List<PuntajeCuestionario> puntajeCuestionarioListNew = persona.getPuntajeCuestionarioList();
            List<PersonaAsignatura> personaAsignaturaListOld = persistentPersona.getPersonaAsignaturaList();
            List<PersonaAsignatura> personaAsignaturaListNew = persona.getPersonaAsignaturaList();
            List<Usuario> usuarioListOld = persistentPersona.getUsuarioList();
            List<Usuario> usuarioListNew = persona.getUsuarioList();
            List<PuntajeCuestionario> attachedPuntajeCuestionarioListNew = new ArrayList<PuntajeCuestionario>();
            for (PuntajeCuestionario puntajeCuestionarioListNewPuntajeCuestionarioToAttach : puntajeCuestionarioListNew) {
                puntajeCuestionarioListNewPuntajeCuestionarioToAttach = em.getReference(puntajeCuestionarioListNewPuntajeCuestionarioToAttach.getClass(), puntajeCuestionarioListNewPuntajeCuestionarioToAttach.getIdPuntaje());
                attachedPuntajeCuestionarioListNew.add(puntajeCuestionarioListNewPuntajeCuestionarioToAttach);
            }
            puntajeCuestionarioListNew = attachedPuntajeCuestionarioListNew;
            persona.setPuntajeCuestionarioList(puntajeCuestionarioListNew);
            List<PersonaAsignatura> attachedPersonaAsignaturaListNew = new ArrayList<PersonaAsignatura>();
            for (PersonaAsignatura personaAsignaturaListNewPersonaAsignaturaToAttach : personaAsignaturaListNew) {
                personaAsignaturaListNewPersonaAsignaturaToAttach = em.getReference(personaAsignaturaListNewPersonaAsignaturaToAttach.getClass(), personaAsignaturaListNewPersonaAsignaturaToAttach.getIdPersonaA());
                attachedPersonaAsignaturaListNew.add(personaAsignaturaListNewPersonaAsignaturaToAttach);
            }
            personaAsignaturaListNew = attachedPersonaAsignaturaListNew;
            persona.setPersonaAsignaturaList(personaAsignaturaListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            persona.setUsuarioList(usuarioListNew);
            persona = em.merge(persona);
            for (PuntajeCuestionario puntajeCuestionarioListOldPuntajeCuestionario : puntajeCuestionarioListOld) {
                if (!puntajeCuestionarioListNew.contains(puntajeCuestionarioListOldPuntajeCuestionario)) {
                    puntajeCuestionarioListOldPuntajeCuestionario.setPcIdPersona(null);
                    puntajeCuestionarioListOldPuntajeCuestionario = em.merge(puntajeCuestionarioListOldPuntajeCuestionario);
                }
            }
            for (PuntajeCuestionario puntajeCuestionarioListNewPuntajeCuestionario : puntajeCuestionarioListNew) {
                if (!puntajeCuestionarioListOld.contains(puntajeCuestionarioListNewPuntajeCuestionario)) {
                    Persona oldPcIdPersonaOfPuntajeCuestionarioListNewPuntajeCuestionario = puntajeCuestionarioListNewPuntajeCuestionario.getPcIdPersona();
                    puntajeCuestionarioListNewPuntajeCuestionario.setPcIdPersona(persona);
                    puntajeCuestionarioListNewPuntajeCuestionario = em.merge(puntajeCuestionarioListNewPuntajeCuestionario);
                    if (oldPcIdPersonaOfPuntajeCuestionarioListNewPuntajeCuestionario != null && !oldPcIdPersonaOfPuntajeCuestionarioListNewPuntajeCuestionario.equals(persona)) {
                        oldPcIdPersonaOfPuntajeCuestionarioListNewPuntajeCuestionario.getPuntajeCuestionarioList().remove(puntajeCuestionarioListNewPuntajeCuestionario);
                        oldPcIdPersonaOfPuntajeCuestionarioListNewPuntajeCuestionario = em.merge(oldPcIdPersonaOfPuntajeCuestionarioListNewPuntajeCuestionario);
                    }
                }
            }
            for (PersonaAsignatura personaAsignaturaListOldPersonaAsignatura : personaAsignaturaListOld) {
                if (!personaAsignaturaListNew.contains(personaAsignaturaListOldPersonaAsignatura)) {
                    personaAsignaturaListOldPersonaAsignatura.setPaIdPersona(null);
                    personaAsignaturaListOldPersonaAsignatura = em.merge(personaAsignaturaListOldPersonaAsignatura);
                }
            }
            for (PersonaAsignatura personaAsignaturaListNewPersonaAsignatura : personaAsignaturaListNew) {
                if (!personaAsignaturaListOld.contains(personaAsignaturaListNewPersonaAsignatura)) {
                    Persona oldPaIdPersonaOfPersonaAsignaturaListNewPersonaAsignatura = personaAsignaturaListNewPersonaAsignatura.getPaIdPersona();
                    personaAsignaturaListNewPersonaAsignatura.setPaIdPersona(persona);
                    personaAsignaturaListNewPersonaAsignatura = em.merge(personaAsignaturaListNewPersonaAsignatura);
                    if (oldPaIdPersonaOfPersonaAsignaturaListNewPersonaAsignatura != null && !oldPaIdPersonaOfPersonaAsignaturaListNewPersonaAsignatura.equals(persona)) {
                        oldPaIdPersonaOfPersonaAsignaturaListNewPersonaAsignatura.getPersonaAsignaturaList().remove(personaAsignaturaListNewPersonaAsignatura);
                        oldPaIdPersonaOfPersonaAsignaturaListNewPersonaAsignatura = em.merge(oldPaIdPersonaOfPersonaAsignaturaListNewPersonaAsignatura);
                    }
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.setUsIdPersona(null);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Persona oldUsIdPersonaOfUsuarioListNewUsuario = usuarioListNewUsuario.getUsIdPersona();
                    usuarioListNewUsuario.setUsIdPersona(persona);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldUsIdPersonaOfUsuarioListNewUsuario != null && !oldUsIdPersonaOfUsuarioListNewUsuario.equals(persona)) {
                        oldUsIdPersonaOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldUsIdPersonaOfUsuarioListNewUsuario = em.merge(oldUsIdPersonaOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = persona.getIdPersona();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
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
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<PuntajeCuestionario> puntajeCuestionarioList = persona.getPuntajeCuestionarioList();
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionario : puntajeCuestionarioList) {
                puntajeCuestionarioListPuntajeCuestionario.setPcIdPersona(null);
                puntajeCuestionarioListPuntajeCuestionario = em.merge(puntajeCuestionarioListPuntajeCuestionario);
            }
            List<PersonaAsignatura> personaAsignaturaList = persona.getPersonaAsignaturaList();
            for (PersonaAsignatura personaAsignaturaListPersonaAsignatura : personaAsignaturaList) {
                personaAsignaturaListPersonaAsignatura.setPaIdPersona(null);
                personaAsignaturaListPersonaAsignatura = em.merge(personaAsignaturaListPersonaAsignatura);
            }
            List<Usuario> usuarioList = persona.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.setUsIdPersona(null);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
