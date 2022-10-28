**Introduzione al Software**

Con la presente guida si vuole illustrare il funzionamento del software realizzato, il suo scopo e mostrare come utilizzare il software mediante interfaccia grafica o a riga di comando.

Alla base del progetto vi è l’algoritmo di regressione k-nearest neighbors, più comunemente noto come KNN; esso è un algoritmo che utilizza la prossimità per effettuare previsioni sul raggruppamento di un singolo punto dati.

L’algoritmo, per funzionare, si basa su un training set contenente una serie di dati che verranno utilizzati per prevenire il dato numerico mancante. Più nello specifico, si rapportano i dati forniti ad una distanza k, la quale indica il numero di attributi che possono differire dalla query dell’utente, e si effettua una media dei valori nei vicini selezionati, in base al valore ignoto richiesto dall’utente nella query.

Il calcolo della distanza k si differenzia in base alla natura dell’attributo:
1. Per attributi discreti, si calcola la distanza di Hamming tra i due attributi 
	
2. Per attributi continui, si effettua un processo di scalatura sfruttando l’algoritmo di min-max scaler (utilizzando la seguente formula: ((valore-min))/((max-min))) per poi effettuare la seguente differenza: 1-valore ottenuto con il min-max⁡scaler
(Per min e max si intende il minimo ed il massimo della variabile da scalare)

È necessario applicare un processo di scalatura per non avere risultati falsati in caso due valori siano troppo distanti numericamente tra loro.

**Organizzazione delle cartelle**
