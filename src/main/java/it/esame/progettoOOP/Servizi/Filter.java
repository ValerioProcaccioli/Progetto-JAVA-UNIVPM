package it.esame.progettoOOP.Servizi;


import it.esame.progettoOOP.Modello.Modellante;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

public class Filter {
    private String oper;
    private List<String> campoFiltro=new ArrayList<>();
    private List<Object> rif=new ArrayList<>();
public Filter(Map<String,Map<String,Object>[]> body)
{
     oper= body.keySet().iterator().next();
     for(Map<String,Object> m: body.get(oper))
     {campoFiltro.add((m.keySet()).iterator().next());
     rif.add((m.values()).iterator().next()); }
     if(campoFiltro.size()==1)
     {String temp=campoFiltro.get(0);
     campoFiltro.add(0,oper);
     oper=temp;}

}

public List<Modellante> Filtra(List<Modellante> rec) {
    List<Modellante> filtrato=new ArrayList<>();
    if(oper.equals("$and")|| oper.equals("$or")) {
        if (oper.equals("$and")){
        oper = "$eq";
        filtrato = Filtra(rec);
        campoFiltro.add(0, campoFiltro.get(1));
        rif.add(0, rif.get(1));
        return Filtra(filtrato);
        } else {
            oper = "$eq";
            filtrato = Filtra(rec);
            campoFiltro.add(0, campoFiltro.get(1));
            rif.add(0, rif.get(1));
            filtrato.addAll(Filtra(filtrato));
            Set<Modellante> temp = new HashSet<>(filtrato);
            filtrato.clear();
            filtrato.addAll(temp);
            return filtrato;
        }
    }
    else if(rif.get(0) instanceof Number ) {
        float r = ((Number) rif.get(0)).floatValue();
        float o;
        for (Modellante m : rec) {
            if (!(m.getValori(campoFiltro.get(0)).equals("nnn"))) {
                try {
                    o = (float) m.getValori(campoFiltro.get(0));
                } catch (NumberFormatException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non esistono operatori di confronto tra numeri e stringhe");
                }
                if (oper.equals("$eq") || oper.equals("$not") || oper.equals("$mag") || oper.equals("$min")) {
                    if (oper.equals("$eq") || oper.equals("$not") || oper.equals("$mag")) {
                        if (oper.equals("$eq") || oper.equals("$not")) {
                            if (oper.equals("$eq")) {
                                if (o == r) {
                                    filtrato.add(m);
                                }
                            } else if (!(o == r)) {
                                filtrato.add(m);
                            }
                        } else if (o > r) {
                            filtrato.add(m);
                        }
                    } else if (o < r) {
                        filtrato.add(m);
                    }
                } else
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operatore non presente o non consentito per i valori forniti");
            }
        }
    }
    else if (rif.get(0) instanceof String) {
            String r = (String) rif.get(0);
            for (Modellante m : rec) {
                String o = (String) m.getValori(campoFiltro.get(0));
                if (oper.equals("$eq") || oper.equals("$not")) {
                    if (oper.equals("$eq")) {
                        if (o.equals(r)) {
                            filtrato.add(m);
                        }
                    } else if (!(o.equals(r))) {
                        filtrato.add(m);
                    }
                } else
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operatore non presente o non consentito per i valori forniti.");

            }
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Riferimento non accettabile. Il programma supporta un formato stringa o numerico.");

        return filtrato;
    }



}
