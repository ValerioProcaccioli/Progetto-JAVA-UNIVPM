package it.esame.progettoOOP.Servizi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Statistics {

    protected int count;

    public Statistics(List list) {
        count = list.size();
    }

    public abstract String toString();
}

