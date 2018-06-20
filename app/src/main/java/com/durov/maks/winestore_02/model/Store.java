package com.durov.maks.winestore_02.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Store implements Serializable {

    //constants for database name
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TAGS = "tags";
    private static final String ADRESS_LINE_1 = "address_line_1";
    private static final String ADRESS_LINE_2 = "address_line_2";
    private static final String CITY = "city";
    private static final String POSTAL_CODE = "postal_code";
    private static final String TELEPHONE = "telephone";
    private static final String FAX = "fax";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String PRODUCTS_COUNT = "products_count";
    private static final String HAS_PARKING = "has_parking";

    //variables
    @SerializedName("id")
    private int id;
    @SerializedName("is_dead")
    private boolean isDead;
    @SerializedName("name")
    private String name;
    @SerializedName("tags")
    private String tags;
    @SerializedName("address_line_1")
    private String addressLine1;
    @SerializedName("address_line_2")
    private String addressLine2;
    @SerializedName("city")
    private String city;
    @SerializedName("postal_code")
    private String postalCode;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("fax")
    private String fax;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("products_count")
    private int productsCount;
    @SerializedName("has_parking")
    private boolean hasParking;

    public Store(int id, boolean isDead, String name, String tags, String addressLine1, String addressLine2, String city, String postalCode, String telephone, String fax, double latitude, double longitude, int productsCount, boolean hasParking) {
        this.id = id;
        this.isDead = isDead;
        this.name = name;
        this.tags = tags;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.postalCode = postalCode;
        this.telephone = telephone;
        this.fax = fax;
        this.latitude = latitude;
        this.longitude = longitude;
        this.productsCount = productsCount;
        this.hasParking = hasParking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    public boolean isHasParking() {
        return hasParking;
    }

    public void setHasParking(boolean hasParking) {
        this.hasParking = hasParking;
    }

    /*{"id":217,
    "is_dead":false,
    "name":"Queens Quay \u0026 Yonge",
    "tags":"queens quay yonge 2 cooper street queen's toronto central toronto-central torontocentral m5e0b8",
    "address_line_1":"2 Cooper Street",
    "address_line_2":"Queen's Quay",
    "city":"Toronto",
    "postal_code":"M5E0B8",
    "telephone":"(416) 864-6777",
    "fax":"(416) 864-6863",
    "latitude":43.643,
    "longitude":-79.3723,
    "products_count":5071,
    "inventory_count":184579,
    "inventory_price_in_cents":457923726,
    "inventory_volume_in_milliliters":135626242,
    "has_wheelchair_accessability":true,
    "has_bilingual_services":true,
    "has_product_consultant":true,
    "has_tasting_bar":true,
    "has_beer_cold_room":false,
    "has_special_occasion_permits":false,
    "has_vintages_corner":true,
    "has_parking":true,
    "has_transit_access":true,
    "sunday_open":660,
    "sunday_close":1080,
    "monday_open":540,
    "monday_close":1320,
    "tuesday_open":540,
    "tuesday_close":1320,
    "wednesday_open":540,
    "wednesday_close":1320,
    "thursday_open":540,
    "thursday_close":1320,
    "friday_open":540,
    "friday_close":1320,
    "saturday_open":540,
    "saturday_close":1320,
    "updated_at":"2018-06-17T14:15:21.972Z",
    "store_no":217}*/

}
