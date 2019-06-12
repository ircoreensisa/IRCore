package com.example.ircore.cloudanchor;

public class Ancrage {

    private String idAnchor;
    private  int character;
    private int place;

    public Ancrage(String idAnchor, int place) {
        this.idAnchor = idAnchor;
        this.place=place;
    }

    public String getIdAnchor() {
        return idAnchor;
    }

    public int getPlace() {return place;}


    public String toString(){
        return "L'ID est :" + idAnchor;
    }
}
