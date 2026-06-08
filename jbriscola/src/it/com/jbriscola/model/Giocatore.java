package it.com.jbriscola.model;

import java.util.List;
import java.util.Objects;

/**
 * Classe astratta che rappresenta un giocatore che sia esso Umano o Artificiale
 */
public abstract class Giocatore {
    private String nome;
    private List<Carta> mano;
    private String avatar;
    private Carta cartaScartata;

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

    public void pesca(Carta carta){
        this.mano.add(carta);
    }

    /**
     * Restituisce la carta che il giocatore ha scartato (giocato) sul tavolo.
     * Complessità computazionale: O(1).
     *
     * @return la carta scartata dal giocatore
     */
    public Carta getCartaScartata() {
        return cartaScartata;
    }

    /**
     * Imposta la carta scartata dal giocatore durante il suo turno.
     * Complessità computazionale: O(1).
     *
     * @param cartaScartata la carta da assegnare come scartata
     */
    public void setCartaScartata(Carta cartaScartata) {
        this.cartaScartata = cartaScartata;
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

    /**
     * Confronta questo giocatore con l'oggetto specificato per verificarne l'uguaglianza.
     * Due giocatori sono considerati uguali se hanno lo stesso nome, la stessa mano,
     * lo stesso avatar e la stessa carta scartata.
     * Complessità computazionale: O(N) dove N è il numero di carte in mano.
     *
     * @param o l'oggetto da confrontare con questo giocatore
     * @return true se l'oggetto è uguale a questo giocatore, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Giocatore giocatore = (Giocatore) o;
        return Objects.equals(nome, giocatore.nome) && Objects.equals(mano, giocatore.mano) && Objects.equals(avatar, giocatore.avatar) && Objects.equals(cartaScartata, giocatore.cartaScartata);
    }

    /**
     * Calcola il valore hash per questo giocatore basandosi sui suoi attributi.
     * Complessità computazionale: O(N) dove N è il numero di carte in mano.
     *
     * @return il valore hash calcolato
     */
    @Override
    public int hashCode() {
        return Objects.hash(nome, mano, avatar, cartaScartata);
    }
}
