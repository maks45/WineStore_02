package com.durov.maks.winestore_02.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.durov.maks.winestore_02.R;
import com.durov.maks.winestore_02.StoreApplication;
import com.durov.maks.winestore_02.adapter.StoreAdapter;
import com.durov.maks.winestore_02.database.StoreDatabaseHelper;
import com.durov.maks.winestore_02.model.Store;
import com.durov.maks.winestore_02.model.StoreList;
import com.durov.maks.winestore_02.network.RequestStoreListInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final int STORES_PER_PAGE = 50;
    public static final String TAG = "MY_LOG";
    public static final String EXTRA_STORE ="com.durov.maks.winestore_02.STORE";

    private CompositeDisposable compositeDisposable;
    private StoreAdapter storeAdapter;
    private ArrayList<Store> storesArrayList;
    private StoreList storeList;
    private int nextPage;
    private boolean isPageAddNow;
    private boolean isPageLast;
    private boolean offlineMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init
        compositeDisposable = new CompositeDisposable();
        storesArrayList = new ArrayList<>();
        nextPage =1;
        isPageLast =false;//if true: no need load new page
        isPageAddNow = false;//if true: no need load new page (page already loaded)
        offlineMode =false;//if true: load page from database
        initRecyclerView();
        loadData();
    }
    private void initRecyclerView(){
            Log.d(TAG,"init recycler view");
            RecyclerView recyclerView = findViewById(R.id.main_activity_recycler_view);
            storeAdapter = new StoreAdapter(storesArrayList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(storeAdapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        loadData();
                        storeAdapter.notifyDataSetChanged();
                        isPageAddNow = true;
                    }
                }
            });
            storeAdapter.setOnClick(new StoreAdapter.OnItemClicked() {
                @Override
                public void onItemClick(Store store) {
                    startStoreActivity(store);
                }
            });
    }

    private void loadData(){
        if(storeList!=null && !offlineMode){
            nextPage = storeList.getPager().getNextPage();
            isPageLast = storeList.getPager().isFinalPage();
        }
        //load from network
        if(!isPageLast && !isPageAddNow && !offlineMode) {
            //Log.d(TAG,"online_mode");
            compositeDisposable.add(((StoreApplication) this.getApplication())
                    .getRequestStoreListInterface()
                    .register(String.valueOf(nextPage),String.valueOf(STORES_PER_PAGE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        }
        //load from database if you are offline
        if(offlineMode){
            //Log.d(TAG,"offline mode");
            storesArrayList.clear();
            storesArrayList.addAll(((StoreApplication) this.getApplication()).getStoreDatabaseHelper().getAllStores());
            if(storesArrayList!=null){
                storeAdapter.notifyDataSetChanged();
            }else{
                Log.d(TAG,"database return null");
            }
            isPageAddNow = false;
        }

    }

    private void handleResponse(StoreList storeList) {
        Log.d(TAG,"handleResponse");
        this.storeList = storeList;
        if(storeList!=null){
            storesArrayList.addAll(storeList.getStores());
            storeAdapter.notifyDataSetChanged();
            isPageAddNow = false;
        }else{
            isPageAddNow = false;
            offlineMode = true;
            loadData();
        }
        if(!offlineMode) {
            ((StoreApplication) this.getApplication()).getStoreDatabaseHelper().saveStoreArrayList(storeList.getStores());
        }
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            HttpException httpException = (HttpException) error;
            Log.i(TAG, error.getMessage() + " / " + error.getClass());
        }
        // A network error happened
        if (error instanceof IOException) {
            Log.i(TAG, error.getMessage() + " / " + error.getClass());
            offlineMode =true;
            Toast.makeText(this,"You are offline",Toast.LENGTH_SHORT).show();
            loadData();
        }
        isPageAddNow = false;
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
}
