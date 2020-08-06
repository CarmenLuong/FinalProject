package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.ArrayList;
import java.util.Arrays;

public class DeezerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ProgressBar deezerLoadingBar;
    String searchedString;
    EditText deezerSearchBox;
    Button deezerArtistSearch;
    Button deezerFavouriteButton;
    Button deezerSaveButton;
    URL artistUrl;
    URL searchedUrl;
    String tracklist;
    Bitmap albumCoverImage;
    String albumCoverUrl;
    String songDuration;
    String songTitle;
    String albumName;
    String iconName;
    ArrayList<DeezerArtistClass> savedArtistArray = new ArrayList<>();
    SQLiteDatabase sqlDB;

    /**
     * file existance is intended to confirm a file exists before redownloading it
     *
     */
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
        deezerFavouriteButton = findViewById(R.id.favouriteButton);
        deezerSaveButton = findViewById(R.id.deezerSaveButton);
        Toolbar tBar = (Toolbar)findViewById(R.id.TB);
        setSupportActionBar(tBar);


//        loadFromDataBase();

        deezerLoadingBar.setVisibility(View.VISIBLE);

        deezerSaveButton.setOnClickListener( v -> {
            DeezerDatabase savedDB;
            String searchToSave = deezerSearchBox.getText().toString();
            ContentValues newRow = new ContentValues();
            newRow.put(DeezerDatabase.COL_SEARCH_TITLE, searchToSave);
            long newID = sqlDB.insert(DeezerDatabase.TABLE_NAME, null, newRow);
            DeezerArtistClass saveSearch = new DeezerArtistClass(newID, searchToSave);
            savedArtistArray.add(saveSearch);
            savedDB = new DeezerDatabase(this);
            sqlDB = savedDB.getWritableDatabase();
            deezerSearchBox.setText("");
        });

        deezerFavouriteButton.setOnClickListener( v -> {
            Intent goToDeezerList = new Intent(DeezerActivity.this, DeezerListView.class);
            startActivity(goToDeezerList);
        });

        deezerArtistSearch.setOnClickListener( v -> {
            tracklist = null;
            //clear arraylist for listview
            searchedString = deezerSearchBox.getText().toString().replace(" ", "%20");
            if (!searchedString.equals("")) {
//                DeezerQuery query = new DeezerQuery();
//                query.execute("https://api.deezer.com/search/artist/?q=" + searchedString + "&output=xml");
                Log.e("DeezerActivity", "should run query");
              //  Log.i("DeezerActivity", "The variables are: " + songTitle + " " + songDuration + " " + songTitle + " " + albumCoverUrl + " " + tracklist);


//                Bundle extra = new Bundle();
//                extra.putSerializable("artistArrayList", savedArtistArray);

                Intent goToDeezerSearchedArtist = new Intent(DeezerActivity.this, DeezerSearchedArtist.class);
                goToDeezerSearchedArtist.putExtra("searchedString", searchedString);
                goToDeezerSearchedArtist.putExtra("Url", tracklist);
//                goToDeezerSearchedArtist.putExtra("artistTitle", songTitle);


//                goToDeezerSearchedArtist.putExtra("extra", extra);
//                goToDeezerSearchedArtist.putExtra("songDuration", songDuration);
//                goToDeezerSearchedArtist.putExtra("albumName", albumName);
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

    /**
     * printCursor is intended to pass through and track each entry inside the database
     */
    void printCursor(Cursor c, int version) {

        int searchIndex = c.getColumnIndex(DeezerDatabase.COL_SEARCH_TITLE);
        int idIndex = c.getColumnIndex(DeezerDatabase.COL_ID);
        StringBuilder sb = new StringBuilder("\nResults list: ");

        if (c.moveToFirst()) {
            do {
                String search = c.getString(searchIndex);
                long id = c.getLong(idIndex);
                sb.append("\nRow: ").append(c.getPosition()).append("\nId: ").append(id).append("\nMessage: ")
                        .append(search + "\n");
            } while (c.moveToNext());
        }
        Log.i("DeezerActivity", sb.toString());
    }

    /**
     * loadFromDataBase is intended to load entries in the database to be displayed after a user leaves the app
     */
    protected void loadFromDataBase() {
        DeezerDatabase myOpener = new DeezerDatabase(this);
        sqlDB = myOpener.getWritableDatabase();

        String[] columns = {DeezerDatabase.COL_ID, DeezerDatabase.COL_SEARCH_TITLE};

        Cursor results = sqlDB.query(false, DeezerDatabase.TABLE_NAME, columns, null, null, null, null, null, null);

        int idColIndex = results.getColumnIndex(DeezerDatabase.COL_ID);
        int searchColumnIndex = results.getColumnIndex(DeezerDatabase.COL_SEARCH_TITLE);

        while(results.moveToNext())
        {
            Long id = results.getLong(idColIndex);
            String search = results.getString(searchColumnIndex);

            //add the new Contact to the array list:
            savedArtistArray.add(new DeezerArtistClass(id, search));
        }
        printCursor(results, 1);
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

    /**
     * DeezerQuery is intended to search the api and collect information releveant to the users search
     */
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
//                                JSONObject artistInfo = new JSONObject(result);
//


                                Log.e("DeezerActivity", "opened "+ tracklist +" url");
                                Log.e("DeezerActivity", "result string is " + result);

                                JSONObject artistInfo = new JSONObject(result);
                                JSONArray artistData = artistInfo.getJSONArray("data");

                                for(int i = 0; i < artistData.length(); i++) {
                                    JSONObject jsonObject = artistData.getJSONObject(i);


                                    songTitle = jsonObject.getString("title");
//                                    songDuration = String.valueOf((String) artistInfo.getString("duration"));
//                                    albumName = String.valueOf((String) artistInfo.getString("album"));
//                                    albumCoverUrl = String.valueOf((String) artistInfo.getString("cover"));
                                    savedArtistArray.add(new DeezerArtistClass(songTitle));
                                    Log.i("DeezerActivity", "The song titles are: " + songTitle) ;

                                }

//                                for (int i = 0 ; i < artistData.length() ; i++) {
//                                    Log.e("DeezerActivity", "Made it in the first loop");
//                                    JSONObject jsonObject = artistInfo.getJSONObject(i);
//                                    JSONArray artistData = jsonObject.getJSONArray("data");
//                                    for (int l = 0; l < artistData.length(); l++) {
//                                        Log.e("DeezerActivity", "Made it in the second loop");
//                                        jsonObject = artistData.getJSONObject(l);
//                                        songTitle = jsonObject.getString("title");
//                                        Log.i("DeezerActivity", "The song titles are: " + songTitle) ;
//                                    }
//                                }

                                Log.i("DeezerActivity", "The array of titles is" + savedArtistArray.toString());
                               // Log.i("DeezerActivity", "The variables are: " + songTitle + " " + songDuration + " " + songTitle + " " + albumCoverUrl + " " + tracklist) ;

                            } catch (Exception e){
                            e.printStackTrace();
                            Log.e("DeezerActivity", "I fucked up");
                            }
//                            String imageFileName = albumCoverUrl;
//                            try{
//                                if(!fileExistance(imageFileName)) {
//                                    albumCoverImage = null;
//
//                                    URL url = new URL(albumCoverUrl);
//
//                                    urlConnection = (HttpURLConnection) url.openConnection();
//                                    urlConnection.connect();
//                                    int responseCode = urlConnection.getResponseCode();
//                                    if (responseCode == 200) {
//                                        albumCoverImage = BitmapFactory.decodeStream(urlConnection.getInputStream());
//                                        publishProgress(100);
//                                    }
//                                    FileOutputStream outputStream = openFileOutput(imageFileName, Context.MODE_PRIVATE);
//                                    albumCoverImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//                                    outputStream.flush();
//                                    outputStream.close();
//                                    Log.i("DeezerActivity", "Image downloaded");
//                                } else{
//                                    FileInputStream fis = null;
//                                    try {    fis = openFileInput(imageFileName);   }
//                                    catch (FileNotFoundException e) {    e.printStackTrace();  }
//                                    albumCoverImage = BitmapFactory.decodeStream(fis);
//                                    Log.i("DeezerActivity", "Image used from " + iconName + ".png, already downloaded");
//
//                                }
//
//                            } catch(Exception e){
//
//                            }
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


        @Override
        protected void onPostExecute(String s) {

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
                String url = "https://developers.deezer.com/guidelines";

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