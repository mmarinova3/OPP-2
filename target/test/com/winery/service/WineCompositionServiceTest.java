package com.winery.service;

import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class WineCompositionServiceTest {
    private static EntityManager entityManager;
    private static WineCompositionService wineCompositionService;

    @BeforeAll
    static void setUp() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        wineCompositionService = WineCompositionService.getInstance(entityManager, Session.getInstance());

    }

    @AfterAll
    static void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }
    @Test
    void findIdByName() {
        wineCompositionService.findIdByName("Merlot");
    }
}