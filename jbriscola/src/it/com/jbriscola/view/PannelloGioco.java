package it.com.jbriscola.view;

import it.com.jbriscola.model.Carta;
import it.com.jbriscola.model.Giocatore;
import it.com.jbriscola.model.PartitaBriscola;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Observable;

public class PannelloGioco extends Pannello {

    private JLabel punti;
    private JButton bottoneMenu;
    private JButton bottoneConferma;
    private JPanel vistaTavolo;

    private JPanel vistaGiocatore;
    private Carta cartaSelezionata = null;
    private Giocatore giocatoreUmano;
    private Boolean isTurnoUmano = false;

    private JPanel vistaAlleato;
    private JPanel vistaSinistra;
    private JPanel vistaDestra;

    private static final String PATH_CARTE = "asset/carte";
    private static final String[] carte = {"ASSO_DENARI", "DUE_DENARI", "TRE_DENARI", "QUATTRO_DENARI"};
    private static final String estenzione = ".png";


    private static String indicazioneMenu = "Menù";
    private static String indicazioneConferma = "Conferma";

    public PannelloGioco(Giocatore... giocatori) {
        this(GRAFICA_DEFAULT, giocatori);
    }

    public PannelloGioco(GraficaPannello grafica, Giocatore... giocatori) {
        super(new BorderLayout(20, 20), grafica);
        bottoneMenu = grafica.creaBottone(indicazioneMenu);
        bottoneConferma = grafica.creaBottone(indicazioneConferma);

        inizializzaPannello(giocatori);
    }

    private void inizializzaPannello(Giocatore... giocatori) {
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        this.giocatoreUmano = giocatori[0];
        add(creaPannelloInferiore(giocatoreUmano), BorderLayout.SOUTH);

    }

    private JPanel creaPannelloInferiore(Giocatore giocatore) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        // Pannello Mano Giocatore Umano
        vistaGiocatore = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        vistaGiocatore.setOpaque(false);

        disegnaCarteInMano();

        // Pannello Conferma Giocata
        JPanel conferma = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        conferma.setOpaque(false);
        conferma.add(bottoneConferma);

        bottoneConferma.addActionListener(e -> {

        });

        // Pannello Menu
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        menu.setOpaque(false);
        menu.add(bottoneMenu);

        p.add(vistaGiocatore, BorderLayout.CENTER);
        p.add(conferma, BorderLayout.SOUTH);
        p.add(menu, BorderLayout.EAST);


        return p;
    }

    public void disegnaCarteInMano() {
        vistaGiocatore.removeAll(); // Pulisce le vecchie carte

        Border bordoNormale = BorderFactory.createLineBorder(Color.GRAY, 2);
        Border bordoSelezionato = BorderFactory.createLineBorder(GraficaPannello.ARANCIONE, 5);

        for (Carta carta : giocatoreUmano.getMano()) {
            JLabel labelImmagine = new JLabel();
            ImageIcon iconaOriginale = new ImageIcon(carta.getPathCarta());
            Image imgScalata = iconaOriginale.getImage().getScaledInstance(125, 215, Image.SCALE_SMOOTH);
            labelImmagine.setIcon(new ImageIcon(imgScalata));
            labelImmagine.setBorder(bordoNormale);
            labelImmagine.setCursor(new Cursor(Cursor.HAND_CURSOR));

            labelImmagine.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Pulisce visivamente
                    for (Component c : vistaGiocatore.getComponents()) {
                        if (c instanceof JLabel) {
                            ((JLabel) c).setBorder(bordoNormale);
                        }
                    }
                    // Aggiorna la vista
                    labelImmagine.setBorder(bordoSelezionato);
                    // AGGIORNA LO STATO LOGICO INTERNO DELLA VISTA
                    cartaSelezionata = carta;
                }
            });
            vistaGiocatore.add(labelImmagine);
        }

        // Forza Swing a ricalcolare il layout e ridisegnare
        vistaGiocatore.revalidate();
        vistaGiocatore.repaint();
    }

    public JButton getBottoneMenu() {
        return bottoneMenu;
    }

    public JButton getBottoneConferma() {
        return bottoneConferma;
    }

    public Carta getCartaSelezionata() {
        return cartaSelezionata;
    }

    public void setCartaSelezionata(Carta cartaSelezionata) {
        this.cartaSelezionata = cartaSelezionata;
    }

    public Giocatore getGiocatoreUmano() {
        return giocatoreUmano;
    }

    @Override
    public void update(Observable partita, Object arg) {
        PartitaBriscola partitaBriscola = (PartitaBriscola) partita;
        // Resetta la selezione locale perché il modello è cambiato
        this.cartaSelezionata = null;
        // Ridisegna TUTTO basandoti sullo stato attuale di PartitaBriscola
        disegnaCarteInMano();
        // Qui disegnerai anche le carte sul tavolo, quando lo implementerai

    }
}
