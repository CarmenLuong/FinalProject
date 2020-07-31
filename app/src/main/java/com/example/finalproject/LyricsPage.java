package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LyricsPage extends AppCompatActivity {
    ProgressBar prog;
    String songName;
    String artistName;
    SQLiteDatabase db;
    String lyricss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyricspage);
        Button favoriteB;
        Button goB;
        prog = findViewById(R.id.progBar);
        prog.setVisibility(View.VISIBLE);
        favoriteB = findViewById(R.id.SaveSongButton);
        goB=findViewById(R.id.returnButt);

        goB.setOnClickListener(v->{
            Intent goBack = new Intent(LyricsPage.this, SearchClass.class);
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




        SongSearchQuery que = new SongSearchQuery();
        que.execute(urlString);
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
                AlertDialog.Builder alterDialogueBilder = new AlertDialog.Builder(LyricsPage.this);
                alterDialogueBilder.setTitle("Could not generate results for:").setMessage("\nSong: \"" + songName + "\", Artist: \"" + artistName + "\"")
                        .setPositiveButton("Okay", (Click, arg) -> {
                            Intent goBack = new Intent(LyricsPage.this, SearchClass.class);
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