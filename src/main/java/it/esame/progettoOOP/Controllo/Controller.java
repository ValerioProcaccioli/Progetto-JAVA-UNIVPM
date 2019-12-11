package it.esame.progettoOOP.Controllo;

import it.esame.progettoOOP.Modello.AnimalProduction;
import it.esame.progettoOOP.Servizi.Download;
import it.esame.progettoOOP.Servizi.Filtri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    private Download service;

    /**
     * Castruttore della classe Controller
     *
     * @param service oggetto di tipo download
     */

     @Autowired
    public Controller(Download service) {
         this.service = service;
     }

     /**
     * Metodo GET che su richiesta dell'utente restituisce la lista dei metadata
     *
     * @return "Lista" ovvero la lista nella classe Download che contiene i metadata
     */
    @GetMapping("/Metadati")
    public List getMetadati() {
        return service.getMetadata();
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce un elemento all'indice i della lista
     *
     * @param i indice della lista che si vuole ottenere
     * @return "record" ovvero la lista con gli oggetti AnimalProduction
     */

    @GetMapping("/Record/{i}")
    public AnimalProduction getAnimalProduction(@PathVariable int i) {
        return service.getRecord(i);
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce tutto il dataset parsato
     *
     *
     * @return "record" ovvero la lista con gli oggetti AnimalProduction
     */
    @GetMapping("/Record")
    public List<AnimalProduction> record(){
        return service.getRecord();
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce tutte le statistiche relative ad un campo,
     * o le statistiche relative a tutti i campi se non viene passato il nome di un campo
     * @param nameField nome del campo inserito dall'utente valore di default settato a "", per il quale si calcolano tutte le statistiche
     * @return restituisce tutte le statistiche relative al campo nameField o a tutti i campi
     */
    @GetMapping("/Statistiche")
    public List<Map> getStatistiche(@RequestParam(value = "Field", required = false, defaultValue = "") String nameField) {
        return service.getStats(nameField);
    }

    //Richieste POST per gestione filtri
    /**
     * Metodo che elimina dalla stringa body caratteri di "disturbo" restituendo una linea parsata da cui estrarre i
     * parametri necessari al filtro
     *
     * @param body body della richiesta POST contenente un filtro
     * @return linea stringa contenente oper, nomeCampo e rif separati da ":"
     */

    private static String ottieniFiltro(String body){
        String linea=body;

        linea=linea.replace("{","");
        linea=linea.replace("\"","");
        linea=linea.replace("}","");
        linea=linea.replace(",",":");
        linea=linea.replace("[","");
        linea=linea.replace("]","");
        return linea;
    }
    /**
     * Metodo che gestisce una richiesta POST restituendo il dataset filtrato dal filtro passato nel body.
     *
     * @param body body della richiesta POST contenente il filtro
     * @return lista di oggetti che soddisfano il filtro
     */
    @PostMapping("/DatiFiltrati")
    public List<AnimalProduction> datiFiltrati(@RequestBody String body) {

        String linea=ottieniFiltro(body);
        String[] line =linea.trim().split(":");   //si crea un'array di stringhe
        String nomeCampo,oper,rif;
        if(linea.contains("and") || linea.contains("or")) {
        //se l'operatore è $and o $or  si memorizzano due parametri in più essendoci un rif e un nomeCampo in più
            oper = line[0].trim();
            String[] nome = {line[1].trim(), line[3].trim()};
            String[] rifer = {line[2].trim(), line[4].trim()};
           /*viene chiamata la funzione filtro in ognuno di questi 4 casi:
           *  1.nomeCampo1 è un anno, nomeCampo2 è un anno
           *  2.nomeCampo1 è una stringa, nomeCampo2 è un anno
           *  3.nomeCampo1 è un anno, nomeCampo2 è una stringa
           *  4.nomeCampo1 è una stringa, nomeCampo2 è una stringa*/
            try {
                return Filtri.FilteredAndOr(service.getValues(Integer.parseInt(nome[0]), service.getRecord()), service.getValues(Integer.parseInt(nome[1]), service.getRecord()), oper, rifer);
            } catch (NumberFormatException e) {
                try {
                    return Filtri.FilteredAndOr(service.getValues(nome[0], service.getRecord()), service.getValues(Integer.parseInt(nome[1]), service.getRecord()), oper, rifer);
                } catch (NumberFormatException e1) {
                    try {
                        return Filtri.FilteredAndOr(service.getValues(Integer.parseInt(nome[0]), service.getRecord()), service.getValues(nome[1], service.getRecord()), oper, rifer);
                    } catch (NumberFormatException e2) {
                        return Filtri.FilteredAndOr(service.getValues(nome[0], service.getRecord()), service.getValues(nome[1], service.getRecord()), oper, rifer);
                    }
                }
            }
        }
       //per qualsiasi altro operatore sono sempre 3 i parametri
       else{ nomeCampo=line[0].trim();
        oper=line[1].trim();
        rif=line[2].trim();
        //differente chiamata a seconda del tipo della variabile nomeCampo
        try {Integer.parseInt(nomeCampo);
            return Filtri.FilteredValues(service.getValues(Integer.parseInt(nomeCampo), service.getRecord()), oper, rif); //chiamata al metodo getDatiFiltrati del package service che restituisce la lista di dati filtrati
        }
        catch ( NumberFormatException e){
        return Filtri.FilteredValues(service.getValues( nomeCampo, service.getRecord()), oper, rif);}}
    }
    /**
     * Metodo POST che su richiesta dell'utente restituisce le statistiche filtrate relative ad un campo(opzionale)
     * o su tutti i campi, considerando i record che soddisfano il filtro
     *
     * @param nomeCampo se è vuoto (valore di default) le statistiche si calcolano su tutti i campi
     * @param body body della richiesta POST contenente il filtro
     * @return restituice le statistiche filtrate di un campo inserito dall'utente o tutte le statistiche se il campo è vuoto
     */
   @PostMapping("/StatisticheFiltrate")
  public List<Map> StatisticheFiltrate(@RequestParam(value = "Field", required = false, defaultValue = "") String nomeCampo, @RequestBody String body){
                                    //Effettua il parsing del body
       String linea=ottieniFiltro(body);
       String[] line =linea.trim().split(":");
       if(linea.contains("and") || linea.contains("or"))
       //si chiama la funzione che calcola le statistiche sul dataset filtrato da $and o $or
       {
        return  service.andOrStats(nomeCampo, datiFiltrati(body));
       }
       else{
           String FilteredField,oper,rif;
           FilteredField=line[0];
           oper=line[1];
           rif=line[2];
           //differente chiamata a seconda del tipo della variabile nomeCampo
        try {Integer.parseInt(FilteredField);
            return service.FilteredStats(Integer.parseInt(FilteredField), oper, rif, nomeCampo);  //chiamata al metodo getDatiFiltrati del package service che restituisce la lista di dati filtrati
    }catch (NumberFormatException e)
        {return service.FilteredStats(FilteredField, oper, rif, nomeCampo);}
       }
    }


}

