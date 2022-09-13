/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kidsgames;

import java.sql.Connection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ASUS TUF GAMING
 */
public class ManagerFactory {
    
    private EntityManagerFactory emf = null;

    public EntityManagerFactory getentityManagerFactory() {
        return emf = Persistence.createEntityManagerFactory("proyecto_productiPU");
    }

    public static final Connection getConnection(final EntityManager entityManager) {
        entityManager.getTransaction().begin();
        Connection connection = entityManager.unwrap(java.sql.Connection.class);
        return connection;

    }
}
