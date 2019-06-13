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
        this.emploisDuTemps.add("\nDenis Lienhart ,Algo SD");
        this.emploisDuTemps.add("\nJean-Marc Perronne , AOO");
        this.emploisDuTemps.add("\nPierre Ambs , ASLC");
        this.emploisDuTemps.add("\nLaurent Thiry , UML");
        this.emploisDuTemps.add("\nJonathan Weber , Systeme d'exploitation ");
        this.emploisDuTemps.add("\nJulien Dietrich , Techno Web");
        this.emploisDuTemps.add("\nBenoit Hilt , Réseaux");
        this.emploisDuTemps.add("\nMichel Hassenforder , Unix ");
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
