package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SoccerEmptyActivity extends AppCompatActivity {

    /**
     * this loads the data from the previous activity and into this empty activity, in order to visualize
     * a match from the list view
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Bundle dataToPass = getIntent().getExtras();

        SoccerDetailFragment dFragment = new SoccerDetailFragment();
        dFragment.setArguments( dataToPass );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame1, dFragment)
                .commit();
    }
}