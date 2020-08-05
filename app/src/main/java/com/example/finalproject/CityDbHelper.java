package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CityDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Message.db";
    public static final String TABLE_NAME = "Message";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNTRY = "Country";
    public static final String COLUMN_REGION = "REGION";
    public static final String COLUMN_CITY = "City";
    public static final String COLUMN_CURRENCY = "Currency";
    public static final String COLUMN_LATITUDE = "Latitude";
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
