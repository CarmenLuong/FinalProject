package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SongDetailsFragment extends Fragment {
    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    public SongDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.songdetails_fragment, container, false);


        TextView message = (TextView)result.findViewById(R.id.fragmentTitle);
        message.setText(dataFromActivity.getString(SongListClass.ITEM_SONGN));

        TextView lyrics = (TextView)result.findViewById(R.id.LyricsFragm);
        lyrics.setText(dataFromActivity.getString(SongListClass.ITEM_LYRICS));

        Button hideButt = (Button) result.findViewById(R.id.hideButton);
        hideButt.setOnClickListener( clk->{
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        return result;
    }

    public void onAttach(Context context) {

        super.onAttach(context);
        parentActivity=(AppCompatActivity)context;
    }
}