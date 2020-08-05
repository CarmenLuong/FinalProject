package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.material.snackbar.Snackbar;


public class DeezerSearch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button loginButton;
    EditText email;
    String stringToSave;
    SharedPreferences myPrefs=null;
    ImageButton questionButton;
    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_search);

        email = findViewById(R.id.DeezerEmail);
        loginButton = findViewById(R.id.loginButton);
        questionButton = findViewById(R.id.questionButton);
        Toolbar tBar = (Toolbar)findViewById(R.id.TB);
        setSupportActionBar(tBar);



        myPrefs = getSharedPreferences("Email", Context.MODE_PRIVATE);
        String savedString = myPrefs.getString("email", "");
        email.setText(savedString);
        questionButton.setOnClickListener(v ->
                Toast.makeText(DeezerSearch.this, getResources().getString(R.string.deezeremailtoast), Toast.LENGTH_LONG).show());

        loginButton.setOnClickListener(v ->
        {
            String com = ".com";
            String ca = ".ca";

            if (!email.getText().toString().contains("@") || !(email.getText().toString().contains(com) || email.getText().toString().contains(ca))  || email.getText().toString() == "") {
                String blankEmail = "Email cannot be blank";
                String invalidEmail = "\""+email.getText() + "\" is not a valid email address. Please try again";

                if (email.getText().length()==0) {
                    Snackbar.make(email, blankEmail, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(email, invalidEmail, Snackbar.LENGTH_LONG).show();
                }

            } else {
                Intent goToProfile = new Intent(DeezerSearch.this, DeezerActivity.class);

                goToProfile.putExtra("email", savedString);
                startActivity(goToProfile);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar,R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_deezer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.soccermatchitem:
                Intent goToSoccer = new Intent(DeezerSearch.this, SoccerMain.class);
                startActivity(goToSoccer);
                break;
            case R.id.geodatasourceitem:

                Intent goToGeo = new Intent(DeezerSearch.this, GeoDataActivity.class);
                startActivity(goToGeo);
                break;
            case R.id.lyricsongsearchitem:
                Intent goToSongSearch = new Intent(DeezerSearch.this, SongSearch.class);
                startActivity(goToSongSearch);
                break;
            case R.id.help_item:
                String message= getResources().getString(R.string.deezerhelpitem);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }




    protected void onPause(){
        super.onPause();
        stringToSave = email.getText().toString();
        SharedPreferences.Editor ed = myPrefs.edit();
        ed.putString("email", stringToSave);
        ed.commit();
    }


    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        AlertDialog.Builder alterDialogueBilder;
        String message = null;

        switch(item.getItemId())
        {
            case R.id.instructions:
                alterDialogueBilder = new AlertDialog.Builder(DeezerSearch.this);
                alterDialogueBilder.setMessage("Use this search to find and play your favourite songs.\n\nYou can always save your favorites for later listening!")
                        .setPositiveButton("Okay", (Click, arg) -> {
                            Intent goBack = new Intent(DeezerSearch.this, DeezerSearch.class);
                            startActivity(goBack);
                        }).create().show();
                break;
            case R.id.abouttheapi:
                String url = "https://developers.deezer.com/guidelines";

                Intent goToapi = new Intent(Intent.ACTION_VIEW);
                goToapi.setData(Uri.parse(url));
                startActivity(goToapi);
                break;
            case R.id.donate:

                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                alterDialogueBilder = new AlertDialog.Builder(DeezerSearch.this);
                input.setLayoutParams(lp);
                alterDialogueBilder.setView(input);
                alterDialogueBilder.setTitle("Please give generously");
                alterDialogueBilder.setMessage("How much money do you want to donate?")
                        .setPositiveButton("Thank you", (Click, arg) -> {
                            Intent goBack = new Intent(DeezerSearch.this, DeezerSearch.class);
                            startActivity(goBack);
                        }).setNegativeButton("Cancel", (click, arg) -> {
                }).create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;

    }
}
