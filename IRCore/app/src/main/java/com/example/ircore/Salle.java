package com.example.ircore;


import com.google.ar.core.Point;

/*
* on enregistre dans une salle son nom et sa localisation pour pouvoir la retrouver plus facilement et aisin guider plus rapidement vers ses coordonnées
* */
public class Salle {

    private int id;
    private int etage;
    private int zone;
    private String name;
    private double lat;
    private double lon;


    public Salle(int id,String s,int e,int z,double lo , double lat){
        this.id = id;
        this.name = s;
        this.etage = e;
        this.lat = lat;
        this.lon = lo;
        this.zone = z ;
    }

    public String getName(){
        return this.name;
    }

    public double getLong(){
        return this.lon ;
    }

    public double getLat() { return this.lat ;}

    public int getId(){
        return this.id;
    }

    public int getfloor(){
        return this.etage;
    }

    public int getzone(){
        return this.zone;
    }
}
