package com.winery.utils;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection {
    private static final Logger log = LogManager.getLogger(Connection.class);
    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
        } catch (Throwable ex) {
            log.error("Initial EntityManagerFactory creation failed: " + ex);
        }
    }

    public static EntityManager getEntityManager() {
        try {
            if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
                entityManagerFactory = Persistence.createEntityManagerFactory("default");
            }
            return entityManagerFactory.createEntityManager();
        } catch (PersistenceException ex) {
            log.error("Error creating EntityManager: " + ex.getMessage());
            throw ex;
        } catch (Throwable ex) {
            log.error("Unexpected error creating EntityManager: " + ex.getMessage());
            throw new RuntimeException("Error creating EntityManager", ex);
        }
    }

    public static void closeEMF() {
        entityManagerFactory.close();
    }
}
