package com.ensak.project.foodscanner.model;

public class Item {

    private String name;
    private String taille;
    private String portion;

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public Item(String n,String t,String p) {
        name = n;
        taille=t;
        portion=p;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}