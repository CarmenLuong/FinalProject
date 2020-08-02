package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SearchClass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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

        Toolbar tBar = (Toolbar)findViewById(R.id.TB);
        setSupportActionBar(tBar);

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar,R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


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
            alterDialogueBilder.setMessage("Simply type in your desired song and the artist or group's name that you're looking for, then hit search!\n\nYou can select your favorites, look at them later, and if theres anything that you cant find seem to find through us, you can google it!")
                    .setPositiveButton("Okay", (Click, arg) -> {
                        Intent goBack = new Intent(SearchClass.this, SearchClass.class);
                        startActivity(goBack);
                    }).create().show();

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.soccermatchitem:
                Intent goToSoccer = new Intent(this, MainActivity.class);
                startActivity(goToSoccer);
                break;
            case R.id.geodatasourceitem:

                Intent goToGeo = new Intent(this, MainActivity.class);
                startActivity(goToGeo);
                break;
            case R.id.deezersongsearchitem:
                Intent goToDeezer = new Intent(this, MainActivity.class);
                startActivity(goToDeezer);
                break;
            case R.id.help_item:
                String message= getResources().getString(R.string.songlyrichelpitem);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        AlertDialog.Builder alterDialogueBilder;
        String message = null;

        switch(item.getItemId())
        {
            case R.id.instructions:
                alterDialogueBilder = new AlertDialog.Builder(this);
                alterDialogueBilder.setMessage("Simply type in your desired song and the artist or group's name that you're looking for, then hit search!\n\nYou can select your favorites, look at them later, and if theres anything that you cant find seem to find through us, you can google it!")
                        .setPositiveButton("Okay", (Click, arg) -> {
                            Intent goBack = new Intent(this, SongSearch.class);
                            startActivity(goBack);
                        }).create().show();
                break;
            case R.id.abouttheapi:
                String url = "https://lyricovh.docs.apiary.io/#";

                Intent goToapi = new Intent(Intent.ACTION_VIEW);
                goToapi.setData(Uri.parse(url));
                startActivity(goToapi);
                break;
            case R.id.donate:

                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                alterDialogueBilder = new AlertDialog.Builder(this);
                input.setLayoutParams(lp);
                alterDialogueBilder.setView(input);
                alterDialogueBilder.setTitle("Please give generously");
                alterDialogueBilder.setMessage("How much money do you want to donate?")
                        .setPositiveButton("Thank you", (Click, arg) -> {
                            Intent goBack = new Intent(this, SongSearch.class);
                            startActivity(goBack);
                        }).setNegativeButton("Cancel", (click, arg) -> {
                }).create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;

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