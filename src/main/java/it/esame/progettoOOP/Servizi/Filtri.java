package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.AnimalProduction;

import java.util.ArrayList;
import java.util.List;

public class Filtri {
public static boolean Controlla(Object value, String oper, Object rif)
{
    if (value instanceof Number)
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
