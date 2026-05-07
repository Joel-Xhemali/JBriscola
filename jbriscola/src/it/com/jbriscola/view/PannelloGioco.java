package it.com.jbriscola.view;

import it.com.jbriscola.model.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Pannello principale di gioco che gestisce la visualizzazione del tavolo,
 * della mano del giocatore e dei bot.
 */
public class PannelloGioco extends Pannello {

    private JLabel punti;
    private JButton bottoneMenu;
    private JButton bottoneConferma;
    private int numeroTurno;

    private final JLabel labelAvatar = new JLabel();
    private final JLabel labelNome = new JLabel();

    private JPanel vistaTavolo;
    private List<Carta> carteTavolo = new ArrayList<>();

    private JPanel vistaGiocatore;
    private Carta cartaSelezionata = null;
    private Giocatore giocatoreUmano;

    private JPanel vistaAlleato;
    private Giocatore botAlleato;

    private JPanel vistaSinistra;
    private Giocatore botNemico1;

    private JPanel vistaDestra;
    private Giocatore botNemico2;

    private static final String PATH_RETRO_CARTE = "assets/carte/retro_carta.png";

    private static String indicazioneMenu = "Menù";
    private static String indicazioneConferma = "Conferma";

    /**
     * Costruttore con grafica di default.
     * @param giocatori Array di giocatori partecipanti alla partita.
     */
    public PannelloGioco(Giocatore... giocatori) {
        this(GRAFICA_DEFAULT, giocatori);
    }

