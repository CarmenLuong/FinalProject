package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DetailFragment extends Fragment {

    private AppCompatActivity parentActivity;
    private Bundle dataFromActivity;


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_detail, container, false);

        TextView gameTitle = (TextView)result.findViewById(R.id.gameHeader);
        gameTitle.setText(dataFromActivity.getString(SoccerMain.ITEM_SELECTED));

        TextView gameDate = (TextView)result.findViewById(R.id.date);
        gameDate.setText(dataFromActivity.getString(SoccerMain.ITEM_DATE));

        TextView team1 = (TextView)result.findViewById(R.id.team1);
        team1.setText(dataFromActivity.getString(SoccerMain.ITEM_TEAM1));

        TextView team2 = (TextView)result.findViewById(R.id.team2);
        team2.setText(dataFromActivity.getString(SoccerMain.ITEM_TEAM2));

        Button addToFav = (Button)result.findViewById(R.id.favourite);



        Button watchHighlights = (Button)result.findViewById(R.id.watchHighlights);
        watchHighlights.setOnClickListener( click -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataFromActivity.getString(SoccerMain.ITEM_URL)));
            startActivity(browserIntent);
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