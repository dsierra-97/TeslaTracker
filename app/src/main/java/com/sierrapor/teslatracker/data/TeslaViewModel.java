package com.sierrapor.teslatracker.data;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void edit(Tesla editedTesla) {
        repository.executorService.execute(() -> {
            // 1. Obtener el Tesla actual antes de la edición
            Tesla currentTesla = repository.getTesla(editedTesla.getId());

            // 2. Rellenar campos vacíos con datos actuales
            if (editedTesla.getPlate() == null || editedTesla.getPlate().isEmpty()) {
                editedTesla.setPlate(currentTesla.getPlate());
            }
            if (editedTesla.getColor() == null || editedTesla.getColor().isEmpty()) {
                editedTesla.setColor(currentTesla.getColor());
            }
            if (editedTesla.getNumberTimesSeen() == null || editedTesla.getNumberTimesSeen() == 0) {
                editedTesla.setNumberTimesSeen(currentTesla.getNumberTimesSeen());
            }
            // 3. Comprobar si existe otro Tesla con la misma plate y color
            Tesla existingTesla = repository.getTeslaByPlateAndColor(editedTesla.getPlate(), editedTesla.getColor());

            // Si existe y no es el mismo que estamos editando
            if (existingTesla != null && existingTesla.getId() != editedTesla.getId()) {
                // 4. Fusionar datos
                fuseData(existingTesla, editedTesla, currentTesla);
            } else {
                // 6. Si no hay duplicado, simplemente actualiza
                repository.update(editedTesla);
            }
        });
    }

    public void delete(Tesla tesla) { repository.delete(tesla); }
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

    private void fuseData(Tesla existingTesla, Tesla editedTesla, Tesla currentTesla){

        existingTesla.setNumberTimesSeen(existingTesla.getNumberTimesSeen() + editedTesla.getNumberTimesSeen());

        Set<Tesla.players> mergedSeenBy = new HashSet<>(existingTesla.getSeenBy());
        mergedSeenBy.addAll(editedTesla.getSeenBy());
        existingTesla.setSeenBy(new ArrayList<>(mergedSeenBy));

        if (editedTesla.isForeign()){
            existingTesla.setForeign(true);
        }
        //Cuando existingTesla no tiene pais pero editedTesla sí, nos quedamos con el de editedTesla
        if ((existingTesla.getCountry() == null || existingTesla.getCountry().isEmpty()) &&
                (editedTesla.getCountry() != null || !editedTesla.getCountry().isEmpty())){
            existingTesla.setCountry(editedTesla.getCountry());
        }

        if (editedTesla.getFirstTimeSeen().isBefore(existingTesla.getFirstTimeSeen())){
            existingTesla.setFirstTimeSeen(editedTesla.getFirstTimeSeen());
        }

        if (editedTesla.getLastTimeSeen().isAfter(existingTesla.getLastTimeSeen())){
            existingTesla.setLastTimeSeen(editedTesla.getLastTimeSeen());
        }

        repository.update(existingTesla);
        repository.delete(currentTesla);

    }
}
