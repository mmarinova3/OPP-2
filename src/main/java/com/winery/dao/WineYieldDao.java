package com.winery.dao;

import com.winery.entities.WineYield;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class WineYieldDao implements Dao<WineYield> {

    private final static Logger log = LogManager.getLogger(WineYield.class);
    private final EntityManager entityManager;

    public WineYieldDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<WineYield> get(int id) {
        try {
            return Optional.ofNullable(entityManager.find(WineYield.class, id));
        } catch (Exception e) {
            log.error("Wine yield get error: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<WineYield> getAll() {
        try {
            TypedQuery<WineYield> query = entityManager.createQuery("SELECT u FROM WineYield u", WineYield.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Wine yield getAll error: " + e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void save(WineYield wineYield) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(wineYield);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine yield save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(WineYield wineYield, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(wineYield);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine yield update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(WineYield wineYield) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(wineYield);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Wine yield delete error: " + e.getMessage(), e);
            }
        }
    }
}
