package com.winery.service;

import com.winery.entities.GrapeCategory;
import com.winery.entities.GrapeVariety;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class GrapeVarietyServiceTest {
    private static EntityManager entityManager;
    private static GrapeVarietyService grapeVarietyService;
    private static WineYieldService wineYieldService;

    @BeforeAll
    static void setUpBeforeClass() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        grapeVarietyService = GrapeVarietyService.getInstance(entityManager, Session.getInstance());
        wineYieldService = WineYieldService.getInstance(entityManager, Session.getInstance());
    }

    @AfterAll
    static void tearDownAfterClass() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Test
    void getAll() {
        List<GrapeVariety> grapeVarieties=grapeVarietyService.getAll();
        for (GrapeVariety grapeVariety:grapeVarieties) {
            if(grapeVariety.getCategory().equals(GrapeCategory.WHITE)){
            System.out.print(grapeVariety.getGrapeName()+"\n");}
        }
    }

    @Test
    void findIdByName() {
        GrapeVariety testGV = new GrapeVariety();
        testGV.setId( grapeVarietyService.findIdByName("Merlot"));
        System.out.print(wineYieldService.findYieldPerKgById(testGV));
    }

    @Test
    void findQuantityById() {
        GrapeVariety testGV = new GrapeVariety();
        testGV.setId( grapeVarietyService.findIdByName("Merlot"));
        double quantity=grapeVarietyService.findQuantityById(testGV.getId());
        double yieldPerKg =wineYieldService.findYieldPerKgById(testGV);
        System.out.print("Wine in stock: "+quantity*yieldPerKg);
    }
}