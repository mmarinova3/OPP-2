package com.winery.service;

import com.winery.dao.WineYieldDao;
import com.winery.entities.WineYield;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class WineYieldService {

    private static WineYieldService INSTANCE = null;
    private final WineYieldDao wineYieldDao;
    private final Session session;

    private WineYieldService(EntityManager entityManager, Session session) {
        this.wineYieldDao = new WineYieldDao(entityManager);
        this.session = session;
    }

    public static WineYieldService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new WineYieldService(entityManager, session);
        }
        return INSTANCE;
    }

    public Optional<WineYield> getWineProdById(int Id) {
        return wineYieldDao.get(Id);
    }

    public List<WineYield> getAllProductions() {
        return wineYieldDao.getAll();
    }

    public void saveProduction(WineYield wine) {
        wineYieldDao.save(wine);
    }

    public void updateProduction(WineYield wine, String[] params) {
        wineYieldDao.update(wine, params);
    }

    public void deleteProduction(WineYield wine) {
        wineYieldDao.delete(wine);
    }
}
