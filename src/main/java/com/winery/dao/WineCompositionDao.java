package com.winery.dao;

import com.winery.entities.WineComposition;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class WineCompositionDao implements Dao<WineComposition> {
    private final static Logger log = LogManager.getLogger(WineComposition.class);
    private final EntityManager entityManager;

    public WineCompositionDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<WineComposition> get(int id) {
        try {
            return Optional.ofNullable(entityManager.find(WineComposition.class, id));
        } catch (Exception e) {
            log.error("Wine composition get error: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<WineComposition> getAll() {
        try {
            TypedQuery<WineComposition> query = entityManager.createQuery("SELECT u FROM WineComposition u", WineComposition.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Wine composition getAll error: " + e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void save(WineComposition wineComposition) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(wineComposition);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine composition save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(WineComposition wineComposition, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(wineComposition);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine composition update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(WineComposition wineComposition) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(wineComposition);
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

    public Integer findIdByName(String compositionName) {
        try {
            Query query = entityManager.createQuery("SELECT r.id FROM WineComposition r WHERE r.wineName = :wineName");
            query.setParameter("wineName", compositionName);
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            log.error("Error finding WineComposition ID by name: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error during finding WineComposition ID by name: " + e.getMessage(), e);
            return null;
        }
    }


}
