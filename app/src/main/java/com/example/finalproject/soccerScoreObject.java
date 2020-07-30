package com.example.finalproject;

public class soccerScoreObject {

    private long match_id;
    private String gameTitle;
    private String date;
    private String url;


    soccerScoreObject(){
        this(0,"","","");
    }

    soccerScoreObject(String gameTitle){
        this(0,gameTitle,"","");
    }

    soccerScoreObject(String gameTitle, String date){
        this(0,gameTitle,date,"");
    }

    soccerScoreObject(String gameTitle, String date, String url){
        this(0,gameTitle,date,url);
    }

    soccerScoreObject(long match_id, String gameTitle, String date, String url){
        this.match_id = match_id;
        this.gameTitle = gameTitle;
        this.date = date;
        this.url = url;
    }

    public long getMatch_id(){
        return match_id;
    }

    public void setMatch_id(long matchId){
        match_id = matchId;
    }

    public String getGameTitle(){
        return gameTitle;
    }

    public void setGameTitle(String game){
        gameTitle = game;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String dayOfGame){
        date = dayOfGame;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String website){
        url = website;
    }
}
