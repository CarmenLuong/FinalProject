package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SoccerDeleteEmptyActivity extends AppCompatActivity {

    /**
     * this loads the data from the previous activity and into this empty activity, in order to visualize
     * a match from the favourites list
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_empty);

        Bundle dataToPass = getIntent().getExtras();

        SoccerDeleteFragment delFragment = new SoccerDeleteFragment();
        delFragment.setArguments( dataToPass );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame2, delFragment)
                .commit();
    }
}