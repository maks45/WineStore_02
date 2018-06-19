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

    public static final String ROOT_URL = "http://lcboapi.com/";
    public static final String TAG = "MY_LOG";
    public static final String EXTRA_STORE ="com.durov.maks.winestore_01.STORE";


    private StoreDatabaseHelper storeDatabaseHelper;
    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable;
    private StoreAdapter storeAdapter;
    private ArrayList<Store> storesArrayList;
    private StoreList storeList;
    private RecyclerView.LayoutManager layoutManager;
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
        storeDatabaseHelper = new StoreDatabaseHelper(this);
        storesArrayList = new ArrayList<>();
        this.nextPage =1;
        this.isPageLast =false;
        this.isPageAddNow = false;//if true no load new page if page already loaded
        this.offlineMode =false;//if true load page from database
        initRecyclerView();
        loadData();
    }
    private void initRecyclerView(){
            this.recyclerView = findViewById(R.id.main_activity_recycler_view);
            this.storeAdapter = new StoreAdapter(storesArrayList);
            this.layoutManager = new LinearLayoutManager(getApplicationContext());
            this.recyclerView.setLayoutManager(layoutManager);
            this.recyclerView.setAdapter(this.storeAdapter);
            this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        loadData();
                        storeAdapter.notifyDataSetChanged();
                        MainActivity.this.isPageAddNow = true;
                    }
                }
            });
            this.storeAdapter.setOnClick(new StoreAdapter.OnItemClicked() {
                @Override
                public void onItemClick(Store store) {
                    startStoreActivity(store);
                }
            });
    }

    private void loadData(){
        if(this.storeList!=null){
            this.nextPage = this.storeList.getPager().getNextPage();
            this.isPageLast = this.storeList.getPager().isFinalPage();
        }
        //load from network
        if(!this.isPageLast && !this.isPageAddNow && !this.offlineMode) {
            RequestStoreListInterface requestStoreListInterface = new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(RequestStoreListInterface.class);

            compositeDisposable.add(requestStoreListInterface.register(String.valueOf(this.nextPage))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        }
        //load from database if you are offline
        if(this.offlineMode){
            this.storeList = storeDatabaseHelper.getStoreList(this.nextPage);
            if(this.storeList!=null){
                this.storesArrayList.addAll(storeList.getStores());
                this.storeAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(this,"No more results",Toast.LENGTH_SHORT).show();
            }
            this.isPageAddNow = false;
            //Toast.makeText(this,"Load offline page: "+String.valueOf(this.nextPage),Toast.LENGTH_SHORT).show();
        }

    }

    private void handleResponse(StoreList storeList) {
        this.storeList = storeList;
        if(storeList!=null){
            this.storesArrayList.addAll(storeList.getStores());
            this.storeAdapter.notifyDataSetChanged();
            this.isPageAddNow = false;
        }else{
            this.isPageAddNow = false;
            Toast.makeText(this,"No more results",Toast.LENGTH_SHORT).show();
        }
        if(!this.offlineMode) {
            this.storeDatabaseHelper.saveStoreList(storeList);
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
            this.offlineMode =true;
            Toast.makeText(this,"You are offline",Toast.LENGTH_SHORT).show();
            loadData();
        }
        //Toast.makeText(this,"OfflineMode: "+String.valueOf(this.offlineMode),Toast.LENGTH_SHORT).show();
        this.isPageAddNow = false;
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
