package it.esame.progettoOOP.Servizi;

import java.util.List;

public class NumStatistics extends Statistics {
    private float avg, min, max, sum, devstd;

    public NumStatistics(List<Float> list) {
        super(list);
        min = Utilities.min(list);
        max = Utilities.max(list);
        sum = Utilities.sum(list);
        avg = sum / count;
        devstd = Utilities.devStd(list, avg);
    }

@Override
    public String toString() {
        return ("{{ valore minimo: " + min + "}, { valore massimo: " + max +"}, { somma valori: " + sum + "}, { media valori: " + avg +
                "}, { deviazione standard valori: " + devstd +"}, { conteggio elementi lista: "+count+ "}}");
    }
}
