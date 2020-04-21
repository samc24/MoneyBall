package com.example.moneyball;


import android.net.Uri;

public class Wager {
    String pictureUri;
    String id, heading, description, group;
    public Wager(String id, String heading, String group, String pictureUri, String description){
        this.id = id;
        this.heading = heading;
        this.group = group;
        this.pictureUri = pictureUri;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return this.pictureUri;
    }

    public void setPicture(String picture) {
        this.pictureUri = picture;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
