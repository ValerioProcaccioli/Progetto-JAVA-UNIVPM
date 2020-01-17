package it.esame.progettoOOP.Servizi;


import it.esame.progettoOOP.Modello.Modellante;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/*Una volta creato un oggetto di questa classe, operazione effettuabile passando come parametro il body nella forma corretta,
* il costruttore ricava da essso i valori da assegnare agli attributi della classe */
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

/**Questo metodo restituisce il dataset filtrato controllando tramite una struttura ad if annidati se una riga del
* dataset rispetti o meno un filtro
 *
 * @param rec lista da filtrare
 *
 * @return filtrato lista filtrata*/
public List<Modellante> Filtra(List<Modellante> rec) {
    List<Modellante> filtrato=new ArrayList<>();
    if(oper.equals("$and")|| oper.equals("$or")) {
        if (oper.equals("$and")){ //se l'operatore è l'$and viene effettuato un filtraggio del primo campo con l'operatore $eq
        oper = "$eq";
        filtrato = Filtra(rec);
        campoFiltro.add(0, campoFiltro.get(1));
        rif.add(0, rif.get(1));
        return Filtra(filtrato); // la lista ottenuta viene rifilitrata sempre con l'operatore $eq stavolta sul secondo campo, ottenendo così il risultato
        } else {
            oper = "$eq";       //se l'operatore è l'$or viene effettuato un filtraggio del primo campo con l'operatore $eq
            filtrato = Filtra(rec);
            campoFiltro.add(0, campoFiltro.get(1));
            rif.add(0, rif.get(1));
            filtrato.addAll(Filtra(filtrato)); //si filtra anche il secondo campo sempre tramite $eq, unendo i risultati dei due filtraggi in una lista
            Set<Modellante> temp = new HashSet<>(filtrato); //si copia la lista in un set al fine di eliminare le ripetizioni
            filtrato.clear();
            filtrato.addAll(temp);//viene copiato il set nella lista da restituire
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
                } catch (ClassCastException e) {
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
        try {
            String r = (String) rif.get(0);
            for (Modellante m : rec) {
                if (!(m.getValori(campoFiltro.get(0)).equals("nnn"))) {
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
            }
        }catch (ClassCastException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non si possono confrontare numeri con stringhe."); }
    }else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Riferimento non accettabile. Il programma supporta un formato stringa o numerico.");

        return filtrato;
    }



}
