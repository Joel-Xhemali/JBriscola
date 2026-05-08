package it.com.jbriscola.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Observer;
import it.com.jbriscola.view.GraficaPannello.*;

public abstract class Pannello extends JPanel implements Observer {

    public enum TipoPannello {
        MENU, STATISTICHE, GIOCO, GIOCATORE
    }

    static {
        // impostazione colori di default sui bottoni
        UIManager.put("Button.highlight", GraficaPannello.ARANCIONE);
        UIManager.put("Button.select", GraficaPannello.BLU);
        UIManager.put("Button.focus", GraficaPannello.GIALLO);
    }

    GraficaPannello grafica = new GraficaPannello();
    private static Color coloreSfondoDefault = GraficaPannello.VERDE_CHIARO;

    /**
     * GraficaPannello con:
     * sfondo verde chiaro, bottoni gialli e titolo arancione;
     * Font Titolo "Stencil", bottone corsivo, testi "calibri Light"
     */
    public static final GraficaPannello GRAFICA_DEFAULT = new GraficaPannello(
            Map.of(TipoSfondo.PANNELLO, GraficaPannello.VERDE_CHIARO, TipoSfondo.BOTTONE, GraficaPannello.GIALLO,
                    TipoTesto.TITOLO, GraficaPannello.ARANCIONE),
            Map.of(TipoTesto.TITOLO, new Font("Stencil", Font.PLAIN, 65), TipoTesto.BOTTONE, GraficaPannello.CORSIVO,
                    TipoTesto.NORMALE, new Font("Calibri Light", Font.PLAIN, 40)));

    /**
     * Costruttore della classe astratta Pannello.
     * Applica un LayoutManager personalizzato.
     * Complessità computazionale: O(1).
     *
     * @param layout il LayoutManager da applicare al pannello.
     */
    public Pannello(LayoutManager layout) {
        super(layout);
    }

    /**
     * Costruttore della classe astratta Pannello.
     * Imposta le preferenze grafiche da usare.
     * Complessità computazionale: O(1).
     *
     * @param grafica le opzioni grafiche per i colori e i font.
     */
    public Pannello(GraficaPannello grafica) {
        this.grafica = grafica;
    }

    /**
     * Costruttore completo della classe astratta Pannello.
     * Imposta il LayoutManager e le preferenze grafiche.
     * Complessità computazionale: O(1).
     *
     * @param layout il LayoutManager da applicare al pannello.
     * @param grafica le opzioni grafiche per i colori e i font.
     */
    public Pannello(LayoutManager layout, GraficaPannello grafica) {
        super(layout);
        this.grafica = grafica;
    }

    /**
     * Restituisce le opzioni grafiche impostate per questo pannello.
     * Complessità computazionale: O(1).
     *
     * @return l'oggetto GraficaPannello associato.
     */
    public GraficaPannello getGrafica() {
        return grafica;
    }

    /**
     * Sovrascrive il metodo di disegno del pannello per creare uno sfondo a righe oblique.
     * Viene chiamato dal framework Swing automaticamente.
     * Complessità computazionale: O(W + H) dove W e H sono la larghezza e l'altezza in pixel del pannello,
     * determinando il numero di linee disegnate (in base alla density fissa).
     *
     * @param g il contesto grafico usato per disegnare il componente.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int density = 5;
        g.setColor(grafica.getColori().getOrDefault(GraficaPannello.TipoSfondo.PANNELLO, coloreSfondoDefault));
        for (int x = 0; x <= getWidth() + getHeight(); x += density) {
            g.drawLine(x, 0, 0, x);
        }
    }
}
