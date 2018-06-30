package com.durov.maks.winestore_02.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;

public class StoreList {

    @SerializedName("result")
    private ArrayList<Store> stores;
    @SerializedName("pager")
    private Pager pager;

    public StoreList() {
    }

    public StoreList(ArrayList<Store> stores, Pager pager) {
        this.stores = stores;
        this.pager = pager;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

}

