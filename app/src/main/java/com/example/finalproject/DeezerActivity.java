package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeezerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ProgressBar deezerLoadingBar;
    String searchedString;
    EditText deezerSearchBox;
    Button deezerArtistSearch;
    URL artistUrl;
    URL searchedUrl;
    String tracklist;
    Bitmap albumCoverImage;
    String albumCoverUrl;
    String songDuration;
    String songTitle;
    String albumName;
    String iconName;

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer);

        deezerLoadingBar = findViewById(R.id.deezerLoadingBar);
        deezerSearchBox = findViewById(R.id.searchedSong);
        deezerArtistSearch = findViewById(R.id.searchButton);
        Toolbar tBar = (Toolbar)findViewById(R.id.TB);
        setSupportActionBar(tBar);

        deezerLoadingBar.setVisibility(View.VISIBLE);

        deezerArtistSearch.setOnClickListener( v -> {
            tracklist = null;
            //clear arraylist for listview
            searchedString = deezerSearchBox.getText().toString().replace(" ", "%20");
            if (!searchedString.equals("")) {
                DeezerQuery query = new DeezerQuery();
                query.execute("https://api.deezer.com/search/artist/?q=" + searchedString + "&output=xml");
                Log.e("DeezerActivity", "should run query");
                Intent goToDeezerSearchedArtist = new Intent(DeezerActivity.this, DeezerSearchedArtist.class);
                startActivity(goToDeezerSearchedArtist);

            } else {
                Toast.makeText(DeezerActivity.this, getResources().getString(R.string.errorSearching), Toast.LENGTH_LONG).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar,R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_deezer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.soccermatchitem:
                Intent goToSoccer = new Intent(DeezerActivity.this, MainActivity.class);
                startActivity(goToSoccer);
                break;
            case R.id.geodatasourceitem:

                Intent goToGeo = new Intent(DeezerActivity.this, MainActivity.class);
                startActivity(goToGeo);
                break;
            case R.id.lyricsongsearchitem:
                Intent goToSongSearch = new Intent(DeezerActivity.this, SongListClass.class);
                startActivity(goToSongSearch);
                break;
            case R.id.help_item:
                String message= getResources().getString(R.string.deezerhelpitem);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    private class DeezerQuery extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... args) {
            try {

                searchedUrl = new URL(args [0]);

                HttpURLConnection urlConnection = (HttpURLConnection) searchedUrl.openConnection();

                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");


                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT && tracklist == null)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equals("tracklist")) {
                            xpp.next();
                            tracklist = xpp.getText();
                            DeezerQuery artistQuery = new DeezerQuery();

                            try {
                                artistUrl = new URL(tracklist);

                                urlConnection = (HttpURLConnection) artistUrl.openConnection();

                                //wait for data:
                                response = urlConnection.getInputStream();

                                //JSON reading:   Look at slide 26
                                //Build the entire string response:
                                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                                StringBuilder sb = new StringBuilder();

                                String line = null;
                                while ((line = reader.readLine()) != null)
                                {
                                    sb.append(line + "\n");
                                }
                                String result = sb.toString(); //result is the whole string


                                // convert string to JSON: Look at slide 27:
                                JSONObject artistInfo = new JSONObject(result);

                                JSONArray artistData = artistInfo.getJSONArray("data");

                                int i;
                                for(i = 0; i < artistData.length(); i++) {

                                    songTitle = String.valueOf((String) artistInfo.getString("title"));
                                    songDuration = String.valueOf((String) artistInfo.getString("duration"));
                                    albumName = String.valueOf((String) artistInfo.getString("album"));
                                    albumCoverUrl = String.valueOf((String) artistInfo.getString("cover"));
                                }
                                Log.i("MainActivity", "The variables are: " + songTitle + " " + songDuration + " " + songTitle + " " + albumCoverUrl) ;

                            } catch (Exception e){

                            }
                            String imageFileName = albumCoverUrl;
                            try{
                                if(!fileExistance(imageFileName)) {
                                    albumCoverImage = null;

                                    URL url = new URL(albumCoverUrl);

                                    urlConnection = (HttpURLConnection) url.openConnection();
                                    urlConnection.connect();
                                    int responseCode = urlConnection.getResponseCode();
                                    if (responseCode == 200) {
                                        albumCoverImage = BitmapFactory.decodeStream(urlConnection.getInputStream());
                                        publishProgress(100);
                                    }
                                    FileOutputStream outputStream = openFileOutput(imageFileName, Context.MODE_PRIVATE);
                                    albumCoverImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    Log.i("DeezerActivity", "Image downloaded");
                                } else{
                                    FileInputStream fis = null;
                                    try {    fis = openFileInput(imageFileName);   }
                                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                                    albumCoverImage = BitmapFactory.decodeStream(fis);
                                    Log.i("DeezerActivity", "Image used from " + iconName + ".png, already downloaded");

                                }

                            } catch(Exception e){

                            }
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                Log.i("DeezerActivity", "Current Variables: " + tracklist);
            }

            catch (Exception e)
            {

            }
            return "done";

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            deezerLoadingBar.setVisibility(View.VISIBLE);
            deezerLoadingBar.setProgress(values[0]);
        }

        String songDuration;
        String songTitle;
        String albumName;
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            songDuration = findViewById(R.id.minTempTextView);
            maxView = findViewById(R.id.maxTempTextView);
            currentView = findViewById(R.id.currentTempTextView);
            uvView = findViewById(R.id.uvTextView);
            weatherIView = findViewById(R.id.weatherImageView);

            minView.setText(getString(R.string.minTempString, minTemp));
            maxView.setText(getString(R.string.maxTempString, maxTemp));
            currentView.setText(getString(R.string.currentTempString, currentTemp));
            uvView.setText(getString(R.string.uvString, uv));
            weatherIView.setImageBitmap(currentWeatherImage);
            weatherLoadingBar.setVisibility(View.INVISIBLE);
        }
    }

    public boolean onNavigationItemSelected( MenuItem item) {
        AlertDialog.Builder alterDialogueBilder;
        String message = null;

        switch(item.getItemId())
        {
            case R.id.instructions:
                alterDialogueBilder = new AlertDialog.Builder(DeezerActivity.this);
                alterDialogueBilder.setMessage("Use this search to find and play your favourite songs.\n\nYou can always save your favorites for later listening!")
                        .setPositiveButton("Okay", (Click, arg) -> {
                            Intent goBack = new Intent(DeezerActivity.this, DeezerActivity.class);
                            startActivity(goBack);
                        }).create().show();
                break;
            case R.id.abouttheapi:
                String url = "https://rapidapi.com/deezerdevs/api/deezer-1";

                Intent goToapi = new Intent(Intent.ACTION_VIEW);
                goToapi.setData(Uri.parse(url));
                startActivity(goToapi);
                break;
            case R.id.donate:

                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                alterDialogueBilder = new AlertDialog.Builder(DeezerActivity.this);
                input.setLayoutParams(lp);
                alterDialogueBilder.setView(input);
                alterDialogueBilder.setTitle("Please give generously");
                alterDialogueBilder.setMessage("How much money do you want to donate?")
                        .setPositiveButton("Thank you", (Click, arg) -> {
                            Intent goBack = new Intent(DeezerActivity.this, DeezerSearch.class);
                            startActivity(goBack);
                        }).setNegativeButton("Cancel", (click, arg) -> {
                }).create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;

    }
}