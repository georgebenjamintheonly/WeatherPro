package com.kurobarabenjamingeorge.foodisready;

/**
 * Created by KUROBARA BENJAMIN on 1/19/2018.
 */

public class Food {

    private String name, price, description, imageUri;

    public Food(){

    }

    public Food(String name, String price, String desc, String imageUri){
        this.name = name;
        this.price = price;
        this.price = desc;
        this.price = imageUri;

    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
