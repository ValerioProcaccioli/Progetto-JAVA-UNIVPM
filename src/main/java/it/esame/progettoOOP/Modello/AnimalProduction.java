package it.esame.progettoOOP.Modello;


import java.io.Serializable;

public class AnimalProduction implements Serializable {
    private String animals, month, unit, geo;
    private double[] value;

    public AnimalProduction(String animals, String month, String unit, String geo, double[] value) {

        this.animals = animals;

        this.month = month;

        this.unit = unit;

        this.geo = geo;

        this.value = value;

    }


    public String getAnimals() {

        return animals;

    }


    public String getMonth() {

        return month;

    }


    public String getUnit() {

        return unit;

    }


    public String getGeo() {

        return geo;

    }


    public double[] getValori() {
        return value;
    }


    public String toString() {

        return "Produzione{" +

                "animale='" + animals + '\'' +

                ", mese='" + month + '\'' +

                ", unit='" + unit + '\'' +

                ", località='" + geo + '\'' +


                '}';
    }

}