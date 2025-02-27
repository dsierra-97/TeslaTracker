package com.sierrapor.teslatracker.data;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

public class TeslaViewModel extends AndroidViewModel {
    private final TeslaRepository repository;
    private final LiveData<List<Tesla>> allTeslas;

    public TeslaViewModel(@NonNull Application application) {
        super(application);
        TeslaDataBase db = TeslaDataBase.getInstance(application);
        TeslaDao teslaDao = db.teslaDao();
        repository = new TeslaRepository(teslaDao);
        allTeslas = repository.getAllTeslas();
    }
    public LiveData<List<Tesla>> getAllTeslas() {
        return allTeslas;
    }
    public void check(Tesla tesla) {
        repository.executorService.execute(() -> {
            String plate = tesla.getPlate();
            Tesla existingTesla = repository.getTeslaByPlate(plate);
            if (existingTesla != null) {
                existingTesla.setNumberTimesSeen(existingTesla.getNumberTimesSeen() + 1);
                update(existingTesla);
            } else {
                tesla.setFirstTimeSeen(LocalDate.now());
                insert(tesla);
            }
        });
    }
    public void delete(Tesla tesla) {
        repository.delete(tesla);
    }
    private void insert(Tesla tesla) {
        repository.insert(tesla);
    }
    private void update(Tesla tesla) {
        repository.update(tesla);
    }
}
