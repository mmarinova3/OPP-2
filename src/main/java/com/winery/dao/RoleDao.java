package com.winery.dao;

import com.winery.entities.User;
import com.winery.entities.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class RoleDao {

    private final static Logger log = LogManager.getLogger(User.class);
    private final EntityManager entityManager;

    public RoleDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer findIdByName(UserRole roleName) {
        Query query = entityManager.createQuery("SELECT r.id FROM Role r WHERE r.roleName = :roleName");
        query.setParameter("roleName", roleName);

        try {
            // Try to get a single result, which would be the ID of the role with the given name
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            // No role found with the entered role name
            return null;
        }
    }

}
