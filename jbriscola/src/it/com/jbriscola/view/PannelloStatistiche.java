package it.com.jbriscola.view;

import it.com.jbriscola.model.GiocoBriscola;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class PannelloStatistiche extends Pannello {

    private JLabel titolo;
    private JLabel statistiche;
    private JButton bottoneMenu;

    private static String nomeSchermata = "Statistiche";
    private static String indicazioneMenu = "Menù";
    private static String indicazionePartiteGiocate = "Partite giocate: ";
    private static String indicazionePartiteVinte = "Partite vinte: ";
    private static LayoutManager layout = new BorderLayout(50, 50);

    /**
     * Costruttore base di PannelloStatistiche. Utilizza la grafica di default.
     * Complessità computazionale: O(1).
     */
    public PannelloStatistiche() {
        this(GRAFICA_DEFAULT);

    }

    /**
     * Costruttore completo. Inizializza gli elementi grafici del pannello delle statistiche.
     * Complessità computazionale: O(1).
     *
     * @param grafica le preferenze grafiche da usare.
     */
    public PannelloStatistiche(GraficaPannello grafica) {

        super(layout, grafica);
        titolo = grafica.creaTitolo(nomeSchermata);
        statistiche = grafica.creaTestoNormale(generaStatistiche(0, 0));
        bottoneMenu = grafica.creaBottone(indicazioneMenu);

        inizializzaPannelloStatistiche();
    }

    private void inizializzaPannelloStatistiche() {
        setBorder(BorderFactory.createEmptyBorder(70, 70, 70, 70));
        add(new JPanel() {
            {
                add(titolo);
            }
        }, BorderLayout.NORTH);
        add(new JPanel() {
            {
                add(statistiche);
            }
        }, BorderLayout.CENTER);
        add(new JPanel(new BorderLayout()) {
            {
                add(new JPanel() {
                    {
                        add(bottoneMenu);
                    }
                }, BorderLayout.EAST);
            }
        }, BorderLayout.SOUTH);
    }

    /**
     * Restituisce la label usata come titolo della schermata.
     * Complessità computazionale: O(1).
     *
     * @return la JLabel contenente il titolo.
     */
    public JLabel getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la label che contiene le statistiche della partita aggiornate.
     * Complessità computazionale: O(1).
     *
     * @return la JLabel con le statistiche.
     */
    public JLabel getStatistiche() {
        return statistiche;
    }

    /**
     * Restituisce il bottone per tornare al menù principale.
     * Complessità computazionale: O(1).
     *
     * @return il JButton del menù.
     */
    public JButton getBottoneMenu() {
        return bottoneMenu;
    }

    /**
     * Metodo statico che produce una stringa HTML che descrive le statistiche di gioco.
     * Calcola anche automaticamente le partite perse.
     * Complessità computazionale: O(1).
     *
     * @param partiteGiocate il numero totale delle partite giocate
     * @param partiteVinte   il numero delle partite vinte dal giocatore
     * @return la stringa formattata con le statistiche
     */
    public static String generaStatistiche(int partiteGiocate, int partiteVinte) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>" + indicazionePartiteGiocate + partiteGiocate + "<br>");
        sb.append(indicazionePartiteVinte + partiteVinte + "<br>");
        sb.append("Partite perse: " + (partiteGiocate - partiteVinte) + "\n");
        return sb.toString();
    }

    /**
     * Metodo dell'interfaccia Observer. Aggiorna il testo visualizzato
     * richiedendo al Model (GiocoBriscola) i nuovi dati.
     * Complessità computazionale: O(1).
     *
     * @param modello l'oggetto Observable (GiocoBriscola) che ha notificato il cambiamento.
     * @param arg argomenti extra (non usati).
     */
    @Override
    public void update(Observable modello, Object arg) {
        GiocoBriscola g = (GiocoBriscola) modello;
        /*
         * i dati sulle partite precedenti vengono aggiornati prendendo la versione più
         * recente dal modello
         */
        statistiche.setText(generaStatistiche(g.getPartiteGiocate(), g.getPartiteVinte()));
    }
}
