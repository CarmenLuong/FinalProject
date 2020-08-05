package com.example.finalproject;

public class City {
    private long id;
    private String country, region, city, currency, latitude, longitude;

    public City(long id, String country, String region, String city, String currency, String latitude, String longitude){
        this.id = id;
        this.country = country;
        this.region = region;
        this.city = city;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId(){
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
