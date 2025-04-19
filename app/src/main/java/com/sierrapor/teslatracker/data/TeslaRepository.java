package com.sierrapor.teslatracker.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TeslaRepository {
    private final TeslaDao teslaDao;
    final ExecutorService executorService;
    @Inject
    public TeslaRepository(TeslaDao teslaDao) {
        this.teslaDao = teslaDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }
    public void insert(Tesla tesla) {
        executorService.execute(() -> teslaDao.insert(tesla));
    }
    public void update(Tesla tesla) {
        executorService.execute(() -> teslaDao.update(tesla));
    }
    public void delete(Tesla tesla) {
        executorService.execute(() -> teslaDao.delete(tesla));
    }
    public LiveData<List<Tesla>> getAllTeslas() {
        return teslaDao.getAllTeslas();
    }
    public Tesla getTeslaByPlateAndColor(String plate, String color) {
        return teslaDao.getTeslaByPlateAndColor(plate, color);
    }
    public Tesla getTesla(int id) {
        return teslaDao.getTesla(id);
    }
}
