package it.com.jbriscola.model;

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
    public double getPuntiCarta() {
        return valore.getPunti();
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
}
