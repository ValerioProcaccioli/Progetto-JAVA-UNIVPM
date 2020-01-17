# Progetto esame OOP-NV-(Carletti-Procaccioli)

Progetto d'esame per il corso di "Programmazione ad oggetti" a.a. 2018/2019 all'interno del corso di laurea di Ingegneria Informatica e dell'Automazione dell'Università Politecnica delle Marche.

## Introduzione

Questa repository contiene un'applicazione Java basata sul framework Spring che restituisce tramite API REST GET, POST o DELETE dati e statistiche in formato JSON di un dataset assegnatoci. Il progetto può essere compilato attraverso il framework Gradle che gestisce l'importazione delle librerie Spring.

I dati contenuti nel dataset sono tratti dal sito dell'Unione Europea e in particolare essi riguardano la "PigPopulation" durante l'arco di anni 2019-1969
Il dataset TSV contiene:

 - Il dato relativo agli animali (indicato con `animals`);
      
 - Il dato relativo al mese d'avvenimento (indicato con `month`);

      

 - Il dato relativo all'unità di misura (indicata con `unit`);
        
 - Il dato relativo al luogo d'avvenimento (indicato con `geo`);
   
          
 - Il dato relativo agli anni nell'arco 2019-1969;

  
  

## Packages e classi

Il progetto presenta un package principale `it.esame.progettoOOP` che contiene tutti i sorgenti delle classi Java e in particolare la classe main ProgettoopApplication che avvia il server Spring. Le altre classi sono divise in tre package:

   `Modello`: contiene la classe Modellante che trasforma ogni riga del dataset in una mappa nella quale ogni chiave corrisponde ad una delle intestazioni della tabella presente nel dataset;
    `Servizi`: contiene sette classi incaricate della gestione del download, del parsing, delle statistiche e dei filtri;
    `Controllo`: contiene la classe Controller che gestisce richieste da parte dell'utente;

Visionare la `JavaDoc` per informazioni più specifiche su classi e relativi metodi.



## L' APPLICAZIONE

All'avvio dell'applicazione questa verifica se è già presente il dataset nella cartella del programma stesso, in caso contrario lo scarica da un link assegnatoci. Subito dopo vengono estrapolate tutte le informazioni da tale dataset, le quali verranno inserite in una lista di oggetti appartenenti alla classe `Modellante` al fine di poterle gestire in maniera ottimale. Tale operazione prende il nome di `Parsing`.
A questo punto vene avviato un server web locale sulla porta 8080 tramite l'utilizzo del framework Spring. Tramite questo framework sarà possibile effettuare tali richieste GET e POST:

### **RICHIESTE GET**

`/Record`: restituisce l'intero dataset.
`/Metadati`: restituisce la lista dei metadati  (ovvero una lista di mappe che hanno come campi :`Alias` (nome assegnato alla variabile nella classe modellante), `SourceField` (Nome del campo) e `Type` (tipo di dato)).
`/Record/{i}`: restituisce un elemento all'indice i della lista dei record.
`/Statistiche`: restituisce tutte le statistiche relative ad un campo inserito dall'utente, o, se non lo inserisce, per tutti i campi.

### RICHIESTE POST

`/StatisticheFiltrate`: restituisce le statistiche filtrate di un campo inserito dall'utente tramite il body.
`/DatiFiltrati`: restituisce il record filtrato passando tramite l'utente il body.
`/InserisciRecord`: restituisce il record al quale viene aggiunto il record passato.

Ricordando che il `nomedelcampo` e l'`operatore` vanno inseriti in ogni caso come stringa (perciò tra virgolette), mentre il `riferimento` può essere sia un numero che una stringa, il filtro deve essere inserito nel body durante la richiesta POST con la seguente sintassi:

    {"nomedelcampo":[{"operatore":riferimento}]}

Gli operatori supportati per i valori numerici sono:
`$eq` : operatore che indica l'uguaglianza.
`$not`: operatore che indica la disuguaglianza.
`$mag`: operatore che indica il maggiore.
`$min`: operatore che indica il minore.

