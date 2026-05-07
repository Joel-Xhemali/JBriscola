package it.com.jbriscola.model;

import java.util.Observable;
import java.util.Optional;

/**
 * Modello principale del Gioco della Briscola.
 */
@SuppressWarnings("deprecation") // java.util.Observable è deprecata da Java 9, si accetta per continuità con le view pre-esistenti
public class GiocoBriscola extends Observable {
    private int partiteGiocate;
    private int partiteVinte;
    private Optional<Giocatore> giocatore;
    private Optional<PartitaBriscola> partitaCorrente;

    /**
     * Costruttore del modello del Gioco della Briscola.
     * Inizializza i campi con valori vuoti o di default.
     * Complessità computazionale: O(1).
     */
    public GiocoBriscola() {
        giocatore = Optional.empty();
        partitaCorrente = Optional.empty();
    }


    /**
     * Restituisce il numero totale di partite giocate.
     * Complessità computazionale: O(1).
     *
     * @return il numero di partite giocate
     */
    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    /**
     * Restituisce il numero di partite vinte dall'utente.
     * Complessità computazionale: O(1).
     *
     * @return il numero di partite vinte
     */
    public int getPartiteVinte() {
        return partiteVinte;
    }

    /**
     * Restituisce l'Optional contenente il giocatore, se presente.
     * Complessità computazionale: O(1).
     *
     * @return un Optional che contiene il giocatore corrente
     */
    public Optional<Giocatore> getGiocatore() {
        return giocatore;
    }

    /**
     * Restituisce l'Optional contenente la partita corrente.
     * Complessità computazionale: O(1).
     *
     * @return un Optional che contiene la partita attualmente in corso
     */
    public Optional<PartitaBriscola> getPartitaCorrente() {
        return partitaCorrente;
    }

    /**
     * Metodo per creare e avviare una nuova partita. Se un'altra
     * partita era già in corso, viene sovrascritta.
     * Complessità computazionale: O(1) in quanto viene creato un nuovo oggetto PartitaBriscola.
     *
     * @param giocatore il giocatore che partecipa alla partita
     */
    public void iniziaPartita(Giocatore giocatore) {
        this.giocatore = Optional.of(giocatore);
        System.out.println(giocatore);
        
        // Pattern Matching per instanceof (Java 16+)
        if (giocatore instanceof Umano umano) {
            partitaCorrente = Optional.of(new PartitaBriscola(this, umano));
        }
    }

    /**
     * Metodo per terminare la partita corrente del Gioco.
     * Aggiorna le statistiche delle partite giocate e vinte, e notifica gli Observer.
     * Complessità computazionale: O(1) per l'aggiornamento (più eventuali chiamate O(N) in cascata sui listener).
     */
    public void terminaPartita() {

        /*
         * l'azione passata a ifPresent viene eseguita solo se l'Optional
         * partitaCorrente non è vuoto
         */
        partitaCorrente.ifPresent(p -> {
            partiteGiocate++;
            // Uso di '==' invece di equals per gli Enum (più sicuro ed efficiente)
            if (p.getStato() == PartitaBriscola.StatoPartita.VINTA) {
                partiteVinte++;
            }
            notifyObservers(); // vengono aggiornate le statistiche nelle vista
            partitaCorrente = Optional.empty();
        });
    }

    /**
     * Notifica gli observer di un avvenuto cambiamento nello stato.
     * Segna l'oggetto come "modificato" (setChanged) e chiama la superclasse.
     * Complessità computazionale: O(N) dove N è il numero di Observer registrati.
     */
    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
