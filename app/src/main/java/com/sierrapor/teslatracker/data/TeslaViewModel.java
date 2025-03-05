package com.sierrapor.teslatracker.data;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public void check(Tesla newTesla) {
        repository.executorService.execute(() -> {
            String plate = newTesla.getPlate();
            Tesla existingTesla = repository.getTeslaByPlate(plate);
            if (existingTesla != null) {
                Tesla updatedTesla = updateExistingTesla(existingTesla, newTesla);
                update(updatedTesla);
            } else {
                newTesla.setFirstTimeSeen(LocalDate.now());
                insert(newTesla);
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
    private Tesla updateExistingTesla(Tesla existingTesla, Tesla newTesla) {
        existingTesla.setCountry(newTesla.getCountry());
        existingTesla.setColor(newTesla.getColor());
        existingTesla.setForeign(newTesla.isForeign());
        existingTesla.setNumberTimesSeen(existingTesla.getNumberTimesSeen()+1);
        existingTesla.setLastTimeSeen(newTesla.getLastTimeSeen());
        existingTesla.setSeenBy(updateSeenBy(existingTesla, newTesla));
        return existingTesla;
    }

    private List<Tesla.players> updateSeenBy(Tesla existingTesla, Tesla newTesla) {

        List<Tesla.players> updatedSeenByList = new ArrayList<>(existingTesla.getSeenBy());
        for (Tesla.players player : newTesla.getSeenBy()) {
            if (!updatedSeenByList.contains(player)) {
                updatedSeenByList.add(player);
            }
        }
        return updatedSeenByList;
    }
}
