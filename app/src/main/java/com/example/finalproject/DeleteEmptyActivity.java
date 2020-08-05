package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DeleteEmptyActivity extends AppCompatActivity {

    // for phone layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_empty);

        Bundle dataToPass = getIntent().getExtras();

        DeleteFragment delFragment = new DeleteFragment();
        delFragment.setArguments( dataToPass );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame2, delFragment)
                .commit();
    }
}