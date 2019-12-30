package it.esame.progettoOOP.Servizi;

import java.util.*;

public class StrStatistics extends Statistics{
    private Map<String,Integer> contElem;
        public StrStatistics(List<String> list) {
            super(list);
            contElem=Utilities.contaElem(list);
        }

        @Override
        public Map<String, Object> retResult() {
            mappa.put("conteggio elementi con stesso nome: ", contElem);
            return mappa;
        }
    }
