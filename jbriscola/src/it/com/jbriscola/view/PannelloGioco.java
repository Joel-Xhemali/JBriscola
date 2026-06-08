package it.com.jbriscola.view;

import it.com.jbriscola.model.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Pannello principale di gioco che gestisce la visualizzazione del tavolo,
 * della mano dei giocatori e dell'interfaccia utente.
 * Architettura visiva ottimizzata tramite caching e gerarchie di Layout rigorose.
 */
public class PannelloGioco extends Pannello {

    private final JButton bottoneMenu;
    private final JButton bottoneConferma;
    private int numeroTurno;

    private JLabel puntiGiocatore;
    private JLabel puntiNemici;

    private JPanel vistaTavolo;
    private List<Carta> carteTavolo = new ArrayList<>();
    private Carta briscola = null;
    private Mazzo mazzo = null;

    private JPanel vistaGiocatore;
    private Carta cartaSelezionata = null;

    private Giocatore giocatoreUmano;
    private PannelloProfilo profiloUmano;

    private JPanel vistaAlleato;
    private Giocatore botAlleato;
    private PannelloProfilo profiloAlleato;

    private JPanel vistaSinistra;
    private Giocatore botNemico1;
    private PannelloProfilo profiloSinistra;

    private JPanel vistaDestra;
    private Giocatore botNemico2;
    private PannelloProfilo profiloDestra;

    private static final String PATH_RETRO_CARTE = "assets/carte/retro_carta.png";
    private static final String INDICAZIONE_MENU = "Menù";
    private static final String INDICAZIONE_CONFERMA = "Conferma";
    private static final String TEXT_PUNTI_GIOCATORE = "Punti Giocatore: ";
    private static final String TEXT_PUNTI_NEMICI = "Punti Nemici: ";


    private final Map<String, BufferedImage> cacheScalate = new HashMap<>();

    /**
     * Componente UI unificato per il profilo di un Giocatore.
     * Mostra l'avatar e il nome del giocatore.
     */
    private class PannelloProfilo extends JPanel {
        private final JLabel avatarLabel = new JLabel();
        private final JLabel nomeLabel = new JLabel();
        private final Giocatore giocatore;


        /**
         * Crea un nuovo PannelloProfilo.
         * Complessità computazionale: O(1).
         *
         * @param giocatore Il giocatore da rappresentare.
         */
        public PannelloProfilo(Giocatore giocatore) {
            super(new FlowLayout(FlowLayout.CENTER, 10, 5));
            setOpaque(false);
            this.giocatore = giocatore;

            nomeLabel.setText(giocatore != null ? giocatore.getNome() : "");
            nomeLabel.setForeground(Color.DARK_GRAY);

            add(avatarLabel);
            add(nomeLabel);
        }

        /**
         * Aggiorna la grafica del pannello in base alle nuove dimensioni fornite.
         * Complessità computazionale: O(1) (ammortizzato grazie alla cache).
         *
         * @param targetSize Dimensione desiderata per l'avatar.
         * @param fontSize   Dimensione desiderata per il font.
         */
        public void aggiornaGrafica(int targetSize, int fontSize) {
            if (giocatore == null) return;
            try {
                BufferedImage imgOriginale = CacheImmagini.getImmagine(giocatore.getAvatar());
                BufferedImage imgScalata = ottieniImmagineScalata(giocatore.getAvatar(), imgOriginale, targetSize, targetSize);
                avatarLabel.setIcon(new ImageIcon(imgScalata));
            } catch (Exception e) {
                avatarLabel.setText("[IMG]");
            }
            nomeLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        }
    }

    /**
     * Costruttore di default della classe PannelloGioco.
     * Complessità computazionale: O(1).
     *
     * @param giocatori Lista di giocatori che partecipano alla partita.
     */
    public PannelloGioco(Giocatore... giocatori) {
        this(GRAFICA_DEFAULT, giocatori);
    }

