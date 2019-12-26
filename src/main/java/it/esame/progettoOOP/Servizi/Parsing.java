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

public class Parsing {

    public String[] valori;
    public List<Modellante> record = new ArrayList<>();
    public Parsing() throws IOException {
        if(!Files.exists(Paths.get("dataset.tsv"))){
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

            Modellante temp= new Modellante(valori);
            record.add(temp);

        }
    }

    public List<Modellante> getRecord() {
       return record;
    }

}
