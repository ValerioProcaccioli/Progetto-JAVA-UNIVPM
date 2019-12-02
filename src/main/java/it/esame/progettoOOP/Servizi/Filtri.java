package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.AnimalProduction;

import java.util.ArrayList;
import java.util.List;

public class Filtri {

    public static List<AnimalProduction> FilteredValues (List values, String oper, Object rif)
    {
        List<AnimalProduction> filtrati = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {

            if (controlla(values.get(i), oper, rif)) // eseguiamo il controllo per ogni elemento della lista: se soddisfatto aggiungo l'indice alla lista

                filtrati.add(Download.getRecord(i));

        }

        return filtrati;
    }
}
