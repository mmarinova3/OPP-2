package com.winery.dao;

import com.winery.entities.WineComposition;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class WineCompositionDao implements Dao<WineComposition>{
    private final static Logger log = LogManager.getLogger(WineComposition.class);
    private final EntityManager entityManager;

    public WineCompositionDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<WineComposition> get(int id) {
        return Optional.ofNullable(entityManager.find(WineComposition.class, id));
    }

    @Override
    public List<WineComposition> getAll() {
        TypedQuery<WineComposition> query = entityManager.createQuery("SELECT u FROM WineComposition u", WineComposition.class);
        return query.getResultList();
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
                log.error("Wine save error: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(WineComposition wineComposition, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            // Update user properties based on params
            // Example: user.setUsername(params[0]);
            // Update other properties as needed
            entityManager.merge(wineComposition);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine update error: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete(WineComposition wineComposition) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(wineComposition);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine delete error: " + e.getMessage());
            }
        }
    }

    public Integer findIdByName(String compositionName) {
        Query query = entityManager.createQuery("SELECT r.id FROM WineComposition r WHERE r.wineName = :wineName");
        query.setParameter("wineName", compositionName);

        try {
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
