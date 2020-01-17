package it.esame.progettoOOP.Servizi;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Superclasse di NumStatistics e StrStastics*/
public abstract class Statistics {

    protected Map<String,Object> mappa=new HashMap<>();;

    public Statistics(List list) {
        if(list.size()==0)
        {throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Non esistono elementi per il campo inserito o sono stati esclusi tramite un filtro");}
        else{
            mappa.put("conteggio elementi lista", list.size());}
    }

    public abstract Map<String,Object> retResult();
}

