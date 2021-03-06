package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 *
 * this class allows the user to visualize the fragment in the activity delete fragment
 * from the list view, here you will find the match highlights, the date, the two teams and
 * the remove from favourites button
 */

public class SoccerDeleteFragment extends Fragment {

    ArrayList<soccerScoreObject> favoritesList = new ArrayList<>();
    private AppCompatActivity parentActivity;
    private Bundle dataFromActivity;
    SQLiteDatabase db;
    SoccerAdapter myAdapter;
    public static final String URL = "match";


    // for tablet

    public SoccerDeleteFragment() {
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
        View result =  inflater.inflate(R.layout.activity_delete_fragment, container, false);

        Long gameId = dataFromActivity.getLong(SoccerFavoriteList.ITEM_ID);
        String url = dataFromActivity.getString(SoccerFavoriteList.ITEM_URL);

        TextView gameTitle = (TextView)result.findViewById(R.id.gameHeader);
        gameTitle.setText(dataFromActivity.getString(SoccerFavoriteList.ITEM_SELECTED));

        TextView gameDate = (TextView)result.findViewById(R.id.date);
        gameDate.setText(dataFromActivity.getString(SoccerFavoriteList.ITEM_DATE));

        TextView team1 = (TextView)result.findViewById(R.id.favTeam1);
        team1.setText(dataFromActivity.getString(SoccerFavoriteList.ITEM_TEAM1));

        TextView team2 = (TextView)result.findViewById(R.id.favTeam2);
        team2.setText(dataFromActivity.getString(SoccerFavoriteList.ITEM_TEAM2));

        /**
         *
         * this button allows the user to watch the highlights of the specific game in a webview
         */

        Button watchHighlights = (Button)result.findViewById(R.id.watchHighlights);
        watchHighlights.setOnClickListener( click -> {
            Bundle urlToPass = new Bundle();
            urlToPass.putString(URL,url);


            Intent intent = new Intent(getActivity(), SoccerWebViewActivity.class);
            intent.putExtras(urlToPass);
            startActivity(intent);
        });

        /**
         *
         * this button removes the match from favourites and deletes it from the database
         */

        Button deleteFromDb = (Button)result.findViewById(R.id.RemoveFavourite);
        deleteFromDb.setOnClickListener( click -> {
            db.execSQL("DELETE FROM " + SoccerOpener.TABLE_NAME +  " WHERE " + SoccerOpener.COL_ID + "=?",
                    new String[] {String.valueOf(gameId)});
            Snackbar.make(deleteFromDb,"\"" + dataFromActivity.getString(SoccerFavoriteList.ITEM_SELECTED) + " was removed from favorites!",Snackbar.LENGTH_LONG).show();

        });


        return result;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

}