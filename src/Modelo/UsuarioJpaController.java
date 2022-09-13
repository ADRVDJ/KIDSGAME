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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ASUS TUF GAMING
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona usIdPersona = usuario.getUsIdPersona();
            if (usIdPersona != null) {
                usIdPersona = em.getReference(usIdPersona.getClass(), usIdPersona.getIdPersona());
                usuario.setUsIdPersona(usIdPersona);
            }
            Rol usIdRol = usuario.getUsIdRol();
            if (usIdRol != null) {
                usIdRol = em.getReference(usIdRol.getClass(), usIdRol.getIdRol());
                usuario.setUsIdRol(usIdRol);
            }
            em.persist(usuario);
            if (usIdPersona != null) {
                usIdPersona.getUsuarioList().add(usuario);
                usIdPersona = em.merge(usIdPersona);
            }
            if (usIdRol != null) {
                usIdRol.getUsuarioList().add(usuario);
                usIdRol = em.merge(usIdRol);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Persona usIdPersonaOld = persistentUsuario.getUsIdPersona();
            Persona usIdPersonaNew = usuario.getUsIdPersona();
            Rol usIdRolOld = persistentUsuario.getUsIdRol();
            Rol usIdRolNew = usuario.getUsIdRol();
            if (usIdPersonaNew != null) {
                usIdPersonaNew = em.getReference(usIdPersonaNew.getClass(), usIdPersonaNew.getIdPersona());
                usuario.setUsIdPersona(usIdPersonaNew);
            }
            if (usIdRolNew != null) {
                usIdRolNew = em.getReference(usIdRolNew.getClass(), usIdRolNew.getIdRol());
                usuario.setUsIdRol(usIdRolNew);
            }
            usuario = em.merge(usuario);
            if (usIdPersonaOld != null && !usIdPersonaOld.equals(usIdPersonaNew)) {
                usIdPersonaOld.getUsuarioList().remove(usuario);
                usIdPersonaOld = em.merge(usIdPersonaOld);
            }
            if (usIdPersonaNew != null && !usIdPersonaNew.equals(usIdPersonaOld)) {
                usIdPersonaNew.getUsuarioList().add(usuario);
                usIdPersonaNew = em.merge(usIdPersonaNew);
            }
            if (usIdRolOld != null && !usIdRolOld.equals(usIdRolNew)) {
                usIdRolOld.getUsuarioList().remove(usuario);
                usIdRolOld = em.merge(usIdRolOld);
            }
            if (usIdRolNew != null && !usIdRolNew.equals(usIdRolOld)) {
                usIdRolNew.getUsuarioList().add(usuario);
                usIdRolNew = em.merge(usIdRolNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Persona usIdPersona = usuario.getUsIdPersona();
            if (usIdPersona != null) {
                usIdPersona.getUsuarioList().remove(usuario);
                usIdPersona = em.merge(usIdPersona);
            }
            Rol usIdRol = usuario.getUsIdRol();
            if (usIdRol != null) {
                usIdRol.getUsuarioList().remove(usuario);
                usIdRol = em.merge(usIdRol);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public Usuario buscarByCredenciales(String us, String pasw, String rol) {
        Usuario u = null;
        for (Usuario usuario : findUsuarioEntities()) {
             if (usuario.getUsUsuario().equals(us) && usuario.getUsContraseña().equals(pasw) && usuario.getUsPermisos().equals(rol)) {
                u = usuario;
             }
        }
        return u;
    } 
}
