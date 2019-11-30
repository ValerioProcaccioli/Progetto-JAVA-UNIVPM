package it.esame.progettoOOP.Servizi;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistiche {
    public static float sum (List<Float> lista )
    {
        float s=0;
        for (Float n: lista)
            s+=n;
        return s;
    }

    public static int count(List lista)
    {
        return lista.size();
    }

    public static float avg(List<Float> lista)
    {
        return sum(lista)/count(lista);
    }

    public static float min(List<Float> lista)
    {
        float min = lista.get(0);

        for (Float n: lista)
            if (n < min) min = n;

        return min;
    }

    public static float max(List<Float> lista)
    {
        float max = lista.get(0);

        for (Float n: lista)
            if (n > max) max = n;

        return max;
    }

    public static float devStd(List<Float> lista) {

        float avg = avg(lista);

        float var = 0;

        for (Float n : lista) {

            var += Math.pow(n - avg, 2);

        }

        return (float) Math.sqrt(var);

    }

    public static Map<Object, Integer> contaElementiUnici(List lista) {

        Map<Object, Integer> m = new HashMap<>();

        for (Object o : lista) {

            Integer num=m.get(o);

            m.put(o, (num == null ? 1 : num+1));       //se la chiave si trova già nella mappa aumento di 1 il valore

        }                                               // altrimenti aggiungo l'oggetto appena trovato alla mappa con valore 1

        return m;

    }

    public static Map<String, Object> getNumStatistiche(String nomeCampo, List<Float> lista) {

        Map<String, Object> m = new HashMap<>();

        m.put("field", nomeCampo);

        if (!lista.isEmpty()) { //calcolo le statistiche solo se la lista non è vuota


                //riempio la mappa con le statistiche numeriche relative all'anno richiesto

                m.put("avg", avg(lista));

                m.put("min", min(lista));

                m.put("max", max(lista));

                m.put("dev std", devStd(lista));

                m.put("sum", sum(lista));

                m.put("count", count(lista));

                return m;

            }

        return m;

    }

    public static   Map<String, Object> getStatistiche(String nomeCampo, List<String> lista) {

        Map<String, Object> m = new HashMap<>();

        m.put("field", nomeCampo);

        if (!lista.isEmpty()) {
            m.put("elementiUnici", contaElementiUnici(lista));

            m.put("count", count(lista));
        }
        return m;
    }

}
