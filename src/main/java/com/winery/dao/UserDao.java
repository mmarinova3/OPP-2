package com.winery.dao;

import com.winery.entities.User;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User> {
    private final static Logger log = LogManager.getLogger(User.class);
    private final EntityManager entityManager;

    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> get(int id) {
        try {
            return Optional.ofNullable(entityManager.find(User.class, id));
        } catch (Exception e) {
            log.error("User get error: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("User getAll error: " + e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void save(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("User save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(User user, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("User update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("User delete error: " + e.getMessage(), e);
            }
        }
    }

    public User validateLogin(String enteredUsername, String enteredPassword) {
        try {
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password");
            query.setParameter("username", enteredUsername);
            query.setParameter("password", enteredPassword);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            log.error("User login validation error: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error during user login validation: " + e.getMessage(), e);
            return null;
        }
    }
}
