package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.AnimalProduction;



import org.json.simple.JSONValue;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Compito di questa classe è di caricare il dataset e svolgere il parsing del file .tsv
 * Un file tsv è una tabella con delimiter "\t" o ","
 */
 @Service
public class Download {


    private static List<AnimalProduction> record = new ArrayList<>(); //Lista di oggetti della Classe Modellante
    private final static String TAB_DELIMITER = "\t";  //separatore TSV
    private static List<Map> Lista = new ArrayList();  //Lista di stringhe dedicata per i Metadata

    /**
     * Costruttore della classe Download
     *
     * @throws IOException lancia l'eccezione nel caso si verifichino errori nell'Input o nell'Output
     */
    public Download() throws IOException {
        String fileTSV = "dataset.tsv";
        if (Files.exists(Paths.get(fileTSV)))      //se il file è già stato scaricato si procede al parsing altrimenti si deve effettuare il download prima
            System.out.println("Dataset caricato da locale");

        else {
            String url = "http://data.europa.eu/euodp/data/api/3/action/package_show?id=5QxHU0Y7cs987D7T9AA";
            try {
                //si trasforma l'URL in oggetto JSON dal quale si ricava il link da cui effettuare il download
                    String jline= getContent(url);
                    JSONObject obj = (JSONObject) JSONValue.parseWithException(jline);
                    JSONObject objI = (JSONObject) (obj.get("result"));
                    JSONArray objA = (JSONArray) (objI.get("resources"));

                    for (Object o : objA) { //scorro tutti gli oggetti fino a trovare quello di formato corretto
                        if (o instanceof JSONObject) {
                            JSONObject o1 = (JSONObject) o;  //converto il generico Object in JSONObject
                            String format = (String) o1.get("format");  //mi sposto all'interno del JSON per trovare l'url desiderato
                            String link = (String) o1.get("url");
                            System.out.println(format + " | " + link);
                            if (format.toLowerCase().contains("tsv")) {
                                downloadTSV(link, fileTSV);        //viene effettuato il download
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        MetadataParsing(fileTSV);

    }



    /**
     * Metodo che effettua il parsing del file tsv
     *
     * @param fileTSV Stringa con il nome del file tsv
     *
     */
    private void MetadataParsing(String fileTSV) {
        try (BufferedReader bfrd = new BufferedReader(new FileReader(fileTSV))) {
/*le successive istruzioni servono a riempire Lista con i metadata, ovvero una mappa nella quale si collocano
*  i nomi delle intestazioni delle colonne delle tabelle, al loro tipo e alla loro variabile associata (alias) definiti nella classe modellante*/
            String temp = bfrd.readLine();
            Field[] fields = AnimalProduction.class.getDeclaredFields();   //vettore contenente i campi della classe modellante
            temp = temp.replace(",", TAB_DELIMITER);
            temp = temp.replace("\\", TAB_DELIMITER);
            String[] splittedline = temp.trim().split(TAB_DELIMITER);
            int i = 0;
            for (Field f : fields) {
                Map<String, String> map = new HashMap<>();
                map.put("Alias", f.getName());
                map.put("SourceField", splittedline[i]);
                map.put("Type", f.getType().getSimpleName());
                Lista.add(map);
                i++;
            }

            /* Qui si effettua il parsing del dataset ovvero si riempe una lista di oggetti della classe modellante (record)
            * con i relativi valori presi dalla tabella */

            String linea;
            while ((linea = bfrd.readLine()) != null) {
                linea = linea.replace(",", TAB_DELIMITER);
                linea = linea.replace(":", "0");
                linea = linea.replace("p", "");
                linea = linea.replace("d", "");
                linea = linea.replace("e", ""); // si sono eliminati caratteri di "disturbo"
                splittedline = linea.trim().split(TAB_DELIMITER);
                String animals = splittedline[0].trim();
                String month = splittedline[1].trim();
                String unit = splittedline[2].trim();
                String geo = splittedline[3].trim();
                List<Float> anni = new ArrayList<>();
                i=0;
                while (i + 4 < splittedline.length) {
                    anni.add(Float.parseFloat(splittedline[4 + i].trim()));
                    i++;
                }
             //creato oggetto della classe modellante tramite chiamata del costruttore della classe modellante e lo aggiungo a record
                AnimalProduction parsedObj = new AnimalProduction(animals, month, unit, geo, anni);
                record.add(parsedObj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    /**
     * Metodo che restituisce record
     *
     * @return record
     */

    public List<AnimalProduction> getRecord() {
        return record;
    }

    /**
     * Metodo che restituisce la lista dei metadati
     *
     * @return Lista
     */
    public List<Map> getMetadata() {
        return Lista;
    }

    /**
     * Metodo che restituisce il record all'indice i
     *
     * @param i indice del record
     * @return restituisce il record all'indice i
     */
    public static AnimalProduction getRecord(int i) {
        if (i < record.size()) return record.get(i);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oggetto di indice " + i + " non esiste!");
    }

    /**
     * Metodo che restituisce la lista delle statistiche calcolate sui valori di un certo campo dei record oppure
     * su tutti i campi
     *
     */
    public List<Map> getStats(String nomeCampo) {
        List<Map> list = new ArrayList<>();
        if (nomeCampo.equals("")) {  //se non è stato passato il nome di un campo si scorrono tutti i campi
            Field[] fields = AnimalProduction.class.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals("anni") ) //si distingue se il campo è un anno o meno
                {for(int i=0; i<51; i++)
                    //viene chiamata la funzione che calcola le statistiche sui valori numerici passandole l'anno su cui effettuarle e la lista di avalori associata a quella'anno tramite getValues
                    list.add(Statistiche.getNumStatistiche(2019-i, getValues(2019-i,record))); }
                //viene chiamata la funzione che calcola le statistiche sulle stringhe passandole il nome del campo su cui effettuarle e la lista di avalori associata a quella'anno tramite getValues
                else {list.add(Statistiche.getStatistiche(f.getName(), getValues(f.getName(),record)));}
            }
        } else //le operazioni descritte in precedenza si effettuano solo per il campo inviato dall'utente
            try{Integer.parseInt(nomeCampo); //se la funzione parseInt non da errore il campo è un anno perciò si chiama la funzione per i numeri
            list.add(Statistiche.getNumStatistiche(Integer.parseInt(nomeCampo), getValues(Integer.parseInt(nomeCampo), record)));}
            catch ( NumberFormatException e) //se la funzione parseInt da errore il campo non è un anno perciò si chiama la funzione per le stringhe
            {list.add(Statistiche.getStatistiche(nomeCampo, getValues(nomeCampo, record)));}

        return list;  //si restituisce al controller la lista con le statistiche
    }

    /**

     * Metodo che estrae dalla lista di oggetti la lista di stringhe relativi ad animals,month,unit o geo

     *

     * @param nome campo del dataset del quale estrarre i valori
     * @return Values lista valori del campo richiesto

     */
    public List<String> getValues(String nome, List<AnimalProduction> lista) {
        List<String> Values = new ArrayList<>();
        for (AnimalProduction a : lista) {
            Values.add(a.getCampo(nome));
        }
        return Values;
    }

    /**

     * Metodo che estrae dalla lista di oggetti la lista dei valori relativi ad un anno

     *

     * @param nome anno del dataset del quale estrarre i valori

     * @return Values dei valori dell'anno richiesto

     */
    public List<Float> getValues(Integer nome, List<AnimalProduction> lista) {
        List<Float> Values = new ArrayList<>();
        for (AnimalProduction a : lista) {
            if(nome<2017 && Values.size()==50)
            {return Values;}
            else{
            Values.add(a.getAnni().get(2019 - nome));}
        }
        return Values;
    }



    /*Metodo che restituisce le statistiche su un dataset filtrato tramite operatori logici e condizionali,
    * il funzionamento è all'incirca come per getStats, sono differenti le chiamate alle funzioni presenti in statistiche
    * in quanto viene filtrato il dataset sulla base di un confronto tra ogni elemento corrispondente ad uno tra i campi animals,month,unit e geo
    * ed un riferimento dato dall'utente tramite uno degli operatori precedentemente selezionati */
    public List<Map> FilteredStats(String campoFiltro, String oper, String rif, String nomeCampo) {
        List<Map> lista = new ArrayList<>();
        if (nomeCampo.equals("")) {
            Field[] fields = AnimalProduction.class.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals("anni")) { //differente chiamata a seconda del tipo del campo corrente
                    for(int i=0;i<51;i++)
                        //questa volta alle funzioni che calcolano le statistiche non viene passato tutto il dataset, ma solo quello filtrato da filteredValues
                    lista.add(Statistiche.getNumStatistiche(2019-i, getValues(2019 - i, Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));

                } else {
                    lista.add(Statistiche.getStatistiche(f.getName(), getValues(f.getName(), Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
                }
            }
        } else try{Integer.parseInt(nomeCampo); //differente chiamata a seconda del tipo della variabile nomeCampo
            lista.add(Statistiche.getNumStatistiche(Integer.parseInt(nomeCampo), getValues(Integer.parseInt(nomeCampo), Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));}
            catch ( NumberFormatException e)
            {lista.add(Statistiche.getStatistiche(nomeCampo, getValues(nomeCampo, Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif)))); }
            return lista;
        }


/*Metodo equivalente all'omonimo, eccezion fatta per il campo sul quale effettuare il filtro, che in questo caso è uno degli anni*/
      public List<Map> FilteredStats(int campoFiltro, String oper, String rif, String nomeCampo) {
          List<Map> lista = new ArrayList<>();
          if(nomeCampo.equals("")) {
          Field[] fields = AnimalProduction.class.getDeclaredFields();
          for (Field f : fields) {
              if (f.getName().equals("anni")) { //differente chiamata a seconda del tipo del campo corrente
                  for(int i=0;i<51;i++)
                  lista.add(Statistiche.getNumStatistiche(2019-i, getValues(2019 - i, Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
              } else {
                  lista.add(Statistiche.getStatistiche(f.getName(), getValues(f.getName(), Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
              }
          }
           } else try {Integer.parseInt(nomeCampo); //differente chiamata a seconda del tipo della variabile nomeCampo
               lista.add(Statistiche.getNumStatistiche(Integer.parseInt(nomeCampo), getValues(Integer.parseInt(nomeCampo), Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif)))); }
               catch(NumberFormatException e) {lista.add(Statistiche.getStatistiche(nomeCampo, getValues(nomeCampo, Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));}
          return lista;
      }

    /*Metodo utilizzato unicamente per i filtri "$and" e "$or" il quale restituisce le statistiche in maniera similare al
     * getStats ma calcolandole sul dataset filtrato con uno dei due operatori sopra menzionati*/
    public List<Map> andOrStats(String nomeCampo, List <AnimalProduction> list) {
        List<Map> lista = new ArrayList<>();
        if (nomeCampo.equals("")){
            Field[] fields = AnimalProduction.class.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals("anni")) {      //differente chiamata a seconda del tipo del campo corrente
                    for (int i = 0; i < 51; i++) {
                        lista.add(Statistiche.getNumStatistiche(2019 - i, getValues(2019 - i, list)));
                    }
                }else{
                    lista.add(Statistiche.getStatistiche(f.getName(), getValues(f.getName(),list)));
                }
            }
        }
        else
            try {Integer.parseInt(nomeCampo);  //differente chiamata a seconda del tipo della variabile nomeCampo
                lista.add(Statistiche.getNumStatistiche(Integer.parseInt(nomeCampo), getValues(Integer.parseInt(nomeCampo), list)));
            } catch (NumberFormatException e) {
                lista.add(Statistiche.getStatistiche(nomeCampo, getValues(nomeCampo, list)));
            }
            return lista;
    }
    private static void downloadTSV(String url, String fileName) throws Exception {

        HttpURLConnection openConnection = (HttpURLConnection) new URL(url).openConnection();
        openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        InputStream in = openConnection.getInputStream();
        try {

            if(openConnection.getResponseCode() >= 300 && openConnection.getResponseCode() < 400) {
                downloadTSV(openConnection.getHeaderField("Location"),fileName);
                in.close();
                openConnection.disconnect();
                return;
            }
            Files.copy(in, Paths.get(fileName));
            System.out.println("File size " + Files.size(Paths.get(fileName)));
        } finally {
            in.close();
        }
    }
    private static String getContent(String url) throws Exception {
        HttpURLConnection openConnection = (HttpURLConnection) new URL(url).openConnection();
        openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        InputStream in = openConnection.getInputStream();
        String data = "";
        String line = "";
        try {
            if(openConnection.getResponseCode() >= 300 && openConnection.getResponseCode() < 400) {
                data = getContent(openConnection.getHeaderField("Location"));
                in.close();
                openConnection.disconnect();
                return data;
            }
            try { //lettura JSON e salvataggio su stringa
                InputStreamReader inR = new InputStreamReader(in);
                BufferedReader buf = new BufferedReader(inR);
                while ((line = buf.readLine()) != null) { //salvo il file json sulla stringa line
                    data+=line;
                }
            } finally {
                in.close();
            }
        } finally {
            in.close();
        }
        return data;
      }
    }