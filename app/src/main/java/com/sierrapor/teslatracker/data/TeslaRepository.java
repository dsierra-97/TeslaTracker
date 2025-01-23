package com.sierrapor.teslatracker.data;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TeslaRepository {

    private final TeslaDao teslaDao;
    public TeslaRepository(TeslaDao teslaDao) {
        this.teslaDao = teslaDao;
    }
    public void insert(Tesla tesla) {
        teslaDao.insert(tesla);
    }

    public void update(Tesla tesla) {
        teslaDao.update(tesla);
    }

    public void delete(Tesla tesla) {
        teslaDao.delete(tesla);
    }
    public LiveData<List<Tesla>> getAllTeslas() {
        return teslaDao.getAllTeslas();
    }

    public LiveData<Tesla> getTesla(int id) {
        return teslaDao.getTesla(id);
    }



}
