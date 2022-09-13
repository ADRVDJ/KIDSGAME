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
public class AsignaturaJpaController implements Serializable {

    public AsignaturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignatura asignatura) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        PersonaAsignatura personaAsignaturaOrphanCheck = asignatura.getPersonaAsignatura();
        if (personaAsignaturaOrphanCheck != null) {
            Asignatura oldAsignaturaOfPersonaAsignatura = personaAsignaturaOrphanCheck.getAsignatura();
            if (oldAsignaturaOfPersonaAsignatura != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The PersonaAsignatura " + personaAsignaturaOrphanCheck + " already has an item of type Asignatura whose personaAsignatura column cannot be null. Please make another selection for the personaAsignatura field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AnioLectivo anioLectivo = asignatura.getAnioLectivo();
            if (anioLectivo != null) {
                anioLectivo = em.getReference(anioLectivo.getClass(), anioLectivo.getIdAnioLectivo());
                asignatura.setAnioLectivo(anioLectivo);
            }
            PersonaAsignatura personaAsignatura = asignatura.getPersonaAsignatura();
            if (personaAsignatura != null) {
                personaAsignatura = em.getReference(personaAsignatura.getClass(), personaAsignatura.getIdUsuarioA());
                asignatura.setPersonaAsignatura(personaAsignatura);
            }
            em.persist(asignatura);
            if (anioLectivo != null) {
                Asignatura oldAsignaturaOfAnioLectivo = anioLectivo.getAsignatura();
                if (oldAsignaturaOfAnioLectivo != null) {
                    oldAsignaturaOfAnioLectivo.setAnioLectivo(null);
                    oldAsignaturaOfAnioLectivo = em.merge(oldAsignaturaOfAnioLectivo);
                }
                anioLectivo.setAsignatura(asignatura);
                anioLectivo = em.merge(anioLectivo);
            }
            if (personaAsignatura != null) {
                personaAsignatura.setAsignatura(asignatura);
                personaAsignatura = em.merge(personaAsignatura);
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

    public void edit(Asignatura asignatura) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignatura persistentAsignatura = em.find(Asignatura.class, asignatura.getIdAsignatura());
            AnioLectivo anioLectivoOld = persistentAsignatura.getAnioLectivo();
            AnioLectivo anioLectivoNew = asignatura.getAnioLectivo();
            PersonaAsignatura personaAsignaturaOld = persistentAsignatura.getPersonaAsignatura();
            PersonaAsignatura personaAsignaturaNew = asignatura.getPersonaAsignatura();
            List<String> illegalOrphanMessages = null;
            if (anioLectivoOld != null && !anioLectivoOld.equals(anioLectivoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain AnioLectivo " + anioLectivoOld + " since its asignatura field is not nullable.");
            }
            if (personaAsignaturaNew != null && !personaAsignaturaNew.equals(personaAsignaturaOld)) {
                Asignatura oldAsignaturaOfPersonaAsignatura = personaAsignaturaNew.getAsignatura();
                if (oldAsignaturaOfPersonaAsignatura != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The PersonaAsignatura " + personaAsignaturaNew + " already has an item of type Asignatura whose personaAsignatura column cannot be null. Please make another selection for the personaAsignatura field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (anioLectivoNew != null) {
                anioLectivoNew = em.getReference(anioLectivoNew.getClass(), anioLectivoNew.getIdAnioLectivo());
                asignatura.setAnioLectivo(anioLectivoNew);
            }
            if (personaAsignaturaNew != null) {
                personaAsignaturaNew = em.getReference(personaAsignaturaNew.getClass(), personaAsignaturaNew.getIdUsuarioA());
                asignatura.setPersonaAsignatura(personaAsignaturaNew);
            }
            asignatura = em.merge(asignatura);
            if (anioLectivoNew != null && !anioLectivoNew.equals(anioLectivoOld)) {
                Asignatura oldAsignaturaOfAnioLectivo = anioLectivoNew.getAsignatura();
                if (oldAsignaturaOfAnioLectivo != null) {
                    oldAsignaturaOfAnioLectivo.setAnioLectivo(null);
                    oldAsignaturaOfAnioLectivo = em.merge(oldAsignaturaOfAnioLectivo);
                }
                anioLectivoNew.setAsignatura(asignatura);
                anioLectivoNew = em.merge(anioLectivoNew);
            }
            if (personaAsignaturaOld != null && !personaAsignaturaOld.equals(personaAsignaturaNew)) {
                personaAsignaturaOld.setAsignatura(null);
                personaAsignaturaOld = em.merge(personaAsignaturaOld);
            }
            if (personaAsignaturaNew != null && !personaAsignaturaNew.equals(personaAsignaturaOld)) {
                personaAsignaturaNew.setAsignatura(asignatura);
                personaAsignaturaNew = em.merge(personaAsignaturaNew);
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

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            AnioLectivo anioLectivoOrphanCheck = asignatura.getAnioLectivo();
            if (anioLectivoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Asignatura (" + asignatura + ") cannot be destroyed since the AnioLectivo " + anioLectivoOrphanCheck + " in its anioLectivo field has a non-nullable asignatura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PersonaAsignatura personaAsignatura = asignatura.getPersonaAsignatura();
            if (personaAsignatura != null) {
                personaAsignatura.setAsignatura(null);
                personaAsignatura = em.merge(personaAsignatura);
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
