package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    SQLiteDatabase db;
    ArrayList<soccerScoreObject> favoritesList = new ArrayList<>();
    SoccerAdapter adp;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);


    }


}