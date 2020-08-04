package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button goToSongSearch = findViewById(R.id.goToSongSearch);
        Button goToDeezer = findViewById(R.id.goToDeezer);

        goToSongSearch.setOnClickListener(v->
                { Intent goToProfile = new Intent(MainActivity.this, SongSearch.class);
                    startActivity(goToProfile);
                }

        );

        goToDeezer.setOnClickListener(v->
                { Intent goToProfile = new Intent(MainActivity.this, DeezerSearch.class);
                    startActivity(goToProfile);
                }

        );


    }
}