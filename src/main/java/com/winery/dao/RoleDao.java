package com.winery.dao;

import com.winery.entities.User;
import com.winery.entities.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoleDao {

    private final static Logger log = LogManager.getLogger(User.class);
    private final EntityManager entityManager;

    public RoleDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer findIdByName(UserRole roleName) {
        try {
            Query query = entityManager.createQuery("SELECT r.id FROM Role r WHERE r.roleName = :roleName");
            query.setParameter("roleName", roleName);
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error("Error finding Role ID by name: " + e.getMessage(), e);
            return null;
        }
    }
}
