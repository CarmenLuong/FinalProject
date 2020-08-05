package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DeezerListView extends AppCompatActivity {

    ArrayList<DeezerArtistClass> deezerArrayList = new ArrayList<>();
    ListView deezerList;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        deezerArrayList.add(new DeezerArtistClass("sample title name"));
//        Log.i("DeezerListView", "item at position 0:" + deezerArrayList.get(0));
//        deezerArrayList.add(new DeezerArtistClass("sample title name1"));
//        deezerArrayList.add(new DeezerArtistClass("sample title name2"));

        setContentView(R.layout.activity_deezer_list_view);
        deezerList = findViewById(R.id.deezerList);

        ListAdapter myListAdapter = new ListAdapter();
        deezerList.setAdapter(myListAdapter);


    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return deezerArrayList.size();
        }

        @Override
        public DeezerArtistClass getItem(int position) {
            return deezerArrayList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            DeezerArtistClass tempMessage = getItem(position);
            LayoutInflater inflater = getLayoutInflater();

            row = inflater.inflate(R.layout.deezer_text_list_load, parent, false);
            TextView setDeezerList = row.findViewById(R.id.sampleSongText);
            setDeezerList.setText(tempMessage.getTitle());
            return row;

        }
    }
}