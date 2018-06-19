package com.durov.maks.winestore_02.network;

import com.durov.maks.winestore_02.model.StoreList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestProductInterface {
        @GET("lcboapi.com/products?store_id={@store}&page ={@page}")
        Observable<StoreList> register(@Query(value = "page", encoded = true) String page, @Query(value = "store", encoded = true) String store);
}

