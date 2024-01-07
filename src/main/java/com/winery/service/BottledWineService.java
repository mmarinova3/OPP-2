package com.winery.service;

import com.winery.dao.BottledWineDao;
import com.winery.entities.BottledWine;
import com.winery.utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class BottledWineService {
    private static BottledWineService INSTANCE = null;
    private final BottledWineDao bottledWineDao;

    private BottledWineService(EntityManager entityManager, Session session) {
        this.bottledWineDao = new BottledWineDao(entityManager);
    }

    public static BottledWineService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new BottledWineService(entityManager,session);
        }
        return INSTANCE;
    }

    public Optional<BottledWine> getById(int Id) {
        return bottledWineDao.get(Id);
    }

    public List<BottledWine> getAll() {
        return bottledWineDao.getAll();
    }

    public void save(BottledWine bottledWine){ bottledWineDao.save(bottledWine);
    }

    public void update(BottledWine bottledWine, String[] params) {
        bottledWineDao.update(bottledWine, params);
    }

    public void delete(BottledWine bottledWine) {
        bottledWineDao.delete(bottledWine);
    }



}
