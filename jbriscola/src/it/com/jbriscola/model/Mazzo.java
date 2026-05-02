package it.com.jbriscola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe che rappresenta il mazzo da gioco
 */
public class Mazzo {

    private static Mazzo instance;
    private List<Carta> carte;

    private Mazzo() {
        this.carte = new ArrayList<>();

        // Recupero i semi e i valori
        Seme[] semi = Seme.values();
        Valore[] valori = Valore.values();

        // Ciclo prima i semi e poi i valori delle carte
        for(Seme seme : semi) {
            for (Valore valore : valori) {
                this.carte.add(new Carta(seme, valore));
            }
        }
        mescola();
    }

    public static Mazzo getMazzo(){
        if (instance == null)
            instance = new Mazzo();
        return instance;
    }

    /**
     * Mescola il mazzo in modo che l'ordine di creazione sia random
     */
    public void mescola() {
        Random rand = new Random();

        // scorro la lista e scambio ogni carta con un'altra in posizione casuale
        for (int i = 0; i < this.carte.size(); i++) {
            int indiceCasuale = rand.nextInt(this.carte.size());

            // Scambio manuale delle carte
            Carta cartaCorrente = this.carte.get(i);
            Carta cartaCasuale = this.carte.get(indiceCasuale);

            this.carte.set(i, cartaCasuale);
            this.carte.set(indiceCasuale, cartaCorrente);
        }
    }

    /**
     * Metodo che ritorna la prima carta del mazzo e la toglie dal mazzo stesso
     */
    public Carta pesca(){
        return carte.removeFirst();
    }

    public List<Carta> pescaCarte(){
        List<Carta> mano = new ArrayList<>(3);
        for(int i=0; i<3;i++){
            mano.add(carte.removeFirst());
        }
        return mano;
    }

    @Override
    public String toString() {
        return "Mazzo{" +
                carte.size() +
                " carte=" + carte +
                '}';
    }
}
