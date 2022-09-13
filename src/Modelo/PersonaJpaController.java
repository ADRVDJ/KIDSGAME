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
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (persona.getPuntajeCuestionarioList() == null) {
            persona.setPuntajeCuestionarioList(new ArrayList<PuntajeCuestionario>());
        }
        List<String> illegalOrphanMessages = null;
        Usuario usuarioOrphanCheck = persona.getUsuario();
        if (usuarioOrphanCheck != null) {
            Persona oldPersonaOfUsuario = usuarioOrphanCheck.getPersona();
            if (oldPersonaOfUsuario != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Usuario " + usuarioOrphanCheck + " already has an item of type Persona whose usuario column cannot be null. Please make another selection for the usuario field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = persona.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                persona.setUsuario(usuario);
            }
            PersonaAsignatura personaAsignatura = persona.getPersonaAsignatura();
            if (personaAsignatura != null) {
                personaAsignatura = em.getReference(personaAsignatura.getClass(), personaAsignatura.getIdUsuarioA());
                persona.setPersonaAsignatura(personaAsignatura);
            }
            List<PuntajeCuestionario> attachedPuntajeCuestionarioList = new ArrayList<PuntajeCuestionario>();
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionarioToAttach : persona.getPuntajeCuestionarioList()) {
                puntajeCuestionarioListPuntajeCuestionarioToAttach = em.getReference(puntajeCuestionarioListPuntajeCuestionarioToAttach.getClass(), puntajeCuestionarioListPuntajeCuestionarioToAttach.getIdPuntaje());
                attachedPuntajeCuestionarioList.add(puntajeCuestionarioListPuntajeCuestionarioToAttach);
            }
            persona.setPuntajeCuestionarioList(attachedPuntajeCuestionarioList);
            em.persist(persona);
            if (usuario != null) {
                usuario.setPersona(persona);
                usuario = em.merge(usuario);
            }
            if (personaAsignatura != null) {
                Persona oldPersonaOfPersonaAsignatura = personaAsignatura.getPersona();
                if (oldPersonaOfPersonaAsignatura != null) {
                    oldPersonaOfPersonaAsignatura.setPersonaAsignatura(null);
                    oldPersonaOfPersonaAsignatura = em.merge(oldPersonaOfPersonaAsignatura);
                }
                personaAsignatura.setPersona(persona);
                personaAsignatura = em.merge(personaAsignatura);
            }
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionario : persona.getPuntajeCuestionarioList()) {
                Persona oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario = puntajeCuestionarioListPuntajeCuestionario.getPcIdPersona();
                puntajeCuestionarioListPuntajeCuestionario.setPcIdPersona(persona);
                puntajeCuestionarioListPuntajeCuestionario = em.merge(puntajeCuestionarioListPuntajeCuestionario);
                if (oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario != null) {
                    oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario.getPuntajeCuestionarioList().remove(puntajeCuestionarioListPuntajeCuestionario);
                    oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario = em.merge(oldPcIdPersonaOfPuntajeCuestionarioListPuntajeCuestionario);
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

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getIdPersona());
            Usuario usuarioOld = persistentPersona.getUsuario();
            Usuario usuarioNew = persona.getUsuario();
            PersonaAsignatura personaAsignaturaOld = persistentPersona.getPersonaAsignatura();
            PersonaAsignatura personaAsignaturaNew = persona.getPersonaAsignatura();
            List<PuntajeCuestionario> puntajeCuestionarioListOld = persistentPersona.getPuntajeCuestionarioList();
            List<PuntajeCuestionario> puntajeCuestionarioListNew = persona.getPuntajeCuestionarioList();
            List<String> illegalOrphanMessages = null;
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                Persona oldPersonaOfUsuario = usuarioNew.getPersona();
                if (oldPersonaOfUsuario != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuario " + usuarioNew + " already has an item of type Persona whose usuario column cannot be null. Please make another selection for the usuario field.");
                }
            }
            if (personaAsignaturaOld != null && !personaAsignaturaOld.equals(personaAsignaturaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain PersonaAsignatura " + personaAsignaturaOld + " since its persona field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                persona.setUsuario(usuarioNew);
            }
            if (personaAsignaturaNew != null) {
                personaAsignaturaNew = em.getReference(personaAsignaturaNew.getClass(), personaAsignaturaNew.getIdUsuarioA());
                persona.setPersonaAsignatura(personaAsignaturaNew);
            }
            List<PuntajeCuestionario> attachedPuntajeCuestionarioListNew = new ArrayList<PuntajeCuestionario>();
            for (PuntajeCuestionario puntajeCuestionarioListNewPuntajeCuestionarioToAttach : puntajeCuestionarioListNew) {
                puntajeCuestionarioListNewPuntajeCuestionarioToAttach = em.getReference(puntajeCuestionarioListNewPuntajeCuestionarioToAttach.getClass(), puntajeCuestionarioListNewPuntajeCuestionarioToAttach.getIdPuntaje());
                attachedPuntajeCuestionarioListNew.add(puntajeCuestionarioListNewPuntajeCuestionarioToAttach);
            }
            puntajeCuestionarioListNew = attachedPuntajeCuestionarioListNew;
            persona.setPuntajeCuestionarioList(puntajeCuestionarioListNew);
            persona = em.merge(persona);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.setPersona(null);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.setPersona(persona);
                usuarioNew = em.merge(usuarioNew);
            }
            if (personaAsignaturaNew != null && !personaAsignaturaNew.equals(personaAsignaturaOld)) {
                Persona oldPersonaOfPersonaAsignatura = personaAsignaturaNew.getPersona();
                if (oldPersonaOfPersonaAsignatura != null) {
                    oldPersonaOfPersonaAsignatura.setPersonaAsignatura(null);
                    oldPersonaOfPersonaAsignatura = em.merge(oldPersonaOfPersonaAsignatura);
                }
                personaAsignaturaNew.setPersona(persona);
                personaAsignaturaNew = em.merge(personaAsignaturaNew);
            }
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

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            PersonaAsignatura personaAsignaturaOrphanCheck = persona.getPersonaAsignatura();
            if (personaAsignaturaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the PersonaAsignatura " + personaAsignaturaOrphanCheck + " in its personaAsignatura field has a non-nullable persona field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuario = persona.getUsuario();
            if (usuario != null) {
                usuario.setPersona(null);
                usuario = em.merge(usuario);
            }
            List<PuntajeCuestionario> puntajeCuestionarioList = persona.getPuntajeCuestionarioList();
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionario : puntajeCuestionarioList) {
                puntajeCuestionarioListPuntajeCuestionario.setPcIdPersona(null);
                puntajeCuestionarioListPuntajeCuestionario = em.merge(puntajeCuestionarioListPuntajeCuestionario);
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
