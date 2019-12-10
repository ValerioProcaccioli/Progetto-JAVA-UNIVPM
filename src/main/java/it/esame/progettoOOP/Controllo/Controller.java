package it.esame.progettoOOP.Controllo;

import it.esame.progettoOOP.Modello.AnimalProduction;
import it.esame.progettoOOP.Servizi.Download;
import it.esame.progettoOOP.Servizi.Filtri;
import it.esame.progettoOOP.Servizi.Statistiche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

@RestController
public class Controller {
    private Download service;

    /**
     * Castruttore della classe Controller
     *
     * @param service
     */

     @Autowired
    public Controller(Download service) {
         this.service = service;
     }

    /**
     * Metodo GET che su richiesta dell'utente restituisce il vettore degli anni
     *
     * @return "
     */
      /*@GetMapping("/Time")
      public List getAnni() {
            return service.getTime();  //commentato in download
      }*/

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

    @GetMapping("/Record")
    public List<AnimalProduction> record(){
        return service.getRecord();
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce tutte le statistiche relative ad un campo
     *
     * @param nameField nome del campo inserito dall'utente
     * @return restituisce tutte le statistiche relative al campo nameField
     */
    @GetMapping("/Statistiche")
    public List<Map> getStatistiche(@RequestParam(value = "Field", required = false, defaultValue = "") String nameField) {
        return service.getStats(nameField);
    }

    //Richieste POST per gestione filtri
    /**
     * Metodo per eseguire il parsing del filtro passato tramite body di una POST
     *
     * @param body body della richiesta POST contenente un filtro
     * @return mappa contenente i parametri del filtro: nomeCampo, operatore e valore di riferimento
     */
  /*  private static Map<String, Object> ottieniFiltro(String body) {
        Map<String, Object> bodyParsato = new BasicJsonParser().parseMap(body); //il filtro ha la sintassi di un json
        Object nomeCampo = bodyParsato.keySet().toArray(new String[0])[0];
        Object valore = bodyParsato.get(nomeCampo);


        Object rif;
        String oper;
        if (valore instanceof Map) {
            Map filtro = (Map) valore;
            oper = ((String) filtro.keySet().toArray()[0]).toLowerCase(); //dentro la stringa oper salvo la prima chiave che coincide con l'operatore
            rif = filtro.get(oper); //prendo il valore associato alla chiave oper
        } else { //l' operatore di default è l'uguaglianza
            oper = "$eq";
            rif = valore;
        }
        Map<String, Object> filtro = new HashMap<>(); //creo la mappa che conterrà i parametri del filtro
        filtro.put("oper", oper);
        filtro.put("campo", nomeCampo);
        filtro.put("rif", rif);
        return filtro;
    }*/
    private static String ottieniFiltro(String body){
        String linea=body;
        List<AnimalProduction> list= new ArrayList<>();
        linea=linea.replace("{","");
        linea=linea.replace("\"","");
        linea=linea.replace("}","");
        linea=linea.replace(",",":");
        linea=linea.replace("[","");
        linea=linea.replace("]","");
        return linea;
    }
    /**
     * Metodo che gestisce una richiesta POST alla rotta "/data", resituendo la lista dei record che soddisfano il filtro
     *
     * @param body body della richiesta POST contenente il filtro
     * @return lista di oggetti che soddisfano il filtro
     */
    @PostMapping("/DatiFiltrati")
    public List<AnimalProduction> getDatiFiltrati(@RequestBody String body) {
        //Map<String, Object> filter = ottieniFiltro(body); //creo mappa per contenere il filtro parsato
        //estraggo i parametri del filtro
        String linea=ottieniFiltro(body);
        String line[]=linea.trim().split(":");
        String nomeCampo,oper,rif;
        if(linea.contains("and") || linea.contains("or")) {

            oper = line[0].trim();
            String[] nome = {line[1].trim(), line[3].trim()};
            String[] rifer = {line[2].trim(), line[4].trim()};
            try {
                return Filtri.FilteredEndor(service.getValues(Integer.parseInt(nome[0]), service.getRecord()), service.getValues(Integer.parseInt(nome[1]), service.getRecord()), oper, rifer);
            } catch (NumberFormatException e) {
                try {
                    return Filtri.FilteredEndor(service.getValues(nome[0], service.getRecord()), service.getValues(Integer.parseInt(nome[1]), service.getRecord()), oper, rifer);
                } catch (NumberFormatException e1) {
                    try {
                        return Filtri.FilteredEndor(service.getValues(Integer.parseInt(nome[0]), service.getRecord()), service.getValues(nome[1], service.getRecord()), oper, rifer);
                    } catch (NumberFormatException e2) {
                        return Filtri.FilteredEndor(service.getValues(nome[0], service.getRecord()), service.getValues(nome[1], service.getRecord()), oper, rifer);
                    }
                }
            }
        }
       // Object nomeCampo = filter.get("campo");
        //String oper = (String) filter.get("oper");
        //Object rif = filter.get("rif");
       else{ nomeCampo=line[0].trim();
        oper=line[1].trim();
        rif=line[2].trim();
        try {Integer.parseInt(nomeCampo);
            return Filtri.FilteredValues(service.getValues(Integer.parseInt(nomeCampo), service.getRecord()), oper, rif); //chiamata al metodo getDatiFiltrati del package service che restituisce la lista di dati filtrati
        }
        catch ( NumberFormatException e){
        return Filtri.FilteredValues(service.getValues( nomeCampo, service.getRecord()), oper, rif);}}
    }
    /**
     * Metodo POST che su richiesta dell'utente restituisce le statistiche filtrate relative ad un campo(opzionale) o su tutti i campi, considerando i record che soddisfano il filtro
     *
     * @param nomeCampo
     * @param body il JSON passato dall'utente con i relativi campo, operatore e riferimento
     * @return restituice le statistiche filtrate di un campo inserito dall'utente
     */
   @PostMapping("/StatisticheFiltrate")
  public List<Map> getStatisticheFiltrate(@RequestParam(value = "Field", required = false, defaultValue = "") String nomeCampo, @RequestBody String body){
                                    //Effettua il parsing del body
       String linea=ottieniFiltro(body);
       String line[]=linea.trim().split(":");
       if(linea.contains("and") || linea.contains("or"))
       {
        return  service.getEndorStats(nomeCampo,getDatiFiltrati(body));
       }
       else{
           String FilteredField,oper,rif;
           FilteredField=line[0];
           oper=line[1];
           rif=line[2];
        try {Integer.parseInt(FilteredField);
            return service.getFilteredStats(Integer.parseInt(FilteredField), oper, rif, nomeCampo);  //chiamata al metodo getDatiFiltrati del package service che restituisce la lista di dati filtrati
    }catch (NumberFormatException e)
        {return service.getFilteredStats(FilteredField, oper, rif, nomeCampo);}
       }
    }


}

