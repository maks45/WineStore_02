package com.durov.maks.winestore_02.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.durov.maks.winestore_02.model.Pager;
import com.durov.maks.winestore_02.model.Store;
import com.durov.maks.winestore_02.model.StoreList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class StoreDatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "StoresList.db";
        private static final String TABLE_NAME = "StoresList";
        private static final String PAGER = "pager";
        private static final String STORES = "stores";


        public StoreDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table "+TABLE_NAME+" ("
                    + "_id INTEGER ,"
                    + PAGER+" TEXT NOT NULL," +
                    STORES+" TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


        public StoreList getStoreList(int pageNumber) {
            try {
                SQLiteDatabase sqLiteDatabase = getReadableDatabase();
                Gson gson = new Gson();
                String sql = "SELECT _id FROM " + TABLE_NAME + " WHERE _id=" + String.valueOf(pageNumber);
                Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
                StoreList storeList = null;
                if (cursor.getCount() == 1) {
                    cursor.close();
                    cursor = sqLiteDatabase.query(TABLE_NAME,
                            new String[]{"_id", PAGER, STORES},
                            "_id=" + String.valueOf(pageNumber),
                            null, null, null, null);
                    if (cursor.moveToFirst()) {

                            Pager pager = gson.fromJson(cursor.getString(cursor.getColumnIndex(PAGER)), Pager.class);
                            ArrayList<Store> stores = gson.fromJson(cursor.getString(cursor.getColumnIndex(STORES)),
                                new TypeToken<List<Store>>() {
                                }.getType());
                            storeList = new StoreList();
                            storeList.setPager(pager);
                            storeList.setStores(stores);
                    }
                }
                cursor.close();
                return storeList;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        public void saveStoreList(StoreList storeList){
            try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            String sql ="SELECT _id FROM "+TABLE_NAME+" WHERE _id="+String.valueOf(storeList.getPager().getCurrentPage());
            Cursor cursor= sqLiteDatabase.rawQuery(sql,null);
            Log.d("Cursor Count : ", String.valueOf(cursor.getCount()));
            ContentValues contentValues = new ContentValues();
            Gson gson = new Gson();
            contentValues.put("_id",Long.valueOf(storeList.getPager().getCurrentPage()));
            contentValues.put(PAGER, gson.toJson(storeList.getPager()));
            contentValues.put(STORES, gson.toJson(storeList.getStores()));
            if(cursor.getCount()>0){
                sqLiteDatabase.update(TABLE_NAME,contentValues,"_id="+String.valueOf(
                        storeList.getPager().getCurrentPage()
                ),null);
            }else{
                sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
            }
            cursor.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }