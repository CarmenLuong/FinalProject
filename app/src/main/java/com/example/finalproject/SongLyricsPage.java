package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SongLyricsPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ProgressBar prog;
    String songName;
    String artistName;
    SQLiteDatabase db;
    String lyricss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlyricspage);
        Button favoriteB;
        Button goB;
        prog = findViewById(R.id.progBar);
        prog.setVisibility(View.VISIBLE);
        favoriteB = findViewById(R.id.SaveSongButton);
        goB=findViewById(R.id.returnButt);

        Toolbar tBar = (Toolbar)findViewById(R.id.TB);
        setSupportActionBar(tBar);

        goB.setOnClickListener(v->{
            Intent goBack = new Intent(SongLyricsPage.this, SongSearchClass.class);
            startActivity(goBack);
        });
        favoriteB.setOnClickListener(v->{
            ContentValues newRowValues= new ContentValues();
            SongListClass.MyOpener dbOpen = new SongListClass.MyOpener(this);
            db = dbOpen.getWritableDatabase();
            newRowValues.put(SongListClass.MyOpener.COL_SONG, songName);
            newRowValues.put(SongListClass.MyOpener.COL_ARTIST,artistName);
            newRowValues.put(SongListClass.MyOpener.COL_LYRICS, lyricss);
            long newId = db.insert(SongListClass.MyOpener.TABLE_NAME, null, newRowValues);
            Snackbar.make(favoriteB, "\""+songName+ "\" by: "+artistName + " was added to favorites!", Snackbar.LENGTH_LONG).show();

            });



        Intent fromSearch = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            songName = fromSearch.getStringExtra("songNameSaved");
            artistName = fromSearch.getStringExtra("artistNameSaved");
        }
        String urlString = "https://api.lyrics.ovh/v1/" + artistName + "/" + songName;
        urlString = urlString.replaceAll(" ", "%20");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar,R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        SongSearchQuery que = new SongSearchQuery();
        que.execute(urlString);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.soccermatchitem:
                Intent goToSoccer = new Intent(this, SoccerMain.class);
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
    class SongSearchQuery extends AsyncTask<String, Integer, String> {



        private HttpURLConnection urlConnection;
        JSONObject obj;

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(args[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();


                obj = new JSONObject(result);
                lyricss = (String) obj.getString("lyrics");
                publishProgress(50);


            } catch (Exception e) {

            }
            return "done";
        }

        public void onProgressUpdate(Integer... Values) {
            super.onProgressUpdate();

            prog = findViewById(R.id.progBar);
            prog.setVisibility(View.VISIBLE);
            prog.setProgress(Values[0]);
        }

        public void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView lyricst = findViewById(R.id.lyrics);
            TextView lyricTitle = findViewById(R.id.LyricsPageTitle);

            if (lyricss==null) {
                AlertDialog.Builder alterDialogueBilder = new AlertDialog.Builder(SongLyricsPage.this);
                alterDialogueBilder.setTitle("Could not generate results for:").setMessage("\nSong: \"" + songName + "\", Artist: \"" + artistName + "\"")
                        .setPositiveButton("Okay", (Click, arg) -> {
                            Intent goBack = new Intent(SongLyricsPage.this, SongSearchClass.class);
                            startActivity(goBack);
                        }).create().show();
            } else {

                lyricst.setText(lyricss);
                lyricTitle.setText(songName);
                prog.setVisibility(View.INVISIBLE);

            }
            }
        }
    }