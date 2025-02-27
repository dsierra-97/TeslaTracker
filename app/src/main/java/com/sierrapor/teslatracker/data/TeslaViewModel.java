package com.sierrapor.teslatracker.data;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

    public void insert(Tesla tesla) {
        new Thread(() -> repository.insert(tesla)).start();
    }

    public void update(Tesla tesla) {
        new Thread(() -> repository.update(tesla)).start();
    }

    public void delete(Tesla tesla) {
        new Thread(() -> repository.delete(tesla)).start();
    }
}
