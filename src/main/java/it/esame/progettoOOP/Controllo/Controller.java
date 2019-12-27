package it.esame.progettoOOP.Controllo;


import it.esame.progettoOOP.Modello.Modellante;
import it.esame.progettoOOP.Servizi.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@RestController
public class Controller {
    private Parsing p;
    private List<Modellante> rec;

    /**
     * Costruttore della classe Controller
     *
     * @param p oggetto di tipo Parsing
     */

    @Autowired
    public Controller(Parsing p){
        this.p = p;
        rec=p.getRecord();
    }

    public List ottieniColonna(String nome) {
        List<String> colonnas = new ArrayList<>();
        List<Float> colonnaf= new ArrayList<>();
        Object val;
        for (Modellante m : rec) {
            val=m.getValori(nome);
            if (!(val.equals("nnn"))) {
                try{colonnaf.add(Float.parseFloat(val.toString()));}
                catch (NumberFormatException e){colonnas.add(val.toString());}
            }
        }
        if(!colonnaf.isEmpty())
        {return colonnaf;}
        else
        {return colonnas;}

    }
    @GetMapping("/Record")
    public List<Modellante> getRecord(){

        return rec;
    }


    @GetMapping("/Metadata")
    public List<Map> getMetadata(){
        List<Map> list=new ArrayList<>();
        Set<Object> nomi = new HashSet<>();
        for (int i = 2019; i > 1968; i--)
            nomi.add(i);
        for(Object o: (rec.get(0)).getNomi(nomi)) {
            Map<String,Object> meta=new HashMap<>();
            meta.put("Alias", o);
            meta.put("SourceField", o.toString());
            if(o instanceof Integer)
            {meta.put("Type", "Float");}
            else
            {meta.put("Type", "String");}
            list.add(meta);
        }
        return list;
    }

@GetMapping ("/Statistiche")
    public String getStats(@RequestParam(value = "Field", required = false, defaultValue = "") String nomeCampo) {
        Statistics stats;
    try {Integer.parseInt(nomeCampo);
        stats=new NumStatistics(ottieniColonna(nomeCampo));
    }catch (NumberFormatException e){
        stats=new StrStatistics(ottieniColonna(nomeCampo));
    }
    return nomeCampo+" : "+stats.toString();
}

@PostMapping ("/DatiFiltrati")
    public List<Modellante> datiFiltrati(@RequestBody Map<String,Map<String,Object>[]>body) {
    Filter ogg=new Filter(body);

    return rec;
}
}
