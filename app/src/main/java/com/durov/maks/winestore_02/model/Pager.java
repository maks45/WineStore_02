package com.durov.maks.winestore_02.model;

import com.google.gson.annotations.SerializedName;

public class Pager {
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("next_page")
    private int nextPage;
    @SerializedName("is_final_page")
    private boolean isFinalPage;
    @SerializedName("total_record_count")
    private int totalRecordsCount;


    public Pager(int currentPage, int nextPage, boolean isFinalPage, int totalRecordsCount) {
        this.currentPage = currentPage;
        this.nextPage = nextPage;
        this.isFinalPage = isFinalPage;
        this.totalRecordsCount = totalRecordsCount;
    }

    public int getTotalRecordsCount() {
        return totalRecordsCount;
    }

    public void setTotalRecordsCount(int totalRecordsCount) {
        this.totalRecordsCount = totalRecordsCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isFinalPage() {
        return isFinalPage;
    }

    public void setFinalPage(boolean finalPage) {
        isFinalPage = finalPage;
    }
}

        /*{"records_per_page":5,"total_record_count":662,
        "current_page_record_count":5,
        "is_first_page":true,
        "is_final_page":false,
        "current_page":1,
        "current_page_path":"/stores?per_page=5\u0026page=1",
        "next_page":2,
        "next_page_path":"/stores?per_page=5\u0026page=2",
        "previous_page":null,
        "previous_page_path":null,
        "total_pages":133,
        "total_pages_path":"/stores?per_page=5\u0026page=133"}*/