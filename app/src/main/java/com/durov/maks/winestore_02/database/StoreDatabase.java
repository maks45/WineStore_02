package com.durov.maks.winestore_02.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.durov.maks.winestore_02.model.Store;


@Database(entities = {Store.class}, version = 1)
public abstract class StoreDatabase extends RoomDatabase{
    public static final String DATABASE_NAME = "wine_stores";
    public abstract StoreDao storeDao();
}
