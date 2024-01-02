package com.winery.dao;

import com.winery.entities.Bottle;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BottleDao implements Dao<Bottle> {
    private final static Logger log = LogManager.getLogger(Bottle.class);
    private final EntityManager entityManager;

    public BottleDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Bottle> get(int id) {
        try {
            return Optional.ofNullable(entityManager.find(Bottle.class, id));
        } catch (Exception e) {
            log.error("Error retrieving Bottle with ID " + id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Bottle> getAll() {
        try {
            TypedQuery<Bottle> query = entityManager.createQuery("SELECT u FROM Bottle u", Bottle.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error retrieving all Bottles", e);
            return List.of();
        }
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
                log.error("Error saving Bottle: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(Bottle bottle, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(bottle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Error updating Bottle: " + e.getMessage(), e);
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
                log.error("Error deleting Bottle: " + e.getMessage(), e);
            }
        }
    }

    public boolean existsByVolume(double volume) {
        try {
            String queryStr = "SELECT COUNT(b) FROM Bottle b WHERE b.volume = :volume";
            TypedQuery<Long> query = entityManager.createQuery(queryStr, Long.class);
            query.setParameter("volume", volume);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            log.error("Error checking existence by volume: " + e.getMessage(), e);
            return false;
        }
    }

    public Integer findIdByVolume(Double volume) {
        try {
            Query query = entityManager.createQuery("SELECT r.id FROM Bottle r WHERE r.volume = :volume");
            query.setParameter("volume", volume);
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error("Error finding Bottle ID by volume: " + e.getMessage(), e);
            return null;
        }
    }

    public Integer getQuantityInStockById(int id) {
        try {
            Query query = entityManager.createQuery("SELECT r.quantity FROM Bottle r WHERE r.id = :id");
            query.setParameter("id", id);
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error("Error finding Bottle in stock by id: " + e.getMessage(), e);
            return null;
        }
    }

    public Integer getAndUpdateQuantityInStockById(int id, int bottlesUsed) {
        try {
            Query query = entityManager.createQuery("SELECT r.quantity FROM Bottle r WHERE r.id = :id");
            query.setParameter("id", id);
            Integer currentQuantity = (Integer) query.getSingleResult();

            if (currentQuantity != null && currentQuantity >= bottlesUsed) {
                int newQuantity = currentQuantity - bottlesUsed;
                entityManager.getTransaction().begin();
                Query updateQuery = entityManager.createQuery("UPDATE Bottle r SET r.quantity = :newQuantity WHERE r.id = :id");
                updateQuery.setParameter("newQuantity", newQuantity);
                updateQuery.setParameter("id", id);
                updateQuery.executeUpdate();
                entityManager.getTransaction().commit();

                return newQuantity;
            } else {
                log.error("Not enough stock for operation");
                return null;
            }
        } catch (NoResultException e) {
            log.error("Bottle not found: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Error updating Bottle quantity: " + e.getMessage(), e);
            return null;
        }
    }


}
