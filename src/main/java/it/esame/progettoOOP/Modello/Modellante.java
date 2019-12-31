package it.esame.progettoOOP.Modello;


import it.esame.progettoOOP.Servizi.Utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Modellante {

    private Map<Object,Object> modella=new HashMap<>();

   public Modellante(Object[] valori) {
       modella.put("animals", valori[0].toString());
       modella.put("month", valori[1].toString());
       modella.put("unit", valori[2].toString());
       modella.put("geo", valori[3].toString());
       for (int i = 2019; i > 1968; i--) {
           if ((2023 - i) < valori.length) {
               if (!((valori[2023 - i].toString()).charAt(0) == ':')) {
                   try {
                       modella.put(i, Float.parseFloat(valori[2023 - i].toString()));
                   } catch (NumberFormatException e) {
                       modella.put(i, Utilities.trasforma(valori[2023 - i].toString()));
                   }
               }
           }
       }
   }
   public Object getValori(String nome)
       {
           try{
           return modella.getOrDefault(Integer.parseInt(nome), "nnn");}
           catch (NumberFormatException e){return modella.getOrDefault(nome, "nnn");}
       }

   public Object getRecord(){
       return modella;
   }

    public Set<Object> getNomi(Set<Object> nomi) {
        nomi.addAll(modella.keySet());
        return nomi;
    }
}


