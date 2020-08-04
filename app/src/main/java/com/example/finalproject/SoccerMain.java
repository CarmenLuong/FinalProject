package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SoccerMain extends AppCompatActivity {
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
    boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_main);

        ScoreQuery score = new ScoreQuery();
        score.execute("https://www.scorebat.com/video-api/v1/");

        myList = findViewById(R.id.FinalProjectList);

        isTablet = findViewById(R.id.frame1) != null;

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

                   soccerScoreObject matchTitles = new soccerScoreObject(title,date,game_url,team1,team2);

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

//    private void loadDataFromDatabase() {
//        SoccerOpener dbOpener = new SoccerOpener(this);
//        db = dbOpener.getWritableDatabase();
//
//        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TITLE, dbOpener.COL_DATE, dbOpener.COL_URL};
//
//        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);
//
//        int dateColIndex = results.getColumnIndex(dbOpener.COL_DATE);
//        int titleColIndex = results.getColumnIndex(dbOpener.COL_TITLE);
//        int urlColIndex = results.getColumnIndex(dbOpener.COL_URL);
//        int idColIndex = results.getColumnIndex(dbOpener.COL_ID);
//
//        while(results.moveToNext())
//        {
//            String match = results.getString(titleColIndex);
//            long id = results.getLong(idColIndex);
//            String date = results.getString(dateColIndex);
//            String thisUrl = results.getString(urlColIndex);
//
////            matchList.add(new soccerScoreObject(id,match,date,thisUrl));
//
//
//        }
//
//
//
//    }
}