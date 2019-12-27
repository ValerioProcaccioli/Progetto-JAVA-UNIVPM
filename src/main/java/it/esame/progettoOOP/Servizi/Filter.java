package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.Modellante;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Filter {
    private String oper;
    private List<String> campoFiltro=new ArrayList<>();
    private List<Object> rif=new ArrayList<>();
public Filter(Map<String,Map<String,Object>[]> body)
{
     oper= body.keySet().iterator().next();
     for(Map m: body.get(oper))
     { campoFiltro.add((String) (m.keySet()).iterator().next());
     rif.add((m.values()).iterator().next()); }
     if(campoFiltro.size()==1)
     {String temp=campoFiltro.get(0);
     campoFiltro.add(0,oper);
     oper=temp;}
     
}

/*List<Modellante> Filtra(List<Modellante> record,List list) {
    List<Modellante> filtrata = new ArrayList<>();

    for (Object o : list) {
        if (oper.equals("$eq")){

        }
    }
}*/
}
