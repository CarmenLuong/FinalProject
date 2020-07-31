package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchclass);

        EditText songName;
        EditText artist;
        ImageButton google;
        ImageButton help;
        Button search;
        Button favorite;

        songName = findViewById(R.id.songNameText);
        artist=findViewById(R.id.ArtistNameText);
        google = findViewById(R.id.googleButton);
        help = findViewById(R.id.helpButton);
        search=findViewById(R.id.letsGo);
        favorite=findViewById(R.id.favouritesTab);

        search.setOnClickListener(v->
        {Intent goToLyrics = new Intent(SearchClass.this, LyricsPage.class);
            goToLyrics.putExtra("songNameSaved", songName.getText().toString() );
            goToLyrics.putExtra("artistNameSaved", artist.getText().toString());
            songName.setText("");
            artist.setText("");
            startActivity(goToLyrics);
        }
        );

        favorite.setOnClickListener(v->
        {
            Intent goTofavs = new Intent(SearchClass.this, SongListClass.class);
            startActivity(goTofavs);
        });

        google.setOnClickListener(v-> {
            String url = "https://www.google.ca/search?q=" + songName.getText().toString() + "+" + artist.getText().toString();
            url = url.replaceAll(" ", "+");
            Intent goToG = new Intent(Intent.ACTION_VIEW);
            songName.setText("");
            artist.setText("");
            goToG.setData(Uri.parse(url));
            startActivity(goToG);
        } );

        help.setOnClickListener(v->{
            AlertDialog.Builder alterDialogueBilder = new AlertDialog.Builder(SearchClass.this);
            alterDialogueBilder.setMessage("Simply type in your desired song and the artist or group's name that you're looking for, then hit search!\n\n  You can select your favorites, look at them later, and if theres anything that you cant find seem to find through us, you can google it!")
                    .setPositiveButton("Okay", (Click, arg) -> {
                        Intent goBack = new Intent(SearchClass.this, SearchClass.class);
                        startActivity(goBack);
                    }).create().show();

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}