package it.esame.progettoOOP.Servizi;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Questa classe apre l'url assegnatoci al fine di ricavare il link alla pagina web contenente il dataset
 * che poi viene salvato in un file omonimo in formato tsv
 */

 public class Download {
    public Download() throws IOException {
        HttpURLConnection open= Utilities.Connect("https://data.europa.eu/euodp/data/api/3/action/package_show?id=5QxHU0Y7cs987D7T9AA");
        InputStream in= open.getInputStream();
        BufferedReader leggi = new BufferedReader(new InputStreamReader(in));
            String linea;
            linea = (leggi.readLine());
            linea = linea.substring(linea.indexOf("\"resources\""), linea.indexOf(", \"inte")).replaceFirst("\"", "{\"").concat("}");
            //viene eliminato dal json originale il testo non necessario facendo in modo che linea contenga
            // solo l'oggetto json resources il quale contiene il link dal quale effettuare il download
            try {
                JSONObject job = (JSONObject) JSONValue.parseWithException(linea);
                JSONArray jar = (JSONArray) job.get("resources");
                for (Object temp : jar) {
                    JSONObject o = (JSONObject) temp;
                    if (((String) o.get("format")).contains("TSV")) {
                        linea = (String) o.get("url");                    //linea ora contiene il link per il download
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            in.close();
            open.disconnect();
            open=Utilities.Connect(linea);
            in = open.getInputStream();
            //è possibile che il nuovo link ci porti ad una pagina di reindirizzamento (segnalato dallo specifico
            // responseCode) questo if gestisce tale problematica
            if (open.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                in.close();
                open.disconnect();
                open=Utilities.Connect(open.getHeaderField("Location"));
                in=open.getInputStream();
            }
        Files.copy(in, Paths.get("dataset.tsv"));

    }





}