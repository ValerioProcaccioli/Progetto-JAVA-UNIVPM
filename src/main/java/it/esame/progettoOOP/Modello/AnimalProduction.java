package it.esame.progettoOOP.Modello;


import java.io.Serializable;
import java.util.List;

public class AnimalProduction implements Serializable {
    private String animals, month, unit, geo;
    private List<Float> value;

    public AnimalProduction(String animals, String month, String unit, String geo, List<Float> value) {

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


    public List<Float> getValue() {
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