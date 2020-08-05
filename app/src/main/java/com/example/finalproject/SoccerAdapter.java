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

    private ArrayList<soccerScoreObject> matchList;
    private Context context;
    private int layoutRes;
    TextView score;
    boolean isFavourite;

    SoccerAdapter(Context context,int layoutRes, ArrayList<soccerScoreObject> matchList){
        this.context = context;
        this.matchList = matchList;
        this.layoutRes = layoutRes;
    }

    /**
     *
     * this class allows the fragment to be inflated from the list view
     *
     */



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

        return matchList.get(position).getId();
    }

    /**
     * this method will select the the proper view to inflated in order see the list of games
     *
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;



        soccerScoreObject matchScore = (soccerScoreObject) getItem(position);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if(view == null) {

            view = inflater.inflate(R.layout.game_score, parent, false);

            while (matchScore.isFavorite()){
                view = inflater.inflate(R.layout.favorites_score,parent,false);
                TextView displayedScore = view.findViewById(R.id.favorites_games);
                displayedScore.setText(matchScore.getGameTitle());
            }

        }
        TextView displayedScore = view.findViewById(R.id.games);
        displayedScore.setText(matchScore.getGameTitle());

        return view;
    }

    /**
     *
     * this method is setting the data list for the list view when called upon
     * @param mListInfo
     */

    public void setListData(ArrayList<soccerScoreObject> mListInfo){
        this.matchList = mListInfo;
        notifyDataSetChanged();
    }


}
