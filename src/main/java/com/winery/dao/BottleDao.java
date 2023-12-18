package com.winery.dao;

import com.winery.entities.Bottle;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BottleDao implements Dao<Bottle>{
    private final static Logger log = LogManager.getLogger(Bottle.class);
    private final EntityManager entityManager;

    public BottleDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Bottle> get(int id) {
        return Optional.ofNullable(entityManager.find(Bottle.class, id));
    }

    @Override
    public List<Bottle> getAll() {
        TypedQuery<Bottle> query = entityManager.createQuery("SELECT u FROM Bottle u", Bottle.class);
        return query.getResultList();
    }

    @Override
    public void save(Bottle bottle) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(bottle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Bottle save error: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(Bottle bottle, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            // Update user properties based on params
            // Example: user.setUsername(params[0]);
            // Update other properties as needed
            entityManager.merge(bottle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Bottle update error: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete(Bottle bottle) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(bottle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Bottle delete error: " + e.getMessage());
            }
        }
    }

    public boolean existsByVolume(double volume) {
        String queryStr = "SELECT COUNT(b) FROM Bottle b WHERE b.volume = :volume";
        TypedQuery<Long> query = entityManager.createQuery(queryStr, Long.class);
        query.setParameter("volume", volume);
        Long count = query.getSingleResult();
        return count > 0;
    }

    public Integer findIdByVolume(Double volume) {
        Query query = entityManager.createQuery("SELECT r.id FROM Bottle r WHERE r.volume = :volume");
        query.setParameter("volume", volume);

        try {
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
