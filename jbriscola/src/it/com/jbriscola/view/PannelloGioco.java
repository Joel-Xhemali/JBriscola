package it.com.jbriscola.view;

import it.com.jbriscola.model.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

public class PannelloGioco extends Pannello {

    private JLabel punti;
    private JButton bottoneMenu;
    private JButton bottoneConferma;

    private JPanel vistaTavolo;
    private List<Carta> carteTavolo = new ArrayList<>();;

    private JPanel vistaGiocatore;
    private Carta cartaSelezionata = null;
    private Giocatore giocatoreUmano;
    private Boolean isTurnoUmano = false;

    private JPanel vistaAlleato;
    private Giocatore botAlleato;

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
        add(creaPannelloInferiore(), BorderLayout.SOUTH);
        add(creaPannelloCentrale(), BorderLayout.CENTER);
        add(creaPannelloSuperiore(), BorderLayout.NORTH);

    }

    private JPanel creaPannelloInferiore() {
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

        // Pannello Menu
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        menu.setOpaque(false);
        menu.add(bottoneMenu);

        p.add(vistaGiocatore, BorderLayout.CENTER);
        p.add(conferma, BorderLayout.SOUTH);
        p.add(menu, BorderLayout.EAST);


        return p;
    }

    private JPanel creaPannelloCentrale() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        vistaTavolo = new JPanel(null);
        vistaTavolo.setOpaque(false);
        disegnaCarteTavolo();

        p.add(vistaTavolo, BorderLayout.CENTER);
        return p;
    }

    private JPanel creaPannelloSuperiore() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        vistaAlleato = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        vistaAlleato.setOpaque(false);

        return p;
    }

    public void disegnaCarteInMano() {
        vistaGiocatore.removeAll(); // Pulisce le vecchie carte

        Border bordoNormale = BorderFactory.createLineBorder(Color.GRAY, 2);
        Border bordoSelezionato = BorderFactory.createLineBorder(GraficaPannello.ARANCIONE, 5);


        for (Carta carta : giocatoreUmano.getMano()) {
            JLabel labelImmagine = new JLabel();

            // Prestazioni ottimali: lettura da RAM + Scaling via Graphics2D
//            String pathCarta = Utils.PATH_RETRO_CARTA;
//            if (giocatore instanceof Umano){
//                pathCarta = carta.getPathCarta();
//            }

            BufferedImage imgOriginale = CacheImmagini.getImmagine(carta.getPathCarta());
            BufferedImage imgScalata = scalaImmagine(imgOriginale, 125, 215);

            labelImmagine.setIcon(new ImageIcon(imgScalata));
            labelImmagine.setBorder(bordoNormale);
            labelImmagine.setCursor(new Cursor(Cursor.HAND_CURSOR));

            labelImmagine.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Component c : vistaGiocatore.getComponents()) {
                        if (c instanceof JLabel) ((JLabel) c).setBorder(bordoNormale);
                    }
                    labelImmagine.setBorder(bordoSelezionato);
                    cartaSelezionata = carta; // La View segna la selezione temporanea, il Controller agirà su di essa
                }
            });
            vistaGiocatore.add(labelImmagine);
        }

        // Forza Swing a ricalcolare il layout e ridisegnare
        vistaGiocatore.revalidate();
        vistaGiocatore.repaint();
    }

    public void disegnaCarteTavolo() {
        vistaTavolo.removeAll();

        // 1. Calcolo dinamico del centro reale del tavolo
        // Se il pannello è appena stato creato e non ha ancora una dimensione (width=0),
        // usiamo un valore di fallback sensato (es. centro di un monitor medio).
        int centroTavoloX = vistaTavolo.getWidth() > 0 ? vistaTavolo.getWidth() / 2 : 400;
        int centroTavoloY = vistaTavolo.getHeight() > 0 ? vistaTavolo.getHeight() / 2 : 300;

        int cardW = 125;
        int cardH = 215;

        int counter = 0;

        for (Carta carta : carteTavolo.reversed()) {
            JLabel labelImmagine = new JLabel();

            // 2. Generazione dell'angolo organico (Jittering)
            // Invece di un rigido += 45, creiamo un angolo realistico.
            // La prima carta (counter == 0) cade dritta. Le successive cadono in modo pseudo-casuale.
            int angolo = counter * 45;

            BufferedImage imgOriginale = CacheImmagini.getImmagine(carta.getPathCarta());
            BufferedImage imgFinale = creaImmagineRuotata(imgOriginale, cardW, cardH, angolo);
            labelImmagine.setIcon(new ImageIcon(imgFinale));

            // 3. LA MATEMATICA DEL PIVOT (Il vero fix)
            // Troviamo le dimensioni del nuovo bounding box espanso
            int finalWidth = imgFinale.getWidth();
            int finalHeight = imgFinale.getHeight();

            // Calcoliamo x e y affinché il CENTRO dell'immagine coincida col CENTRO del tavolo
            int xPerfetta = centroTavoloX - (finalWidth / 2);
            int yPerfetta = centroTavoloY - (finalHeight / 2);

            // Aggiungiamo un leggerissimo offset per far capire che sono impilate
            xPerfetta += (counter * 4);
            yPerfetta -= (counter * 2);

            labelImmagine.setBounds(xPerfetta, yPerfetta, finalWidth, finalHeight);
            vistaTavolo.add(labelImmagine);

            counter++;
        }

        vistaTavolo.revalidate();
        vistaTavolo.repaint();
    }

    private BufferedImage scalaImmagine(Image imgOriginale, int targetWidth, int targetHeight) {
        BufferedImage scalata = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scalata.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(imgOriginale, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return scalata;
    }

    private BufferedImage creaImmagineRuotata(Image imgOriginale, int targetWidth, int targetHeight, int angoloGradi) {
        double radianti = Math.toRadians(angoloGradi);
        double sin = Math.abs(Math.sin(radianti));
        double cos = Math.abs(Math.cos(radianti));

        int nuovaLarghezza = (int) Math.floor(targetWidth * cos + targetHeight * sin);
        int nuovaAltezza = (int) Math.floor(targetHeight * cos + targetWidth * sin);

        BufferedImage ruotata = new BufferedImage(nuovaLarghezza, nuovaAltezza, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = ruotata.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate((nuovaLarghezza - targetWidth) / 2.0, (nuovaAltezza - targetHeight) / 2.0);
        g2d.rotate(radianti, targetWidth / 2.0, targetHeight / 2.0);
        g2d.drawImage(imgOriginale, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return ruotata;
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

        // Ordine di sincronizzazione forzato
        this.cartaSelezionata = null;
        this.carteTavolo = partitaBriscola.getCarteSulTavolo(); // 1. Aggiorna i dati

        disegnaCarteInMano(); // 2. Ridisegna basandosi sui nuovi dati
        disegnaCarteTavolo();
    }
}
