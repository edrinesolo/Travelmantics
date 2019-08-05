package com.example.travelmantics;

public class Hotels {
    public String title, price, image, address;


    //empty consqtructor if we ever h`ave to cre`ate objectsq `and set properties l`ater
    public Hotels() {
    }

    public Hotels(String title, String price, String image, String address) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.address = address;
        
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}


