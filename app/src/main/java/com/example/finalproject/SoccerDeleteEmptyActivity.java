package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SoccerDeleteEmptyActivity extends AppCompatActivity {

    // for phone layout

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