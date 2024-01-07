package com.winery.service;

import com.winery.entities.Bottle;
import com.winery.entities.GrapeVariety;
import com.winery.entities.WineComposition;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

class OptimalBottlingTest {

    private static EntityManager entityManager;
    private static GrapeVarietyService grapeVarietyService;
    private static WineYieldService wineYieldService;
    private static BottleService bottleService;

    @BeforeAll
    static void setUp() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());
        wineYieldService = WineYieldService.getInstance(Connection.getEntityManager(), Session.getInstance());
        bottleService = BottleService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @AfterAll
    static void afterAllTearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @AfterEach
    void afterEachTearDown() {
        entityManager.clear();
    }


    @Test
    void getLitersWineInStock() {
        GrapeVariety grapeVariety = new GrapeVariety();
        grapeVariety.setId(1);

        // Adding null checks to prevent NullPointerException
        Double quantity = grapeVarietyService.findQuantityById(grapeVariety.getId());
        Double yieldPerKg = wineYieldService.findYieldPerKgById(grapeVariety);

        if (quantity != null && yieldPerKg != null) {
            double v = quantity * yieldPerKg;
            System.out.println(v);
        } else {
            System.out.println("Unable to calculate wine quantity due to missing data.");
        }
    }


    @Test
    void getMaxBottling() {
        OptimalBottling optimalBottling = new OptimalBottling();
        WineComposition wineComposition = new WineComposition();
        wineComposition.setId(1);

        Bottle bottle = new Bottle();
        bottle.setId(bottleService.findIdByVolume(0.2));

        if (bottle.getId() != null) {
            bottle.setVolume(0.2);
            bottle.setQuantity(bottleService.getQuantityInStockById(bottle.getId()));

            optimalBottling.getMaxBottling(wineComposition, bottle);
        } else {
            System.out.println("Unable to proceed with bottle ID being null.");
        }
    }


    @Test
    void getOptimalBottling() {
        OptimalBottling optimalBottling = new OptimalBottling();
        WineComposition wineComposition = new WineComposition();
        wineComposition.setId(1);

        Bottle bottle = new Bottle();
        bottle.setId(3);
        bottle.setVolume(0.2);

        if (bottle.getId() != null) {
            Integer quantityInStock = bottleService.getQuantityInStockById(bottle.getId());

            if (quantityInStock != null) {
                bottle.setQuantity(quantityInStock);
                optimalBottling.getOptimalBottling(wineComposition, bottle);
            } else {
                System.out.println("Quantity in stock is null.");
            }
        } else {
            System.out.println("Unable to proceed with bottle ID being null.");
        }
    }

}
