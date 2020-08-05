package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CityDbHelper extends SQLiteOpenHelper {
    /**
     * Version of the DB
     */
    public static final int DATABASE_VERSION = 4;
    /**
     * Name of the DB
     */
    public static final String DATABASE_NAME = "Message.db";
    /**
     * Table name
     */
    public static final String TABLE_NAME = "City";
    /**
     * Column name for the id
     */
    public static final String COLUMN_ID = "_id";
    /**
     * Column name for the country
     */
    public static final String COLUMN_COUNTRY = "Country";
    /**
     * Column name for the region
     */
    public static final String COLUMN_REGION = "Region";
    /**
     * Column name for the city
     */
    public static final String COLUMN_CITY = "City";
    /**
     * Column name for the currency
     */
    public static final String COLUMN_CURRENCY = "Currency";
    /**
     * Column name for the latitude
     */
    public static final String COLUMN_LATITUDE = "Latitude";
    /**
     * Column name for the longitude
     */
    public static final String COLUMN_LONGITUDE = "Longitude";

    public CityDbHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COUNTRY + " TEXT,"
                + COLUMN_REGION + " TEXT,"
                + COLUMN_CITY + " TEXT,"
                + COLUMN_CURRENCY + " TEXT,"
                + COLUMN_LATITUDE + " TEXT,"
                + COLUMN_LONGITUDE + " TEXT,"
                + "UNIQUE(" + COLUMN_LATITUDE + ", " + COLUMN_LONGITUDE + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
