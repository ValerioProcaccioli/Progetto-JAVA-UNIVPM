package it.esame.progettoOOP.Controllo;




import it.esame.progettoOOP.Modello.Modellante;
import it.esame.progettoOOP.Servizi.*;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;



@RestController
public class Controller {
    private List<Modellante> rec;


    /**
     * Costruttore della classe Controller
     *
     * @param  p oggetto di tipo Parsing
     */

    @Autowired
    public Controller(Parsing p){
        rec=p.getRecord();
    }

    /**
     * Metodo get che resituisce tutti i record del dataset
    *
    * @return rec*/

    @GetMapping("/Record")
    public List<Modellante> getRecord(){
        return rec;
    }

    /**
     * Metodo get che resituisce il record del dataset corrispondente all'intero passato come parametro
     *
     * @param i indice della riga del dataset
     * @return riga del dataset*/
    @GetMapping("/Record/{i}")
    public Modellante getRecord(@PathVariable int i) {
        return rec.get(i);
    }

    /**
     * Metodo Get che restituisce i metadata
     *
     * @return list lista coi metadata*/
    @GetMapping("/Metadata")
    public List<Map<String,Object>> getMetadata(){
        List<Map<String,Object>> list=new ArrayList<>();
        Set<Object> nomi = new HashSet<>();
        for (int i = 2019; i > 1968; i--)
            nomi.add(i);      //si aggiungono all'insieme di nomi tutti gli anni presenti nel dataset
        for(Object o: (rec.get(0)).getNomi(nomi)) { //si scorre l'insieme definito in precedenza arricchito con i nomi dei campi stringa
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

/**Metodo che restituisce tutte le statistiche su un campo o su tutti i campi se l'utente non ne passa uno
specifico come parametro. Il secondo parametro è stato inserito poichè questo metodo può calcolare statistiche sia sul
dataset originale che sullo stesso sottoposto ad un eventuale fitro. Dato che nel primo caso il metodo viene chiamato
direttamente dall'utente era necessario che tutti i parametri della funzione fossero inseribili dall'utente,
anche se in realtà quest'ultimo dovrà inserire solo il primo, mentre il secondo parametro verrà utilizzato solo
ed esclusivamente nella chiamata di questo metodo contenuta nel corpo del metodo FilteredStats,
il quale passerà un dataset filtrato

 @param nomeCampo nome del campo da filtrare
 @param lista lista su cui effettuare il filtraggio se è vuoto assume il valore rec
 @return list lista contenente tutte le statistiche*/
@SafeVarargs
@GetMapping ("/Statistiche")
    public final List<Map<String,Map<String,Object>>> getStats(@RequestParam(value = "Campo", required = false, defaultValue = "all") String nomeCampo, @RequestParam(value="nnn",required = false) List<Modellante>... lista) {
    List<Map<String,Map<String, Object>>> list = new ArrayList<>();
    if (nomeCampo.equals("all")) {

        for (Map<String,Object> map : getMetadata()) {
            list.add(getOneStats((String) map.get("SourceField"),lista));
        }
    } else {
        list.add(getOneStats(nomeCampo, lista));
        return list;
    }
    return list;
}

/**Questo metodo restituisce a getStats le statistiche relative ad un singolo campo, le quali possono essere calcolate
sia sul dataset originale sia sullo stesso sottoposto ad un eventuale fitro.

 @param nomeCampo nome del campo da filtrare
 @param lista lista su cui effettuare il filtraggio se è vuoto assume il valore rec*/
    @SafeVarargs
    public final Map<String,Map<String,Object>> getOneStats(String nomeCampo, List<Modellante>... lista){
        List<Modellante> m=new ArrayList<>();
        Map<String,Map<String,Object>> map=new HashMap<>();
        try {
            for(List<Modellante> m1: lista)
                m=m1;
        }catch (NullPointerException e){m=rec;}  //se il parametro lista non è stato passato, la lista su qui calcolare le statistiche è il datset completo
        try {Integer.parseInt(nomeCampo);//se il nome campo è un anno si chiama il metodo relativa alla classe NumStatistics
        map.put(nomeCampo,new NumStatistics(Utilities.ottieniColonna(nomeCampo,m)).retResult());
    }catch (NumberFormatException e){//se invece non lo è si chiama il metodo relativo alla classe StrStatistics
        map.put(nomeCampo,new StrStatistics(Utilities.ottieniColonna(nomeCampo,m)).retResult());}
        return map;
}


/**Questo è un particolare metodo, utile per segnalare all'utente che ha effettuato una richiesta POST un errore nella
* sintassi della richiesta senza generare uno stop nell'esecuzione.*/
@ExceptionHandler ({HttpMessageNotReadableException.class})
public String gestisciEccezioni () { return "Richiesta POST illeggibile. Consultare il readMe per chiarimenti.";}

/**Questo metodo restituisce il dataset filtrato tramite il filtro espresso nel body. Da sottolineare che viene
effettuato un casting del body a Map<String,Map<String,Object>[]> direttamente nella chiamata del metodo (operazione che
verrà effettuata anche nei successivi due metodi), motivo per il quale si è resa necessario il metodo precedente.

 @param body oggetto json contenente il filtro da applicare
 @return dataset filtrato */
@PostMapping ("/DatiFiltrati")
    public List<Modellante> datiFiltrati (@RequestBody  Map<String,Map<String,Object>[]> body) {
        return new Filter(body).Filtra(rec);
}

/**Questo metodo restuiscce le statistiche sul dataset filtrato
 *
 * @param body oggetto json contenente il filtro da applicare
 * @param nomeCampo nome del campo sul quale effettuare le statistiche
 *
 * @return la lista contenente le statistiche calcolate su uno o più campi */
@PostMapping("/StatisticheFiltrate")
   public List<Map<String,Map<String,Object>>> FilteredStats(@RequestBody  Map<String,Map<String,Object>[]>body,@RequestParam(value = "Campo", required = false, defaultValue = "") String nomeCampo)
{
    return getStats(nomeCampo,datiFiltrati(body));
}

/**Questo metodo restituisce il dataset privato delle righe individuate dal filtro
 *
 * @param body oggetto json contenente il filtro da applicare
 *
 * @return il dataset privato degli elementi filtrati*/
@DeleteMapping ("/RimuoviRecord")
    public List<Modellante> deleteRecord(@RequestBody  Map<String,Map<String,Object>[]>body)
{
    for(Modellante m: datiFiltrati(body))
    {
        rec.removeIf(m1 -> m1.equals(m));   //si scorrono tutte le righe del dataset eliminando quelle selezionate dal filtro
    }
    return rec;
}

/**Questo metodo riceve come parametro un'array di oggetti che verrà convertito in un oggetto della classe modellante
* il quale verrà poi inserito nel dataset rispettando l'ordine crescente, il dataset modificato verrà restituito.
 *
 * @param body jsonArray contenente il record da inserire
 *
 * @return il dataset arricchito del nuovo elemento*/

@PostMapping ("/InserisciRecord")
    public List<Modellante> insertRecord(@RequestBody Object[] body) {
    String[] campi = new String[4];
    int i = 0, j = 0;
    boolean check=false;
    for (Map<String, Object> map : getMetadata()) {
        if (map.containsValue("String")) {
            campi[i++] = (String) map.get("SourceField");   //si riempe il vettore campi con i 4 campi stringa
        }
    }
    Utilities.sort(campi); //il vettore campi viene ordinato nella maniera corretta
    for (i = 0; i < 4; i++) { //si verifica l'ordine per ognuno dei 4 campi al fine ddi trovare la posizione in cui inserire la nuova riga
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
while (i<body.length)
{  if(!(body[i] instanceof Number))  //se è presente un valore non numerico, esso viene trasformato nel carattere ":" che sarà poi eliminato
{body[i]=':';}
    i++;}
rec.add(j,new Modellante(body));
   return rec; }
}
