package it.esame.progettoOOP.Servizi;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Superclasse di NumStatistics e StrStastics*/
public abstract class Statistics {

    protected Map<String,Object> mappa=new HashMap<>();;

    public Statistics(List list) {
        mappa.put("conteggio elementi lista",list.size());
    }

    public abstract Map<String,Object> retResult();
}

