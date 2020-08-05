package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SoccerDetailFragment extends Fragment {

    ArrayList<soccerScoreObject> favoritesList = new ArrayList<>();
    private AppCompatActivity parentActivity;
    private Bundle dataFromActivity;
    SQLiteDatabase db;
    SoccerAdapter myAdapter;
    public static final String URL = "match";



    public SoccerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myAdapter = new SoccerAdapter(getContext(),R.layout.activity_favorite_list,favoritesList);
        // Inflate the layout for this fragment
        SoccerOpener dbSoccer = new SoccerOpener(getContext());
        db = dbSoccer.getWritableDatabase();
        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_detail, container, false);
        String game = dataFromActivity.getString(SoccerMain.ITEM_SELECTED);
        String date = dataFromActivity.getString(SoccerMain.ITEM_DATE);
        String url = dataFromActivity.getString(SoccerMain.ITEM_URL);
        String firstTeam = dataFromActivity.getString(SoccerMain.ITEM_TEAM1);
        String secondTeam = dataFromActivity.getString(SoccerMain.ITEM_TEAM2);

        TextView gameTitle = (TextView)result.findViewById(R.id.gameHeader);
        gameTitle.setText(dataFromActivity.getString(SoccerMain.ITEM_SELECTED));

        TextView gameDate = (TextView)result.findViewById(R.id.date);
        gameDate.setText(dataFromActivity.getString(SoccerMain.ITEM_DATE));

        TextView team1 = (TextView)result.findViewById(R.id.team1);
        team1.setText(dataFromActivity.getString(SoccerMain.ITEM_TEAM1));

        TextView team2 = (TextView)result.findViewById(R.id.team2);
        team2.setText(dataFromActivity.getString(SoccerMain.ITEM_TEAM2));

        Button addToFav = (Button)result.findViewById(R.id.favourite);
        Long mId = dataFromActivity.getLong(SoccerMain.ITEM_ID);


        addToFav.setOnClickListener(Click -> {
                
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(SoccerOpener.COL_TITLE, game);
            newRowValues.put(SoccerOpener.COL_DATE, date);
            newRowValues.put(SoccerOpener.COL_URL,url);
            newRowValues.put(SoccerOpener.COL_TEAM1,firstTeam);
            newRowValues.put(SoccerOpener.COL_TEAM2,secondTeam);
            long newId = db.insert(SoccerOpener.TABLE_NAME,null, newRowValues);
            Snackbar.make(addToFav,"\"" + dataFromActivity.getString(SoccerMain.ITEM_SELECTED) + " was added to favorites!",Snackbar.LENGTH_LONG).show();

            soccerScoreObject newGame = new soccerScoreObject(newId,game,date,url,firstTeam,secondTeam,true);
            favoritesList.add(newGame);
            myAdapter.setListData(favoritesList);

        });

        Button watchHighlights = (Button)result.findViewById(R.id.watchHighlights);
        watchHighlights.setOnClickListener( click -> {
            Bundle urlToPass = new Bundle();
            urlToPass.putString(URL,url);


                Intent intent = new Intent(getActivity(), SoccerWebViewActivity.class);
                intent.putExtras(urlToPass);
                startActivity(intent);

//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataFromActivity.getString(SoccerMain.ITEM_URL)));
//            startActivity(browserIntent);
        });

        return result;
    }

    public boolean contains(ArrayList<soccerScoreObject> matches, soccerScoreObject game){
        for(soccerScoreObject e : matches){
            if(e.equals(game))
                return true;
        }
        return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }



}