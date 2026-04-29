package it.com.jbriscola.model;

/**
 * Enum Valore che rappresenta i valori delle carte da gioco ed i rispettivi punti
 */
public enum Valore {
    ASSO(11),
    TRE(10),
    RE(4),
    CAVALLO(3),
    FANTE(2),
    DUE(0),
    QUATTRO(0),
    CINQUE(0),
    SEI(0),
    SETTE(0);

    private final int punti;

    /**
     * Costruttore privato per il salvataggio dei punti
     * @param punti
     */
    private Valore(int punti) {
        this.punti = punti;
    }

    /**
     * Metodo che restituisce i punti di una singolo valore
     */
    public int getPunti() {
        return punti;
    }
}
