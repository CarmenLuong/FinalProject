package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SoccerMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<soccerScoreObject> matchList = new ArrayList<>();
    SQLiteDatabase db;
    SoccerAdapter adp;
    ListView myList;
    TextView scores;
    String team1, team2;
    String title;
    String date;
    String game_url;
    public static final String ITEM_SELECTED = "GAME";
    public static final String ITEM_TEAM1 = "team1";
    public static final String ITEM_TEAM2 = "team2";
    public static final String ITEM_DATE = "date";
    public static final String ITEM_URL = "match";
    public static final String ITEM_IS_FAVORITE = "favorites";
    boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_main);

        ScoreQuery score = new ScoreQuery();
        score.execute("https://www.scorebat.com/video-api/v1/");

        myList = findViewById(R.id.FinalProjectList);

        isTablet = findViewById(R.id.frame1) != null;

        Toolbar tBar = (Toolbar)findViewById(R.id.TB);
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar,R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        adp = new SoccerAdapter(this,R.layout.game_score,matchList);
        adp.setListData(matchList);
        myList.setAdapter(adp);
//        loadDataFromDatabase();


        myList.setOnItemClickListener((list, view, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, String.valueOf(matchList.get(position).getGameTitle()));
            dataToPass.putString(ITEM_TEAM1, String.valueOf(matchList.get(position).getTeam1()));
            dataToPass.putString(ITEM_TEAM2, String.valueOf(matchList.get(position).getTeam2()));
            dataToPass.putString(ITEM_DATE, String.valueOf(matchList.get(position).getDate()));
            dataToPass.putString(ITEM_URL, String.valueOf(matchList.get(position).getUrl()));
            dataToPass.putBoolean(ITEM_IS_FAVORITE,matchList.get(position).isFavorite());

            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment();
                dFragment.setArguments( dataToPass );
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame1, dFragment)
                        .commit();
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(SoccerMain.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.songlyrichelpitem:
                Intent goToLyric = new Intent(this, MainActivity.class);
                startActivity(goToLyric);
                break;
            case R.id.geodatasourceitem:
                Intent goToGeo = new Intent(this, MainActivity.class);
                startActivity(goToGeo);
                break;
            case R.id.deezersongsearchitem:
                Intent goToDeezer = new Intent(this, MainActivity.class);
                startActivity(goToDeezer);
                break;
            case R.id.help_item:
                String message= getResources().getString(R.string.soccerHelp);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        AlertDialog.Builder alterDialogueBuilder;
        String message = "you have selected the navigation menu";

        switch(menuItem.getItemId())
        {
            case R.id.instructions:
                alterDialogueBuilder = new AlertDialog.Builder(this);
                alterDialogueBuilder.setMessage("Find any soccer match that may catch your interest!!\n\nYou can select your favorite matches, look at them later, and if there is a game that cannot be found through our app, you can try to google it!")
                        .setPositiveButton("Okay", (Click, arg) -> {
                            Intent goBack = new Intent(this, SoccerMain.class);
                            startActivity(goBack);
                        }).create().show();
                break;

                case R.id.abouttheapi:
                String url = "https://lyricovh.docs.apiary.io/#";
                Intent goToApi = new Intent(Intent.ACTION_VIEW);
                goToApi.setData(Uri.parse(url));
                startActivity(goToApi);
                break;

                case R.id.donate:
                    final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                alterDialogueBuilder = new AlertDialog.Builder(this);
                input.setLayoutParams(lp);
                alterDialogueBuilder.setView(input);
                alterDialogueBuilder.setTitle("Please give generously");
                alterDialogueBuilder.setMessage("How much money do you want to donate?")
                        .setPositiveButton("Thank you", (Click, arg) -> {
                            Intent goBack = new Intent(this, MainActivity.class);
                            startActivity(goBack);
                        }).setNegativeButton("Cancel", (click, arg) -> {
                }).create().show();

                break;
            case R.id.favourites:
                Intent SeeFavorites = new Intent(SoccerMain.this, FavoriteList.class);
                startActivity(SeeFavorites);
                message = "You clicked on favorites";
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        return false;
    }


    class ScoreQuery extends AsyncTask<String, Integer, String> {


        String link = "https://www.scorebat.com/video-api/v1/";


        @Override
        protected String doInBackground(String... args) {

            try {


                URL url = new URL(args[0]);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                InputStream response = urlConnection.getInputStream();


                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONArray array = new JSONArray(result);
                for(int i = 0; i < array.length();i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    title = jsonObject.getString("title");
                    game_url = jsonObject.getString("url");
                    date = jsonObject.getString("date");

                    JSONObject s1 = jsonObject.getJSONObject("side1");
                    JSONObject s2 = jsonObject.getJSONObject("side2");

                    team1 = s1.getString("name");
                    team2 = s2.getString("name");

                   soccerScoreObject matchTitles = new soccerScoreObject(title,date,game_url,team1,team2,false);

                   matchList.add(matchTitles);
                }

                Log.i("MainActivity", "The title:  " + title  + " url :" + game_url + " date: " + date + " team1: " +
                        team1 + " team2: " + team2) ;

            }
            catch (Exception e)
            {

            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adp.notifyDataSetChanged();


        }
    }


    private void loadDataFromDatabase() {
        SoccerOpener dbOpener = new SoccerOpener(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TITLE, dbOpener.COL_DATE, dbOpener.COL_URL};

        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int dateColIndex = results.getColumnIndex(dbOpener.COL_DATE);
        int titleColIndex = results.getColumnIndex(dbOpener.COL_TITLE);
        int urlColIndex = results.getColumnIndex(dbOpener.COL_URL);
        int idColIndex = results.getColumnIndex(dbOpener.COL_ID);

        while(results.moveToNext())
        {
            String match = results.getString(titleColIndex);
            long id = results.getLong(idColIndex);
            String date = results.getString(dateColIndex);
            String thisUrl = results.getString(urlColIndex);

            matchList.add(new soccerScoreObject(id,match,date,thisUrl));


        }
    }

}