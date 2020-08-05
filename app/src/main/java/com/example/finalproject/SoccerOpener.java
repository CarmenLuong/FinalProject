package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SoccerOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "ScoreDb";
    protected final static int VERSION_NUM = 8;
    public final static String TABLE_NAME = " HIGHLIGHTS";
    public final static String COL_TITLE = "GAME";
    public final static String COL_DATE = "GAME_DATE";
    public final static String COL_ID = "_id";
    public final static String COL_URL = "URL";
    public final static String COL_TEAM1 = "team1";
    public final static String COL_TEAM2 = "team2";

    public SoccerOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_DATE + " TEXT," + COL_URL + " TEXT,"
                + COL_TEAM1 + " TEXT, " + COL_TEAM2 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }


}

