package it.com.jbriscola.model;

/**
 * Classe Carta che rappresenta la singola carta da gioco
 */
public class Carta {

    Seme seme;
    Valore valore;

    public Carta(Seme seme, Valore valore) {
        this.seme = seme;
        this.valore = valore;
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

    public int getPuntiCarta(){
        return valore.getPunti();
    }
}
