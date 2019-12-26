package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.Modellante;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utilities {
    public static HttpURLConnection Connect(String url) throws IOException {
        HttpURLConnection open = (HttpURLConnection) new URL(url).openConnection();
        open.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

        return open;
    }

    public static Object trasforma(String numero) {

        return numero.replace(numero.charAt(numero.length() - 1), '0');
    }

    public static float min(List<Float> list)
    {float min = list.get(0);
        for (Float n: list)
            if (n < min) min = n;
            return min;}

    public static float max(List<Float> lista)
    {
        float max = lista.get(0);
        for (Float n: lista)
            if (n > max) max = n;
        return max;
    }

    public static float sum (List<Float> lista )
    {
        float s=0;
        for (Float n: lista)
            s+=n;
        return s;
    }

    public static float devStd(List<Float> lista, float avg) {
        float var = 0;
        for (Float n : lista) {
            var += Math.pow(n - avg, 2);
        }
        return (float) Math.sqrt(var);

    }

    public static String contaElem(List<String> list, Set<String> elem )
    {
        int n=0;
        String risultato="";
        for(Object o: elem)
        {
            for(Object o1: list)
            {
                if(o1.equals(o))
                {n++;}
            }
            risultato=risultato+"{"+o+" : "+n+"}";
        }
        return risultato;
    }

}
