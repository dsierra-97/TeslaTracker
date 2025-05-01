package com.sierrapor.teslatracker.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface TeslaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tesla tesla);
    @Update
    void update(Tesla tesla);
    @Delete
    void delete(Tesla tesla);
    @Query("SELECT * FROM teslas WHERE id = :id")
    Tesla getTesla(int id);
    @Query("SELECT * FROM teslas WHERE plate = :plate AND color = :color LIMIT 1")
    Tesla getTeslaByPlateAndColor(String plate, String color);
    @Query("SELECT * FROM teslas ORDER BY plate ASC")
    LiveData<List<Tesla>> getAllTeslas();
    @RawQuery(observedEntities = Tesla.class)
    LiveData<List<Tesla>> getFilteredTeslas(SupportSQLiteQuery query);

    // Para búsqueda por placa directamente (más eficiente que usar raw)
    @Query("SELECT * FROM teslas WHERE plate LIKE '%' || :query || '%'")
    LiveData<List<Tesla>> searchByPlate(String query);
}