package it.com.jbriscola.view;

import it.com.jbriscola.model.Carta;
import it.com.jbriscola.model.Giocatore;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

public class PannelloGioco extends Pannello {

    private JLabel punti;
    private JButton bottoneMenu;
    private JPanel vistaTavolo;
    private JPanel vistaGiocatore;
    private JPanel vistaAlleato;
    private JPanel vistaSinistra;
    private JPanel vistaDestra;

    private static final String PATH_CARTE = "asset/carte";
    private static final String[] carte = {"ASSO_DENARI", "DUE_DENARI", "TRE_DENARI","QUATTRO_DENARI"};
    private static final String estenzione = ".png";


    private static String indicazioneMenu = "Menù";

    public PannelloGioco(Giocatore... giocatori) {
        this(GRAFICA_DEFAULT, giocatori);
    }

    public PannelloGioco(GraficaPannello grafica, Giocatore... giocatori) {
        super(new BorderLayout(20, 20), grafica);
        bottoneMenu = grafica.creaBottone(indicazioneMenu);

        inizializzaPannello(giocatori);
    }

    private void inizializzaPannello(Giocatore... giocatori){
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        // SUD: Bottone Conferma
        add(new JPanel(new BorderLayout()) {
            {
                JPanel manoGiocatore = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
                manoGiocatore.setOpaque(false);
                Border bordoNormale = BorderFactory.createLineBorder(Color.GRAY, 2);
                Border bordoSelezionato = BorderFactory.createLineBorder(GraficaPannello.ARANCIONE, 5);
                Giocatore giocatore = giocatori[0];
                for(Carta carta : giocatore.getMano()){
                    JLabel labelImmagine = new JLabel();

                    ImageIcon iconaOriginale = new ImageIcon(carta.getPathCarta());
                    // Scaliamo l'immagine a una dimensione fissa
                    Image imgScalata = iconaOriginale.getImage().getScaledInstance(125, 215, Image.SCALE_SMOOTH);
                    labelImmagine.setIcon(new ImageIcon(imgScalata));

                    labelImmagine.setBorder(bordoNormale);
                    labelImmagine.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    labelImmagine.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                            // Resetta i bordi di tutte le immagini nel pannello
                            for (Component c : manoGiocatore.getComponents()) {
                                if (c instanceof JLabel && c != manoGiocatore.getComponent(0)) {
                                    ((JLabel) c).setBorder(bordoNormale);
                                }
                            }
                            // Evidenzia quella cliccata
                            labelImmagine.setBorder(bordoSelezionato);
                        }
                    });
                    manoGiocatore.add(labelImmagine);
                }

                add(new JPanel() {
                    {
                        setOpaque(false);
                        add(bottoneMenu);
                    }
                }, BorderLayout.EAST);
            }
        }, BorderLayout.SOUTH);
    }

    public JButton getBottoneMenu() {
        return bottoneMenu;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
