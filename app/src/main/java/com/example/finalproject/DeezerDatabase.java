package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeezerDatabase extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "DeezerDB";
    protected final static int VERSION_NUM = 2;
    public final static String TABLE_NAME = "DEEZER_TABLE";
    public final static String COL_SEARCH_TITLE = "TITLE";
    public final static String COL_ID = "_id";


    /**
     * class is intended to setup and refresh the database used in storing information for favourites
     *
     */
    public DeezerDatabase(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SEARCH_TITLE + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
