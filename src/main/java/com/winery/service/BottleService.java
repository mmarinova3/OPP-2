package com.winery.service;

import com.winery.dao.BottleDao;
import com.winery.entities.Bottle;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class BottleService {

    private static BottleService INSTANCE = null;
    private final BottleDao bottleDao;

    private BottleService(EntityManager entityManager) {
        this.bottleDao = new BottleDao(entityManager);
    }

    public static BottleService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new BottleService(entityManager);
        }
        return INSTANCE;
    }

    public Optional<Bottle> getById(int Id) {
        return bottleDao.get(Id);
    }
    public List<Bottle> getAll() {
        return bottleDao.getAll();
    }
    public void save(Bottle bottle){ bottleDao.save(bottle);}
    public void update(Bottle bottle, String[] params) {
        bottleDao.update(bottle, params);
    }
    public void delete(Bottle bottle) {
        bottleDao.delete(bottle);
    }
    public boolean existsByVolume(double volume) {
        return bottleDao.existsByVolume(volume);
    }
    public Integer findIdByVolume(Double volume) {
        return bottleDao.findIdByVolume(volume);
    }
    public Integer getQuantityInStockById(int id){return bottleDao.getQuantityInStockById(id);}
    public void getAndUpdateQuantityInStockById(int id, int bottlesUsed){bottleDao.getAndUpdateQuantityInStockById(id,bottlesUsed);}
    public void returnQuantityInStockById(int id, int bottles){bottleDao.returnQuantityInStockById(id,bottles);}
}
