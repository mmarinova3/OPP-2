package com.winery.service;

import com.winery.entities.BottledWine;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;


class BottledWineServiceTest {

    private static EntityManager entityManager;
    private static BottledWineService bottledWineService;

    @BeforeAll
    static void setUp() {
        entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
        bottledWineService = BottledWineService.getInstance(entityManager, Session.getInstance());
    }

    @AfterAll
    static void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }


    @Test
    void getAll() {
      List<BottledWine> bottledWineList=bottledWineService.getAll();
        for (BottledWine bottledWine:bottledWineList) {
            System.out.print(bottledWine.getWineComposition().getWineName()+"\n");
        }

    }


}
