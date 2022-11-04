# Introduzione al Software

Di seguito viene riportato il funzionamento del software e l'organizzazione delle directory.

Alla base del progetto vi è l’algoritmo di regressione k-nearest neighbors, più comunemente noto come KNN; esso è un algoritmo che utilizza la prossimità per effettuare previsioni sul raggruppamento di un singolo punto dati.

L’algoritmo, per funzionare, si basa su un training set contenente una serie di dati che verranno utilizzati per prevenire il dato numerico mancante. Più nello specifico, si rapportano i dati forniti ad una distanza k, la quale indica il numero di attributi che possono differire dalla query dell’utente, e si effettua una media dei valori nei vicini selezionati, in base al valore ignoto richiesto dall’utente nella query.

Il calcolo della distanza k si differenzia in base alla natura dell’attributo:
1. Per attributi discreti, si calcola la distanza di Hamming tra i due attributi 
	
2. Per attributi continui, si effettua un processo di scalatura sfruttando l’algoritmo di min-max scaler (utilizzando la seguente formula: ((valore-min))/((max-min))) per poi effettuare la seguente differenza: 1-valore ottenuto con il min-max⁡scaler
(Per min e max si intende il minimo ed il massimo della variabile da scalare)

È necessario applicare un processo di scalatura per non avere risultati falsati in caso due valori siano troppo distanti numericamente tra loro.

# Organizzazione delle cartelle

La root directory del progetto è organizzata nel seguente modo:

## Cartelle:
1. ***File***, all'interno sono inseriti i file contenenti i Training Set

2. ***Progetto CLI***, all'interno è contenuto il progetto a riga di comando

3. ***Progetto Gui***, all'interno è contenuto il progetto con interfaccia grafica

## File:
1. ***Casi di Test***, in cui sono riportati i casi di test effettuati sui vari file

2. ***Guida all'utente***, in cui viene spiegato come l'utente può eseguire il software

3. ***Guida all'installazione***, in cui viene spiegato come installare il software per 
eseguirlo sull'IDE Visual Studio Code

4. ***README***, in cui è spiegato il funzionamento dell'algoritmo

5. ***Script***, in cui è contenuto lo Script SQL per l'inizializzazione del Database 



Le cartelle Progetto CLI  e Progetto GUI sono strutturate come segue:

1. ***Codice Sorgente***, in cui è contenuto il codice sorgente del Client e del Server

2. **Jar-Bat***, in cui sono contenuti i file Jar e Bat per eseguire il progetto senza doverlo compilare da IDE

3. ***Javadoc***, in cui sono contenute le documentazioni Javadoc del Client e del Server

4. ***Uml***, in cui sono contenute le documentazioni Uml del Client e del Server