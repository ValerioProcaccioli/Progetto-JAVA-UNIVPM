package it.esame.progettoOOP.Servizi;

import java.util.List;
import java.util.Map;

public class NumStatistics extends Statistics {

/*Il metodo costruttore effettua alla sua chiamata tutte le stistiche numeriche*/
    public NumStatistics(List<Float> list) {
        super(list);
        mappa.put("valore minimo",Utilities.min(list));
        mappa.put("valore massimo",Utilities.max(list));
        mappa.put("somma valori",Utilities.sum(list));
        mappa.put("media valori",(float)mappa.get("somma valori") / (int)mappa.get("conteggio elementi lista"));
        mappa.put("deviazione standard valori",Utilities.devStd(list, (float)mappa.get("media valori")));
    }

@Override
    public Map<String, Object> retResult() {
        return mappa;
    }
}
