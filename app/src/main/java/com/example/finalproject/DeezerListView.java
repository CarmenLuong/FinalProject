package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
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
    ArrayList<DeezerArtistClass> savedArtistArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deezerArrayList.add(new DeezerArtistClass("sample title name"));
        Log.i("DeezerListView", "item at position 0:" + deezerArrayList.get(0));
        deezerArrayList.add(new DeezerArtistClass("sample title name1"));
        deezerArrayList.add(new DeezerArtistClass("sample title name2"));

        setContentView(R.layout.activity_deezer_list_view);
        deezerList = findViewById(R.id.deezerList);

        ListAdapter myListAdapter = new ListAdapter();
        deezerList.setAdapter(myListAdapter);


//        loadFromDataBase();


    }

//    void printCursor(Cursor c, int version) {
//
//        int searchIndex = c.getColumnIndex(DeezerDatabase.COL_SEARCH_TITLE);
//        int idIndex = c.getColumnIndex(DeezerDatabase.COL_ID);
//        StringBuilder sb = new StringBuilder("\nResults list: ");
//
//        if (c.moveToFirst()) {
//            do {
//                String search = c.getString(searchIndex);
//                long id = c.getLong(idIndex);
//                sb.append("\nRow: ").append(c.getPosition()).append("\nId: ").append(id).append("\nMessage: ")
//                        .append(search + "\n");
//            } while (c.moveToNext());
//        }
//        Log.i("DeezerActivity", sb.toString());
//    }

//    protected void loadFromDataBase() {
//        DeezerDatabase myOpener = new DeezerDatabase(this);
//        sqlDB = myOpener.getWritableDatabase();
//
//        String[] columns = {DeezerDatabase.COL_ID, DeezerDatabase.COL_SEARCH_TITLE};
//
//        Cursor results = sqlDB.query(false, DeezerDatabase.TABLE_NAME, columns, null, null, null, null, null, null);
//
//        int idColIndex = results.getColumnIndex(DeezerDatabase.COL_ID);
//        int searchColumnIndex = results.getColumnIndex(DeezerDatabase.COL_SEARCH_TITLE);
//
//        while(results.moveToNext())
//        {
//            Long id = results.getLong(idColIndex);
//            String search = results.getString(searchColumnIndex);
//
//            //add the new Contact to the array list:
//
//            savedArtistArray.add(new DeezerArtistClass(id, search));
//        }
//    }

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