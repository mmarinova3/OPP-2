package com.winery.service;

import com.winery.dao.WineYieldDao;
import com.winery.entities.GrapeVariety;
import com.winery.entities.WineYield;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class WineYieldService {

    private static WineYieldService INSTANCE = null;
    private final WineYieldDao wineYieldDao;

    private WineYieldService(EntityManager entityManager, Session session) {
        this.wineYieldDao = new WineYieldDao(entityManager);
    }

    public static WineYieldService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new WineYieldService(entityManager, session);
        }
        return INSTANCE;
    }

    public Optional<WineYield> getById(int Id) {
        return wineYieldDao.get(Id);
    }

    public List<WineYield> getAll() {
        return wineYieldDao.getAll();
    }

    public void save(WineYield wine) {
        wineYieldDao.save(wine);
    }

    public void update(WineYield wine, String[] params) {
        wineYieldDao.update(wine, params);
    }

    public void delete(WineYield wine) {
        wineYieldDao.delete(wine);
    }

    public Double findYieldPerKgById(GrapeVariety grape) {
        return wineYieldDao.findYieldPerKgById(grape);
    }

}
