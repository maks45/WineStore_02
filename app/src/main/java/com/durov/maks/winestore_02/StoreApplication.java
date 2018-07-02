package com.durov.maks.winestore_02;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.durov.maks.winestore_02.database.StoreDao;
import com.durov.maks.winestore_02.database.StoreDatabase;
import com.durov.maks.winestore_02.database.StoreDatabaseHelper;
import com.durov.maks.winestore_02.network.RequestProductListInterface;
import com.durov.maks.winestore_02.network.RequestStoreListInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StoreApplication extends Application{

    public static final String ROOT_URL = "http://lcboapi.com/";

    private StoreDatabaseHelper storeDatabaseHelper;
    private RequestStoreListInterface requestStoreListInterface;
    private RequestProductListInterface requestProductListInterface;
    private StoreDao storeDao;


    @Override
    public void onCreate() {
        super.onCreate();
        storeDatabaseHelper = new StoreDatabaseHelper(this);
        requestStoreListInterface = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestStoreListInterface.class);
        requestProductListInterface = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestProductListInterface.class);
        StoreDatabase storeDatabase = Room
                .databaseBuilder(getApplicationContext(), StoreDatabase.class,StoreDatabase.DATABASE_NAME)
               // .allowMainThreadQueries() //need if you work in ui thread
                .build();
        storeDao = storeDatabase.storeDao();

    }

    public StoreDatabaseHelper getStoreDatabaseHelper() {
        return storeDatabaseHelper;
    }

    public RequestStoreListInterface getRequestStoreListInterface() {
        return requestStoreListInterface;
    }

    public RequestProductListInterface getRequestProductListInterface() {
        return requestProductListInterface;
    }
    public StoreDao getStoreDatabase(){
        return storeDao;
    }


}
