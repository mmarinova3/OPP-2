package com.winery.dao;

import com.winery.entities.BottledWine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BottledWineDao implements Dao<BottledWine>{

    private final static Logger log = LogManager.getLogger(BottledWine.class);
    private final EntityManager entityManager;

    public BottledWineDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<BottledWine> get(int id) {
       return Optional.ofNullable(entityManager.find(BottledWine.class, id));
    }

    @Override
    public List<BottledWine> getAll() {
        TypedQuery<BottledWine> query = entityManager.createQuery("SELECT u FROM Bottled_Wine u", BottledWine.class);
        return query.getResultList();
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
                log.error("Bottled wine save error: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(BottledWine bottledWine, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            // Update user properties based on params
            // Example: user.setUsername(params[0]);
            // Update other properties as needed
            entityManager.merge(bottledWine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Bottled wine update error: " + e.getMessage());
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
                log.error("Bottled wine delete error: " + e.getMessage());
            }
        }
    }
}
