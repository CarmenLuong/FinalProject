package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class GeoDataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_data);
        getSupportFragmentManager().beginTransaction().add(R.id.Container, new GeoDataFragment()).commit();

        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.geo_data_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onMenuItemClicked(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        onMenuItemClicked(item);
        return false;
    }

    private void onMenuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.soccermatchitem:
                Intent goToSoccer = new Intent(this, MainActivity.class);
                startActivity(goToSoccer);
                break;
            case R.id.songitem:

                Intent goToGeo = new Intent(this, SongSearchClass.class);
                startActivity(goToGeo);
                break;
            case R.id.deezersongsearchitem:
                Intent goToDeezer = new Intent(this, MainActivity.class);
                startActivity(goToDeezer);
                break;
            case R.id.menu_help:
                new AlertDialog.Builder(GeoDataActivity.this)
                        .setTitle(R.string.how_to_title)
                        .setMessage(R.string.how_to_message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
        }
    }
}