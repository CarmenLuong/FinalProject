package com.example.finalproject;

public class soccerScoreObject {

    private long id;
    private String gameTitle;
    private String date;
    private String url;
    private String team1;
    private String team2;
    private boolean isFavorite;


    soccerScoreObject(){
        this(0,"","","","","",false);
    }

    soccerScoreObject(String gameTitle){
        this(0,gameTitle,"","","","",false);
    }

    soccerScoreObject(String gameTitle, String date){
        this(0,gameTitle,date,"","","",false);
    }

    soccerScoreObject(String gameTitle, String date, String url){
        this(0,gameTitle,date,url,"","",false);
    }

    soccerScoreObject(long id, String match, String date, String url) {
        this(id,match,date,url,"","",false);
    }

    soccerScoreObject(String gameTitle, String date, String url, String team1, String team2,boolean isFavorite){
        this(0,gameTitle,date,url,team1,team2,isFavorite);
    }

    public soccerScoreObject(long id, String gameTitle, String date, String url, boolean isFavorite) {
        this(id,gameTitle,date,url,"","",isFavorite);
    }

    soccerScoreObject(long match_id, String gameTitle, String date, String url, String team1, String team2, boolean isFavorite){
        this.id = match_id;
        this.gameTitle = gameTitle;
        this.date = date;
        this.url = url;
        this.team1 = team1;
        this.team2 = team2;
        this.isFavorite = isFavorite;
    }




    public long getId(){
        return id;
    }

    public void setId(long matchId){
        id = matchId;
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

    public String getTeam1(){
        return team1;
    }
    public void setTeam1(String team1){
        this.team1 = team1;
    }

    public String getTeam2(){
        return team2;
    }

    public void setTeam2(String team2){
        this.team2 = team2;
    }

    public boolean isFavorite(){
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
    }
}
