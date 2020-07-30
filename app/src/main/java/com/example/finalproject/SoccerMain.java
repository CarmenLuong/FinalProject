package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_main);

        ScoreQuery score = new ScoreQuery();
        score.execute("https://www.scorebat.com/video-api/v1/");

        myList = findViewById(R.id.FinalProjectList);

        adp = new SoccerAdapter(this,R.layout.game_score,matchList);
        adp.setListData(matchList);
        myList.setAdapter(adp);
        loadDataFromDatabase();

        soccerScoreObject temp = new soccerScoreObject();

        ContentValues newRowValues = new ContentValues();
        newRowValues.put(SoccerOpener.COL_TITLE,temp.getGameTitle());
        newRowValues.put(SoccerOpener.COL_DATE, temp.getDate());
        newRowValues.put(SoccerOpener.COL_URL,temp.getUrl());
        long newId = db.insert(SoccerOpener.TABLE_NAME,null, newRowValues);
        soccerScoreObject scoreEntry = new soccerScoreObject(String.valueOf(newId),temp.getGameTitle());

        matchList.add(scoreEntry);
        adp.notifyDataSetChanged();
    }


    class ScoreQuery extends AsyncTask<String, Integer, String> {

        String firstTeam, secondTeam;
        String title;
        String date;
        String game_url;
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
                for(int i=0;i<array.length();i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    title = jsonObject.getString("title");
                    date = jsonObject.getString("date");
                    game_url = jsonObject.getString("url");

                    soccerScoreObject games = new soccerScoreObject(title,date,game_url);
                }
                Log.i("MainActivity", "The title:  " + title + " date: " + date + " url :" + game_url) ;

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
            scores = findViewById(R.id.games);

//            scores.setText(matchTitle);


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

            //add the new Contact to the array list:
            matchList.add(new soccerScoreObject(String.valueOf(id), match));
        }

//        printCursor(results, 2);


    }
}