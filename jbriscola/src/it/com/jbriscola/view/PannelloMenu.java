package it.com.jbriscola.view;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class PannelloMenu extends Pannello {

    private JLabel titolo;
    private JLabel immagine;
    private JButton bottoneGioco;
    private JButton bottoneStatistiche;

    private static String nomeGioco = "<html>Gioco della Briscola</html>";
    private static String pathImmagine = "assets/bg.jpg";
    private static String indicazioneGioco = "Gioca";
    private static String indicazioneStatistiche = "Statistiche";
    private static LayoutManager layout = new GridLayout(1, 2, 40, 0);

    /**
     * Costruttore base di PannelloMenu. Utilizza la grafica e il layout di default.
     * Complessità computazionale: O(1).
     */
    public PannelloMenu() {
        this(GRAFICA_DEFAULT);
    }

    /**
     * Costruttore completo. Inizializza gli elementi grafici del menu principale.
     * Complessità computazionale: O(1).
     *
     * @param graficaPannello l'oggetto contenente le impostazioni grafiche del pannello.
     */
    public PannelloMenu(GraficaPannello graficaPannello) {
        super(layout, graficaPannello);

        titolo = grafica.creaTitolo(nomeGioco);
        immagine = grafica.creaImmagine(pathImmagine);
        bottoneGioco = grafica.creaBottone(indicazioneGioco);
        bottoneStatistiche = grafica.creaBottone(indicazioneStatistiche);

        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        add(immagine);
        add(creaPannelloPulsanti());
    }

    private JPanel creaPannelloPulsanti() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false); // Imposta il pannello come trasparente
        GridBagConstraints disp = GraficaPannello.generaDisposizione(0, 3, 1, 1, GridBagConstraints.SOUTH);
        p.add(titolo, disp);
        disp = GraficaPannello.generaDisposizione(0, 4, 1, 1, GridBagConstraints.CENTER);
        p.add(bottoneGioco, disp);
        disp = GraficaPannello.generaDisposizione(0, 5, 1, 1, GridBagConstraints.NORTH);
        p.add(bottoneStatistiche, disp);
        return p;
    }

    /**
     * Restituisce la label usata per il titolo del gioco.
     * Complessità computazionale: O(1).
     *
     * @return la JLabel del titolo.
     */
    public JLabel getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la label usata per l'immagine di sfondo del menu.
     * Complessità computazionale: O(1).
     *
     * @return la JLabel con l'immagine di sfondo.
     */
    public JLabel getImmagine() {
        return immagine;
    }

    /**
     * Restituisce il bottone per avviare il gioco.
     * Complessità computazionale: O(1).
     *
     * @return il JButton Gioca.
     */
    public JButton getBottoneGioco() {
        return bottoneGioco;
    }

    /**
     * Restituisce il bottone per visualizzare le statistiche.
     * Complessità computazionale: O(1).
     *
     * @return il JButton Statistiche.
     */
    public JButton getBottoneStatistiche() {
        return bottoneStatistiche;
    }

    /**
     * Sovrascrive il metodo update del pattern Observer.
     * Il menu principale non necessita di aggiornamenti dinamici in risposta
     * agli eventi del Model, pertanto il metodo è vuoto.
     * Complessità computazionale: O(1).
     *
     * @param o l'oggetto Observable che ha notificato un cambiamento.
     * @param arg argomenti extra (non usati).
     */
    @Override
    public void update(Observable o, Object arg) {
        // il menù non ha dati da aggiornare
    }
}
