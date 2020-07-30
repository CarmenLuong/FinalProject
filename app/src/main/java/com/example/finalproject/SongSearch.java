package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class SongSearch extends AppCompatActivity {

    Button loginButton;
    EditText email;
    String stringToSave;
    SharedPreferences myPrefs=null;
    ImageButton questionButton;
    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_search);

        email = findViewById(R.id.LyricEmail);
        loginButton = findViewById(R.id.loginButton);
        questionButton = findViewById(R.id.questionButton);


        myPrefs = getSharedPreferences("Email", Context.MODE_PRIVATE);
        String savedString = myPrefs.getString("email", "");
        email.setText(savedString);
        questionButton.setOnClickListener(v ->
                Toast.makeText(SongSearch.this, getResources().getString(R.string.lyricemailtoast), Toast.LENGTH_LONG).show());

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
                Intent goToProfile = new Intent(SongSearch.this, SearchClass.class);

                goToProfile.putExtra("email", savedString);
                startActivity(goToProfile);
            }
        });
    }
            protected void onPause(){
                super.onPause();
                stringToSave = email.getText().toString();
                SharedPreferences.Editor ed = myPrefs.edit();
                ed.putString("email", stringToSave);
                ed.commit();
            }
        }
