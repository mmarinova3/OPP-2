package com.winery.service;

import com.winery.entities.UserRole;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class RoleServiceTest {
    private static EntityManager entityManager;
    private static RoleService roleService;

    @BeforeAll
    static void setUp() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        roleService = RoleService.getInstance(entityManager, Session.getInstance());

    }

    @AfterAll
    static void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }


    @Test
    void findIdByName() {
       int id = roleService.findIdByName(UserRole.ADMIN);
        assertEquals(1,id );
    }
}