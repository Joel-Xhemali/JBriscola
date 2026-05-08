package it.com.jbriscola.model;

/**
 * Enum Valore che rappresenta i valori delle carte da gioco ed i rispettivi punti
 */
public enum Valore {
    ASSO(0.11),
    TRE(0.10),
    RE(0.04),
    CAVALLO(0.03),
    FANTE(0.02),
    DUE(0.001),
    QUATTRO(0.002),
    CINQUE(0.003),
    SEI(0.004),
    SETTE(0.005);

    private final double punti;

    /**
     * Costruttore privato per il salvataggio dei punti
     * @param punti il punteggio assegnato alla carta
     */
    private Valore(double punti) {
        this.punti = punti;
    }

    /**
     * Restituisce i punti associati al valore della carta.
     * Complessità computazionale: O(1).
     *
     * @return i punti attribuiti al valore specifico.
     */
    public double getPunti() {
        return punti;
    }
}