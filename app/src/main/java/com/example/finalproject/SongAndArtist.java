package com.example.finalproject;

public class SongAndArtist {
    String song;
    String artist;
    Long id;
    String lyrics;

    public SongAndArtist(Long id, String song, String artist, String lyrics) {
        this.id=id;
        this.song = song;
        this.artist=artist;
        this.lyrics = lyrics;

    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) { this.song = song;   }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) { this.artist = artist; }

    public long getId() { return id;  }

    public void setId(Long id ){this.id = id;}
    public String getlyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) { this.artist = lyrics; }
}
