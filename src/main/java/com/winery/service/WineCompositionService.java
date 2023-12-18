package com.winery.service;

import com.winery.dao.WineCompositionDao;
import com.winery.entities.WineComposition;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class WineCompositionService {
    private static WineCompositionService INSTANCE = null;
    private final WineCompositionDao wineCompositionDao;
    private final Session session;

    private WineCompositionService(EntityManager entityManager, Session session) {
        this.wineCompositionDao = new WineCompositionDao(entityManager);
        this.session = session;
    }

    public static WineCompositionService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new WineCompositionService(entityManager, session);
        }
        return INSTANCE;
    }

    public Optional<WineComposition> getById(int Id) {
        return wineCompositionDao.get(Id);
    }

    public List<WineComposition> getAll() {
        return wineCompositionDao.getAll();
    }

    public void save(WineComposition wineComposition) {
        wineCompositionDao.save(wineComposition);
    }

    public void update(WineComposition wineComposition, String[] params) {
        wineCompositionDao.update(wineComposition, params);
    }
    public void delete(WineComposition wineComposition) {
        wineCompositionDao.delete(wineComposition);
    }

    public Integer findIdByName(String compositionName) {
        return wineCompositionDao.findIdByName(compositionName);
    }
}