    /**
     * Costruttore completo.
     * @param grafica Configurazione grafica del pannello.
     * @param giocatori Array di giocatori partecipanti alla partita.
     */
    public PannelloGioco(GraficaPannello grafica, Giocatore... giocatori) {
        super(new BorderLayout(20, 20), grafica);
        bottoneMenu = grafica.creaBottone(indicazioneMenu);
        bottoneConferma = grafica.creaBottone(indicazioneConferma);

        // Disabilitato di default in attesa che il Model comunichi che è il turno 0
        bottoneConferma.setEnabled(false);

        inizializzaPannello(giocatori);
        
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                aggiornaInterfaccia();
            }
        });
    }

    private void inizializzaPannello(Giocatore... giocatori) {
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.giocatoreUmano = giocatori[0];
        this.botNemico1 = giocatori[1];
        this.botAlleato = giocatori[2];
        this.botNemico2 = giocatori[3];

        add(creaPannelloInferiore(), BorderLayout.SOUTH);
        add(creaPannelloCentrale(), BorderLayout.CENTER);
        add(creaPannelloSuperiore(), BorderLayout.NORTH);
        add(creaPannelloSinistro(), BorderLayout.WEST);
        add(creaPannelloDestro(), BorderLayout.EAST);
    }

    private JPanel creaPannelloInferiore() {
        JPanel p = new JPanel(new BorderLayout(20, 10));
        p.setOpaque(false);

        p.add(creaPannelloProfilo(), BorderLayout.WEST);

        vistaGiocatore = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        vistaGiocatore.setOpaque(false);
        disegnaCarteGiocatore();
        p.add(vistaGiocatore, BorderLayout.CENTER);

        JPanel pannelloBottoni = new JPanel(new BorderLayout());
        pannelloBottoni.setOpaque(false);

        JPanel conferma = new JPanel(new FlowLayout(FlowLayout.CENTER));
        conferma.setOpaque(false);
        conferma.add(bottoneConferma);

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menu.setOpaque(false);
        menu.add(bottoneMenu);

        pannelloBottoni.add(conferma, BorderLayout.CENTER);
        pannelloBottoni.add(menu, BorderLayout.EAST);
        p.add(pannelloBottoni, BorderLayout.SOUTH);

        return p;
    }

    private JPanel creaPannelloProfilo() {
        JPanel profilo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        profilo.setOpaque(false);

        labelNome.setForeground(Color.DARK_GRAY);
        labelNome.setText(giocatoreUmano.getNome());

        profilo.add(labelAvatar);
        profilo.add(labelNome);

        aggiornaProfilo();
        return profilo;
    }

    private double calcolaFattoreScala() {
        var w = getWidth();
        var h = getHeight();
        if (w == 0 || h == 0) return 1.0;
        return Math.min((double) w / 1000.0, (double) h / 700.0);
    }

    private void aggiornaInterfaccia() {
        aggiornaProfilo();
        disegnaCarteGiocatore();
        disegnaCarteAlleato();
        disegnaCarteNemico1();
        disegnaCarteNemico2();
        disegnaCarteTavolo();

        var scala = calcolaFattoreScala();
        var fontSize = (int) Math.max(12, 16 * scala);
        var font = new Font("Arial", Font.BOLD, fontSize);
        bottoneMenu.setFont(font);
        bottoneConferma.setFont(font);

        this.revalidate();
        this.repaint();
    }

    private void aggiornaProfilo() {
        if (giocatoreUmano == null) return;
        var scala = calcolaFattoreScala();
        var size = (int) Math.max(30, 60 * scala);
        try {
            var imgOriginale = CacheImmagini.getImmagine(giocatoreUmano.getAvatar());
            var imgScalata = scalaImmagine(imgOriginale, size, size);
            labelAvatar.setIcon(new ImageIcon(imgScalata));
        } catch (Exception e) {
            labelAvatar.setText("[IMG]");
        }
        var fontSize = (int) Math.max(12, 18 * scala);
        labelNome.setFont(new Font("Arial", Font.BOLD, fontSize));
    }

    private JPanel creaPannelloCentrale() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        vistaTavolo = new JPanel(null);
        vistaTavolo.setOpaque(false);
        vistaTavolo.setPreferredSize(new Dimension(800, 400));
        disegnaCarteTavolo();

        p.add(vistaTavolo, BorderLayout.CENTER);
        return p;
    }

    private JPanel creaPannelloSuperiore() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        vistaAlleato = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        vistaAlleato.setOpaque(false);
        disegnaCarteAlleato();

        p.add(vistaAlleato, BorderLayout.CENTER);
        return p;
    }

    private JPanel creaPannelloSinistro() {
        vistaSinistra = new JPanel(new GridLayout(3, 1, 10, 10));
        vistaSinistra.setOpaque(false);
        disegnaCarteNemico1();
        return vistaSinistra;
    }

    private JPanel creaPannelloDestro() {
        vistaDestra = new JPanel(new GridLayout(3, 1, 10, 10));
        vistaDestra.setOpaque(false);
        disegnaCarteNemico2();
        return vistaDestra;
    }

    public void disegnaCarteGiocatore() {
        if (vistaGiocatore == null || giocatoreUmano == null) return;
        vistaGiocatore.removeAll();

        Border bordoNormale = BorderFactory.createLineBorder(Color.GRAY, 2);
        Border bordoSelezionato = BorderFactory.createLineBorder(GraficaPannello.ARANCIONE, 5);

        var scala = calcolaFattoreScala();
        var cardW = (int) Math.max(50, 90 * scala);
        var cardH = (int) Math.max(86, 140 * scala);

        for (Carta carta : giocatoreUmano.getMano()) {
            JLabel labelImmagine = new JLabel();
            BufferedImage imgOriginale = CacheImmagini.getImmagine(carta.getPathCarta());
            BufferedImage imgScalata = scalaImmagine(imgOriginale, cardW, cardH);

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
                    cartaSelezionata = carta;
                }
            });
            vistaGiocatore.add(labelImmagine);
        }
    }

    public void disegnaCarteAlleato() {
        if (vistaAlleato == null || botAlleato == null) return;
        vistaAlleato.removeAll();

        var scala = calcolaFattoreScala();
        var cardW = (int) Math.max(50, 90 * scala);
        var cardH = (int) Math.max(86, 140 * scala);

        for (int i = 0; i < botAlleato.getMano().size(); i++) {
            JLabel labelImmagine = new JLabel();
            BufferedImage imgOriginale = CacheImmagini.getImmagine(PATH_RETRO_CARTE);
            BufferedImage imgScalata = scalaImmagine(imgOriginale, cardW, cardH);
            labelImmagine.setIcon(new ImageIcon(imgScalata));
            vistaAlleato.add(labelImmagine);
        }
    }

    private void disegnaCarteNemico1() {
        if (vistaSinistra == null || botNemico1 == null) return;
        vistaSinistra.removeAll();
        disegnaCarteVerticali(vistaSinistra, botNemico1, 90);
    }

    private void disegnaCarteNemico2() {
        if (vistaDestra == null || botNemico2 == null) return;
        vistaDestra.removeAll();
        disegnaCarteVerticali(vistaDestra, botNemico2, -90);
    }

    private void disegnaCarteVerticali(JPanel pannello, Giocatore bot, int angolo) {
        var scala = calcolaFattoreScala();
        double aspectRatio = 140.0 / 90.0;
        var cardW = (int) Math.max(40, 70 * scala);
        var cardH = (int) (cardW * aspectRatio);

        for (int i = 0; i < bot.getMano().size(); i++) {
            JLabel labelImmagine = new JLabel();
            BufferedImage imgOriginale = CacheImmagini.getImmagine(PATH_RETRO_CARTE);
            BufferedImage imgRuotata = creaImmagineRuotata(imgOriginale, cardW, cardH, angolo);
            labelImmagine.setIcon(new ImageIcon(imgRuotata));
            pannello.add(labelImmagine);
        }
    }

    public void disegnaCarteTavolo() {
        if (vistaTavolo == null || carteTavolo == null) return;
        vistaTavolo.removeAll();

        int centroTavoloX = vistaTavolo.getWidth() > 0 ? vistaTavolo.getWidth() / 2 : 400;
        int centroTavoloY = vistaTavolo.getHeight() > 0 ? vistaTavolo.getHeight() / 2 : 200;

        var scala = calcolaFattoreScala();
        var cardW = (int) Math.max(50, 90 * scala);
        var cardH = (int) Math.max(86, 140 * scala);

        int counter = 0;
        for (var carta : carteTavolo.reversed()) {
            JLabel labelImmagine = new JLabel();
            int angolo = counter * 45;

            BufferedImage imgOriginale = CacheImmagini.getImmagine(carta.getPathCarta());
            BufferedImage imgFinale = creaImmagineRuotata(imgOriginale, cardW, cardH, angolo);
            labelImmagine.setIcon(new ImageIcon(imgFinale));

            int finalWidth = imgFinale.getWidth();
            int finalHeight = imgFinale.getHeight();
            int xPerfetta = centroTavoloX - (finalWidth / 2) + (counter * 4);
            int yPerfetta = centroTavoloY - (finalHeight / 2) - (counter * 2);

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

    public int getNumeroTurno() {
        return numeroTurno;
    }

    @Override
    public void update(Observable partita, Object arg) {
        PartitaBriscola partitaBriscola = (PartitaBriscola) partita;

        this.cartaSelezionata = null;
        this.carteTavolo = partitaBriscola.getCarteSulTavolo();
        this.numeroTurno = partitaBriscola.getNumeroTurno();

        // Abilita il bottone di conferma ESCLUSIVAMENTE se è il turno dell'utente (0)
        bottoneConferma.setEnabled(this.numeroTurno == 0);

        disegnaCarteGiocatore();
        disegnaCarteAlleato();
        disegnaCarteNemico1();
        disegnaCarteNemico2();
        disegnaCarteTavolo();
    }
}
