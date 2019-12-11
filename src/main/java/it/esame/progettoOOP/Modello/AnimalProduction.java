package it.esame.progettoOOP.Modello;


import java.util.List;

public class AnimalProduction {             //definizione classe modellante
    private String animals, month, unit, geo;
    private List<Float> anni;

    public AnimalProduction(String animals, String month, String unit, String geo, List<Float> anni) {      //costruttore classe modellante

        this.animals = animals;

        this.month = month;

        this.unit = unit;

        this.geo = geo;

        this.anni = anni;

    }

    //chiamate get che restituiscono un oggetto della classe modellante
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

    public List<Float> getAnni() {
        return anni;
    }


    //funzione chiamata in getValues che restituisce solo il campo (tra quelli in formato stringa) selezionato
   public String getCampo(String nomeCampo){

      switch(nomeCampo)
      {
          case "animals": return animals;
          case "month": return month;
          case "unit": return unit;
          case "geo": return geo;
          default: return "";
      }

   }
}