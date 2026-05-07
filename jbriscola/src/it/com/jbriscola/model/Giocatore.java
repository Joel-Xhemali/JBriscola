package it.com.jbriscola.model;

import java.util.List;

/**
 * Classe astratta che rappresenta un giocatore che sia esso Umano o Artificiale
 */
public abstract class Giocatore {
    private String nome;
    private List<Carta> mano;
    private String avatar;
    private double puntiTavolo;

    /**
     * Costruttore della classe astratta Giocatore.
     * Inizializza i parametri di base per ogni giocatore (nome, avatar e carte in mano).
     * Complessità computazionale: O(1).
     *
     * @param nome   il nome del giocatore
     * @param avatar il percorso dell'immagine del profilo del giocatore
     * @param mano   la lista delle carte iniziali in mano
     */
    public Giocatore(String nome, String avatar, List<Carta> mano) {
        this.nome = nome;
        this.mano = mano;
        this.avatar = avatar;
    }

    /**
     * Restituisce il nome del giocatore.
     * Complessità computazionale: O(1).
     *
     * @return il nome del giocatore
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del giocatore.
     * Complessità computazionale: O(1).
     *
     * @param nome il nuovo nome da assegnare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce la lista di carte attualmente in mano al giocatore.
     * Complessità computazionale: O(1).
     *
     * @return la lista delle carte
     */
    public List<Carta> getMano() {
        return mano;
    }

    /**
     * Imposta le carte in mano al giocatore.
     * Complessità computazionale: O(1).
     *
     * @param mano la nuova lista di carte
     */
    public void setMano(List<Carta> mano) {
        this.mano = mano;
    }

    /**
     * Restituisce i punti attualmente guadagnati dal giocatore nel corso della partita.
     * Complessità computazionale: O(1).
     *
     * @return i punti attuali del giocatore
     */
    public double getPuntiTavolo() {
        return puntiTavolo;
    }

    /**
     * Imposta il punteggio accumulato dal giocatore.
     * Complessità computazionale: O(1).
     *
     * @param puntiTavolo il nuovo punteggio da assegnare
     */
    public void setPuntiTavolo(double puntiTavolo) {
        this.puntiTavolo = puntiTavolo;
    }

    /**
     * Restituisce il percorso dell'immagine del profilo del giocatore.
     * Complessità computazionale: O(1).
     *
     * @return il percorso dell'avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Imposta il percorso dell'immagine del profilo del giocatore.
     * Complessità computazionale: O(1).
     *
     * @param avatar il percorso del nuovo avatar da assegnare
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Restituisce una stringa formattata con le informazioni principali del giocatore.
     * Complessità computazionale: O(N) dove N è il numero di carte in mano (a causa del toString di List).
     *
     * @return una stringa contenente nome, carte in mano e percorso dell'avatar
     */
    @Override
    public String toString() {
        return "nome='" + nome + '\'' +
                ", mano=" + mano +
                ", avatar='" + avatar + '\'';
    }
}
