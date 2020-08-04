package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    SQLiteDatabase db;
    ArrayList<soccerScoreObject> favoritesList = new ArrayList<>();
    SoccerAdapter adp;
    ListView myList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

//        myList = findViewById(R.id.FavoritesList);

        adp = new SoccerAdapter(this,R.layout.favorites_score,favoritesList);
        loadDataFromDatabase();
        adp.setListData(favoritesList);
        myList.setAdapter(adp);

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

            favoritesList.add(new soccerScoreObject(id,match,date,thisUrl));


        }
    }

    protected void deleteMessage(soccerScoreObject c)
    {
        db.delete(SoccerOpener.TABLE_NAME, SoccerOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }


}