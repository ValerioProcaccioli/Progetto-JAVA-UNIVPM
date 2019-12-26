package it.esame.progettoOOP.Servizi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StrStatistics extends Statistics{
    private String contElem;
        public StrStatistics(List<String> list) {
            super(list);
            Set<String> elem = new HashSet<>(list);
            contElem=Utilities.contaElem(list, elem);
        }

        @Override
        public String toString() {
            return ("{{Conteggio elementi con stesso nome: ["+contElem+"]}, { conteggio elementi lista: "+count+" }}");
        }
    }
