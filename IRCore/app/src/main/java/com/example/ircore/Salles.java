package com.example.ircore;


import java.util.ArrayList;


public class Salles {

    ArrayList<Salle> salles;

    public Salles(){
        this.salles = new ArrayList<Salle>();
        //1er étage
        this.salles.add(new Salle(1, "Grand amphi", 1,1,7.310641, 47.729676) );
        this.salles.add(new Salle(2, "Petit amphi", 1,1,7.310716,47.729584) );
        this.salles.add(new Salle(3, "Kfet", 1,2,7.310661,47.729495) );
        this.salles.add(new Salle(4, "Amicale", 1,4,7.310142,47.729173) );
        this.salles.add(new Salle(5, "Xid", 1,3,7.310462,47.729226) );
        this.salles.add(new Salle(6, "Electro 1.16", 1,3,7.310529,47.729309) );
        this.salles.add(new Salle(7, "Physique 1.15", 1,3,7.310689,47.729255) );
        this.salles.add(new Salle(8, "Systeme", 1,3,7.310543,47.729199) );
        this.salles.add(new Salle(9, "Mbot", 1,3,7.310667,47.729157) );
        this.salles.add(new Salle(10, "Accueil", 1,2,7.310278,47.729366) );

        //2eme étage
        this.salles.add(new Salle(11, "PC1", 2,3,7.310730, 47.729151) );
        this.salles.add(new Salle(12, "PC2", 2,3,7.310747, 47.729155) );
        this.salles.add(new Salle(13, "PC3", 2,3,7.310742,  47.729184) );
        this.salles.add(new Salle(14, "PC4", 2,3,7.310527, 47.729301) );
        this.salles.add(new Salle(15, "PC Master", 2,3,7.310628, 47.729199) );
        this.salles.add(new Salle(16, "TP informatique industrielle", 2,3,7.310580, 47.729268) );
        this.salles.add(new Salle(17, "E 20A", 2,4,7.310125, 47.729231) );
        this.salles.add(new Salle(18, "E 20B", 2,4,7.310105,  47.729197) );
        this.salles.add(new Salle(19, "E 21", 2,4,7.310045, 47.729087) );
        this.salles.add(new Salle(20, "E 22", 2,6,7.309997,  47.729038) );
        this.salles.add(new Salle(21, "E 23", 2,6,7.309966, 47.728977) );
        this.salles.add(new Salle(22, "E 24", 2,6,7.309912,  47.728919) );
        this.salles.add(new Salle(23, "E 25", 2,6,7.309793, 47.728820) );
        this.salles.add(new Salle(24, "Prof 1, Etage 2", 2,5,7.310381,  47.728955) );
        this.salles.add(new Salle(25, "Prof 2, Etage 2", 2,7, 7.310142, 47.728758) );
        this.salles.add(new Salle(26,"Scolarité",2,1,7.310713,47.729660));


        //3ème étage
        this.salles.add(new Salle(27, "E 30", 3,4,7.310140, 47.729227) );
        this.salles.add(new Salle(28, "E 31", 3,4,7.310096, 47.729195) );
        this.salles.add(new Salle(29, "E 32", 3,4,7.310029, 47.729114) );
        this.salles.add(new Salle(30, "E 33", 3,4,7.310002, 47.729091) );
        this.salles.add(new Salle(31, "E 34", 3,6,7.309967, 47.729006) );
        this.salles.add(new Salle(32, "E 35", 3,6,7.309909, 47.728943) );
        this.salles.add(new Salle(33, "E 36", 3,6,7.309873, 47.728918) );
        this.salles.add(new Salle(34, "E 37", 3,6,7.309848, 47.728884) );
        this.salles.add(new Salle(35, "Salle Réseau",3, 7,7.310065, 47.728818) );
        this.salles.add(new Salle(36, "Prof 1, Etage 3", 3,5,7.310381,  47.728955) );
        this.salles.add(new Salle(37, "Prof 2, Etage 3", 3,7, 7.310142, 47.728758) );



    }

    public ArrayList<Salle> getList(){
        return this.salles;
    }

    public Salle getSalle(int i){
        return this.salles.get(i);
    }
}
