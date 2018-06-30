package com.durov.maks.winestore_02.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.durov.maks.winestore_02.model.Store;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface StoreDao {

        @Query("SELECT * FROM store")
        List<Store> getAll();

        @Query("SELECT * FROM store WHERE id IN (:storesIds)")
        List<Store> loadAllByIds(int[] storesIds);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(List<Store> stores);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Store store);

        @Update
        void update(Store store);

        @Delete
        void delete(Store store);
    }
