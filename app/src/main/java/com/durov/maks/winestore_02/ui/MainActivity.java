package com.durov.maks.winestore_02.ui;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.durov.maks.winestore_02.R;
import com.durov.maks.winestore_02.StoreApplication;
import com.durov.maks.winestore_02.adapter.StoreAdapter;
import com.durov.maks.winestore_02.model.Store;
import com.durov.maks.winestore_02.model.StoreList;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final int STORES_PER_PAGE = 50;
    public static final String TAG = "MY_LOG";
    public static final String EXTRA_STORE ="com.durov.maks.winestore_02.STORE";
    public static final String PARCELABLE_STORE_ARRAY_LIST = "com.durov.maks.winestore_02.STORE_ARRAY_LIST";
    public static final String STORE_NEXT_PAGE = "com.durov.maks.winestore_02.STORE_NEXT_PAGE";
    public static final String STORE_IS_PAGE_LAST = "com.durov.maks.winestore_02.STORE_IS_PAGE_LAST";
    public static final String STORE_OFFLINE_MODE = "com.durov.maks.winestore_02.STORE_OFFLINE_MODE";


    private CompositeDisposable compositeDisposable;
    private StoreAdapter storeAdapter;
    private ArrayList<Store> storesArrayList;
    private StoreList storeList;
    private int nextPage;
    private StoreApplication storeApplication;
    private boolean isPageLast;
    private boolean offlineMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init
        storeApplication = (StoreApplication) this.getApplication();
        compositeDisposable = new CompositeDisposable();
        storesArrayList = new ArrayList<>();
        initRecyclerView();
        if(savedInstanceState!=null){
            storesArrayList.addAll(savedInstanceState.getParcelableArrayList(PARCELABLE_STORE_ARRAY_LIST));
            nextPage = savedInstanceState.getInt(STORE_NEXT_PAGE);
            isPageLast = savedInstanceState.getBoolean(STORE_IS_PAGE_LAST);
            offlineMode = savedInstanceState.getBoolean(STORE_OFFLINE_MODE);
        }else{
            nextPage =1;
            isPageLast =false; //if true: no need load new page
            offlineMode =false; //if true: load page from database
            loadDataFromNetwork();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PARCELABLE_STORE_ARRAY_LIST,storesArrayList);
        outState.putInt(STORE_NEXT_PAGE,nextPage);
        outState.putBoolean(STORE_IS_PAGE_LAST,isPageLast);
        outState.putBoolean(STORE_OFFLINE_MODE,offlineMode);
    }

    private void initRecyclerView(){
            //Log.d(TAG,"init recycler view");
            RecyclerView recyclerView = findViewById(R.id.main_activity_recycler_view);
            storeAdapter = new StoreAdapter(storesArrayList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(storeAdapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(layoutManager.findFirstVisibleItemPosition()+ layoutManager.getChildCount() > storesArrayList.size()-3){
                        loadDataFromNetwork();
                        storeAdapter.setLoadData(true);
                        recyclerView.post(() -> storeAdapter.notifyDataSetChanged());
                    }
                }

    });
            storeAdapter.setOnClick(this::startStoreActivity);
    }

    private void loadDataFromNetwork() {
       /* if (storeList != null) {
            nextPage = storeList.getPager().getNextPage();
            isPageLast = storeList.getPager().isFinalPage();
        }*/
        //load from network
        if (!isPageLast && !storeAdapter.isLoadData()) {
            //Log.d(TAG,"online_mode");
            compositeDisposable.add(storeApplication
                    .getRequestStoreListInterface()
                    .register(String.valueOf(nextPage), String.valueOf(STORES_PER_PAGE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        }
    }
    private void loadDataFromDataBase(){
        //load from database if you are offline
        if(!storeAdapter.isLoadData()){
            Toast.makeText(this,"You are offline",Toast.LENGTH_SHORT).show();
            compositeDisposable.add(Single.fromCallable(
                    new CallableGetStoresFromDb())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Store>>() {
                        @Override
                        public void accept(List<Store> stores) throws Exception {
                            nextPage = stores.size()/STORES_PER_PAGE+1;
                            changeDisplayData(stores);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(final Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    }));
        }
    }

    private void handleResponse(StoreList storeList) {
        //Log.d(TAG,"handleResponse");
        this.storeList = storeList;
        if(storeList!=null){
            nextPage = storeList.getPager().getNextPage();
            isPageLast = storeList.getPager().isFinalPage();
            offlineMode =false;
            changeDisplayData(storeList.getStores());
        }
        if(!offlineMode) {
            compositeDisposable.add(Completable.fromCallable( new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    storeApplication.getStoreDatabase().insert(storeList.getStores());
                    //Log.d("Write db in thread",Thread.currentThread().getName());
                    return null;
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe());

        }
    }

    private void changeDisplayData(List<Store> stores){
        if(offlineMode){storesArrayList.clear();}
        storeAdapter.setLoadData(false);
        storesArrayList.addAll(stores);
        storeAdapter.notifyDataSetChanged();
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            HttpException httpException = (HttpException) error;
            Log.i(TAG, error.getMessage() + " / " + error.getClass());
        }
        // A network error happened
        if (error instanceof IOException) {
            Log.i(TAG, error.getMessage() + " / " + error.getClass());
            //Log.d("On offline mode","true");
            storeAdapter.setLoadData(false);
            if(!offlineMode) {
                offlineMode = true;
                loadDataFromDataBase();
            }
        }
        //storeAdapter.setLoadData(false);
    }

    private void startStoreActivity(Store store){
        Intent intent = new Intent(this,StoreActivity.class);
        intent.putExtra(EXTRA_STORE,store);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    class CallableGetStoresFromDb implements Callable<List<Store>> {
        @Override
        public List<Store> call() throws Exception {
            //Log.d("Get databese in thread",Thread.currentThread().getName());
            return storeApplication.getStoreDatabase().getAll();
        }
    }

}
