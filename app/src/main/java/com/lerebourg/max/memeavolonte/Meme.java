package com.lerebourg.max.memeavolonte;

/**
 * Created by Max on 30/12/2017.
 */

public class Meme {

    private String title;
    private String imageUrl;
    private String date;
    private int id;


    public Meme(String text, String imageUrl, int id, String date) {
        this.title = text;
        this.imageUrl = imageUrl;
        this.id = id;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString()  {
        return title;
    }

}

