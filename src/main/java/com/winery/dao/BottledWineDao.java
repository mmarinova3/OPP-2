package com.winery.dao;

import com.winery.entities.BottledWine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BottledWineDao implements Dao<BottledWine> {

    private final static Logger log = LogManager.getLogger(BottledWine.class);
    private final EntityManager entityManager;

    public BottledWineDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<BottledWine> get(int id) {
        try {
            return Optional.ofNullable(entityManager.find(BottledWine.class, id));
        } catch (Exception e) {
            log.error("Bottled wine get error: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<BottledWine> getAll() {
        try {
            TypedQuery<BottledWine> query = entityManager.createQuery("SELECT u FROM BottledWine u", BottledWine.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Bottled wine getAll error: " + e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void save(BottledWine bottledWine) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(bottledWine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Bottled wine save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(BottledWine bottledWine, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(bottledWine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Bottled wine update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(BottledWine bottledWine) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(bottledWine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Bottled wine delete error: " + e.getMessage(), e);
            }
        }
    }
}
