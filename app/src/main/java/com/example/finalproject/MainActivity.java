package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button goToScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToScore = findViewById(R.id.SoccerMatch);

        goToScore.setOnClickListener( Click -> { Intent SeeScores = new Intent (MainActivity.this, SoccerMain.class);
        startActivity(SeeScores);

        });


    }


}