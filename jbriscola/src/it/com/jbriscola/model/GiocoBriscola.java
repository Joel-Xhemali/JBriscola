package it.com.jbriscola.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GiocoBriscola extends Observable {
    private int partiteGiocate;
    private int partiteVinte;
    private Optional<Giocatore> giocatore;
    private Optional<PartitaBriscola> partitaCorrente;

    /**
     * Costruttore del modello del Gioco della Briscola
     */
    public GiocoBriscola() {
        giocatore = Optional.empty();
        partitaCorrente = Optional.empty();
    }



    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    public int getPartiteVinte() {
        return partiteVinte;
    }

    public Optional<Giocatore> getGiocatore() {
        return giocatore;
    }

    public Optional<PartitaBriscola> getPartitaCorrente() {
        return partitaCorrente;
    }


    /**
     * Metodo per creare una nuova partita; se un'altra
     * partita era già in corso essa viene sovrascritta
     */
    public void iniziaPartita(Giocatore giocatore) {
        this.giocatore = Optional.of(giocatore);
        System.out.println(giocatore);
        partitaCorrente = Optional.of(new PartitaBriscola(this, (Umano) giocatore));
    }

    /**
     * Metodo per terminare la partita corrente del Gioco
     */
    public void terminaPartita() {

        /*
         * l'azione passata a ifPresent viene eseguita solo se l'Optional
         * partitaCorrente non è vuoto
         */
        partitaCorrente.ifPresent(p -> {
            partiteGiocate++;
            if (p.getStato().equals(PartitaBriscola.StatoPartita.VINTA)) {
                partiteVinte++;
            }
            notifyObservers(); // vengono aggiornate le statistiche nelle vista
            partitaCorrente = Optional.empty();
        });
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
