package com.muskan.sehyog_ekpehel;

public class UserInformation {

    public UserInformation(){
        //Required empty public constructor.
    }

    //Profile Details
    String name;
    String locality;

    public String getName() {
        return name;
    }

    public String getLocality() {
        return locality;
    }

    public UserInformation(String name, String locality)
    {
        this.name = name;
        this.locality = locality;
    }


}
