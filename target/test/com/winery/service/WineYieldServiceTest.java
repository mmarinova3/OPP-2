package com.winery.service;

import com.winery.entities.GrapeVariety;
import com.winery.entities.WineYield;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;


class WineYieldServiceTest {
    private static EntityManager entityManager;
    private static WineYieldService wineYieldService;
    private static GrapeVarietyService grapeVarietyService;

    @BeforeAll
    static void setUp() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        wineYieldService = WineYieldService.getInstance(entityManager, Session.getInstance());
        grapeVarietyService = GrapeVarietyService.getInstance(entityManager, Session.getInstance());
    }

    @AfterAll
    static void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }
    @Test
    void getAll() {
        List<WineYield> wineYields=wineYieldService.getAll();
        for (WineYield wineYield:wineYields) {
            if(wineYield.getYieldPerKg()>0.7){
                System.out.print(wineYield.getGrape().getGrapeName()+"\n");}
        }
    }

}