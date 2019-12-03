package it.esame.progettoOOP.Controllo;



import it.esame.progettoOOP.Modello.AnimalProduction;

import it.esame.progettoOOP.Servizi.Download;

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

    @GetMapping("/Time")

    public List getAnni() {

        return service.getTime();  //commentato in download

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

     * Metodo GET che su richiesta dell'utente restituisce tutte le statistiche relative ad un campo

     *

     * @param nameField nome del campo inserito dall'utente

     * @return restituisce tutte le statistiche relative al campo nameField

     */

    @GetMapping("/Statistiche")

    public List<Map> getStatistiche(@RequestParam(value = "Field", required = false, defaultValue = "") String nameField) {

        return service.getStats(nameField);

    }





}



