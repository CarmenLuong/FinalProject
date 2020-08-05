package com.example.finalproject;

import android.graphics.Bitmap;
public class DeezerArtistClass {
    String title;
    String duration;
    String albumName;
    Bitmap albumCover;
    Long id;

    public DeezerArtistClass(String title){
        this.title = title;
    }

    public DeezerArtistClass(Long id, String title){
        this.id = id;
        this.title = title;
    }

    public DeezerArtistClass(Long id, String title, String duration, String albumName, Bitmap albumCover){
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCover = albumCover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Bitmap getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(Bitmap albumCover) {
        this.albumCover = albumCover;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
