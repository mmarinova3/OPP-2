package com.winery.service;

import com.winery.dao.WineComponentsDao;
import com.winery.entities.WineComponents;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class WineComponentsService {
    private static WineComponentsService INSTANCE = null;
    private final WineComponentsDao wineComponentsDao;

    private WineComponentsService(EntityManager entityManager, Session session) {
        this.wineComponentsDao = new WineComponentsDao(entityManager);
    }

    public static WineComponentsService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new WineComponentsService(entityManager, session);
        }
        return INSTANCE;
    }


    public Optional<WineComponents> getById(int Id) {
        return wineComponentsDao.get(Id);
    }

    public List<WineComponents> getAll() {
        return wineComponentsDao.getAll();
    }

    public void save(WineComponents wineComponents) {
        wineComponentsDao.save(wineComponents);
    }

    public void update(WineComponents wineComponents, String[] params) {
        wineComponentsDao.update(wineComponents, params);
    }

    public void delete(WineComponents wineComponents) {
        wineComponentsDao.delete(wineComponents);
    }

    public List<WineComponents> findComponents(String compositionName){ return wineComponentsDao.findComponents(compositionName);}
}
