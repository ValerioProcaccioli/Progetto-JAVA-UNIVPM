package it.esame.progettoOOP.Controllo;


import com.fasterxml.jackson.databind.JsonMappingException;
import it.esame.progettoOOP.Modello.Modellante;
import it.esame.progettoOOP.Servizi.*;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
public class Controller {
    private List<Modellante> rec;

    /**
     * Costruttore della classe Controller
     *
     * @param p oggetto di tipo Parsing
     */

    @Autowired
    public Controller(Parsing p){
        rec=p.getRecord();
    }


    @GetMapping("/Record")
    public List<Modellante> getRecord(){
        return rec;
    }

    @GetMapping("/Record/{i}")
    public Modellante getRecord(@PathVariable int i) {
        return rec.get(i);
    }

    @GetMapping("/Metadata")
    public List<Map<String,Object>> getMetadata(){
        List<Map<String,Object>> list=new ArrayList<>();
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

    public  List ottieniColonna(String nome,List<Modellante> lista) {
        List<String> colonnas = new ArrayList<>();
        List<Float> colonnaf= new ArrayList<>();
        Object val;
        for (Modellante m : lista) {
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
@GetMapping ("/Statistiche")
    public Map getStats(@RequestParam(value = "Campo", required = false, defaultValue = "") String nomeCampo) {
        Statistics stats;
    try {Integer.parseInt(nomeCampo);
        stats=new NumStatistics(ottieniColonna(nomeCampo,rec));
    }catch (NumberFormatException e){
        stats=new StrStatistics(ottieniColonna(nomeCampo,rec));
    }
    return new BasicJsonParser().parseMap("{"+nomeCampo+" : "+stats.toString());
}

@ExceptionHandler (JsonMappingException.class)
public String gestisciEccezioni(JsonMappingException e){return "Richiesta POST illeggibile. Consultare il readMe per chiarimenti.";}

@PostMapping ("/DatiFiltrati")
    public List<Modellante> datiFiltrati(@RequestBody  Map<String,Map<String,Object>[]>body) {
        return new Filter(body).Filtra(rec);
}

@PostMapping("/StatisticheFiltrate")
   public Map<String,Object> FilteredStats(@RequestBody  Map<String,Map<String,Object>[]>body,@RequestParam(value = "Campo", required = false, defaultValue = "") String nomeCampo)
{
    Statistics stats;
    try {Integer.parseInt(nomeCampo);
        return new NumStatistics(ottieniColonna(nomeCampo,datiFiltrati(body))).retResult();
    }catch (NumberFormatException e){
        return new StrStatistics(ottieniColonna(nomeCampo,datiFiltrati(body))).retResult();

    }

}

@DeleteMapping ("/RimuoviRecord")
    public List<Modellante> deleteRecord(@RequestBody  Map<String,Map<String,Object>[]>body)
{
    for(Modellante m: datiFiltrati(body))
    {
        rec.removeIf(m1 -> m1.equals(m));
    }
    return rec;
}

//@PostMapping ("/InserisciRecord")
}
