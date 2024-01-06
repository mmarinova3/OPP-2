package com.winery.service;

import com.winery.entities.Bottle;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BottleServiceTest {
    private static EntityManager entityManager;
    private static BottleService bottleService;

    @BeforeAll
    static void setUpBeforeClass() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        bottleService = BottleService.getInstance(entityManager, Session.getInstance());

    }

    @AfterAll
    static void tearDownAfterClass() {
        if (entityManager != null) {
            entityManager.close();
        }
    }


    @Test
    @Order(1)
    void createBottles() {
        Bottle b1 = new Bottle();
        b1.setVolume(0.1400);
        b1.setQuantity(50);

        Bottle b2 = new Bottle();
        b2.setVolume(0.800);
        b2.setQuantity(50);

        bottleService.save(b1);
        bottleService.save(b2);


        bottleService.delete(b1);
        bottleService.delete(b2);

    }

    @Test
    @Order(2)
    void getById() {
        Bottle testBottle = new Bottle();
        testBottle.setId(bottleService.findIdByVolume(0.2));

        Optional<Bottle> result = bottleService.getById(testBottle.getId());

        assertTrue(result.isPresent());
        assertEquals(testBottle.getId(), result.get().getId());
    }

    @Test
    @Order(3)
    void getAll() {

        List<Bottle> result = bottleService.getAll();

        assertEquals(4,result.size());

    }

    @Test
    @Order(4)
    void existsByVolume() {
        Bottle b1 = new Bottle();
        b1.setVolume(0.200);
        b1.setId(bottleService.findIdByVolume(b1.getVolume()));

        boolean result = bottleService.existsByVolume(b1.getVolume());

        assertTrue(result);
    }

    @Test
    @Order(5)
    void getAndUpdateQuantityInStockById(){
        Bottle testBottle = new Bottle();
        testBottle.setId(1102);
        testBottle.setVolume(0.100);
        testBottle.setQuantity(100);

        bottleService.save(testBottle);
        bottleService.getAndUpdateQuantityInStockById(testBottle.getId(),50);
        bottleService.save(testBottle);

        assertEquals(100,testBottle.getQuantity());
        bottleService.delete(testBottle);
    }

    @Test
    @Order(6)
    void returnQuantityInStockById(){
        Bottle testBottle = new Bottle();
        testBottle.setId(1102);
        testBottle.setVolume(0.100);
        testBottle.setQuantity(100);

        bottleService.save(testBottle);
        bottleService.returnQuantityInStockById(testBottle.getId(),50);
        bottleService.save(testBottle);

        assertEquals(100,testBottle.getQuantity());
        bottleService.delete(testBottle);
    }



}