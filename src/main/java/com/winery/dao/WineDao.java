package com.winery.dao;

import com.winery.entities.Wine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class WineDao implements Dao<Wine>{
    private final static Logger log = LogManager.getLogger(Wine.class);
    private final EntityManager entityManager;

    public WineDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Wine> get(int id) {
        return Optional.ofNullable(entityManager.find(Wine.class, id));
    }

    @Override
    public List<Wine> getAll() {
        TypedQuery<Wine> query = entityManager.createQuery("SELECT u FROM Wine u", Wine.class);
        return query.getResultList();
    }

    @Override
    public void save(Wine wine) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(wine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine save error: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(Wine wine, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            // Update user properties based on params
            // Example: user.setUsername(params[0]);
            // Update other properties as needed
            entityManager.merge(wine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine update error: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete(Wine wine) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(wine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine delete error: " + e.getMessage());
            }
        }
    }
}
