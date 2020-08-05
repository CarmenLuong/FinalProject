package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class SoccerFavoriteList extends AppCompatActivity {

    SQLiteDatabase db;
    ArrayList<soccerScoreObject> favoritesList = new ArrayList<>();
    SoccerAdapter adp;
    ListView myList;
    public static final String ITEM_ID = "id";
    public static final String ITEM_SELECTED = "GAME";
    public static final String ITEM_TEAM1 = "team1";
    public static final String ITEM_TEAM2 = "team2";
    public static final String ITEM_DATE = "date";
    public static final String ITEM_URL = "match";
    public static final String ITEM_IS_FAVORITE = "favorites";
    boolean isTablet;
    ProgressBar favLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        myList = findViewById(R.id.FavoritesList);
        favLoading = findViewById(R.id.favBar);
        favLoading.setVisibility(View.VISIBLE);

        adp = new SoccerAdapter(this,R.layout.favorites_score,favoritesList);
        loadDataFromDatabase();
        adp.setListData(favoritesList);
        myList.setAdapter(adp);
        favLoading.setProgress(100);
        favLoading.setVisibility(View.GONE);




        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alterDialogueBuilder = new AlertDialog.Builder(this);
            soccerScoreObject games = (soccerScoreObject) myList.getItemAtPosition(position);

            alterDialogueBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + (myList.indexOfChild(view)+1)+ "\nThe database Id is: " + favoritesList.get(position).getId())
                    .setPositiveButton("Yes",(Click, arg)->{
                        favoritesList.remove(position);
                        deleteMessage(games);
                        adp.notifyDataSetChanged();
                    }).setNegativeButton("No", (Click, arg) ->{

            }).create().show();
            return true;
        });

        myList.setOnItemClickListener((list, view, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putLong(ITEM_ID, favoritesList.get(position).getId());
            dataToPass.putString(ITEM_SELECTED, String.valueOf(favoritesList.get(position).getGameTitle()));
            dataToPass.putString(ITEM_TEAM1, String.valueOf(favoritesList.get(position).getTeam1()));
            dataToPass.putString(ITEM_TEAM2, String.valueOf(favoritesList.get(position).getTeam2()));
            dataToPass.putString(ITEM_DATE, String.valueOf(favoritesList.get(position).getDate()));
            dataToPass.putString(ITEM_URL, String.valueOf(favoritesList.get(position).getUrl()));
            dataToPass.putBoolean(ITEM_IS_FAVORITE,favoritesList.get(position).isFavorite());

            if(isTablet)
            {
                SoccerDeleteFragment delFragment = new SoccerDeleteFragment();
                delFragment.setArguments( dataToPass );
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame2, delFragment)
                        .commit();
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(SoccerFavoriteList.this, SoccerDeleteEmptyActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }
        });




    }

    private void loadDataFromDatabase() {
        SoccerOpener dbOpener = new SoccerOpener(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TITLE, dbOpener.COL_DATE, dbOpener.COL_URL,dbOpener.COL_TEAM1,dbOpener.COL_TEAM2};

        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int dateColIndex = results.getColumnIndex(dbOpener.COL_DATE);
        int titleColIndex = results.getColumnIndex(dbOpener.COL_TITLE);
        int urlColIndex = results.getColumnIndex(dbOpener.COL_URL);
        int idColIndex = results.getColumnIndex(dbOpener.COL_ID);
        int team1ColIndex = results.getColumnIndex(dbOpener.COL_TEAM1);
        int team2ColIndex = results.getColumnIndex(dbOpener.COL_TEAM2);

        while(results.moveToNext())
        {
            String match = results.getString(titleColIndex);
            long id = results.getLong(idColIndex);
            String date = results.getString(dateColIndex);
            String thisUrl = results.getString(urlColIndex);
            String mTeam1 = results.getString(team1ColIndex);
            String mTeam2 = results.getString(team2ColIndex);

            favoritesList.add(new soccerScoreObject(id,match,date,thisUrl,mTeam1,mTeam2));


        }
    }

    protected void deleteMessage(soccerScoreObject c)
    {
        db.delete(SoccerOpener.TABLE_NAME, SoccerOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }


}