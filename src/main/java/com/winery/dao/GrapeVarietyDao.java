package com.winery.dao;

import com.winery.entities.GrapeVariety;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class GrapeVarietyDao implements Dao<GrapeVariety>{

    private final static Logger log = LogManager.getLogger(GrapeVariety.class);
    private final EntityManager entityManager;

    public GrapeVarietyDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<GrapeVariety> get(int id) {
        return Optional.ofNullable(entityManager.find(GrapeVariety.class, id));
    }

    @Override
    public List<GrapeVariety> getAll() {
        TypedQuery<GrapeVariety> query = entityManager.createQuery("SELECT u FROM Grape_Variety u", GrapeVariety.class);
        return query.getResultList();
    }

    @Override
    public void save(GrapeVariety grapeVariety) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(grapeVariety);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Grape variety save error: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(GrapeVariety grapeVariety, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            // Update user properties based on params
            // Example: user.setUsername(params[0]);
            // Update other properties as needed
            entityManager.merge(grapeVariety);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Grape variety update error: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete(GrapeVariety grapeVariety) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(grapeVariety);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Grape variety delete error: " + e.getMessage());
            }
        }
    }
}
