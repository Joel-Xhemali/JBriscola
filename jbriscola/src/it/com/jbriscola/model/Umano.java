package it.com.jbriscola.model;

import java.util.List;

/**
 * Classe che rappresenta un giocatore umano.
 * Estende la classe Giocatore.
 */
public class Umano extends Giocatore{

    /**
     * Costruttore completo della classe Umano.
     *
     * @param nome il nome del giocatore.
     * @param avatar il percorso dell'avatar del giocatore.
     * @param mano la lista di carte in mano al giocatore.
     */
    public Umano(String nome, String avatar, List<Carta> mano) {
        super(nome, avatar, mano);
    }

    /**
     * Costruttore semplificato della classe Umano, senza una mano iniziale.
     *
     * @param nome il nome del giocatore.
     * @param avatar il percorso dell'avatar del giocatore.
     */
    public Umano(String nome, String avatar){
        this(nome, avatar,null);
    }
}
