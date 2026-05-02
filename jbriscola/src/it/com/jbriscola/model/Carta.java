package it.com.jbriscola.model;

/**
 * Classe Carta che rappresenta la singola carta da gioco
 */
public class Carta {

    private Seme seme;
    private Valore valore;
    private int punto;
    private String pathCarta;

    private static final String PATH_CARTE = "assets/carte/";

    public Carta(Seme seme, Valore valore) {
        this.seme = seme;
        this.valore = valore;
        pathCarta = getPathCarta();
    }

    public Seme getSeme() {
        return seme;
    }

    public void setSeme(Seme seme) {
        this.seme = seme;
    }

    public Valore getValore() {
        return valore;
    }

    public void setValore(Valore valore) {
        this.valore = valore;
    }

    public int getPuntiCarta() {
        return valore.getPunti();
    }

    public String getPathCarta() {
        return PATH_CARTE + valore.name() + "_" + seme.name() + ".png";
    }

    @Override
    public String toString() {
        return "Carta{" +
                "seme=" + seme +
                ", valore=" + valore +
                ", path=" + pathCarta +
                '}';
    }
}
