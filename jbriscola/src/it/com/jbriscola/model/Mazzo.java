package it.com.jbriscola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe che rappresenta il mazzo da gioco
 * Utilizzo pattern Singleton
 */
public class Mazzo {

    private static Mazzo instance;
    private List<Carta> carte;

    /**
     * Costruttore privato
     * Inizializza la lista di carte e mescola il mazzo
     */
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

    /**
     * Implementa il pattern Singleton. Restituisce l'istanza univoca del mazzo da gioco.
     * Se l'istanza non esiste, la crea (e mescola le carte).
     *
     * @return l'unica istanza di Mazzo
     */
    public static Mazzo getMazzo(){
        if (instance == null)
            instance = new Mazzo();
        return instance;
    }

    /**
     * Chiude l'istanza del mazzo dopo ogni partita.
     */
    public static void close(){
        instance = null;
    }

    /**
     * Mescola il mazzo scambiando ogni carta con un'altra in posizione casuale.
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
     * Estrae la prima carta dalla cima del mazzo e la rimuove.
     *
     * @return la carta pescata
     */
    public Carta pesca(){
        return carte.removeFirst();
    }

    /**
     * Pesca esattamente 3 carte dalla cima del mazzo.
     * Utile per distribuire le mani iniziali ai giocatori.
     *
     * @return una lista contenente le tre carte appena pescate
     */
    public List<Carta> pescaCarte(){
        List<Carta> mano = new ArrayList<>(3);
        for(int i=0; i<3;i++){
            mano.add(carte.removeFirst());
        }
        return mano;
    }

    /**
     * Verifica se il mazzo è vuoto.
     *
     * @return true se non ci sono più carte nel mazzo, false altrimenti
     */
    public boolean isEmpty(){
        return carte.isEmpty();
    }

    /**
     * Restituisce una rappresentazione testuale del mazzo di carte.
     *
     * @return una stringa con la quantità di carte e il loro elenco
     */
    @Override
    public String toString() {
        return "Mazzo{" +
                carte.size() +
                " carte=" + carte +
                '}';
    }
}
