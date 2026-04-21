# JBriscola 🃏

Un'implementazione in Java del classico gioco di carte italiano della Briscola, sviluppata come progetto d'esame per il corso di Metodologie di Programmazione. 

Il progetto applica rigorosamente i principi dell'ingegneria del software, con una netta separazione delle responsabilità garantita dall'architettura MVC (Model-View-Controller) e dal pattern Observer/Observable implementato custom.

## 🎯 Specifiche e Funzionalità Implementate

- **Architettura Solida:** Piena adozione del pattern **MVC** per separare la logica di dominio dalla rappresentazione grafica.
- **Pattern Observer:** Sviluppo di un sistema di eventi custom per la comunicazione disaccoppiata tra il Modello (stato della partita) e la View (interfaccia utente).
- **Intelligenza Artificiale (Bot):** Supporto per partite a 4 giocatori (1 Utente Umano vs 3 Bot) con logica decisionale per la scelta delle carte.
- **Gestione Profili:** Creazione e salvataggio dei profili utente, inclusivi di nickname, avatar personalizzato e statistiche persistenti (partite giocate, vinte, perse).
- **Interfaccia Grafica:** GUI reattiva sviluppata in Java Swing.
- **Stream API:** Utilizzo intensivo di `java.util.stream.Stream<T>` per l'elaborazione dei dati, la gestione dei mazzi e il calcolo dei punteggi, garantendo codice dichiarativo ed efficiente.
- **Multimedia:** Integrazione di sample audio per gli eventi di gioco (pescata, carta calata, vittoria) e gestione di animazioni grafiche per migliorare la User Experience.

## 🏗️ Struttura Architetturale

Il progetto è diviso in tre macro-componenti principali:
1. **Model:** Contiene la logica pura della Briscola, le regole, la gestione del mazzo e dei giocatori. Non ha alcuna dipendenza dalla libreria grafica.
2. **View:** Componenti grafici che si limitano ad "osservare" il Model e reagire ai cambiamenti di stato aggiornando il display.
3. **Controller:** Intercetta gli input dell'utente (click sulle carte, avvio partita) e orchestra le modifiche sul Model, gestendo inoltre i turni asincroni dei Bot.

## 🛠️ Tecnologie Utilizzate
- **Linguaggio:** Java
- **GUI:** Java Swing
- **Build/IDE:** IntelliJ

## 👤 Autore
- **Nome Cognome:** Joel Xhemali
- **Matricola:** 2279694
- **Corso:** Informatica
