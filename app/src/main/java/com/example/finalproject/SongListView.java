package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongListView extends AppCompatActivity {
    ArrayList<DeezerArtistClass> artistInfo = new ArrayList<>();
    ListView artistList;
    Button saveToFavourites;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_view);

        artistList = findViewById(R.id.artistInfo);
        saveToFavourites = findViewById(R.id.favouriteButton);

        ListAdapter myListAdapter = new ListAdapter();
        artistList.setAdapter(myListAdapter);
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return artistInfo.size();
        }

        @Override
        public DeezerArtistClass getItem(int position) {
            return artistInfo.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }


    }
}