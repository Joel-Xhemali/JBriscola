package it.com.jbriscola.model;

import java.util.Observable;
import java.util.Optional;

/**
 * Modello principale del Gioco della Briscola.
 */
@SuppressWarnings("deprecation")
public class GiocoBriscola extends Observable {
    private int partiteGiocate;
    private int partiteVinte;
    private int partitePerse;
    private Optional<Giocatore> giocatore;
    private Optional<PartitaBriscola> partitaCorrente;

    /**
     * Costruttore del modello del Gioco della Briscola.
     * Inizializza i campi con valori vuoti o di default.
     */
    public GiocoBriscola() {
        giocatore = Optional.empty();
        partitaCorrente = Optional.empty();
    }


    /**
     * Restituisce il numero totale di partite giocate.
     *
     * @return il numero di partite giocate
     */
    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    /**
     * Restituisce il numero di partite vinte dall'utente.
     *
     * @return il numero di partite vinte
     */
    public int getPartiteVinte() {
        return partiteVinte;
    }

    /**
     * Restituisce il numero di partite perse dall'utente.
     *
     * @return il numero di partite perse
     */
    public int getPartitePerse() {
        return partitePerse;
    }

    /**
     * Restituisce l'Optional contenente il giocatore, se presente.
     *
     * @return un Optional che contiene il giocatore corrente
     */
    public Optional<Giocatore> getGiocatore() {
        return giocatore;
    }

    /**
     * Restituisce l'Optional contenente la partita corrente.
     *
     * @return un Optional che contiene la partita attualmente in corso
     */
    public Optional<PartitaBriscola> getPartitaCorrente() {
        return partitaCorrente;
    }

    /**
     * Metodo per creare e avviare una nuova partita. Se un'altra
     * partita era già in corso, viene sovrascritta.
     *
     * @param giocatore il giocatore che partecipa alla partita
     */
    public void iniziaPartita(Giocatore giocatore) {
        this.giocatore = Optional.of(giocatore);

        // Pattern Matching per instanceof (Java 16+)
        if (giocatore instanceof Umano umano) {
            partitaCorrente = Optional.of(new PartitaBriscola(this, umano));
        }
    }

    /**
     * Metodo per terminare la partita corrente del Gioco.
     * Aggiorna le statistiche delle partite giocate e vinte, e notifica gli Observer.
     */
    public void terminaPartita() {
        partitaCorrente.ifPresent(p -> {
            partiteGiocate++;

            if (p.getStato() == PartitaBriscola.StatoPartita.VINTA) partiteVinte++;
            else if (p.getStato() == PartitaBriscola.StatoPartita.PERSA) partitePerse++;
            notifyObservers();
            Mazzo.close();
            partitaCorrente = Optional.empty();
        });
    }

    /**
     * Notifica gli observer di un avvenuto cambiamento nello stato.
     * Segna l'oggetto come "modificato" (setChanged) e chiama la superclasse.
     */
    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
