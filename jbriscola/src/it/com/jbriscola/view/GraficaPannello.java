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
    public static final Color VERDE = Color.decode("#2f9e44");
    public static final Color VERDE_CHIARO = Color.decode("#b2f2bb");
    public static final Color AZZURRO = Color.decode("#a5d8ff");
    public static final Color BLU = Color.decode("#1971c2");
    public static final Color ROSSO = Color.decode("#e03131");
    public static final Color ROSSO_CHIARO = Color.decode("#ffc9c9");

    public static final Font CORSIVO = new Font("Segoe Script", Font.PLAIN, 30);

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
     * Complessità computazionale: O(1).
     */
    public GraficaPannello() {
    }

    /**
     * Costruttore che accetta mappe preesistenti per colori e font.
     * Complessità computazionale: O(1).
     *
     * @param colori mappa dei colori associati ai componenti.
     * @param fonts mappa dei font associati ai testi.
     */
    public GraficaPannello(Map<Colorabile, Color> colori, Map<TipoTesto, Font> fonts) {
        this.colori = colori;
        this.fonts = fonts;
    }

    /**
     * Restituisce la mappa dei colori.
     * Complessità computazionale: O(1).
     *
     * @return la mappa dei colori.
     */
    public Map<Colorabile, Color> getColori() {
        return colori;
    }

    /**
     * Restituisce la mappa dei font.
     * Complessità computazionale: O(1).
     *
     * @return la mappa dei font.
     */
    public Map<TipoTesto, Font> getFonts() {
        return fonts;
    }

    /**
     * Associa un colore a uno specifico tipo di componente grafico (testo o sfondo).
     * Complessità computazionale: O(1).
     *
     * @param componente il tipo di componente.
     * @param colore il colore da assegnare.
     */
    public void setColore(Colorabile componente, Color colore) {
        colori.put(componente, colore);
    }

    /**
     * Associa un font a uno specifico tipo di testo.
     * Complessità computazionale: O(1).
     *
     * @param componente il tipo di testo.
     * @param font il font da assegnare.
     */
    public void setFont(TipoTesto componente, Font font) {
        fonts.put(componente, font);
    }

    /**
     * Crea un campo di testo (JTextField) formattato con il font di default.
     * Complessità computazionale: O(1).
     *
     * @param defaultValue il testo iniziale da mostrare nel campo.
     * @return il JTextField creato e formattato.
     */
    public JTextField creaCampoTesto(String defaultValue) {
        JTextField textField = new JTextField(defaultValue, 15);
        textField.setFont(CORSIVO);
        return textField;
    }

    /**
     * Crea un'etichetta di testo adibita a titolo (JLabel).
     * Complessità computazionale: O(1).
     *
     * @param testo il contenuto dell'etichetta.
     * @return l'etichetta titolo.
     */
    public JLabel creaTitolo(String testo) {
        return creaTesto(TipoTesto.TITOLO, testo);
    }

    /**
     * Crea un'etichetta di testo normale (JLabel).
     * Complessità computazionale: O(1).
     *
     * @param testo il contenuto dell'etichetta.
     * @return l'etichetta testuale.
     */
    public JLabel creaTestoNormale(String testo) {
        return creaTesto(TipoTesto.NORMALE, testo);
    }

    /**
     * Crea un'etichetta di testo normale con un colore specifico.
     * Complessità computazionale: O(1).
     *
     * @param testo il contenuto dell'etichetta.
     * @param colore il colore del testo.
     * @return l'etichetta testuale colorata.
     */
    public JLabel creaTestoNormale(String testo, Color colore) {
        return creaTesto(TipoTesto.NORMALE, testo, colore);
    }

    private JLabel creaTesto(TipoTesto t, String contenuto) {
        return creaTesto(contenuto, Optional.ofNullable(colori.get(t)), Optional.ofNullable(fonts.get(t)));
    }

    private JLabel creaTesto(TipoTesto t, String contenuto, Color colore) {
        return creaTesto(contenuto, Optional.of(colore), Optional.ofNullable(fonts.get(t)));
    }

    private JLabel creaTesto(TipoTesto t, String contenuto, Font font) {
        return creaTesto(contenuto, Optional.ofNullable(colori.get(t)), Optional.of(font));
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
     * Complessità computazionale: O(1).
     *
     * @param testo l'etichetta del bottone.
     * @return il bottone formattato.
     */
    public JButton creaBottone(String testo) {
        return creaBottone(testo, Optional.ofNullable(colori.get(TipoTesto.BOTTONE)),
                Optional.ofNullable(colori.get(TipoSfondo.BOTTONE)), Optional.ofNullable(fonts.get(TipoTesto.BOTTONE)));
    }

    /**
     * Crea un bottone con uno sfondo specifico.
     * Complessità computazionale: O(1).
     *
     * @param testo l'etichetta del bottone.
     * @param coloreSfondo il colore di sfondo del bottone.
     * @return il bottone formattato.
     */
    public JButton creaBottone(String testo, Color coloreSfondo) {

        return creaBottone(testo, Optional.ofNullable(colori.get(TipoTesto.BOTTONE)), Optional.of(coloreSfondo),
                Optional.ofNullable(fonts.get(TipoTesto.BOTTONE)));
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
     * Complessità computazionale: O(1) in quanto la creazione dell'etichetta delega al caricatore di icone.
     *
     * @param path il percorso del file immagine.
     * @return il JLabel con l'immagine.
     */
    public JLabel creaImmagine(String path) {
        return new JLabel(new ImageIcon(path));
    }

    /**
     * Sostituisce l'immagine corrente all'interno di un'etichetta.
     * Complessità computazionale: O(1).
     *
     * @param label l'etichetta in cui sostituire l'immagine.
     * @param path il percorso della nuova immagine.
     */
    public void sostituisciImmagine(JLabel label, String path) {
        label.setIcon(new ImageIcon(path));
    }

    /**
     * Genera e configura un oggetto GridBagConstraints per il posizionamento in un GridBagLayout.
     * Usa i pesi di default (weightx=1, weighty=1).
     * Complessità computazionale: O(1).
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
     * Complessità computazionale: O(1).
     *
     * @param x colonna della griglia.
     * @param y riga della griglia.
     * @param w numero di colonne occupate.
     * @param h numero di righe occupate.
     * @param a ancoraggio.
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
