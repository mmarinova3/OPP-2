package com.winery.service;

import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class WineComponentsServiceTest {
    private static EntityManager entityManager;
    private static WineComponentsService wineComponentsService;

    @BeforeAll
    static void setUp() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        wineComponentsService = WineComponentsService.getInstance(entityManager, Session.getInstance());

    }

    @AfterAll
    static void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Test
    void findComponents() {
       wineComponentsService.findComponents("Cabernet Sauvignon Special");
    }
}