package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.AnimalProduction;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class Filtri {
public static boolean Controlla(Object value, String oper, Object rif)
{
    /*if (value instanceof Number)
    {if(rif instanceof Number)
    {
        switch(oper)
        {
            case "$not":

                return (float)value != (float)rif;

            case "$eq":

                return (float)value == (float)rif;

            case "$gt":

                return (float)value > (float)rif;

            case "$gte":

                return (float)value >= (float)rif;

            case "$lt":

                return (float)value < (float)rif;

            case "$lte":

                return (float)value <= (float)rif;

            default: String errore= "l'operatore" + oper + "non è presente o è inadatto al confronto tra"+ value +"e"+ rif;
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errore);
        }

    }
    }**/
    return true;
}
    public static List<AnimalProduction> FilteredValues (List values, String operatore, Object rif)
    {
        List<AnimalProduction> filtrati = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {

            if (Controlla(values.get(i), operatore, rif)) // eseguiamo il controllo per ogni elemento della lista: se soddisfatto aggiungo l'indice alla lista

                filtrati.add(Download.getRecord(i));

        }

        return filtrati;
    }
}
