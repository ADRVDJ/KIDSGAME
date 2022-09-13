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
public class JuegosJpaController implements Serializable {

    public JuegosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Juegos juegos) throws PreexistingEntityException, Exception {
        if (juegos.getCuestionarioList() == null) {
            juegos.setCuestionarioList(new ArrayList<Cuestionario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cuestionario> attachedCuestionarioList = new ArrayList<Cuestionario>();
            for (Cuestionario cuestionarioListCuestionarioToAttach : juegos.getCuestionarioList()) {
                cuestionarioListCuestionarioToAttach = em.getReference(cuestionarioListCuestionarioToAttach.getClass(), cuestionarioListCuestionarioToAttach.getIdCuestionario());
                attachedCuestionarioList.add(cuestionarioListCuestionarioToAttach);
            }
            juegos.setCuestionarioList(attachedCuestionarioList);
            em.persist(juegos);
            for (Cuestionario cuestionarioListCuestionario : juegos.getCuestionarioList()) {
                Juegos oldCrIdJuegoOfCuestionarioListCuestionario = cuestionarioListCuestionario.getCrIdJuego();
                cuestionarioListCuestionario.setCrIdJuego(juegos);
                cuestionarioListCuestionario = em.merge(cuestionarioListCuestionario);
                if (oldCrIdJuegoOfCuestionarioListCuestionario != null) {
                    oldCrIdJuegoOfCuestionarioListCuestionario.getCuestionarioList().remove(cuestionarioListCuestionario);
                    oldCrIdJuegoOfCuestionarioListCuestionario = em.merge(oldCrIdJuegoOfCuestionarioListCuestionario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJuegos(juegos.getIdJuego()) != null) {
                throw new PreexistingEntityException("Juegos " + juegos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Juegos juegos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juegos persistentJuegos = em.find(Juegos.class, juegos.getIdJuego());
            List<Cuestionario> cuestionarioListOld = persistentJuegos.getCuestionarioList();
            List<Cuestionario> cuestionarioListNew = juegos.getCuestionarioList();
            List<Cuestionario> attachedCuestionarioListNew = new ArrayList<Cuestionario>();
            for (Cuestionario cuestionarioListNewCuestionarioToAttach : cuestionarioListNew) {
                cuestionarioListNewCuestionarioToAttach = em.getReference(cuestionarioListNewCuestionarioToAttach.getClass(), cuestionarioListNewCuestionarioToAttach.getIdCuestionario());
                attachedCuestionarioListNew.add(cuestionarioListNewCuestionarioToAttach);
            }
            cuestionarioListNew = attachedCuestionarioListNew;
            juegos.setCuestionarioList(cuestionarioListNew);
            juegos = em.merge(juegos);
            for (Cuestionario cuestionarioListOldCuestionario : cuestionarioListOld) {
                if (!cuestionarioListNew.contains(cuestionarioListOldCuestionario)) {
                    cuestionarioListOldCuestionario.setCrIdJuego(null);
                    cuestionarioListOldCuestionario = em.merge(cuestionarioListOldCuestionario);
                }
            }
            for (Cuestionario cuestionarioListNewCuestionario : cuestionarioListNew) {
                if (!cuestionarioListOld.contains(cuestionarioListNewCuestionario)) {
                    Juegos oldCrIdJuegoOfCuestionarioListNewCuestionario = cuestionarioListNewCuestionario.getCrIdJuego();
                    cuestionarioListNewCuestionario.setCrIdJuego(juegos);
                    cuestionarioListNewCuestionario = em.merge(cuestionarioListNewCuestionario);
                    if (oldCrIdJuegoOfCuestionarioListNewCuestionario != null && !oldCrIdJuegoOfCuestionarioListNewCuestionario.equals(juegos)) {
                        oldCrIdJuegoOfCuestionarioListNewCuestionario.getCuestionarioList().remove(cuestionarioListNewCuestionario);
                        oldCrIdJuegoOfCuestionarioListNewCuestionario = em.merge(oldCrIdJuegoOfCuestionarioListNewCuestionario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = juegos.getIdJuego();
                if (findJuegos(id) == null) {
                    throw new NonexistentEntityException("The juegos with id " + id + " no longer exists.");
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
            Juegos juegos;
            try {
                juegos = em.getReference(Juegos.class, id);
                juegos.getIdJuego();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The juegos with id " + id + " no longer exists.", enfe);
            }
            List<Cuestionario> cuestionarioList = juegos.getCuestionarioList();
            for (Cuestionario cuestionarioListCuestionario : cuestionarioList) {
                cuestionarioListCuestionario.setCrIdJuego(null);
                cuestionarioListCuestionario = em.merge(cuestionarioListCuestionario);
            }
            em.remove(juegos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Juegos> findJuegosEntities() {
        return findJuegosEntities(true, -1, -1);
    }

    public List<Juegos> findJuegosEntities(int maxResults, int firstResult) {
        return findJuegosEntities(false, maxResults, firstResult);
    }

    private List<Juegos> findJuegosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Juegos.class));
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

    public Juegos findJuegos(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Juegos.class, id);
        } finally {
            em.close();
        }
    }

    public int getJuegosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Juegos> rt = cq.from(Juegos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
