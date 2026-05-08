package it.com.jbriscola.model;

import java.util.List;

/**
 * Classe che rappresenta un giocatore controllato dal computer (Bot).
 * Estende la classe base Giocatore, ereditandone le funzionalità principali.
 */
public class Bot extends Giocatore {

    /**
     * Costruttore della classe Bot. Inizializza un Bot con nome, percorso
     * dell'avatar e mano iniziale.
     * Complessità computazionale: O(1) in quanto chiama semplicemente il costruttore della superclasse.
     *
     * @param nome   il nome del bot
     * @param avatar il percorso dell'immagine dell'avatar del bot
     * @param mano   la lista di carte inizialmente in mano al bot
     */
    public Bot(String nome, String avatar, List<Carta> mano) {
        super(nome, avatar, mano);
    }

    /**
     * Costruttore della classe Bot. Inizializza un Bot con nome e mano iniziale,
     * assegnandogli un avatar di default.
     * Complessità computazionale: O(1) in quanto chiama l'altro costruttore della classe.
     *
     * @param nome il nome del bot
     */
    public Bot(String nome) {
        this(nome, Utils.getPathAvatar(), null);
    }
}
