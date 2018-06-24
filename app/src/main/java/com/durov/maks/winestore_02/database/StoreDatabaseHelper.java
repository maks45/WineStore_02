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
import com.durov.maks.winestore_02.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class StoreDatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Stores.db";
        private static final String STORES_TABLE_NAME = "StoresTable";
        private static final String PRODUCT_TABLE_NAME = "ProductTable";
        private static final String AUTO_ID = "_id";





        public StoreDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table "+STORES_TABLE_NAME+" ("
                    + AUTO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Store.ID +" INTEGER NOT NULL,"
                    + Store.IS_DEAD +" INTEGER DEFAULT 0,"
                    + Store.NAME +" TEXT ,"
                    + Store.TAGS +" TEXT ,"
                    + Store.ADRESS_LINE_1 + " TEXT ,"
                    + Store.ADRESS_LINE_2 + " TEXT ,"
                    + Store.CITY + " TEXT ,"
                    + Store.POSTAL_CODE + " TEXT ,"
                    + Store.TELEPHONE + " TEXT ,"
                    + Store.FAX + " TEXT ,"
                    + Store.LATITUDE + " REAL,"
                    + Store.LONGITUDE + " REAL,"
                    + Store.PRODUCTS_COUNT + " INTEGER,"
                    + Store.HAS_PARKING + " INTEGER DEFAULT 0,"
                    + Store.HAS_PRODUCT_CONSULTANT + " INTEGER DEFAULT 0,"
                    + Store.HAS_TASTING_BAR + " INTEGER DEFAULT 0,"
                    + Store.HAS_TRANSIT_ACCES + " INTEGER DEFAULT 0,"
                    + Store.SUNDAY_OPEN + " INTEGER,"
                    + Store.SUNDAY_CLOSE + " INTEGER,"
                    + Store.TUESDAY_OPEN + " INTEGER,"
                    + Store.TUESDAY_CLOSE + " INTEGER,"
                    + Store.WEDNESDAY_OPEN + " INTEGER,"
                    + Store.WEDNESDAY_CLOSE + " INTEGER,"
                    + Store.THURSDAY_OPEN + " INTEGER,"
                    + Store.THURSDAY_CLOSE + " INTEGER,"
                    + Store.FRIDAY_OPEN + " INTEGER,"
                    + Store.FRIDAY_CLOSE + " INTEGER,"
                    + Store.SATURDAY_OPEN + " INTEGER,"
                    + Store.SATURDAY_CLOSE + " INTEGER,"
                    + Store.MONDAY_OPEN + " INTEGER,"
                    + Store.MONDAY_CLOSE + " INTEGER,"
                    + Store.UPDATED_AT + " TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public StoreList getStoreList(int pageNumber) {
            try {
                SQLiteDatabase sqLiteDatabase = getReadableDatabase();
                Gson gson = new Gson();
                String sql = "SELECT _id FROM " + STORES_TABLE_NAME + " WHERE _id=" + String.valueOf(pageNumber);
                Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
                StoreList storeList = null;
                if (cursor.getCount() == 1) {
                    cursor.close();
                    cursor = sqLiteDatabase.query(STORES_TABLE_NAME,
                            new String[]{"_id"}, //PAGER, STORES},
                            "_id=" + String.valueOf(pageNumber),
                            null, null, null, null);
                    if (cursor.moveToFirst()) {

                            Pager pager = gson.fromJson(cursor.getString(cursor.getColumnIndex(" ")), Pager.class);
                            ArrayList<Store> stores = gson.fromJson(cursor.getString(cursor.getColumnIndex(" ")),
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

        public ArrayList<Store> getAllStores(){
            ArrayList<Store> stores = new ArrayList<>();
            try {
                SQLiteDatabase sqLiteDatabase = getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query(STORES_TABLE_NAME, null, null, null,
                        null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Store store = new Store();
                    store.setId(cursor.getInt(cursor.getColumnIndex(Store.ID)));
                    store.setDead(cursor.getInt(cursor.getColumnIndex(Store.IS_DEAD))==1);
                    store.setName(cursor.getString(cursor.getColumnIndex(Store.NAME)));
                    store.setTags(cursor.getString(cursor.getColumnIndex(Store.TAGS)));
                    store.setAddressLine1(cursor.getString(cursor.getColumnIndex( Store.ADRESS_LINE_1)));
                    store.setAddressLine2(cursor.getString(cursor.getColumnIndex( Store.ADRESS_LINE_2)));
                    store.setCity(cursor.getString(cursor.getColumnIndex( Store.CITY)));
                    store.setPostalCode(cursor.getString(cursor.getColumnIndex(Store.POSTAL_CODE)));
                    store.setTelephone(cursor.getString(cursor.getColumnIndex(Store.TELEPHONE)));
                    store.setFax(cursor.getString(cursor.getColumnIndex(Store.FAX)));
                    store.setLatitude(cursor.getInt(cursor.getColumnIndex(Store.LATITUDE)));
                    store.setLongitude(cursor.getInt(cursor.getColumnIndex(Store.LONGITUDE)));
                    store.setProductsCount(cursor.getInt(cursor.getColumnIndex(Store.PRODUCTS_COUNT)));
                    store.setHasParking(cursor.getInt(cursor.getColumnIndex( Store.HAS_PARKING))==1);
                    store.setHasProductConsultant(cursor.getInt(cursor.getColumnIndex(Store.HAS_PRODUCT_CONSULTANT))==1);
                    store.setHasTestingBar(cursor.getInt(cursor.getColumnIndex(Store.HAS_TASTING_BAR))==1);
                    store.setHasTransitAccess(cursor.getInt(cursor.getColumnIndex(Store.HAS_TRANSIT_ACCES))==1);
                    store.setSundayOpen(cursor.getInt(cursor.getColumnIndex(Store.SUNDAY_OPEN)));
                    store.setSundayClose(cursor.getInt(cursor.getColumnIndex(Store.SUNDAY_CLOSE)));
                    store.setTuesdayOpen(cursor.getInt(cursor.getColumnIndex(Store.TUESDAY_OPEN)));
                    store.setTuesdayClose(cursor.getInt(cursor.getColumnIndex(Store.TUESDAY_CLOSE)));
                    store.setWednesdayOpen(cursor.getInt(cursor.getColumnIndex(Store.WEDNESDAY_OPEN)));
                    store.setWednesdayClose(cursor.getInt(cursor.getColumnIndex(Store.WEDNESDAY_CLOSE)));
                    store.setThursdayOpen(cursor.getInt(cursor.getColumnIndex(Store.THURSDAY_OPEN)));
                    store.setThursdayClose(cursor.getInt(cursor.getColumnIndex(Store.THURSDAY_CLOSE)));
                    store.setFridayOpen(cursor.getInt(cursor.getColumnIndex(Store.FRIDAY_OPEN)));
                    store.setFridayClose(cursor.getInt(cursor.getColumnIndex(Store.FRIDAY_CLOSE)));
                    store.setSaturdayOpen(cursor.getInt(cursor.getColumnIndex(Store.SATURDAY_OPEN)));
                    store.setSaturdayClose(cursor.getInt(cursor.getColumnIndex(Store.SATURDAY_CLOSE)));
                    store.setMondayOpen(cursor.getInt(cursor.getColumnIndex(Store.MONDAY_OPEN)));
                    store.setMondayClose(cursor.getInt(cursor.getColumnIndex(Store.MONDAY_CLOSE)));
                    store.setUpdatedAt(cursor.getString(cursor.getColumnIndex(Store.UPDATED_AT)));

                    stores.add(store);
                    cursor.moveToNext();
                }
                cursor.close();
                return stores;
            }catch (Exception e){
                e.printStackTrace();
            }
            //Log.d(MainActivity.TAG,"databasehelper return null");
            //Log.d(MainActivity.TAG,String.valueOf(stores.size()));
            return null;
        }


        public void saveStoreArrayList(ArrayList<Store> stores){
            try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            for(Store store:stores){
                String sql ="SELECT id FROM "+STORES_TABLE_NAME+" WHERE id="+String.valueOf(store.getId());
                Cursor cursor= sqLiteDatabase.rawQuery(sql,null);
                ContentValues contentValues = new ContentValues();
                contentValues.put(Store.ID,Long.valueOf(store.getId()));
                contentValues.put(Store.IS_DEAD,store.isDead());
                contentValues.put(Store.NAME,String.valueOf(store.getName()));
                contentValues.put(Store.TAGS,String.valueOf(store.getTags()));
                contentValues.put(Store.ADRESS_LINE_1,String.valueOf(store.getAddressLine1()));
                contentValues.put(Store.ADRESS_LINE_2,String.valueOf(store.getAddressLine2()));
                contentValues.put(Store.CITY,String.valueOf(store.getCity()));
                contentValues.put(Store.POSTAL_CODE,String.valueOf(store.getPostalCode()));
                contentValues.put(Store.TELEPHONE,String.valueOf(store.getTelephone()));
                contentValues.put(Store.FAX,String.valueOf(store.getFax()));
                contentValues.put(Store.LATITUDE,store.getLatitude());
                contentValues.put(Store.LONGITUDE,store.getLongitude());
                contentValues.put(Store.PRODUCTS_COUNT,Long.valueOf(store.getProductsCount()));
                contentValues.put(Store.HAS_PARKING,store.isHasParking());
                contentValues.put(Store.HAS_PRODUCT_CONSULTANT,store.isHasProductConsultant());
                contentValues.put(Store.HAS_TASTING_BAR,store.isHasTestingBar());
                contentValues.put(Store.HAS_TRANSIT_ACCES,store.isHasTransitAccess());
                contentValues.put(Store.SUNDAY_OPEN,Long.valueOf(store.getSundayOpen()));
                contentValues.put(Store.SUNDAY_CLOSE,Long.valueOf(store.getSundayClose()));
                contentValues.put(Store.TUESDAY_OPEN,Long.valueOf(store.getTuesdayOpen()));
                contentValues.put(Store.TUESDAY_CLOSE,Long.valueOf(store.getTuesdayClose()));
                contentValues.put(Store.WEDNESDAY_OPEN,Long.valueOf(store.getWednesdayOpen()));
                contentValues.put(Store.WEDNESDAY_CLOSE,Long.valueOf(store.getWednesdayClose()));
                contentValues.put(Store.THURSDAY_OPEN,Long.valueOf(store.getThursdayOpen()));
                contentValues.put(Store.THURSDAY_CLOSE,Long.valueOf(store.getThursdayClose()));
                contentValues.put(Store.FRIDAY_OPEN,Long.valueOf(store.getFridayOpen()));
                contentValues.put(Store.FRIDAY_CLOSE,Long.valueOf(store.getFridayClose()));
                contentValues.put(Store.SATURDAY_OPEN,Long.valueOf(store.getSaturdayOpen()));
                contentValues.put(Store.SATURDAY_CLOSE,Long.valueOf(store.getSaturdayClose()));
                contentValues.put(Store.MONDAY_OPEN,Long.valueOf(store.getMondayOpen()));
                contentValues.put(Store.MONDAY_CLOSE,Long.valueOf(store.getMondayClose()));
                contentValues.put(Store.UPDATED_AT,Long.valueOf(store.getUpdatedAt()));
                if(cursor.getCount()>0){
                    sqLiteDatabase.update(STORES_TABLE_NAME,contentValues,"id="+String.valueOf(store.getId()),null);
                }else{
                    sqLiteDatabase.insert(STORES_TABLE_NAME,null,contentValues);
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}