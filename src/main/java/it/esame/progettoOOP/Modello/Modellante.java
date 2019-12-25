package it.esame.progettoOOP.Modello;


import it.esame.progettoOOP.Servizi.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Modellante {

    private Map<Object,Object> modella=new HashMap<>();

   public Modellante(String[] valori) {
       modella.put("animals", valori[0]);
       modella.put("month", valori[1]);
       modella.put("unit", valori[2]);
       modella.put("geo", valori[3]);
       for (int i = 2019; i > 1968; i--)
               modella.put(i, Utilities.trasforma(valori[2023 - i]));


   }
   public Object getValori(Object nome)
       {
           return modella.get(nome);
       }

   public Object getRecord(){
       return modella;
   }

}


