package com.sierrapor.teslatracker.data;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TeslaViewModel extends AndroidViewModel {
    private final TeslaRepository repository;

    @Inject
    public TeslaViewModel(@NonNull Application application, TeslaRepository repository) {
        super(application);
        this.repository = repository;
    }
    public LiveData<List<Tesla>> getAllTeslas() {

        return repository.getAllTeslas();
    }
    public void check(Tesla newTesla) {
        repository.executorService.execute(() -> {
            String plate = newTesla.getPlate();
            String color = newTesla.getColor();
            Tesla existingTesla = repository.getTeslaByPlateAndColor(plate, color);
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
        if (existingTesla.getCountry() == null || existingTesla.getCountry().isEmpty()) {
            existingTesla.setCountry(newTesla.getCountry());
        }
        if(existingTesla.getColor() == null || existingTesla.getColor().isEmpty()){
        existingTesla.setColor(newTesla.getColor());
        }
        if (!existingTesla.isForeign()) {
            existingTesla.setForeign(newTesla.isForeign());
        }
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
