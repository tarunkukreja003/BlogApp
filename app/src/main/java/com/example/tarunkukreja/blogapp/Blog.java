package com.example.tarunkukreja.blogapp;

/**
 * Created by tarunkukreja on 06/04/17.
 */

public class Blog {

    private String title, decription, image ;

    public Blog(){

    }

    public Blog(String title, String decription, String image) {
        this.title = title;
        this.decription = decription;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDecription() {
        return decription;
    }

    public String getImage() {
        return image;
    }


}
