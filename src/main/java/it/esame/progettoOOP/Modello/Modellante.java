package it.esame.progettoOOP.Modello;


import it.esame.progettoOOP.Servizi.Utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*Classe che modella una riga del dataset in una mappa contentente i valori presenti in ogni riga, ripuliti da caratteri
* di disturbo*/
public class Modellante {

    private Map<Object,Object> modella=new HashMap<>();

   public Modellante(Object[] valori) {
       //gli elementi di tipo stringa sono sempre presenti perciò vanno messi in ogni caso
       modella.put("animals", valori[0].toString());
       modella.put("month", valori[1].toString());
       modella.put("unit", valori[2].toString());
       modella.put("geo", valori[3].toString());
       for (int i = 2019; i > 1968; i--) {
           if ((2023 - i) < valori.length) {
               if (!((valori[2023 - i].toString()).charAt(0) == ':')) { //se non c'è un valore relativo ad un anno, esso non viene inserito nella mappa
                   try {
                       modella.put(i, Float.parseFloat(valori[2023 - i].toString()));   //se il numero non presenta caratteri di distubo può essere salvato come float
                   } catch (NumberFormatException e) {
                       modella.put(i, Utilities.trasforma(valori[2023 - i].toString())); //altrimenti un metodo ausiliario lo "ripulisce"
                   }
               }
           }
       }
   }
   /*Metodo che restituisce un valore della mappa modella associato ad una chiave passata come parametro*/
   public Object getValori(String nome)
       {
           try{
           return modella.getOrDefault(Integer.parseInt(nome), "nnn");}
           catch (NumberFormatException e){return modella.get(nome);}
       }


   public Object getRecord(){
       return modella;
   }

   /*Metodo che ricevendo come parametro un set contenente tutti gli anni, al quale vengono aggiunte
    tutte le chiavi stringa. Il set aggiornato viene restituito*/
    public Set<Object> getNomi(Set<Object> nomi) {
        nomi.addAll(modella.keySet());
        return nomi;
    }
}


