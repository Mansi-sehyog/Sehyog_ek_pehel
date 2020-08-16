package com.muskan.sehyog_ekpehel;

class DatabaseActivity {

    String id;

    //Food Request Details
    String quantity;
    String madeOn;

    //Clothes Request Details
    String Clothes;

    //Pay Using Upi
    String status;
    String amount;
    String note;
    String userId;

    //ratings
    float rating;


    public DatabaseActivity(){
        //Required empty public constructor
    }

    //Rating
    public DatabaseActivity( String id , float rating)
    {
        this.rating = rating;
    }

    //Clothes
    public DatabaseActivity( String Clothes)
    {
        this.Clothes = Clothes;
    }


    // Food request Details
    public DatabaseActivity(String id, String quantity, String madeOn)
    {
        this.quantity = quantity;
        this.madeOn = madeOn;
    }

    //Pay Using Upi
    public DatabaseActivity( String status, String amount, String note, String userId) {
        this.status = status;
        this.amount = amount;
        this.note = note;
        this.userId = userId;
    }
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getClothes() {
        return Clothes;
    }

    public void setClothes(String clothes) {
        Clothes = clothes;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMadeOn() {
        return madeOn;
    }
}
