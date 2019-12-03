package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.AnimalProduction;



import org.json.simple.JSONValue;
import org.apache.tomcat.util.json.ParseException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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

public class Download {

    //public final static int intervallo = 51;
    private static List<AnimalProduction> record = new ArrayList<>(); //Lista di oggetti della Classe Modellante
    private final static String TAB_DELIMITER = "\t";  //separatore TSV
    private static List<Map> Lista = new ArrayList();  //Lista di stringhe dedicata per i Metadata
    private static List<String> time = new ArrayList<>(); //Lista di stringhe per il controllo del campo record

    /**
     * Costruttore della classe Download
     *
     * @throws IOException lancia l'eccezione nel caso si verifichino errori nell'Input o nell'Output
     */
    public Download() throws IOException {
        String fileTSV = "dataset.tsv";
        for (int i = 2019; i > 1968; i--)    //inizializzazione del Vettore tempo
            time.add(Integer.toString(i));
        if (Files.exists(Paths.get(fileTSV)))
            System.out.println("Dataset caricato da locale");

        else {
            String url = "http://data.europa.eu/euodp/data/api/3/action/package_show?id=5QxHU0Y7cs987D7T9AA";
            try {
                URLConnection openConnection = new URL(url).openConnection();
                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                InputStream in = openConnection.getInputStream();

                String jline = "";
                //String line = "";
                try {
                    //lettura del JSON e salvataggio su una stringa
                    InputStreamReader inR = new InputStreamReader(in);
                    BufferedReader buf = new BufferedReader(inR);

                    // while ((line = buf.readLine()) != null) {
                    //  jline += line;
                    // System.out.println(line);
                    //  }
                    jline = buf.readLine();
                } finally {
                    in.close();
                }
                //Conversione StringBuilder in oggetto JSON
                JSONObject obj = (JSONObject) JSONValue.parseWithException(jline);
                JSONObject objI = (JSONObject) (obj.get("result"));
                JSONArray objA = (JSONArray) (objI.get("resources"));

                for (Object o : objA) { //scorro tutti gli oggetti fino a trovare quello di formato corretto
                    if (o instanceof JSONObject) {
                        JSONObject o1 = (JSONObject) o;  //converto il generico Object in JSONObject
                        String format = (String) o1.get("format");  //mi sposto all'interno del JSON per trovare l'url desiderato
                        String urlD = (String) o1.get("url");
                        System.out.println(format + " | " + urlD);
                        if (format.toLowerCase().contains("tsv")) {
                            //downloadTSV(urlD, fileName);
                            downloadTSV(urlD, fileTSV);
                        }
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Metadata(fileTSV);
        Parsing(fileTSV);
    }

    private static void downloadTSV(String url, String NomeFile) throws Exception {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(NomeFile));
        }
    }


    /**
     * Metodo che effettua il parsing del file tsv
     *
     * @param fileTSV Stringa con il nome del file tsv
     */
    private void Parsing(String fileTSV) {
        try (BufferedReader bfrd = new BufferedReader(new FileReader(fileTSV))) {

            String temp = bfrd.readLine();           //memorizza la prima riga da utilizzare per i metadata
            Field[] fields = AnimalProduction.class.getDeclaredFields();
            temp = temp.replace(",", TAB_DELIMITER);
            temp = temp.replace("\\", TAB_DELIMITER);
            String[] splittedline = temp.trim().split(TAB_DELIMITER);
            int i = 0;


            for (Field f : fields) {
                Map<String, String> map = new HashMap<>();
                map.put("Alias", f.getName());
                map.put("SourceField", splittedline[i]);               //toscanelli decide di fare i metadati dentro il parsing ;-)
                map.put("Type", f.getType().getSimpleName());
                Lista.add(map);
                i++;
            }

            String linea;
            while ((linea = bfrd.readLine()) != null) {
                linea = linea.replace(",", TAB_DELIMITER);
                linea = linea.replace(":", "0");
                splittedline = linea.trim().split(TAB_DELIMITER);
                String animals = splittedline[0].trim();
                String month = splittedline[1].trim();
                String unit = splittedline[2].trim();
                String geo = splittedline[3].trim();
                List<Float> anni = new ArrayList<>();
                while (i + 4 < splittedline.length) {
                    anni.add(Float.parseFloat(splittedline[4 + i].trim()));
                    i++;
                }

                AnimalProduction parsedObj = new AnimalProduction(animals, month, unit, geo, anni);
                record.add(parsedObj);
            }

        } catch (
                IOException e) {
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
     * @return anni
     */
    //  public List getTime() {
    //      return time;
    //  }

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
     * Metodo che restituisce la lista con i valori di un certo campo dei record
     *
     * @return lista con i valori del campo
     */
    public List<Map> getStats(String nomeCampo) {
        List<Map> list = new ArrayList<>();
        if (nomeCampo.equals("")) {
            Field[] fields = AnimalProduction.class.getDeclaredFields();
            int i=0;
            for (Field f : fields) {
                if (f.getName().equals("anni") )
                {list.add(Statistiche.getNumStatistiche(f.getName(), getValues(2019-i,record)));
                i++;}
                else {list.add(Statistiche.getStatistiche(f.getName(), getValues(f.getName(),record)));}
            }
        } else if(Integer.parseInt(nomeCampo)>1967 || Integer.parseInt(nomeCampo)<2020)
            {list.add(Statistiche.getNumStatistiche(nomeCampo, getValues(Integer.parseInt(nomeCampo), record)));}
            else{list.add(Statistiche.getStatistiche(nomeCampo, getValues(nomeCampo, record)));}

        return list;
    }

    public List<String> getValues(String nome, List<AnimalProduction> lista) {
        List<String> Values = new ArrayList<>();
        for (AnimalProduction a : lista) {
            Values.add(a.getCampo(nome));
        }
        return Values;
    }

    public List<Float> getValues(Integer nome, List<AnimalProduction> lista) {
        List<Float> Values = new ArrayList<>();
        for (AnimalProduction a : lista) {
            Values.add(a.getAnno(2019 - nome + 4));
        }
        return Values;
    }

    
    public List<Map> getFilteredStats(String campoFiltro, String oper, Object rif, String nomeCampo) {
        List<Map> lista = new ArrayList<>();
        if (nomeCampo.equals("")) {
            Field[] fields = AnimalProduction.class.getDeclaredFields();
            int i = 0;
            for (Field f : fields) {
                if (f.getName().equals("anni")) {
                    lista.add(Statistiche.getNumStatistiche(f.getName(), getValues(2019 - i, Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
                    i++;
                } else {
                    lista.add(Statistiche.getStatistiche(f.getName(), getValues(f.getName(), Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
                }
            }
        } else if (nomeCampo.equals("anni")) {
            lista.add(Statistiche.getNumStatistiche(nomeCampo, getValues(Integer.parseInt(nomeCampo), Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
        } else {

            lista.add(Statistiche.getStatistiche(nomeCampo, getValues(nomeCampo, Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
        }
        return lista ;
    }


      public List<Map> getFilteredStats(int campoFiltro, String oper, Object rif, String nomeCampo) {
          List<Map> lista = new ArrayList<>();
          if(nomeCampo.equals("")) {
          Field[] fields = AnimalProduction.class.getDeclaredFields();
          int i = 0;
          for (Field f : fields) {
              if (f.getName().equals("anni")) {
                  lista.add(Statistiche.getNumStatistiche(f.getName(), getValues(2019 - i, Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
                  i++;
              } else {
                  lista.add(Statistiche.getStatistiche(f.getName(), getValues(f.getName(), Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
              }
          }
           } else if (nomeCampo.equals("anni")) {
               lista.add(Statistiche.getNumStatistiche(nomeCampo, getValues(Integer.parseInt(nomeCampo), Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
           } else {

               lista.add(Statistiche.getStatistiche(nomeCampo, getValues(nomeCampo, Filtri.FilteredValues(getValues(campoFiltro, record), oper, rif))));
           }
          return lista;
      }
    }