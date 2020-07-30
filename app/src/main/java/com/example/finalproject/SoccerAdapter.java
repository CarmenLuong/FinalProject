package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SoccerAdapter extends BaseAdapter {

    private ArrayList<soccerScoreObject> matchList = new ArrayList<>();
    private Context context;
    private int layoutRes;

    SoccerAdapter(Context context, int layoutRes, ArrayList<soccerScoreObject> matchList){
        this.context = context;
        this.layoutRes = layoutRes;
        this.matchList = matchList;
    }



    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return matchList.get(position).getMatch_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;


        soccerScoreObject matchScore = (soccerScoreObject) getItem(position);

        if(view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.game_score, parent, false);
            TextView displayedScore = view.findViewById(R.id.games);

            displayedScore.setText(matchScore.getGameTitle());
        }


        return view;
    }

    public void setListData(ArrayList<soccerScoreObject> mListInfo){
        this.matchList = mListInfo;
        notifyDataSetChanged();
    }


}
