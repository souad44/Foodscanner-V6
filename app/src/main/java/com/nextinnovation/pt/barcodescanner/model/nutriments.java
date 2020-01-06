package com.ensak.project.foodscanner.model;

public class nutriments {
    String image;
    private String size;

    public String getImage() {
        return image;
    }

    public nutriments(String size) {
        this.size = size;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
