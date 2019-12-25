package it.esame.progettoOOP.Controllo;


import it.esame.progettoOOP.Modello.Modellante;
import it.esame.progettoOOP.Servizi.Download;

import it.esame.progettoOOP.Servizi.Parsing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    private Parsing p;
    private List<Modellante> rec;

    /**
     * Costruttore della classe Controller
     *
     * @param p oggetto di tipo MetaParsing
     */

    @Autowired
    public Controller(Parsing p){
        this.p = p;
        rec=p.getRecord();
    }




    @GetMapping("/Record")
    public List<Modellante> getRecord(){

        return rec;
    }

}
