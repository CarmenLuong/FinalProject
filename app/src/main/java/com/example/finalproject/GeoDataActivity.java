package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class GeoDataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Toolbar at the top of the activity used to navigate to other features
     */
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
        switch (item.getItemId()) {
            case R.id.soccermatchitem:
                Intent goToSoccer = new Intent(this, MainActivity.class);
                startActivity(goToSoccer);
                break;
            case R.id.songitem:

                Intent goToGeo = new Intent(this, SongSearch.class);
                startActivity(goToGeo);
                break;
            case R.id.deezersongsearchitem:
                Intent goToDeezer = new Intent(this, DeezerSearch.class);
                startActivity(goToDeezer);
                break;
            case R.id.menu_help:
                Toast.makeText(GeoDataActivity.this, getString(R.string.written_by, "GeoData", "Carmen Luong"), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder alertDialog;
        switch (item.getItemId()) {
            case R.id.instructions:
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
                break;
            case R.id.abouttheapi:
                String url = "https://www.geodatasource.com/web-service";

                Intent goToapi = new Intent(Intent.ACTION_VIEW);
                goToapi.setData(Uri.parse(url));
                startActivity(goToapi);
                break;
            case R.id.donate:

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                alertDialog = new AlertDialog.Builder(GeoDataActivity.this);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setTitle(R.string.please_give_generously);
                alertDialog.setMessage(R.string.how_much_money)
                        .setPositiveButton(R.string.thank_you, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}