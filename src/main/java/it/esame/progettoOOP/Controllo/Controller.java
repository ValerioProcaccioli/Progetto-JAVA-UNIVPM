package it.esame.progettoOOP.Controllo;


import it.esame.progettoOOP.Servizi.Download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    private Download service;

    /**
     * Costruttore della classe Controller
     *
     * @param service oggetto di tipo download
     */

    @Autowired
    public Controller(Download service) {
        this.service = service;
    }


}
