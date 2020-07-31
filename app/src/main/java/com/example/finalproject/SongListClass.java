package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SongListClass extends AppCompatActivity {
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_LYRICS = "LYRICS";
   public static final String ITEM_SONGN = "SONGNAME";
   String songg;
   String artists;
   String lyriccs;

    MyListAdapter myAdapter = new MyListAdapter();
    ArrayList<SongAndArtist> elements = new ArrayList<>(Arrays.asList());
    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlistclass);



        ListView list = (ListView)findViewById(R.id.listviewsongs);
        list.setAdapter(myAdapter);



        loadDataFromDatabase();



        list.setOnItemClickListener((ilist,item, position, id)->{
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SONGN, String.valueOf(elements.get(position).getSong() ));
            dataToPass.putString(ITEM_LYRICS, (elements.get(position).getlyrics()));
            Intent nextActivity = new Intent(SongListClass.this, EmptyActivity.class);
            nextActivity.putExtras(dataToPass);
            startActivity(nextActivity);
        });


        list.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alterDialogueBilder = new AlertDialog.Builder(this);
            SongAndArtist selected = (SongAndArtist) list.getItemAtPosition(position);

            alterDialogueBilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + (list.indexOfChild(view) + 1) + "\nThe database id is: " + id +"\nSong name: " +elements.get(position).getSong() +"\nArtist: "+elements.get(position).getArtist())
                    .setPositiveButton("Yes", (Click, arg) -> {

                        elements.remove(position);
                        deleteMessage(selected);
                        myAdapter.notifyDataSetChanged();
                    }).setNegativeButton("No", (click, arg) -> {
            }).create().show();
            return true;
        });
    }
    protected void deleteMessage(SongAndArtist saa) {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[]{Long.toString(saa.getId())});
    }

    private void loadDataFromDatabase() {

        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_SONG, MyOpener.COL_ARTIST, MyOpener.COL_LYRICS};

        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int songIndex = results.getColumnIndex(MyOpener.COL_SONG);
        int artIndex = results.getColumnIndex(MyOpener.COL_ARTIST);
        int lyrIndex = results.getColumnIndex(MyOpener.COL_LYRICS);

        while (results.moveToNext()) {
            long id = results.getLong(idColIndex);
            songg = results.getString(songIndex);
            artists = results.getString(artIndex);
            lyriccs = results.getString(lyrIndex);


            elements.add(new SongAndArtist(id, songg, artists, lyriccs));
        }
    }


    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public SongAndArtist getItem(int position) {
            return elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return elements.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            SongAndArtist songAndArtist = getItem(position);
            View view = inflater.inflate(R.layout.favoritedisplayed, parent, false);
            TextView text = view.findViewById(R.id.textHere);
            text.setText("Song name: " + songAndArtist.getSong() + ", Artist: " + songAndArtist.getArtist());
            return view;
        }
    }


    static class MyOpener extends SQLiteOpenHelper {

        protected final static String DB_NAME = "SongsAndArtistsDB";
        protected final static int VERSION_NUM = 2;
        public final static String COL_ID = "_id";
        public final static String TABLE_NAME = "SongsAndArtists";
        public final static String COL_SONG = "SongName";
        public final static String COL_ARTIST = "ArtistName";
        public final static String COL_LYRICS = "Lyrics";


        public MyOpener(Context ctx) {
            super(ctx, DB_NAME, null, VERSION_NUM);
        }




        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_SONG + " text, "
                    + COL_ARTIST + " text, "
                    +COL_LYRICS +");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            onCreate(db);
        }
    }
}

