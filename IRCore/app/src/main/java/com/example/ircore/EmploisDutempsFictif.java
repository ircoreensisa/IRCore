package com.example.ircore;

import java.util.ArrayList;
import java.util.Random;

public class EmploisDutempsFictif {

    private ArrayList<String> emploisDuTemps;
    private int currentSalleId ;
    private String currentProf;

    public EmploisDutempsFictif(){
        this.currentSalleId = 1;
        this.currentProf = "\n  ";
        this.emploisDuTemps = new ArrayList<String>();
        this.emploisDuTemps.add("\nLienhart ,Algo SD");
        this.emploisDuTemps.add("\nPerronne , AOO");
        this.emploisDuTemps.add("\nAmbs , ASLC");
        this.emploisDuTemps.add("\nThiry , UML");
        this.emploisDuTemps.add("\nWeber , Systeme d'exploitation ");
        this.emploisDuTemps.add("\nDietrich , Techno Web");
        this.emploisDuTemps.add("\nHilt , Réseaux");
        this.emploisDuTemps.add("\nHassenforder , Unix ");
        this.emploisDuTemps.add("\n  ");


    }

    public String getRandomclass(int i ){
        if (i == this.currentSalleId){
            return this.currentProf;
        }
        else{
            Random r = new Random();
            int valeur = 0 + r.nextInt(10 - 0);
            this.currentProf = this.emploisDuTemps.get(valeur);
            this.currentSalleId = i;
            return this.currentProf;
        }


    }

}
