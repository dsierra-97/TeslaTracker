package com.sierrapor.teslatracker.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import androidx.lifecycle.LiveData;

@Dao
public interface TeslaDao {

    @Insert
    void insert(Tesla tesla);
    @Update
    void update(Tesla tesla);
    @Delete
    void delete(Tesla tesla);
    @Query("SELECT * FROM Teslas WHERE id = :id")
    LiveData<Tesla> getTesla(int id);
    @Query("SELECT * FROM Teslas ORDER BY plate ASC")
    LiveData<List<Tesla>> getAllTeslas();
}