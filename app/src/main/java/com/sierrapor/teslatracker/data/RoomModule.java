package com.sierrapor.teslatracker.data;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomModule {

    @Provides
    @Singleton
    public static TeslaDataBase provideTeslaDataBase(Application application) {
        return Room.databaseBuilder(application, TeslaDataBase.class, "tesla_database")
                .build();
    }

    @Provides
    public static TeslaDao provideTeslaDao(TeslaDataBase database) {
        return database.teslaDao();
    }
}
