# Progetto esame OOP-NV-(Carletti-Procaccioli)

Progetto d'esame per il corso di "Programmazione ad oggetti" a.a. 2018/2019 all'interno del corso di laurea di Ingegneria Informatica e dell'Automazione dell'Università Politecnica delle Marche.

## Introduzione

Questa repository contiene un'applicazione Java basata sul framework Spring che restituisce tramite API REST GET o POST dati e statistiche in formato JSON di un dataset assegnatoci. Il progetto può essere compilato attraverso il framework Gradle che gestisce l'importazione delle librerie Spring.
Funzionamento all'avvio

L'applicazione, una volta lanciata, esegue il download di un dataset in formato TSV contenuto in un JSON fornito tramite un URL. Il download del dataset avviene solo se non è stato precedentemente effettuato e verrà salvato nella cartella del progetto con il nome di dataset.tsv. Successivamente viene effettuato il parsing del file TSV in modo da poter creare le istanze del modello che verranno inserite all'interno di una lista. Inoltre il programma avvia un web-server in locale sulla porta 8080 che riceve richieste dall'utente.
Interpretazione modello e dati

I dati sono tratti dal sito dell'Unione Europea e in particolare essi riguardano la "PigPopulation" durante l'arco di anni 2019-1969
Il dataset TSV contiene:

 - Il dato relativo agli animali (indicato con `animals`);
      
 - Il dato relativo al mese d'avvenimento (indicato con `month`);

      

 - Il dato relativo all'unità di misura (indicata con `unit`);
        
 - Il dato relativo al luogo d'avvenimento (indicato con `geo`);
   
          
 - Il dato relativo agli anni già definito nell'arco 2019-1969 (indicato
   con `anni`);

  
  

## Packages e classi

Il progetto presenta un package principale `it.esame.progettoOOP` che contiene tutti i sorgenti delle classi Java e in particolare la classe main ProgettoopApplication che avvia il server Spring. Le altre classi sono divise in tre package:

   `Modello`: contiene la classe AnimalProduction che modella il singolo record del dataset;
    `Servizi`: contiene la classe Download che gestisce il download, il parsing, i metadati, le variabili utili e l'accesso al dataset, la classe Statistiche per il calcolo delle statistiche numeriche e non, la classe Filtri per la gestione del filtraggio dei dati;
    `Controllo`: contiene la classe Controller che gestisce richieste da parte dell'utente (risposte sottoforma di stringhe in formato JSON);

Visionare la `JavaDoc`

per informazioni più specifiche su classi e relativi metodi.



## FUNZIONAMENTO

Il programma, nel momento in cui viene fatto partire, verifica se il dataset è presente nella propria cartella, altrimenti effettua il download salvandolo come "dataset.tsv" nella cartella del progetto; successivamente avviene il riempimento della lista dei metadata (ovvero una lista di mappe che hanno come campi :`Alias` (nome assegnato alla variabile nella classe modellante), `SourceField` (Nome del campo) e `Type` (tipo di dato)) e il parsing dei dati, ovvero il riempimento di una lista con le righe della tabella messe sotto forma di oggetti, per fare ciò non sono stati considerati caratteri presenti accanto numeri float perchè considerati incoerenti, mentre ogni volta che si è trovato il carattere ":" lo si è sostituito con lo "0".
A questo punto vene avviato un server web locale sulla porta 8080 tramite l'utilizzo del framework Spring. Tramite questo framework sarà possibile effettuare tali richieste GET e POST:

### **RICHIESTE GET**

`/Record`: restituisce l'intero dataset.
`/Metadati`: restituisce la lista dei metadati.
`/Record/{i}`: restituisce un elemento all'indice i della lista dei record.
`/Statistiche`: restituisce tutte le statistiche relative ad un campo inserito dall'utente.

### RICHIESTE POST

`/StatisticheFiltrate`: restituisce le statistiche filtrate di un campo inserito dall'utente tramite il body.
`/DatiFiltrati`: restituisce il record filtrato passando tramite l'utente il body.

## STRUTTURA DEL FILTRO

Il filtro deve essere inserito nel body durante la richiesta POST con la seguente sintassi:

    { "nomedelcampo" : { "operatore" : "riferimento"}}

Gli operatori supportati per i valori numerici sono:
`$eq` : operatore che indica l'uguaglianza.
`$not`: operatore che indica la disuguaglianza.
`$gte`: operatore che indica il maggiore e uguale.
`$lte`: operatore che indica il minore e uguale.

Gli operatori supportati per le stringhe sono:
`$eq` : operatore che indica l'uguaglianza.
`$not`: operatore che indica la disuguaglianza.

Invece bisogna adoperare la seguente sintassi per i due filtri, che supportano numeri e stringhe, riportati di seguito:

    {"operatore": [{"nomedelcampo1": "riferimento1"}, {"nomedelcampo2": "riferimento2"}]}

`$and`: operatore che indica l'uguaglianza di entrambi i campi rispetto ai rispettivi riferimenti
`$or` : operatore che indica l'uguaglianza di almeno un campo rispetto al rispettivo riferimento

## TEST DI FUNZIONAMENTO

Di seguito vengono elencati una serie di test di funzionamento del programma per ognuna delle richieste

`http://localhost:8080/Record` : Restituisce tutto il dataset parsato

`http://localhost:8080/getRecord/3` : Restituisce la quarta riga del dataset

`http://localhost:8080/Metadati`: Restituisce la lista con i metadati

`http://localhost:8080/Statistiche?Field=1998`: Restituisce le statistiche calcolate per i tutti i valori presenti per l'anno 1998

`http://localhost:8080/DatiFiltrati` | body: `{ "animals": {"$eq": ["A3100"] }}`  :  Restituisce le prime 110 righe del dataset che sono quelle in cui `A3100` è il valore di `animals`

`http://localhost:8080/DatiFiltrati` | body: `{ "$and": [{"animals": "A3100"},{"geo": "IT"}]}` : Restituisce la 14esima riga del dataset in quanto è l'unica in cui compaiano sia `A3100` e `IT` come valori rispettivamente di `animals` e `geo`

`http://localhost:8080/StatisticheFiltrate?Field=""` | `body: { "2018": {"$gte": 1000 }}` : Restituisce le statistiche calcolate su tutti i campi, ma solo sulle righe del dataset in cui ci sia un valore superiore o uguale a 1000 per il campo 2008

`http://localhost:8080/StatisticheFiltrate? Field="1990"` | body: `{ "$or": [{"animals": "A3100"},{"month": "IT"}]}` : Restituisce le statistiche calcolate sul campo 1990 per le righe del dataset che per il campo `month` abbiano un valore `M04` e per `animals` abbiano `A3100`

## UML

All'interno della repository, nella cartella `UML`, si possono trovare i diagrammi UML delle classi, dei casi d'uso e delle sequenze.

## AUTORI

*Nicola Carletti - Carlito9*
*Valerio Procaccioli - ValerioProcaccioli*

