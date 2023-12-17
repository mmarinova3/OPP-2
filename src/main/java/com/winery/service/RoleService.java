package com.winery.service;

import com.winery.dao.RoleDao;
import com.winery.entities.UserRole;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;

public class RoleService {

    private static RoleService INSTANCE = null;
    private final RoleDao roleDao;

    private RoleService(EntityManager entityManager) {
        this.roleDao = new RoleDao(entityManager);
    }

    public static RoleService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new RoleService(entityManager);
        }
        return INSTANCE;
    }

    public Integer findIdByName(UserRole roleName) {
        return roleDao.findIdByName(roleName);
    }
}
