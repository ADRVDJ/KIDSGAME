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
public class CuestionarioJpaController implements Serializable {

    public CuestionarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuestionario cuestionario) throws PreexistingEntityException, Exception {
        if (cuestionario.getPreguntasList() == null) {
            cuestionario.setPreguntasList(new ArrayList<Preguntas>());
        }
        if (cuestionario.getPuntajeCuestionarioList() == null) {
            cuestionario.setPuntajeCuestionarioList(new ArrayList<PuntajeCuestionario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignatura crIdAsignatura = cuestionario.getCrIdAsignatura();
            if (crIdAsignatura != null) {
                crIdAsignatura = em.getReference(crIdAsignatura.getClass(), crIdAsignatura.getIdAsignatura());
                cuestionario.setCrIdAsignatura(crIdAsignatura);
            }
            Juegos crIdJuego = cuestionario.getCrIdJuego();
            if (crIdJuego != null) {
                crIdJuego = em.getReference(crIdJuego.getClass(), crIdJuego.getIdJuego());
                cuestionario.setCrIdJuego(crIdJuego);
            }
            List<Preguntas> attachedPreguntasList = new ArrayList<Preguntas>();
            for (Preguntas preguntasListPreguntasToAttach : cuestionario.getPreguntasList()) {
                preguntasListPreguntasToAttach = em.getReference(preguntasListPreguntasToAttach.getClass(), preguntasListPreguntasToAttach.getIdPregunta());
                attachedPreguntasList.add(preguntasListPreguntasToAttach);
            }
            cuestionario.setPreguntasList(attachedPreguntasList);
            List<PuntajeCuestionario> attachedPuntajeCuestionarioList = new ArrayList<PuntajeCuestionario>();
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionarioToAttach : cuestionario.getPuntajeCuestionarioList()) {
                puntajeCuestionarioListPuntajeCuestionarioToAttach = em.getReference(puntajeCuestionarioListPuntajeCuestionarioToAttach.getClass(), puntajeCuestionarioListPuntajeCuestionarioToAttach.getIdPuntaje());
                attachedPuntajeCuestionarioList.add(puntajeCuestionarioListPuntajeCuestionarioToAttach);
            }
            cuestionario.setPuntajeCuestionarioList(attachedPuntajeCuestionarioList);
            em.persist(cuestionario);
            if (crIdAsignatura != null) {
                crIdAsignatura.getCuestionarioList().add(cuestionario);
                crIdAsignatura = em.merge(crIdAsignatura);
            }
            if (crIdJuego != null) {
                crIdJuego.getCuestionarioList().add(cuestionario);
                crIdJuego = em.merge(crIdJuego);
            }
            for (Preguntas preguntasListPreguntas : cuestionario.getPreguntasList()) {
                Cuestionario oldPrIdCuestionarioOfPreguntasListPreguntas = preguntasListPreguntas.getPrIdCuestionario();
                preguntasListPreguntas.setPrIdCuestionario(cuestionario);
                preguntasListPreguntas = em.merge(preguntasListPreguntas);
                if (oldPrIdCuestionarioOfPreguntasListPreguntas != null) {
                    oldPrIdCuestionarioOfPreguntasListPreguntas.getPreguntasList().remove(preguntasListPreguntas);
                    oldPrIdCuestionarioOfPreguntasListPreguntas = em.merge(oldPrIdCuestionarioOfPreguntasListPreguntas);
                }
            }
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionario : cuestionario.getPuntajeCuestionarioList()) {
                Cuestionario oldPcIdCuestionarioOfPuntajeCuestionarioListPuntajeCuestionario = puntajeCuestionarioListPuntajeCuestionario.getPcIdCuestionario();
                puntajeCuestionarioListPuntajeCuestionario.setPcIdCuestionario(cuestionario);
                puntajeCuestionarioListPuntajeCuestionario = em.merge(puntajeCuestionarioListPuntajeCuestionario);
                if (oldPcIdCuestionarioOfPuntajeCuestionarioListPuntajeCuestionario != null) {
                    oldPcIdCuestionarioOfPuntajeCuestionarioListPuntajeCuestionario.getPuntajeCuestionarioList().remove(puntajeCuestionarioListPuntajeCuestionario);
                    oldPcIdCuestionarioOfPuntajeCuestionarioListPuntajeCuestionario = em.merge(oldPcIdCuestionarioOfPuntajeCuestionarioListPuntajeCuestionario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCuestionario(cuestionario.getIdCuestionario()) != null) {
                throw new PreexistingEntityException("Cuestionario " + cuestionario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuestionario cuestionario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuestionario persistentCuestionario = em.find(Cuestionario.class, cuestionario.getIdCuestionario());
            Asignatura crIdAsignaturaOld = persistentCuestionario.getCrIdAsignatura();
            Asignatura crIdAsignaturaNew = cuestionario.getCrIdAsignatura();
            Juegos crIdJuegoOld = persistentCuestionario.getCrIdJuego();
            Juegos crIdJuegoNew = cuestionario.getCrIdJuego();
            List<Preguntas> preguntasListOld = persistentCuestionario.getPreguntasList();
            List<Preguntas> preguntasListNew = cuestionario.getPreguntasList();
            List<PuntajeCuestionario> puntajeCuestionarioListOld = persistentCuestionario.getPuntajeCuestionarioList();
            List<PuntajeCuestionario> puntajeCuestionarioListNew = cuestionario.getPuntajeCuestionarioList();
            if (crIdAsignaturaNew != null) {
                crIdAsignaturaNew = em.getReference(crIdAsignaturaNew.getClass(), crIdAsignaturaNew.getIdAsignatura());
                cuestionario.setCrIdAsignatura(crIdAsignaturaNew);
            }
            if (crIdJuegoNew != null) {
                crIdJuegoNew = em.getReference(crIdJuegoNew.getClass(), crIdJuegoNew.getIdJuego());
                cuestionario.setCrIdJuego(crIdJuegoNew);
            }
            List<Preguntas> attachedPreguntasListNew = new ArrayList<Preguntas>();
            for (Preguntas preguntasListNewPreguntasToAttach : preguntasListNew) {
                preguntasListNewPreguntasToAttach = em.getReference(preguntasListNewPreguntasToAttach.getClass(), preguntasListNewPreguntasToAttach.getIdPregunta());
                attachedPreguntasListNew.add(preguntasListNewPreguntasToAttach);
            }
            preguntasListNew = attachedPreguntasListNew;
            cuestionario.setPreguntasList(preguntasListNew);
            List<PuntajeCuestionario> attachedPuntajeCuestionarioListNew = new ArrayList<PuntajeCuestionario>();
            for (PuntajeCuestionario puntajeCuestionarioListNewPuntajeCuestionarioToAttach : puntajeCuestionarioListNew) {
                puntajeCuestionarioListNewPuntajeCuestionarioToAttach = em.getReference(puntajeCuestionarioListNewPuntajeCuestionarioToAttach.getClass(), puntajeCuestionarioListNewPuntajeCuestionarioToAttach.getIdPuntaje());
                attachedPuntajeCuestionarioListNew.add(puntajeCuestionarioListNewPuntajeCuestionarioToAttach);
            }
            puntajeCuestionarioListNew = attachedPuntajeCuestionarioListNew;
            cuestionario.setPuntajeCuestionarioList(puntajeCuestionarioListNew);
            cuestionario = em.merge(cuestionario);
            if (crIdAsignaturaOld != null && !crIdAsignaturaOld.equals(crIdAsignaturaNew)) {
                crIdAsignaturaOld.getCuestionarioList().remove(cuestionario);
                crIdAsignaturaOld = em.merge(crIdAsignaturaOld);
            }
            if (crIdAsignaturaNew != null && !crIdAsignaturaNew.equals(crIdAsignaturaOld)) {
                crIdAsignaturaNew.getCuestionarioList().add(cuestionario);
                crIdAsignaturaNew = em.merge(crIdAsignaturaNew);
            }
            if (crIdJuegoOld != null && !crIdJuegoOld.equals(crIdJuegoNew)) {
                crIdJuegoOld.getCuestionarioList().remove(cuestionario);
                crIdJuegoOld = em.merge(crIdJuegoOld);
            }
            if (crIdJuegoNew != null && !crIdJuegoNew.equals(crIdJuegoOld)) {
                crIdJuegoNew.getCuestionarioList().add(cuestionario);
                crIdJuegoNew = em.merge(crIdJuegoNew);
            }
            for (Preguntas preguntasListOldPreguntas : preguntasListOld) {
                if (!preguntasListNew.contains(preguntasListOldPreguntas)) {
                    preguntasListOldPreguntas.setPrIdCuestionario(null);
                    preguntasListOldPreguntas = em.merge(preguntasListOldPreguntas);
                }
            }
            for (Preguntas preguntasListNewPreguntas : preguntasListNew) {
                if (!preguntasListOld.contains(preguntasListNewPreguntas)) {
                    Cuestionario oldPrIdCuestionarioOfPreguntasListNewPreguntas = preguntasListNewPreguntas.getPrIdCuestionario();
                    preguntasListNewPreguntas.setPrIdCuestionario(cuestionario);
                    preguntasListNewPreguntas = em.merge(preguntasListNewPreguntas);
                    if (oldPrIdCuestionarioOfPreguntasListNewPreguntas != null && !oldPrIdCuestionarioOfPreguntasListNewPreguntas.equals(cuestionario)) {
                        oldPrIdCuestionarioOfPreguntasListNewPreguntas.getPreguntasList().remove(preguntasListNewPreguntas);
                        oldPrIdCuestionarioOfPreguntasListNewPreguntas = em.merge(oldPrIdCuestionarioOfPreguntasListNewPreguntas);
                    }
                }
            }
            for (PuntajeCuestionario puntajeCuestionarioListOldPuntajeCuestionario : puntajeCuestionarioListOld) {
                if (!puntajeCuestionarioListNew.contains(puntajeCuestionarioListOldPuntajeCuestionario)) {
                    puntajeCuestionarioListOldPuntajeCuestionario.setPcIdCuestionario(null);
                    puntajeCuestionarioListOldPuntajeCuestionario = em.merge(puntajeCuestionarioListOldPuntajeCuestionario);
                }
            }
            for (PuntajeCuestionario puntajeCuestionarioListNewPuntajeCuestionario : puntajeCuestionarioListNew) {
                if (!puntajeCuestionarioListOld.contains(puntajeCuestionarioListNewPuntajeCuestionario)) {
                    Cuestionario oldPcIdCuestionarioOfPuntajeCuestionarioListNewPuntajeCuestionario = puntajeCuestionarioListNewPuntajeCuestionario.getPcIdCuestionario();
                    puntajeCuestionarioListNewPuntajeCuestionario.setPcIdCuestionario(cuestionario);
                    puntajeCuestionarioListNewPuntajeCuestionario = em.merge(puntajeCuestionarioListNewPuntajeCuestionario);
                    if (oldPcIdCuestionarioOfPuntajeCuestionarioListNewPuntajeCuestionario != null && !oldPcIdCuestionarioOfPuntajeCuestionarioListNewPuntajeCuestionario.equals(cuestionario)) {
                        oldPcIdCuestionarioOfPuntajeCuestionarioListNewPuntajeCuestionario.getPuntajeCuestionarioList().remove(puntajeCuestionarioListNewPuntajeCuestionario);
                        oldPcIdCuestionarioOfPuntajeCuestionarioListNewPuntajeCuestionario = em.merge(oldPcIdCuestionarioOfPuntajeCuestionarioListNewPuntajeCuestionario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = cuestionario.getIdCuestionario();
                if (findCuestionario(id) == null) {
                    throw new NonexistentEntityException("The cuestionario with id " + id + " no longer exists.");
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
            Cuestionario cuestionario;
            try {
                cuestionario = em.getReference(Cuestionario.class, id);
                cuestionario.getIdCuestionario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuestionario with id " + id + " no longer exists.", enfe);
            }
            Asignatura crIdAsignatura = cuestionario.getCrIdAsignatura();
            if (crIdAsignatura != null) {
                crIdAsignatura.getCuestionarioList().remove(cuestionario);
                crIdAsignatura = em.merge(crIdAsignatura);
            }
            Juegos crIdJuego = cuestionario.getCrIdJuego();
            if (crIdJuego != null) {
                crIdJuego.getCuestionarioList().remove(cuestionario);
                crIdJuego = em.merge(crIdJuego);
            }
            List<Preguntas> preguntasList = cuestionario.getPreguntasList();
            for (Preguntas preguntasListPreguntas : preguntasList) {
                preguntasListPreguntas.setPrIdCuestionario(null);
                preguntasListPreguntas = em.merge(preguntasListPreguntas);
            }
            List<PuntajeCuestionario> puntajeCuestionarioList = cuestionario.getPuntajeCuestionarioList();
            for (PuntajeCuestionario puntajeCuestionarioListPuntajeCuestionario : puntajeCuestionarioList) {
                puntajeCuestionarioListPuntajeCuestionario.setPcIdCuestionario(null);
                puntajeCuestionarioListPuntajeCuestionario = em.merge(puntajeCuestionarioListPuntajeCuestionario);
            }
            em.remove(cuestionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cuestionario> findCuestionarioEntities() {
        return findCuestionarioEntities(true, -1, -1);
    }

    public List<Cuestionario> findCuestionarioEntities(int maxResults, int firstResult) {
        return findCuestionarioEntities(false, maxResults, firstResult);
    }

    private List<Cuestionario> findCuestionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuestionario.class));
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

    public Cuestionario findCuestionario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuestionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuestionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuestionario> rt = cq.from(Cuestionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
