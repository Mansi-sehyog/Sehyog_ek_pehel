package com.muskan.sehyog_ekpehel;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String userName;
    private String userNumber;
    private String imageUrl;

    public UserModel(String userName, String userNumber) {
        this.userName = userName;
        this.userNumber = userNumber;
    }

    public UserModel(String userName, String userNumber, String imageUrl) {
        this.userName = userName;
        this.userNumber = userNumber;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}

