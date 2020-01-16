package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.Modellante;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
/*Classe che si occupa di effettuare il parsing dei dati, ovvero di inserirli in una lista di elementi della classe
* modellante, ciò viene effettuato andando ad eliminare elementi di disturbo come caratteri o spazi*/
public class Parsing {

    public String[] valori;
    public List<Modellante> record = new ArrayList<>();
    public Parsing() throws IOException {
        if(!Files.exists(Paths.get("dataset.tsv"))){   //se il file non esiste si esegue il download
        Download dw= new Download();}
        BufferedReader leggi = new BufferedReader(new FileReader("dataset.tsv"));
        leggi.readLine();
        String linea;
        while (record.size()<351)
        {
            linea = leggi.readLine();
            linea = linea.replace(" ","");
            linea = linea.replace(",", "\t");
            valori = linea.trim().split("\t");
            Modellante temp= new Modellante(valori);    //viene passato l'array di stringhe contenente una riga del dataset al costruttore della claase modellante
            record.add(temp);

        }
    }

    public List<Modellante> getRecord() {
       return record;
    }

}
