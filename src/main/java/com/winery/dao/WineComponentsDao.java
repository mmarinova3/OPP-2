package com.winery.dao;

import com.winery.entities.WineComponents;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class WineComponentsDao implements Dao<WineComponents>{

    private final static Logger log = LogManager.getLogger(WineComponents.class);
    private final EntityManager entityManager;

    public WineComponentsDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<WineComponents> get(int id) {
        return Optional.ofNullable(entityManager.find(WineComponents.class, id));
    }

    @Override
    public List<WineComponents> getAll() {
        TypedQuery<WineComponents> query = entityManager.createQuery("SELECT u FROM WineComponents u", WineComponents.class);
        return query.getResultList();
    }

    @Override
    public void save(WineComponents wineComponents) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(wineComponents);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine composition save error: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(WineComponents wineComponents, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            // Update user properties based on params
            // Example: user.setUsername(params[0]);
            // Update other properties as needed
            entityManager.merge(wineComponents);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine composition update error: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete(WineComponents wineComponents) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(wineComponents);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine composition delete error: " + e.getMessage());
            }
        }
    }
}
