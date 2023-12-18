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
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> getAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public void save(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit(); // Commit the transaction after persisting the user
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("User save error: " + e.getMessage());
            }
        }
    }


    @Override
    public void update(User user, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            // Update user properties based on params
            // Example: user.setUsername(params[0]);
            // Update other properties as needed
            entityManager.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("User update error: " + e.getMessage());
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
                log.error("User delete error: " + e.getMessage());
            }
        }
    }

    public User validateLogin(String enteredUsername, String enteredPassword) {
        try {
            // Query to retrieve a user based on the entered username and password
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password");
            query.setParameter("username", enteredUsername);
            query.setParameter("password", enteredPassword);

            // Try to get a single result, which would be the user with the given credentials
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            // No user found with the entered credentials
            return null;
        }
    }

}