Gli operatori supportati per le stringhe sono:
`$eq` : operatore che indica l'uguaglianza.
`$not`: operatore che indica la disuguaglianza.

Invece bisogna adoperare la seguente sintassi per i due filtri, che supportano numeri e stringhe, riportati di seguito:

    {"operatore":[{"nomedelcampo1":riferimento1},{"nomedelcampo2":riferimento2}]}

`$and`: operatore che indica l'uguaglianza di entrambi i campi rispetto ai rispettivi riferimenti
`$or` : operatore che indica l'uguaglianza di almeno un campo rispetto al rispettivo riferimento

### RICHESTA DELETE

`/RimuoviRecord` : restituisce il dataset privato di uno o più elementi selezionati in base a ciò che richiede l'utente nel body.
La struttura del body per il delete è similare a quella dei filtri per i post.

## TEST 

Di seguito vengono elencati una serie di test di funzionamento del programma per ognuna delle richieste

`http://localhost:8080/Record` : Restituisce tutto il dataset parsato

`http://localhost:8080/Record/3` : Restituisce la quarta riga del dataset

`http://localhost:8080/Metadata`: Restituisce la lista con i metadati

`http://localhost:8080/Statistiche?Field=1998`: Restituisce le statistiche calcolate per i tutti i valori presenti per l'anno 1998

`http://localhost:8080/DatiFiltrati` | body: `{ "animals": [{"$eq": "A3100" }]}`  :  Restituisce le prime 110 righe del dataset che sono quelle in cui `A3100` è il valore di `animals`

`http://localhost:8080/DatiFiltrati` | body: `{ "animals": {"$eq": "A3100" }}`  :  Il programma segnalerà che la richiesta è illeggibile in quanto mancante delle parentesi quadre dopo il campo da filtrare.

`http://localhost:8080/DatiFiltrati` | body: `{ animals: [{"$eq": "A3100" }]}`  : Anche in questo caso verrà segnalata una richiesta illeggibile, anche se in questo caso a causa del fatto che è stata scritta una stringa senza le virgolette.


`http://localhost:8080/DatiFiltrati` | body: `{ "$and": [{"animals": "A3100"},{"geo": "IT"}]}` : Restituisce la 14esima riga del dataset in quanto è l'unica in cui compaiano sia `A3100` e `IT` come valori rispettivamente di `animals` e `geo`

`http://localhost:8080/StatisticheFiltrate?Campo=""` | `body: { "2018": [{"$gte": 1000 }]}` : Restituisce le statistiche calcolate su tutti i campi, ma solo sulle righe del dataset in cui ci sia un valore superiore o uguale a 1000 per il campo 2008

`http://localhost:8080/StatisticheFiltrate?Campo="1990"` | body: `{ "$or": [{"animals": "A3100"},{"month": "IT"}]}` : Restituisce le statistiche calcolate sul campo 1990 per le righe del dataset che per il campo `month` abbiano un valore `M04` e per `animals` abbiano `A3100`.

`http://localhost:8080/InserisciRecord` | body: `["A3100","M04","THS_HD","AU",30000,":",":",":",":",":",":",3000]` Restituisce il dataset con aggiunto il record estrapolato dal vettore passato nel body (è necessario inserire i 4 campi stringa, non è necessario inserire i valori relativi a tutti gli anni, tuttavia se si vogliono inserire valori di anni non consecutivi va inserito almeno un carattere non numerico per ogni anno saltato, non serve inserire altri caratteri dopo l'ultimo valore da inserire).

`http://localhost:8080/RimuoviRecord` | body: `{ "animals": [{"$eq": "A3100" }]}`: Restituisce il dataset privato dei primi 110 record.


## UML

All'interno della repository, nella cartella `UML`, si possono trovare i diagrammi UML delle classi, dei casi d'uso e delle sequenze.

## AUTORI

*Nicola Carletti - Carlito9*
*Valerio Procaccioli - ValerioProcaccioli*

