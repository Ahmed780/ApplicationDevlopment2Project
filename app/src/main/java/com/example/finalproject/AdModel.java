package com.example.finalproject;

import com.google.firebase.database.Exclude;

public class AdModel {

    private String ImageUri,title,price,description,pid, time, mKey;

    public AdModel() {

    }

    public AdModel(String title, String price, String description,String ImageUri, String time) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.ImageUri = ImageUri;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
