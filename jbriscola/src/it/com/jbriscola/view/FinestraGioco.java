package it.com.jbriscola.view;

import it.com.jbriscola.model.Giocatore;
import it.com.jbriscola.view.Pannello.TipoPannello;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class FinestraGioco extends JFrame {

    public final static String TITOLO = "JBriscola";

    private JPanel pannelloGenerale;
    private PannelloMenu pannelloMenu;
    private PannelloStatistiche pannelloStatistiche;
    private PannelloGiocatore pannelloGiocatore;
    private Optional<PannelloGioco> pannelloGioco;

    public FinestraGioco() {
        super(TITOLO);

        pannelloMenu = new PannelloMenu();
        pannelloStatistiche = new PannelloStatistiche();
        pannelloGiocatore = new PannelloGiocatore();
        pannelloGioco = Optional.ofNullable(null);

        pannelloGenerale = new JPanel(new CardLayout()) {
            {
                add(pannelloMenu, TipoPannello.MENU.name());
                add(pannelloStatistiche, TipoPannello.STATISTICHE.name());
                add(pannelloGiocatore, TipoPannello.GIOCATORE.name());
            }
        };

        add(pannelloGenerale);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Crea la finestra della grandezza giusta secondo gli elementi contenuti
        setLocationRelativeTo(null); // Crea la finestra al centro dello schermo
        setVisible(true); // Mostra la finestra a schermo
    }

    public JPanel getPannelloGenerale() {
        return pannelloGenerale;
    }

    public PannelloMenu getPannelloMenu() {
        return pannelloMenu;
    }

    public PannelloStatistiche getPannelloStatistiche() {
        return pannelloStatistiche;
    }

    public PannelloGiocatore getPannelloGiocatore() {
        return pannelloGiocatore;
    }

    public Optional<PannelloGioco> getPannelloGioco() {
        return pannelloGioco;
    }

    /**
     * Metodo per inizializzare un nuovo pannello gioco (un eventuale pannello
     * pre-esistente viene sovrascritto) e aggiungerlo al pannello generale della
     * finestra
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
     * generale (menù, statistiche o gioco)
     *
     * @param tipoPannello Tipo del pannello dal visualizzare
     */
    public void visualizzaPannello(TipoPannello tipoPannello) {
        ((CardLayout) pannelloGenerale.getLayout()).show(pannelloGenerale, tipoPannello.name());
    }
}
