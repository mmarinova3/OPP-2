package com.winery.service;

import com.winery.dao.GrapeVarietyDao;
import com.winery.entities.GrapeVariety;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class GrapeVarietyService {
    private static GrapeVarietyService INSTANCE = null;
    private final GrapeVarietyDao grapeVarietyDao;

    private GrapeVarietyService(EntityManager entityManager, Session session) {
        this.grapeVarietyDao = new GrapeVarietyDao(entityManager);
    }

    public static GrapeVarietyService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new GrapeVarietyService(entityManager,session);
        }
        return INSTANCE;
    }

    public Optional<GrapeVariety> getById(int Id) {
        return grapeVarietyDao.get(Id);
    }

    public List<GrapeVariety> getAll() {
        return grapeVarietyDao.getAll();
    }

    public void save(GrapeVariety grapeVariety) {
        grapeVarietyDao.save(grapeVariety);
    }

    public void update(GrapeVariety grapeVariety, String[] params) {
        grapeVarietyDao.update(grapeVariety, params);
    }

    public void delete(GrapeVariety grapeVariety) {
        grapeVarietyDao.delete(grapeVariety);
    }

    public Integer findIdByName(String grapeName) {
        return grapeVarietyDao.findIdByName(grapeName);
    }
    public Double findQuantityById(int grapeId) {
        return grapeVarietyDao.findQuantityById(grapeId);
    }
    public void updateQuantityInStockById(int id, double grapeUsed) {grapeVarietyDao.getAndUpdateQuantityInStockById(id,grapeUsed);}
}
