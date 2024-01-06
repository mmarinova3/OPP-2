package com.winery.dao;

import com.winery.entities.GrapeVariety;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class GrapeVarietyDao implements Dao<GrapeVariety> {

    private final static Logger log = LogManager.getLogger(GrapeVariety.class);
    private final EntityManager entityManager;

    public GrapeVarietyDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<GrapeVariety> get(int id) {
        try {
            return Optional.ofNullable(entityManager.find(GrapeVariety.class, id));
        } catch (Exception e) {
            log.error("Grape variety get error: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<GrapeVariety> getAll() {
        try {
            TypedQuery<GrapeVariety> query = entityManager.createQuery("SELECT u FROM GrapeVariety u", GrapeVariety.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Grape variety getAll error: " + e.getMessage(), e);
            return List.of();
        }
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
                log.error("Grape variety save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(GrapeVariety grapeVariety, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(grapeVariety);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Grape variety update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(GrapeVariety grapeVariety) throws PersistenceException {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(grapeVariety);
            entityManager.flush();
            transaction.commit();
        } catch (PersistenceException e) {
            throw e;
        } catch (Throwable e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Grape variety delete error: " + e.getMessage(), e);
            }
        }
    }

    public Integer findIdByName(String grapeVariety) {
        try {
            Query query = entityManager.createQuery("SELECT r.id FROM GrapeVariety r WHERE r.grapeName = :grapeName");
            query.setParameter("grapeName", grapeVariety);
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error("Error finding GrapeVariety ID by name: " + e.getMessage(), e);
            return null;
        }
    }

    public Double findQuantityById(int grapeVariety) {
        try {
            Query query = entityManager.createQuery("SELECT r.quantity FROM GrapeVariety r WHERE r.id = :id");
            query.setParameter("id", grapeVariety);
            return (Double) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error("Error finding quantity: " + e.getMessage(), e);
            return null;
        }
    }

    public void getAndUpdateQuantityInStockById(int id, double grapeUsed) {
        try {
            Query query = entityManager.createQuery("SELECT r.quantity FROM GrapeVariety r WHERE r.id = :id");
            query.setParameter("id", id);
            Double currentQuantity = (Double) query.getSingleResult();

            if (currentQuantity != null && currentQuantity >= grapeUsed) {
               double newQuantity = currentQuantity - grapeUsed;
                entityManager.getTransaction().begin();
                Query updateQuery = entityManager.createQuery("UPDATE GrapeVariety r SET r.quantity = :newQuantity WHERE r.id = :id");
                updateQuery.setParameter("newQuantity", newQuantity);
                updateQuery.setParameter("id", id);
                updateQuery.executeUpdate();
                entityManager.getTransaction().commit();

            } else {
                log.error("Not enough stock for operation");
            }
        } catch (NoResultException e) {
            log.error("Grape not found: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error updating Grape quantity: " + e.getMessage(), e);
        }
    }
}
