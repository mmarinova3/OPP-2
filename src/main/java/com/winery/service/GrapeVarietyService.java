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
    private final Session session;

    private GrapeVarietyService(EntityManager entityManager, Session session) {
        this.grapeVarietyDao = new GrapeVarietyDao(entityManager);
        this.session = session;
    }

    public static GrapeVarietyService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new GrapeVarietyService(entityManager, session);
        }
        return INSTANCE;
    }

    public Optional<GrapeVariety> getById(int Id) {
        return grapeVarietyDao.get(Id);
    }

    public List<GrapeVariety> getAllUsers() {
        return grapeVarietyDao.getAll();
    }

    public void saveUser(GrapeVariety grapeVariety) {
        grapeVarietyDao.save(grapeVariety);
    }

    public void updateUser(GrapeVariety grapeVariety, String[] params) {
        grapeVarietyDao.update(grapeVariety, params);
    }

    public void deleteUser(GrapeVariety grapeVariety) {
        grapeVarietyDao.delete(grapeVariety);
    }
}
