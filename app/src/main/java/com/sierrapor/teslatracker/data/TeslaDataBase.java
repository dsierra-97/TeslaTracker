package com.sierrapor.teslatracker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Tesla.class}, version = 1, exportSchema = false)
public abstract class TeslaDataBase extends RoomDatabase {
    private static volatile TeslaDataBase INSTANCE;
    public abstract TeslaDao teslaDao();

    public static TeslaDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TeslaDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TeslaDataBase.class, "tesla_database")
                            .fallbackToDestructiveMigration() // Elimina y recrea la DB si cambia la estructura
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
