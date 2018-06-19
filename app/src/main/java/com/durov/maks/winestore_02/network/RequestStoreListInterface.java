package com.durov.maks.winestore_02.network;

import com.durov.maks.winestore_02.model.StoreList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestStoreListInterface {
    @GET("stores/?page={@page}&per_page=50&order=id.asc")
    Observable<StoreList>register(@Query(value = "page", encoded = true) String page);
}
