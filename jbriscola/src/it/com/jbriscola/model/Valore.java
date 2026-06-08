package it.com.jbriscola.model;

/**
 * Enum Valore che rappresenta i valori delle carte da gioco ed i rispettivi punti
 */
public enum Valore {
    ASSO(11, 10),
    TRE(10, 9),
    RE(4, 8),
    CAVALLO(3, 7),
    FANTE(2, 6),
    SETTE(0, 5),
    SEI(0, 4),
    CINQUE(0, 3),
    QUATTRO(0, 2),
    DUE(0, 1);

    private final int punti;
    private final int forza;

    /**
     * Costruttore privato per il salvataggio dei punti
     * @param punti il punteggio assegnato alla carta
     */
    private Valore(int punti, int forza) {
        this.punti = punti;
        this.forza = forza;
    }

    /**
     * Restituisce i punti associati al valore della carta.
     *
     * @return i punti attribuiti al valore specifico.
     */
    public int getPunti() {
        return punti;
    }
    public int getForza() {return forza;}
}