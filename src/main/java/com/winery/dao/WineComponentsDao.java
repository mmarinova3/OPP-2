package com.winery.dao;

import com.winery.entities.WineComponents;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class WineComponentsDao implements Dao<WineComponents> {

    private final static Logger log = LogManager.getLogger(WineComponents.class);
    private final EntityManager entityManager;

    public WineComponentsDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<WineComponents> get(int id) {
        try {
            return Optional.ofNullable(entityManager.find(WineComponents.class, id));
        } catch (Exception e) {
            log.error("Wine composition get error: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<WineComponents> getAll() {
        try {
            TypedQuery<WineComponents> query = entityManager.createQuery("SELECT u FROM WineComponents u", WineComponents.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Wine composition getAll error: " + e.getMessage(), e);
            return List.of();
        }
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
                log.error("Wine composition save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(WineComponents wineComponents, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(wineComponents);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine composition update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(WineComponents wineComponents) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(wineComponents);
            entityManager.flush();
            transaction.commit();
        } catch (PersistenceException e) {
            throw e;
        } catch (Throwable e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine composition delete error: " + e.getMessage(), e);
            }
        }
    }

    public List<WineComponents> findComponents(String compositionName){
        try {
            TypedQuery<WineComponents> query = entityManager.createQuery("SELECT r.grape, r.quantityNeeded FROM WineComponents r WHERE r.wineName = :wineName", WineComponents.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Wine composition get components error: " + e.getMessage(), e);
            return List.of();
        }
    }

}
