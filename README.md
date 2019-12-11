Progetto esame OOP-NV-(Carletti-Procaccioli)

Progetto d'esame per il corso di "Programmazione ad oggetti" a.a. 2018/2019 all'interno del corso di laurea di Ingegneria Informatica e dell'Automazione dell'Università Politecnica delle Marche.
Introduzione

Questa repository contiene un'applicazione Java basata sul framework Spring che restituisce tramite API REST GET o POST dati e statistiche in formato JSON di un dataset che assegnatoci. Il progetto può essere compilato attraverso il framework Gradle che gestisce l'importazione delle librerie Spring.
Funzionamento all'avvio

L'applicazione, una volta lanciata, esegue il download di un dataset in formato TSV contenuto in un JSON fornito tramite un URL. Il download del dataset avviene solo se non è stato precedentemente effettuato e verrà salvato nella cartella del progetto con il nome di dataset.tsv. Successivamente viene effettuato il parsing del file TSV in modo da poter creare le istanze del modello che verranno inserite all'interno di una lista. Inoltre il programma avvia un web-server in locale sulla porta 8080 che riceve richieste dall'utente.
Interpretazione modello e dati

I dati sono tratti dal sito dell'Unione Europea e in particolare essi riguardano la "PigPopulation" durante l'arco di anni 2019-1969
Il dataset TSV contiene:

    Il dato relativo agli animali (indicato con "animals");
    Il dato relativo al mese d'avvenimento (indicato con "month);
    Il dato relativo all'unità di misura (indicata con "unit");
    Il dato relativo al luogo d'avvenimento (indicato con "geo");
    Il dato relativo agli anni già definito nell'arco 2019-1969 (indicato con "time");

Packages e classi

Il progetto presenta un package principale it.esame.progettoOOP che contiene tutti i sorgenti delle classi Java e in particolare la classe main ProgettoopApplication che avvia il server Spring. Le altre classi sono divise in tre package:

    Modello: contiene la classe AnimalProduction che modella il singolo record del dataset;
    Servizi: contiene la classe Download che gestisce il download, il parsing, i metadati, le variabili utili e l'accesso al dataset, la classe Statistiche per il calcolo delle statistiche numeriche e non, la classe Filtri per la gestione del filtraggio dei dati;
    Controllo: contiene la classe Controller che gestisce richieste da parte dell'utente (risposte sottoforma di stringhe in formato JSON;

Visionare la JavaDoc per informazioni più specifiche su classi e relativi metodi.