    /**
     * Costruttore completo della classe PannelloGioco.
     * Complessità computazionale: O(1).
     *
     * @param grafica   Impostazioni grafiche per il pannello.
     * @param giocatori Lista di giocatori che partecipano alla partita.
     */
    public PannelloGioco(GraficaPannello grafica, Giocatore... giocatori) {
        super(new BorderLayout(20, 20), grafica);
        bottoneMenu = grafica.creaBottone(INDICAZIONE_MENU);
        bottoneConferma = grafica.creaBottone(INDICAZIONE_CONFERMA);

        bottoneConferma.setEnabled(false);

        inizializzaPannello(giocatori);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                aggiornaInterfaccia();
            }
        });
    }

    /**
     * Inizializza il layout principale dividendo il pannello in 5 aree.
     * Complessità computazionale: O(1).
     *
     * @param giocatori I giocatori coinvolti nella partita.
     */
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

    /**
     * Crea il pannello inferiore contenente le carte del giocatore e i comandi base.
     * Complessità computazionale: O(1).
     *
     * @return Il pannello configurato.
     */
    private JPanel creaPannelloInferiore() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setOpaque(false);

        // Blocco logico orizzontale: Profilo -> Carte -> Bottone Conferma
        JPanel centroInferiore = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        centroInferiore.setOpaque(false);

        profiloUmano = new PannelloProfilo(giocatoreUmano);
        centroInferiore.add(profiloUmano);

        vistaGiocatore = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        vistaGiocatore.setOpaque(false);
        disegnaCarteGiocatore();
        centroInferiore.add(vistaGiocatore);

        centroInferiore.add(bottoneConferma);

        p.add(centroInferiore, BorderLayout.CENTER);

        // Bottone Menù relegato in basso a destra
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menuPanel.setOpaque(false);
        menuPanel.add(bottoneMenu);
        p.add(menuPanel, BorderLayout.SOUTH);

        return p;
    }

    /**
     * Crea il pannello centrale, destinato alla rappresentazione del tavolo.
     * Complessità computazionale: O(1).
     *
     * @return Il pannello del tavolo.
     */
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

    /**
     * Crea il pannello superiore contenente le carte del bot alleato e il punteggio.
     * Complessità computazionale: O(1).
     *
     * @return Il pannello configurato.
     */
    private JPanel creaPannelloSuperiore() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        var scala = calcolaFattoreScala();
        var fontPunti = new Font("Arial", Font.BOLD, (int) Math.max(14, 20 * scala));

        JPanel pannelloPuntiGiocatore = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pannelloPuntiGiocatore.setOpaque(false);
        puntiGiocatore = grafica.creaTestoNormale(TEXT_PUNTI_GIOCATORE + "0", GraficaPannello.BLU);
        puntiGiocatore.setFont(fontPunti);
        pannelloPuntiGiocatore.add(puntiGiocatore);
        p.add(pannelloPuntiGiocatore, BorderLayout.WEST);

        JPanel pannelloPuntiNemici = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pannelloPuntiNemici.setOpaque(false);
        puntiNemici = grafica.creaTestoNormale(TEXT_PUNTI_NEMICI + "0", GraficaPannello.ROSSO);
        puntiNemici.setFont(fontPunti);
        pannelloPuntiNemici.add(puntiNemici);
        p.add(pannelloPuntiNemici, BorderLayout.EAST);

        // Blocco logico orizzontale: Profilo -> Carte
        JPanel centroTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        centroTop.setOpaque(false);

        profiloAlleato = new PannelloProfilo(botAlleato);
        centroTop.add(profiloAlleato);

        vistaAlleato = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        vistaAlleato.setOpaque(false);
        disegnaCarteAlleato();
        centroTop.add(vistaAlleato);

        p.add(centroTop, BorderLayout.CENTER);

        return p;
    }

    /**
     * Crea il pannello di sinistra destinato alle carte e al profilo del nemico 1.
     * Complessità computazionale: O(1).
     *
     * @return Il pannello configurato.
     */
    private JPanel creaPannelloSinistro() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        profiloSinistra = new PannelloProfilo(botNemico1);
        p.add(profiloSinistra, BorderLayout.NORTH);

        vistaSinistra = new JPanel(null);
        vistaSinistra.setOpaque(false);

        disegnaCarteNemico1();
        p.add(vistaSinistra, BorderLayout.CENTER);

        return p;
    }

    /**
     * Crea il pannello di destra destinato alle carte e al profilo del nemico 2.
     * Complessità computazionale: O(1).
     *
     * @return Il pannello configurato.
     */
    private JPanel creaPannelloDestro() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        profiloDestra = new PannelloProfilo(botNemico2);
        p.add(profiloDestra, BorderLayout.NORTH);

        vistaDestra = new JPanel(null);
        vistaDestra.setOpaque(false);

        disegnaCarteNemico2();
        p.add(vistaDestra, BorderLayout.CENTER);

        return p;
    }

    /**
     * Calcola un fattore scalare per ridimensionare la grafica in modo dinamico.
     * Complessità computazionale: O(1).
     *
     * @return un numero `double` da utilizzare come modificatore.
     */
    private double calcolaFattoreScala() {
        var w = getWidth();
        var h = getHeight();
        if (w == 0 || h == 0) return 1.0;
        return Math.min((double) w / 1000.0, (double) h / 700.0);
    }

    /**
     * Aggiorna l'interfaccia interamente, richiamando tutti i metodi di draw dei componenti.
     * Complessità computazionale: O(N) dove N è il numero di carte da disegnare nei rispettivi panel.
     */
    private void aggiornaInterfaccia() {
        var scala = calcolaFattoreScala();

        var sizeProfilo = (int) Math.max(25, 45 * scala);
        var fontProfilo = (int) Math.max(12, 16 * scala);

        if (profiloUmano != null) profiloUmano.aggiornaGrafica(sizeProfilo, fontProfilo);
        if (profiloAlleato != null) profiloAlleato.aggiornaGrafica(sizeProfilo, fontProfilo);
        if (profiloSinistra != null) profiloSinistra.aggiornaGrafica(sizeProfilo, fontProfilo);
        if (profiloDestra != null) profiloDestra.aggiornaGrafica(sizeProfilo, fontProfilo);

        disegnaCarteGiocatore();
        disegnaCarteAlleato();
        disegnaCarteNemico1();
        disegnaCarteNemico2();
        disegnaCarteTavolo();

        var fontMenuConferma = new Font("Arial", Font.BOLD, (int) Math.max(12, 16 * scala));
        bottoneMenu.setFont(fontMenuConferma);
        bottoneConferma.setFont(fontMenuConferma);

        var fontPunti = new Font("Arial", Font.BOLD, (int) Math.max(14, 20 * scala));
        puntiGiocatore.setFont(fontPunti);
        puntiNemici.setFont(fontPunti);

        this.revalidate();
        this.repaint();
    }

    /**
     * Aggiorna e ridisegna graficamente le carte attualmente in mano all'utente.
     * Complessità computazionale: O(C) dove C è il numero di carte in mano (max 3).
     */
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
            BufferedImage imgScalata = ottieniImmagineScalata(carta.getPathCarta(), imgOriginale, cardW, cardH);

            labelImmagine.setIcon(new ImageIcon(imgScalata));
            labelImmagine.setBorder(carta.equals(cartaSelezionata) ? bordoSelezionato : bordoNormale);
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

    /**
     * Disegna i dorsi delle carte in mano al bot alleato.
     * Complessità computazionale: O(C) dove C è il numero di carte.
     */
    public void disegnaCarteAlleato() {
        if (vistaAlleato == null || botAlleato == null) return;
        vistaAlleato.removeAll();

        var scala = calcolaFattoreScala();
        var cardW = (int) Math.max(50, 90 * scala);
        var cardH = (int) Math.max(86, 140 * scala);

        BufferedImage imgOriginale = CacheImmagini.getImmagine(PATH_RETRO_CARTE);
        BufferedImage imgScalata = ottieniImmagineScalata(PATH_RETRO_CARTE, imgOriginale, cardW, cardH);

        for (int i = 0; i < botAlleato.getMano().size(); i++) {
            JLabel labelImmagine = new JLabel(new ImageIcon(imgScalata));
            vistaAlleato.add(labelImmagine);
        }
    }

    /**
     * Ridisegna la mano del primo bot nemico.
     * Complessità computazionale: O(C).
     */
    private void disegnaCarteNemico1() {
        if (vistaSinistra == null || botNemico1 == null) return;
        vistaSinistra.removeAll();
        disegnaCarteVerticali(vistaSinistra, botNemico1, 90);
    }

    /**
     * Ridisegna la mano del secondo bot nemico.
     * Complessità computazionale: O(C).
     */
    private void disegnaCarteNemico2() {
        if (vistaDestra == null || botNemico2 == null) return;
        vistaDestra.removeAll();
        disegnaCarteVerticali(vistaDestra, botNemico2, -90);
    }

    /**
     * Disegna i dorsi delle carte ruotati verticalmente all'interno di un pannello specifico.
     * Crea un effetto a "ventaglio chiuso" sovrapponendo le carte.
     * Complessità computazionale: O(C) dove C è il numero di carte.
     *
     * @param pannello Il pannello di destinazione.
     * @param bot      Il giocatore per cui disegnare le carte.
     * @param angolo   L'angolo di rotazione (tipicamente +/- 90).
     */
    private void disegnaCarteVerticali(JPanel pannello, Giocatore bot, int angolo) {
        var scala = calcolaFattoreScala();
        var cardW = (int) Math.max(40, 75 * scala);
        var cardH = (int) Math.max(65, 115 * scala);

        BufferedImage imgOriginale = CacheImmagini.getImmagine(PATH_RETRO_CARTE);
        BufferedImage imgRuotata = ottieniImmagineRuotata(PATH_RETRO_CARTE, imgOriginale, cardW, cardH, angolo);

        int numCarte = bot.getMano().size();
        if (numCarte == 0) return;

        // In un'immagine ruotata a 90°, la larghezza percepita è cardH e l'altezza percepita è cardW.
        int larghezzaReale = imgRuotata.getWidth();
        int altezzaReale = imgRuotata.getHeight();

        // Le carte si sovrappongono mostrando solo un terzo del loro dorso (effetto ventaglio chiuso)
        int stepY = altezzaReale / 3;
        int altezzaTotalePannello = (stepY * (numCarte - 1)) + altezzaReale;

        // Comunichiamo al genitore (BorderLayout) quanto spazio geometrico ci serve esattamente
        pannello.setPreferredSize(new Dimension(larghezzaReale, altezzaTotalePannello));

        for (int i = 0; i < numCarte; i++) {
            JLabel labelImmagine = new JLabel(new ImageIcon(imgRuotata));

            // Posizionamento assoluto: scendiamo sull'asse Y per ogni carta
            labelImmagine.setBounds(0, i * stepY, larghezzaReale, altezzaReale);

            pannello.add(labelImmagine);
        }
    }

    /**
     * Aggiorna e ridisegna le carte giocate sul tavolo da parte di tutti i giocatori,
     * inclusa la carta briscola in alto a sinistra ed il relativo mazzo.
     * Complessità computazionale: O(C) dove C è il numero di carte a terra.
     */
    public void disegnaCarteTavolo() {
        if (vistaTavolo == null || carteTavolo == null) return;
        vistaTavolo.removeAll();

        var scala = calcolaFattoreScala();
        var cardW = (int) Math.max(50, 90 * scala);
        var cardH = (int) Math.max(86, 140 * scala);

        int centroTavoloX = vistaTavolo.getWidth() > 0 ? vistaTavolo.getWidth() / 2 : 400;
        int centroTavoloY = vistaTavolo.getHeight() > 0 ? vistaTavolo.getHeight() / 2 : 200;

        int spreadX = (int) (45 * scala);
        int spreadY = (int) (15 * scala);
        int offsetGruppoX = carteTavolo.isEmpty() ? 0 : ((carteTavolo.size() - 1) * spreadX) / 2;

        int offsetBriscolaX = cardW + (int) (60 * scala) + offsetGruppoX;
        int briscolaX = centroTavoloX - offsetBriscolaX;
        int briscolaY = centroTavoloY - (cardH / 2);

        int counter = 0;
        for (var carta : carteTavolo) {
            JLabel labelImmagine = new JLabel();
            int angolo = counter * 45;

            BufferedImage imgOriginale = CacheImmagini.getImmagine(carta.getPathCarta());
            BufferedImage imgFinale = ottieniImmagineRuotata(carta.getPathCarta(), imgOriginale, cardW, cardH, angolo);
            labelImmagine.setIcon(new ImageIcon(imgFinale));

            int xPerfetta = centroTavoloX - (imgFinale.getWidth() / 2) + (counter * spreadX) - offsetGruppoX;
            int yPerfetta = centroTavoloY - (imgFinale.getHeight() / 2) + ((counter % 2 == 0 ? 1 : -1) * spreadY);

            labelImmagine.setBounds(xPerfetta, yPerfetta, imgFinale.getWidth(), imgFinale.getHeight());
            vistaTavolo.add(labelImmagine, 0);
            counter++;
        }

        if (mazzo != null && !mazzo.isEmpty()) {
            JLabel labelMazzo = new JLabel();
            BufferedImage imgOriginale = CacheImmagini.getImmagine(PATH_RETRO_CARTE);
            BufferedImage imgRuotata = ottieniImmagineRuotata(PATH_RETRO_CARTE, imgOriginale, cardW, cardH, 90);
            labelMazzo.setIcon(new ImageIcon(imgRuotata));

            int offsetSeme = (int) (20 * scala);
            int mazzoX = briscolaX + (cardW - cardH) / 2 + offsetSeme;
            int mazzoY = briscolaY + (cardH - cardW) / 2 + offsetSeme;

            labelMazzo.setBounds(mazzoX, mazzoY, imgRuotata.getWidth(), imgRuotata.getHeight());
            vistaTavolo.add(labelMazzo, vistaTavolo.getComponentCount());
        }

        if (briscola != null) {
            JLabel labelBriscola = new JLabel();
            BufferedImage imgOriginale = CacheImmagini.getImmagine(briscola.getPathCarta());
            BufferedImage imgScalata = ottieniImmagineScalata(briscola.getPathCarta(), imgOriginale, cardW, cardH);

            labelBriscola.setIcon(new ImageIcon(imgScalata));
            labelBriscola.setBounds(briscolaX, briscolaY, cardW, cardH);
            vistaTavolo.add(labelBriscola, vistaTavolo.getComponentCount());
        }

        vistaTavolo.revalidate();
        vistaTavolo.repaint();
    }

    /**
     * Esegue il recupero con caching di un'immagine scalata per massimizzare le performance di rendering.
     * Complessità computazionale: O(1) ammortizzato.
     *
     * @param id           L'identificativo univoco dell'immagine.
     * @param imgOriginale L'immagine di base.
     * @param width        Larghezza desiderata.
     * @param height       Altezza desiderata.
     * @return L'immagine renderizzata.
     */
    private BufferedImage ottieniImmagineScalata(String id, BufferedImage imgOriginale, int width, int height) {
        if (imgOriginale == null) return null;
        String key = id + "_" + width + "x" + height;

        return cacheScalate.computeIfAbsent(key, k -> {
            BufferedImage scalata = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scalata.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(imgOriginale, 0, 0, width, height, null);
            g2d.dispose();
            return scalata;
        });
    }

    /**
     * Esegue il recupero con caching di un'immagine scalata e ruotata.
     * Complessità computazionale: O(1) ammortizzato.
     *
     * @param id           L'identificativo univoco dell'immagine.
     * @param imgOriginale L'immagine di base.
     * @param targetWidth  Larghezza base desiderata.
     * @param targetHeight Altezza base desiderata.
     * @param angoloGradi  Angolo in gradi in base a cui ruotare l'immagine.
     * @return L'immagine scalata e ruotata.
     */
    private BufferedImage ottieniImmagineRuotata(String id, BufferedImage imgOriginale, int targetWidth, int targetHeight, int angoloGradi) {
        if (imgOriginale == null) return null;
        String key = id + "_rot_" + angoloGradi + "_" + targetWidth + "x" + targetHeight;

        return cacheScalate.computeIfAbsent(key, k -> creaImmagineRuotata(imgOriginale, targetWidth, targetHeight, angoloGradi));
    }

    /**
     * Motore di rendering core per la rotazione di un'immagine di base.
     *
     * @param imgOriginale Immagine non ruotata.
     * @param targetWidth  Larghezza base.
     * @param targetHeight Altezza base.
     * @param angoloGradi  Gradi di rotazione.
     * @return L'immagine modificata in memoria.
     */
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

    /**
     * @return il bottone adibito al ritorno al Menù.
     */
    public JButton getBottoneMenu() {
        return bottoneMenu;
    }

    /**
     * @return il bottone adibito alla conferma del proprio turno.
     */
    public JButton getBottoneConferma() {
        return bottoneConferma;
    }

    /**
     * @return La carta selezionata o null se nessuna carta risulta selezionata.
     */
    public Carta getCartaSelezionata() {
        return cartaSelezionata;
    }

    /**
     * Imposta la carta selezionata attivamente dalla UI del tavolo.
     *
     * @param cartaSelezionata La carta in uso.
     */
    public void setCartaSelezionata(Carta cartaSelezionata) {
        this.cartaSelezionata = cartaSelezionata;
    }

    /**
     * Notificato dall'Observer, innesca il ricalcolo e aggiornamento generale
     * della view ricevendo lo stato di PartitaBriscola.
     *
     * @param partita l'oggetto Observable di base.
     * @param arg     Eventuali argomenti associati alla chiamata.
     */
    @Override
    public void update(Observable partita, Object arg) {
        PartitaBriscola partitaBriscola = (PartitaBriscola) partita;

        this.cartaSelezionata = null;
        this.carteTavolo = partitaBriscola.getCarteSulTavolo();
        this.numeroTurno = partitaBriscola.getNumeroTurno();
        this.briscola = partitaBriscola.getBriscola();
        this.mazzo = partitaBriscola.getMazzo();

        puntiGiocatore.setText("Punti Giocatore: " + partitaBriscola.getPuntiGiocatore());
        puntiNemici.setText("Punti Nemici: " + partitaBriscola.getPuntiNemici());

        bottoneConferma.setEnabled(this.numeroTurno == 0);

        aggiornaInterfaccia();
    }
}
