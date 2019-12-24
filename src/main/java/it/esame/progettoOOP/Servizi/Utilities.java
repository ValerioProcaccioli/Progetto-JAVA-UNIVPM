package it.esame.progettoOOP.Servizi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utilities {
    public static HttpURLConnection Connect(String url) throws IOException {
        HttpURLConnection open = (HttpURLConnection) new URL(url).openConnection();
        open.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

        return open;
    }


}
