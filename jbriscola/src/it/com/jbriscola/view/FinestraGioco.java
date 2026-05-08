package it.com.jbriscola.view;

import it.com.jbriscola.model.Giocatore;
import it.com.jbriscola.view.Pannello.TipoPannello;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class FinestraGioco extends JFrame {

    public final static String TITOLO = "JBriscola";

    private final JPanel pannelloGenerale;
    private final PannelloMenu pannelloMenu;
    private final PannelloStatistiche pannelloStatistiche;
    private final PannelloGiocatore pannelloGiocatore;
    private Optional<PannelloGioco> pannelloGioco;

    /**
     * Costruttore della classe FinestraGioco. Inizializza i componenti grafici di base
     * e utilizza un CardLayout per gestire la navigazione tra le schermate.
     * Complessità computazionale: O(1) in quanto viene creato un numero predefinito di pannelli.
     */
    public FinestraGioco() {
        super(TITOLO);

        pannelloMenu = new PannelloMenu();
        pannelloStatistiche = new PannelloStatistiche();
        pannelloGiocatore = new PannelloGiocatore();
        pannelloGioco = Optional.empty();

        // Risolto anti-pattern "Double Brace Initialization"
        pannelloGenerale = new JPanel(new CardLayout());
        pannelloGenerale.add(pannelloMenu, TipoPannello.MENU.name());
        pannelloGenerale.add(pannelloStatistiche, TipoPannello.STATISTICHE.name());
        pannelloGenerale.add(pannelloGiocatore, TipoPannello.GIOCATORE.name());

        add(pannelloGenerale);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Crea la finestra della grandezza giusta secondo gli elementi contenuti
        setLocationRelativeTo(null); // Crea la finestra al centro dello schermo
        setVisible(true); // Mostra la finestra a schermo
    }

    /**
     * Restituisce il pannello generale che contiene tutte le schermate del gioco.
     * Complessità computazionale: O(1).
     *
     * @return il JPanel generale (CardLayout).
     */
    public JPanel getPannelloGenerale() {
        return pannelloGenerale;
    }

    /**
     * Restituisce il pannello del menù principale.
     * Complessità computazionale: O(1).
     *
     * @return il pannello menu.
     */
    public PannelloMenu getPannelloMenu() {
        return pannelloMenu;
    }

    /**
     * Restituisce il pannello delle statistiche.
     * Complessità computazionale: O(1).
     *
     * @return il pannello statistiche.
     */
    public PannelloStatistiche getPannelloStatistiche() {
        return pannelloStatistiche;
    }

    /**
     * Restituisce il pannello per l'inserimento e la gestione del giocatore umano.
     * Complessità computazionale: O(1).
     *
     * @return il pannello giocatore.
     */
    public PannelloGiocatore getPannelloGiocatore() {
        return pannelloGiocatore;
    }

    /**
     * Restituisce un Optional contenente il pannello di gioco corrente, se presente.
     * Complessità computazionale: O(1).
     *
     * @return un Optional con il PannelloGioco.
     */
    public Optional<PannelloGioco> getPannelloGioco() {
        return pannelloGioco;
    }

    /**
     * Metodo per inizializzare un nuovo pannello gioco (un eventuale pannello
     * pre-esistente viene sovrascritto) e aggiungerlo al pannello generale della
     * finestra.
     * Complessità computazionale: O(1) in quanto rimuove e aggiunge un solo componente al CardLayout.
     *
     * @param giocatori che hanno iniziato la partita
     */
    public void setPannelloGioco(Giocatore... giocatori) {
        pannelloGioco.ifPresent(p -> pannelloGenerale.remove(p));
        pannelloGioco = Optional.of(new PannelloGioco(giocatori));
        pannelloGenerale.add(pannelloGioco.get(), Pannello.TipoPannello.GIOCO.name());
    }

    /**
     * Metodo per visualizzare un determinato pannello contenuto nel pannello
     * generale (menù, statistiche o gioco).
     * Complessità computazionale: O(1).
     *
     * @param tipoPannello Tipo del pannello dal visualizzare
     */
    public void visualizzaPannello(TipoPannello tipoPannello) {
        ((CardLayout) pannelloGenerale.getLayout()).show(pannelloGenerale, tipoPannello.name());
    }
}