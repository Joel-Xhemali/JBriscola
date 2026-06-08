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
    private static String indicazioniPartitePerse = "Partite perse: ";
    private static String indicazionePartitePareggiate = "Partite pareggiate: ";
    private static LayoutManager layout = new BorderLayout(50, 50);

    /**
     * Costruttore base di PannelloStatistiche. Utilizza la grafica di default.
     */
    public PannelloStatistiche() {
        this(GRAFICA_DEFAULT);

    }

    /**
     * Costruttore completo. Inizializza gli elementi grafici del pannello delle statistiche.
     *
     * @param grafica le preferenze grafiche da usare.
     */
    public PannelloStatistiche(GraficaPannello grafica) {

        super(layout, grafica);
        titolo = grafica.creaTitolo(nomeSchermata);
        statistiche = grafica.creaTestoNormale(generaStatistiche(0, 0, 0));
        bottoneMenu = grafica.creaBottone(indicazioneMenu);

        inizializzaPannelloStatistiche();
    }

    private void inizializzaPannelloStatistiche() {
        setBorder(BorderFactory.createEmptyBorder(70, 70, 70, 70));
        
        JPanel pannelloTitolo = new JPanel();
        pannelloTitolo.setOpaque(false);
        pannelloTitolo.add(titolo);
        add(pannelloTitolo, BorderLayout.NORTH);

        JPanel pannelloStatistiche = new JPanel();
        pannelloStatistiche.setOpaque(false);
        pannelloStatistiche.add(statistiche);
        add(pannelloStatistiche, BorderLayout.CENTER);

        JPanel pannelloSud = new JPanel(new BorderLayout());
        pannelloSud.setOpaque(false);
        JPanel pannelloBottone = new JPanel();
        pannelloBottone.setOpaque(false);
        pannelloBottone.add(bottoneMenu);
        pannelloSud.add(pannelloBottone, BorderLayout.EAST);
        add(pannelloSud, BorderLayout.SOUTH);
    }

    /**
     * Restituisce la label usata come titolo della schermata.
     *
     * @return la JLabel contenente il titolo.
     */
    public JLabel getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la label che contiene le statistiche della partita aggiornate.
     *
     * @return la JLabel con le statistiche.
     */
    public JLabel getStatistiche() {
        return statistiche;
    }

    /**
     * Restituisce il bottone per tornare al menù principale.
     *
     * @return il JButton del menù.
     */
    public JButton getBottoneMenu() {
        return bottoneMenu;
    }

    /**
     * Metodo statico che produce una stringa HTML che descrive le statistiche di gioco.
     * Calcola anche automaticamente le partite perse.
     *
     * @param partiteGiocate il numero totale delle partite giocate
     * @param partiteVinte   il numero delle partite vinte dal giocatore
     * @return la stringa formattata con le statistiche
     */
    public static String generaStatistiche(int partiteGiocate, int partiteVinte, int partitePerse) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>" + indicazionePartiteGiocate + partiteGiocate + "<br>");
        sb.append(indicazionePartiteVinte + partiteVinte + "<br>");
        sb.append(indicazionePartitePareggiate + (partiteGiocate - (partiteVinte + partitePerse)) + "<br>");
        sb.append(indicazioniPartitePerse + partitePerse + "\n");
        return sb.toString();
    }

    /**
     * Metodo dell'interfaccia Observer. Aggiorna il testo visualizzato
     * richiedendo al Model (GiocoBriscola) i nuovi dati.
     *
     * @param modello l'oggetto Observable (GiocoBriscola) che ha notificato il cambiamento.
     * @param arg argomenti extra (non usati).
     */
    @Override
    public void update(Observable modello, Object arg) {
        GiocoBriscola g = (GiocoBriscola) modello;
        statistiche.setText(generaStatistiche(g.getPartiteGiocate(), g.getPartiteVinte(), g.getPartitePerse()));
    }
}
