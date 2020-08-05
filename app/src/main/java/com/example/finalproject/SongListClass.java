package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class SongListClass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

        Toolbar tBar = (Toolbar)findViewById(R.id.TB);
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar,R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);



        ListView list = (ListView)findViewById(R.id.listviewsongs);
        list.setAdapter(myAdapter);



        loadDataFromDatabase();



        list.setOnItemClickListener((ilist,item, position, id)->{
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SONGN, String.valueOf(elements.get(position).getSong() ));
            dataToPass.putString(ITEM_LYRICS, (elements.get(position).getlyrics()));
            Intent nextActivity = new Intent(SongListClass.this, SongEmptyActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.soccermatchitem:
                Intent goToSoccer = new Intent(this, MainActivity.class);
                startActivity(goToSoccer);
                break;
            case R.id.geodatasourceitem:

                Intent goToGeo = new Intent(this, GeoDataActivity.class);
                startActivity(goToGeo);
                break;
            case R.id.deezersongsearchitem:
                Intent goToDeezer = new Intent(this, DeezerSearch.class);
                startActivity(goToDeezer);
                break;
            case R.id.help_item:
                String message= getResources().getString(R.string.songlyrichelpitem);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        AlertDialog.Builder alterDialogueBilder;
        String message = null;

        switch(item.getItemId())
        {
            case R.id.instructions:
                alterDialogueBilder = new AlertDialog.Builder(this);
                alterDialogueBilder.setMessage("Simply type in your desired song and the artist or group's name that you're looking for, then hit search!\n\nYou can select your favorites, look at them later, and if theres anything that you cant find seem to find through us, you can google it!")
                        .setPositiveButton("Okay", (Click, arg) -> {
                            Intent goBack = new Intent(this, SongSearch.class);
                            startActivity(goBack);
                        }).create().show();
                break;
            case R.id.abouttheapi:
                String url = "https://lyricovh.docs.apiary.io/#";

                Intent goToapi = new Intent(Intent.ACTION_VIEW);
                goToapi.setData(Uri.parse(url));
                startActivity(goToapi);
                break;
            case R.id.donate:

                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                alterDialogueBilder = new AlertDialog.Builder(this);
                input.setLayoutParams(lp);
                alterDialogueBilder.setView(input);
                alterDialogueBilder.setTitle("Please give generously");
                alterDialogueBilder.setMessage("How much money do you want to donate?")
                        .setPositiveButton("Thank you", (Click, arg) -> {
                            Intent goBack = new Intent(this, SongSearch.class);
                            startActivity(goBack);
                        }).setNegativeButton("Cancel", (click, arg) -> {
                }).create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;

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
            View view = inflater.inflate(R.layout.songfavoritedisplayed, parent, false);
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

