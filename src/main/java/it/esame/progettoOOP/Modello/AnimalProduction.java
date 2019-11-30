package it.esame.progettoOOP.Modello;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public class AnimalProduction implements Serializable {
    private String animals, month, unit, geo;
    private List<Float> anni;

    public AnimalProduction(String animals, String month, String unit, String geo, List<Float> anni) {

        this.animals = animals;

        this.month = month;

        this.unit = unit;

        this.geo = geo;

        this.anni = anni;

    }


   /* public String getAnimals() {

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
*/
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

    public List<Float> getAnni() {
        return anni;
    }

    public Float getAnno(Integer pos){return anni.get(pos);}


    public String toString() {

        return "Produzione{" +

                "animale='" + animals + '\'' +

                ", mese='" + month + '\'' +

                ", unit='" + unit + '\'' +

                ", località='" + geo + '\'' +


                '}';
    }

}