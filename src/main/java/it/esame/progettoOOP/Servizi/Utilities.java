package it.esame.progettoOOP.Servizi;

import it.esame.progettoOOP.Modello.Modellante;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Utilities {
    /*Metodo che apre una connessione con l'url permettendo poi di aprire uno stream di input*/
    public static HttpURLConnection Connect(String url) throws IOException {
        HttpURLConnection open = (HttpURLConnection) new URL(url).openConnection();
        open.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

        return open;
    }
/*Metodo che sostituisce i caratteri di disturbo (sempre presenti in fondo al numero)
con uno 0 che lascia immutato il valore numerico*/
    public static Float trasforma(String numero) {
        return Float.parseFloat(numero.replace(numero.charAt(numero.length() - 1), '0'));
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

    public static Map<String,Integer> contaElem(List<String> list)
    {
        int n;
        Map<String,Integer> risultato= new HashMap<>();
        for(String o: list)
        {n=0;
            if(!(risultato.containsKey(o))) {
            for (String o1 : list) {
                if (o.equals(o1)) {
                    n++;
                }
            }
            risultato.put(o, n);
        }
        }
        return risultato;
    }

    /*Metodo che restituisce tutti i valori associati ad uno dei campi del record, viene utilizzato sia per i campi
    * numerici che per i campi stringa*/
    public static  <T> List<T> ottieniColonna(String nome, List<Modellante> lista) {
        List<T> colonna= new ArrayList<>();
        Object val;
        for (Modellante m : lista) {
            val=m.getValori(nome);
            if (!(val.equals("nnn"))) {
                colonna.add((T) val);
            }
        }
       return colonna;
    }

    /*Metodo che riordina i quattro campi stringa secondo l'ordinamento presente nel dataset*/
    public static void sort(String[] vettore) {
        String temp;
        for (int i = 0; i < vettore.length-1; i++) {
            for (int j = i+1; j < vettore.length; j++) {
                if(vettore[j].length()>vettore[i].length())
                {temp=vettore[j];
                    vettore[j]=vettore[i];
                vettore[i]=temp;}
            }
        }
    }
}
