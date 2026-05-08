package it.com.jbriscola.model;

import java.util.Objects;

/**
 * Classe Carta che rappresenta la singola carta da gioco.
 */
public class Carta {

    private Seme seme;
    private Valore valore;
    private int punto;
    private String pathCarta;

    private static final String PATH_CARTE = "assets/carte/";

    /**
     * Costruttore della classe Carta.
     * Inizializza la carta con il seme e il valore specificati.
     * Complessità computazionale: O(1).
     *
     * @param seme   il seme della carta
     * @param valore il valore della carta
     */
    public Carta(Seme seme, Valore valore) {
        this.seme = seme;
        this.valore = valore;
        pathCarta = getPathCarta();
    }

    /**
     * Restituisce il seme della carta.
     * Complessità computazionale: O(1).
     *
     * @return il seme della carta
     */
    public Seme getSeme() {
        return seme;
    }

    /**
     * Imposta un nuovo seme per la carta.
     * Complessità computazionale: O(1).
     *
     * @param seme il nuovo seme da assegnare
     */
    public void setSeme(Seme seme) {
        this.seme = seme;
    }

    /**
     * Restituisce il valore facciale della carta.
     * Complessità computazionale: O(1).
     *
     * @return il valore della carta
     */
    public Valore getValore() {
        return valore;
    }

    /**
     * Imposta un nuovo valore per la carta.
     * Complessità computazionale: O(1).
     *
     * @param valore il nuovo valore da assegnare
     */
    public void setValore(Valore valore) {
        this.valore = valore;
    }

    /**
     * Restituisce i punti attribuiti alla carta in base al suo valore in Briscola.
     * Complessità computazionale: O(1).
     *
     * @return i punti della carta
     */
    public int getPuntiCarta() {
        return valore.getPunti();
    }

    /**
     * Restituisce il valore di forza della carta, utilizzato per determinare la gerarchia di presa.
     * Complessità computazionale: O(1).
     *
     * @return la forza della carta (valore intero)
     */
    public int getForzaCarta() {
        return valore.getForza();
    }

    /**
     * Restituisce il percorso dell'immagine associata alla carta.
     * Complessità computazionale: O(1), poiché l'operazione di concatenazione è su stringhe costanti/corte.
     *
     * @return il percorso del file immagine
     */
    public String getPathCarta() {
        return PATH_CARTE + valore.name() + "_" + seme.name() + ".png";
    }

    /**
     * Restituisce una rappresentazione in formato testuale della Carta.
     * Complessità computazionale: O(1).
     *
     * @return una stringa che rappresenta lo stato della carta
     */
    @Override
    public String toString() {
        return "Carta{" +
                "seme=" + seme +
                ", valore=" + valore +
                ", path=" + pathCarta +
                '}';
    }

    /**
     * Confronta questa carta con l'oggetto specificato per verificarne l'uguaglianza.
     * Due carte sono considerate uguali se hanno lo stesso seme e lo stesso valore.
     * Complessità computazionale: O(1).
     *
     * @param o l'oggetto da confrontare con questa carta
     * @return true se l'oggetto è una Carta identica per seme e valore, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Carta carta = (Carta) o;
        return seme == carta.seme && valore == carta.valore;
    }

    /**
     * Restituisce il valore hash per la carta.
     * L'hash viene generato basandosi sui campi seme e valore per garantire coerenza con equals().
     * Complessità computazionale: O(1).
     *
     * @return il valore hash calcolato per questa carta
     */
    @Override
    public int hashCode() {
        return Objects.hash(seme, valore);
    }
}
