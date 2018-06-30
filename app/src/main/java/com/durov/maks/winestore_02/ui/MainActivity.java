package com.durov.maks.winestore_02.ui;

import android.content.Intent;
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
import com.durov.maks.winestore_02.database.StoreDao;
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
        nextPage =1;
        isPageLast =false; //if true: no need load new page
        offlineMode =false; //if true: load page from database
        initRecyclerView();
        loadData();
    }
    private void initRecyclerView(){
            Log.d(TAG,"init recycler view");
            RecyclerView recyclerView = findViewById(R.id.main_activity_recycler_view);
            storeAdapter = new StoreAdapter(storesArrayList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(storeAdapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(layoutManager.findFirstVisibleItemPosition()+ layoutManager.getChildCount() > storesArrayList.size()-1){
                        if(!offlineMode) {
                            loadData();
                            storeAdapter.setLoadData(true);
                            recyclerView.post(() -> storeAdapter.notifyDataSetChanged());
                        }else{
                            //check internet connection here
                        }
                    }
                }

    });
            storeAdapter.setOnClick(this::startStoreActivity);
    }

    private void loadData(){
        if(storeList!=null && !offlineMode){
            nextPage = storeList.getPager().getNextPage();
            isPageLast = storeList.getPager().isFinalPage();
        }
        //load from network
        if(!isPageLast && !storeAdapter.isLoadData() && !offlineMode) {
            //Log.d(TAG,"online_mode");
            compositeDisposable.add(storeApplication
                    .getRequestStoreListInterface()
                    .register(String.valueOf(nextPage),String.valueOf(STORES_PER_PAGE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        }
        //load from database if you are offline
        if(offlineMode){
            Toast.makeText(this,"You are offline",Toast.LENGTH_SHORT).show();
            compositeDisposable.add(Single.fromCallable(
                    new CallableGetStoresFromDb(storeApplication.getStoreDatabase()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Store>>() {
                        @Override
                        public void accept(List<Store> stores) throws Exception {
                            changeData(stores);
                        }
                    }, new Consumer<Throwable>() {
                                @Override
                                public void accept(final Throwable throwable) throws Exception {
                                    throwable.printStackTrace();
                                }
                            })
                    );
        }
    }

    private void handleResponse(StoreList storeList) {
        //Log.d(TAG,"handleResponse");
        this.storeList = storeList;
        if(storeList!=null){
            changeData(storeList.getStores());
        }else{
            storeAdapter.setLoadData(false);
            offlineMode = true;
            loadData();
        }
        if(!offlineMode) {
            Callable<Void> callable = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    storeApplication.getStoreDatabase().insert(storeList.getStores());
                    Log.d("Write db in thread",Thread.currentThread().getName());
                    return null;
                }
            };
            compositeDisposable.add(Completable.fromCallable(callable)
                    .subscribeOn(Schedulers.io())
                    .subscribe());

        }
    }
    private void changeData(List<Store> stores){
        storesArrayList.clear();
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
            Log.d("On offline mode","true");
            offlineMode =true;
            loadData();
        }
        storeAdapter.setLoadData(false);
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
        StoreDao storeDao;
        public CallableGetStoresFromDb(StoreDao storeDao) {
            this.storeDao = storeDao;
        }
        @Override
        public List<Store> call() throws Exception {
            Log.d("Get databese in thread",Thread.currentThread().getName());
            return storeDao.getAll();
        }
    }

}
