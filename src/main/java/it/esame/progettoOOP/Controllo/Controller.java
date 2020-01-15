package it.esame.progettoOOP.Controllo;


import com.fasterxml.jackson.databind.JsonMappingException;
import it.esame.progettoOOP.Modello.Modellante;
import it.esame.progettoOOP.Servizi.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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


@SafeVarargs
@GetMapping ("/Statistiche")
    public final List<Map<String,Map<String,Object>>> getStats(@RequestParam(value = "Campo", required = false, defaultValue = "") String nomeCampo, @RequestParam(value="nnn",required = false) List<Modellante>... lista) {
    List<Map<String,Map<String, Object>>> list = new ArrayList<>();
    if (nomeCampo.equals("")) {

        for (Map<String,Object> map : getMetadata()) {
            list.add(getOneStats((String) map.get("SourceField"),lista));
        }
    } else {
        list.add(getOneStats(nomeCampo, lista));
        return list;
    }
    return list;
}
    @SafeVarargs
    public final Map<String,Map<String,Object>> getOneStats(String nomeCampo, List<Modellante>... lista){
        List<Modellante> m=new ArrayList<>();
        Map<String,Map<String,Object>> map=new HashMap<>();
        try {
            for(List<Modellante> m1: lista)
                m=m1;
        }catch (NullPointerException e){m=rec;}
        try {Integer.parseInt(nomeCampo);
        map.put(nomeCampo,new NumStatistics(Utilities.ottieniColonna(nomeCampo,m)).retResult());
    }catch (NumberFormatException e){
        map.put(nomeCampo,new StrStatistics(Utilities.ottieniColonna(nomeCampo,m)).retResult());}
        return map;
}

@ExceptionHandler (JsonMappingException.class)
public String gestisciEccezioni(JsonMappingException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Richiesta POST illeggibile. Consultare il readMe per chiarimenti.");}

@PostMapping ("/DatiFiltrati")
    public List<Modellante> datiFiltrati(@RequestBody  Map<String,Map<String,Object>[]> body) {
        return new Filter(body).Filtra(rec);
}

@PostMapping("/StatisticheFiltrate")
   public List<Map<String,Map<String,Object>>> FilteredStats(@RequestBody  Map<String,Map<String,Object>[]>body,@RequestParam(value = "Campo", required = false, defaultValue = "") String nomeCampo)
{
    return getStats(nomeCampo,datiFiltrati(body));
}

@DeleteMapping ("/RimuoviRecord")
    public List<Modellante> deleteRecord(@RequestBody  Map<String,Map<String,Object>[]>body)
{
    for(Modellante m: datiFiltrati(body))
    {
        rec.removeIf(m1 -> m1.equals(m));
        /*for (Modellante m1: rec)
        {
            if(m1.equals(m))
            {rec.remove(m1);}
        }*/
    }
    return rec;
}

@PostMapping ("/InserisciRecord")
    public List<Modellante> insertRecord(@RequestBody Object[] body) {

    String[] campi = new String[4];
    int i = 0, j = 0;
    boolean check=false;
    for (Map<String, Object> map : getMetadata()) {
        if (map.containsValue("String")) {
            campi[i++] = (String) map.get("SourceField");
        }
    }
    Utilities.sort(campi);
    for (i = 0; i < 4; i++) {
        if (body[i] instanceof Number || body.length < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "è obbligatorio l'inserimento di tutti i campi stringa");
        }
    String[] colonna = Utilities.ottieniColonna(campi[i], rec).toArray(new String[ Utilities.ottieniColonna(campi[i], rec).size()]);

        while (j < colonna.length && ((String) body[i]).compareTo(colonna[j]) > 0)
    { j++;
        check=i!=0;}
}
if(!check && i==4)
{ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "è stato inserito un record già presente, per effettuare questa operazione si prega di rimuovere prima il record da sostituire");}
rec.add(j,new Modellante(body));
   return rec; }
}
