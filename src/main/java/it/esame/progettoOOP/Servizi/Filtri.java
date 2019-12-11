package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.AnimalProduction;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class Filtri {

/*funzione che stabilisce se il campo esaminato rispetta o meno il filtro*/
public static boolean Controlla(Object value, String oper, String rif)
{

       if (value instanceof Number) {
           float val = ((Number) value).floatValue();//a seconda del tipo del valore da confrontare c'è un differente elenco di operatori disponibili
           try {
               float rifer = Float.parseFloat(rif);
               switch (oper) {//selezione operatore
                   case "$not":
                       return val != rifer;
                   case "$eq":
                       return val == rifer;
                   case "$gte":
                       return val >= rifer;
                   case "$lte":
                       return val <= rifer;
                   default:
                       String errore = "l'operatore" + oper + "non è presente o è inadatto al confronto tra" + val + "e" + rifer;
                       throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errore);
               }
           } catch (NumberFormatException e) {
               String errore = "Non si può confrontare un numero con una stringa";
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errore);
           }
       } else if (value instanceof String || value instanceof Character) {// caso in cui il valore da controllare sia una stringa o un carattere (per freq)
           if (value instanceof Character)
               value = String.valueOf(value); //nel caso di carattere, lo riporto a stringa
           String val = ((String) value); // conversione

               switch (oper) { //selezione operatore
                   case "$eq":
                       return val.equals(rif);
                   case "$not":
                       return !val.equals(rif);
                   default:

                       String erroreOper = "L'operatore:'" + oper + "' non è presente o risulta inadatto per gli operandi: '" + value + "' , '" + rif + "'";
                       throw new ResponseStatusException(HttpStatus.BAD_REQUEST, erroreOper);
               }

       }else
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato del valore non riconosciuto");

}

/*questo metodo scorre tutti i valori della lista associata al campo da filtrare, se il valore corrente rispetta
 la funzione booleana controlla, si chiama la riga del record dove si trova il valore corrente
 e lo si aggiunge alla lista filtrata*/
    public static List<AnimalProduction> FilteredValues(List values, String operatore, String rif)
    {
        List<AnimalProduction> filtrati = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {

            if (Controlla(values.get(i), operatore, rif)) // eseguiamo il controllo per ogni elemento della lista: se soddisfatto aggiungo l'indice alla lista

                filtrati.add(Download.getRecord(i));

        }

        return filtrati;
    }

    /*questo metodo filtra il dataset usando "$and" o "$or", per farlo riceve in ingresso due liste ed effettua per entrambe
    * un controllo per ognuno degli elementi che le compone, nel caso dell'and per aggiungere alla lista degli elementi filtrati
    * la riga del dataset in cui si trovano i campi esaminati, entrambi devono soddisfare il filtro $eq rispetto al
    * relativo riferimento. Mentre per soddisfare l'or è sufficiente che solo uno degli elementi delle due liste soddisfi $eq*/
    public static List<AnimalProduction> FilteredAndOr(List values, List values2, String operatore, String[] rif )
    {
        List<AnimalProduction> filtrati = new ArrayList<>();
        if (operatore.equals("$and"))
        {
            for (int i=0;i<values.size() && i<values2.size();i++)
            {if (Controlla(values.get(i), "$eq", rif[0]) && Controlla(values2.get(i), "$eq", rif[1]))
                filtrati.add(Download.getRecord(i));
            }
        }
        else { for (int i=0;i<values.size() || i<values2.size();i++)
        {if (Controlla(values.get(i), "$eq", rif[0]) && Controlla(values2.get(i), "$eq", rif[1]))
            filtrati.add(Download.getRecord(i)); }
        }
        return filtrati;
    }
}
