package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SongEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songactivity_empty);
        Bundle dataToPass = getIntent().getExtras();
        SongDetailsFragment dFragment = new SongDetailsFragment();
        dFragment.setArguments( dataToPass );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emptyframe, dFragment) //Add the fragment in FrameLayout
                .commit();
    }
}
