package it.com.jbriscola.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Classe con campi statici e metodi di utilità per gestire l'aspetto (colore,
 * font e disposizione degli elementi) dei pannelli del Gioco
 */
public class GraficaPannello {

    public static final Color TRASPARENTE = new Color(0, 0, 0, 0);
    public static final Color ARANCIONE = Color.decode("#f08c00");
    public static final Color GIALLO = Color.decode("#ffec99");
    public static final Color VERDE_BRISCOLA = Color.decode("#1e995d");
    public static final Color BLU = Color.decode("#1F0062");
    public static final Color ROSSO = Color.decode("#68150A");

    public static final Font CORSIVO = new Font("Segoe Script", Font.BOLD, 30);

    public enum TipoTesto implements Colorabile {
        TITOLO, NORMALE, BOTTONE
    }

    public enum TipoSfondo implements Colorabile {
        PANNELLO, BOTTONE
    }

    private Map<Colorabile, Color> colori = new HashMap<>();
    private Map<TipoTesto, Font> fonts = new HashMap<>();

    /**
     * Costruttore vuoto. Inizializza l'oggetto con mappe vuote per colori e font.
     */
    public GraficaPannello() {
    }

    /**
     * Costruttore che accetta mappe preesistenti per colori e font.
     *
     * @param colori mappa dei colori associati ai componenti.
     * @param fonts  mappa dei font associati ai testi.
     */
    public GraficaPannello(Map<Colorabile, Color> colori, Map<TipoTesto, Font> fonts) {
        this.colori = colori;
        this.fonts = fonts;
    }

    /**
     * Restituisce la mappa dei colori.
     *
     * @return la mappa dei colori.
     */
    public Map<Colorabile, Color> getColori() {
        return colori;
    }

    /**
     * Crea un campo di testo (JTextField) formattato con il font di default.
     *
     * @param defaultValue il testo iniziale da mostrare nel campo.
     * @return il JTextField creato e formattato.
     */
    public JTextField creaCampoTesto(String defaultValue) {
        JTextField textField = new JTextField(defaultValue, 15);
        textField.setFont(CORSIVO);
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return textField;
    }

    /**
     * Crea un'etichetta di testo adibita a titolo (JLabel).
     *
     * @param testo il contenuto dell'etichetta.
     * @return l'etichetta titolo.
     */
    public JLabel creaTitolo(String testo) {
        return creaTesto(TipoTesto.TITOLO, testo);
    }

    /**
     * Crea un'etichetta di testo normale (JLabel).
     *
     * @param testo il contenuto dell'etichetta.
     * @return l'etichetta testuale.
     */
    public JLabel creaTestoNormale(String testo) {
        return creaTesto(TipoTesto.NORMALE, testo);
    }

    /**
     * Crea un'etichetta di testo normale con un colore specifico.
     *
     * @param testo  il contenuto dell'etichetta.
     * @param colore il colore del testo.
     * @return l'etichetta testuale colorata.
     */
    public JLabel creaTestoNormale(String testo, Color colore) {
        return creaTesto(TipoTesto.NORMALE, testo, colore);
    }

    /**
     * Crea un'etichetta di testo dato un TipoTesto ed una stringa
     * @param t il Tipo di testo
     * @param contenuto la stringa da inserire
     * @return l'etichetta testuale generata
     */
    private JLabel creaTesto(TipoTesto t, String contenuto) {
        return creaTesto(contenuto, Optional.ofNullable(colori.get(t)), Optional.ofNullable(fonts.get(t)));
    }

    /**
     * Crea un'etichetta di testo dato un TipoTesto, una stringa ed il colore
     * @param t il Tipo di testo
     * @param contenuto la stringa da inserire
     * @param colore il colore del testo
     * @return l'etichetta testuale generata
     */
    private JLabel creaTesto(TipoTesto t, String contenuto, Color colore) {
        return creaTesto(contenuto, Optional.of(colore), Optional.ofNullable(fonts.get(t)));
    }

    private JLabel creaTesto(String contenuto, Optional<Color> colore, Optional<Font> font) {
        JLabel l = new JLabel(contenuto);
        if (!colore.isEmpty())
            l.setForeground(colore.get());
        if (!font.isEmpty())
            l.setFont(font.get());
        return l;
    }

    /**
     * Crea un bottone (JButton) formattato con i colori e i font di base.
     *
     * @param testo l'etichetta del bottone.
     * @return il bottone formattato.
     */
    public JButton creaBottone(String testo) {
        return creaBottone(testo, Optional.ofNullable(colori.get(TipoTesto.BOTTONE)),
                Optional.ofNullable(colori.get(TipoSfondo.BOTTONE)), Optional.ofNullable(fonts.get(TipoTesto.BOTTONE)));
    }

    private JButton creaBottone(String testo, Optional<Color> coloreTesto, Optional<Color> coloreSfondo,
                                Optional<Font> font) {
        JButton b = new JButton(testo);
        if (!coloreTesto.isEmpty())
            b.setForeground(coloreTesto.get());
        if (!coloreSfondo.isEmpty())
            b.setBackground(coloreSfondo.get());
        else
            b.setBackground(TRASPARENTE);
        if (!font.isEmpty())
            b.setFont(font.get());
        return b;
    }

    /**
     * Crea un'etichetta contenente un'immagine caricata dal percorso indicato.
     *
     * @param path il percorso del file immagine.
     * @return il JLabel con l'immagine.
     */
    public JLabel creaImmagine(String path) {
        return new JLabel(new ImageIcon(path));
    }

    /**
     * Genera e configura un oggetto GridBagConstraints per il posizionamento in un GridBagLayout.
     * Usa i pesi di default (weightx=1, weighty=1).
     *
     * @param x colonna della griglia.
     * @param y riga della griglia.
     * @param w numero di colonne occupate.
     * @param h numero di righe occupate.
     * @param a ancoraggio (es. GridBagConstraints.CENTER).
     * @return l'oggetto GridBagConstraints configurato.
     */
    public static GridBagConstraints generaDisposizione(int x, int y, int w, int h, int a) {
        return generaDisposizione(x, y, w, h, a, 1, 1);
    }

    /**
     * Genera e configura un oggetto GridBagConstraints con parametri completi, compresi i pesi.
     *
     * @param x  colonna della griglia.
     * @param y  riga della griglia.
     * @param w  numero di colonne occupate.
     * @param h  numero di righe occupate.
     * @param a  ancoraggio.
     * @param wx peso orizzontale.
     * @param wy peso verticale.
     * @return l'oggetto GridBagConstraints configurato.
     */
    public static GridBagConstraints generaDisposizione(int x, int y, int w, int h, int a, int wx, int wy) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = w;
        c.gridheight = h;
        c.anchor = a;
        c.weightx = wx;
        c.weighty = wy;
        return c;
    }
}
