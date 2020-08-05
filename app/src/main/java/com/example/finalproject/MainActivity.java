package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button goToSongSearch = findViewById(R.id.goToSongSearch);
        Button goToDeezer = findViewById(R.id.goToDeezer);
        Button goToGeoData = findViewById(R.id.goToGeoDataButton);

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

        goToGeoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGeoData = new Intent(MainActivity.this, GeoDataActivity.class);
                startActivity(goToGeoData);
            }
        });

    }
}