package it.esame.progettoOOP.Servizi;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import sun.tools.asm.CatchData;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Compito di questa classe è di caricare il dataset e svolgere il parsing del file .tsv
 * Un file tsv è una tabella con delimiter "\t" o ","
 */

 public class Download {
    public Download() throws IOException {
        HttpURLConnection open= Utilities.Connect("https://data.europa.eu/euodp/data/api/3/action/package_show?id=5QxHU0Y7cs987D7T9AA");
        InputStream in= open.getInputStream();
        BufferedReader leggi = new BufferedReader(new InputStreamReader(in));
            String linea;
            linea = (leggi.readLine());
            linea = linea.substring(linea.indexOf("\"resources\""), linea.indexOf(", \"inte")).replaceFirst("\"", "{\"").concat("}");

            try {
                JSONObject job = (JSONObject) JSONValue.parseWithException(linea);
                JSONArray jar = (JSONArray) job.get("resources");
                for (Object temp : jar) {
                    JSONObject o = (JSONObject) temp;
                    if (((String) o.get("format")).contains("TSV")) {
                        linea = (String) o.get("url");
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            in.close();
            open.disconnect();
            open=Utilities.Connect(linea);
            in = open.getInputStream();
            if (open.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                in.close();
                open.disconnect();
                open=Utilities.Connect(open.getHeaderField("Location"));
                in=open.getInputStream();
                Files.copy(in, Paths.get("dataset.tsv"));
            }


    }





}