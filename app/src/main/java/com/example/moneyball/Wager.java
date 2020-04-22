package com.example.moneyball;


import android.net.Uri;

import java.util.ArrayList;

public class Wager {
    String id, heading, description, pictureUri, group, wagerCreator;
    ArrayList<String> usersList;
    Boolean openStatus;
    public Wager(String id, String heading, String group, String pictureUri, String description, String wagerCreator, ArrayList<String> usersList, Boolean openStatus){
        this.id = id;
        this.heading = heading;
        this.group = group;
        this.pictureUri = pictureUri;
        this.description = description;
        this.wagerCreator = wagerCreator;
        this.usersList = usersList;
        this.openStatus = openStatus;
    }

    //A function to add a user to the list of users who have entered a wager
    public void addUserToUsersList(String userID){
        this.usersList.add(userID);
    }

    public void setUsersList(ArrayList<String> usersList) {
        this.usersList = usersList;
    }

    public ArrayList<String> getUsersList() {
        return usersList;
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

    public String getWagerCreator() {
        return wagerCreator;
    }

    public void setWagerCreator(String wagerCreator) {
        this.wagerCreator = wagerCreator;
    }

    public Boolean getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(Boolean openStatus) {
        this.openStatus = openStatus;
    }
}
