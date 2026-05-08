package it.com.jbriscola.view;

import it.com.jbriscola.model.GiocoBriscola;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

public class PannelloGiocatore extends Pannello {

    private JLabel titolo;
    private JTextField campoNickname;
    private JButton bottoneConferma;
    private JButton bottoneMenu;

    // Lo stato della view: mantiene temporaneamente la selezione per passarla al Controller
    private String pathAvatarSelezionato = null;

    private static final String nomeSchermata = "Crea Profilo";
    private static String indicazioneMenu = "Menù";
    // Path Avatar da scegliere
    private static final String PATH = "assets/avatar/";
    private static final String[] FILE_AVATAR = {"gamer.png", "girl.png", "cat.png", "meerkat.png", "panda.png", "rabbit.png"};

    /**
     * Costruttore base di PannelloGiocatore.
     * Utilizza la grafica di default e inizializza i componenti grafici.
     * Complessità computazionale: O(1).
     */
    public PannelloGiocatore() {
        this(GRAFICA_DEFAULT);
    }

    /**
     * Costruttore che accetta una grafica personalizzata.
     * Inizializza i componenti per la creazione del profilo utente.
     * Complessità computazionale: O(1) per l'impostazione.
     *
     * @param grafica la grafica da applicare al pannello.
     */
    public PannelloGiocatore(GraficaPannello grafica) {
        super(new BorderLayout(20, 20), grafica);
        titolo = grafica.creaTitolo(nomeSchermata);
        campoNickname = grafica.creaCampoTesto("Nickname");
        bottoneConferma = grafica.creaBottone("Conferma e Gioca");
        bottoneMenu = grafica.creaBottone(indicazioneMenu);

        inizializzaPannello();
    }

    private void inizializzaPannello() {
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // NORD: Titolo
        JPanel pannelloNord = new JPanel();
        pannelloNord.setOpaque(false); // Trasparente per mostrare lo sfondo a righe di Pannello
        pannelloNord.add(titolo);
        add(pannelloNord, BorderLayout.NORTH);

        // CENTRO: Form Nickname e Selezione Avatar
        JPanel pannelloCentro = new JPanel(new GridLayout(2, 1, 20, 20));
        pannelloCentro.setOpaque(false);

        // -- Sezione Nickname
        JPanel pannelloNick = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pannelloNick.setOpaque(false);
        pannelloNick.add(grafica.creaTestoNormale("Nickname: "));
        pannelloNick.add(campoNickname);
        pannelloCentro.add(pannelloNick);

        // -- Sezione Avatar
        JPanel pannelloAvatar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        pannelloAvatar.setOpaque(false);
        pannelloAvatar.add(grafica.creaTestoNormale("Scegli un Avatar: "));

        Border bordoNormale = BorderFactory.createLineBorder(Color.GRAY, 2);
        Border bordoSelezionato = BorderFactory.createLineBorder(GraficaPannello.ARANCIONE, 5);

        for (String filePng : FILE_AVATAR) {
            // Path di ogni singola icona
            String pathRisorsa = PATH + filePng;

            JLabel labelImmagine = new JLabel();

            ImageIcon iconaOriginale = new ImageIcon(pathRisorsa);
            // Scaliamo l'immagine a una dimensione fissa (es: 80x80)
            Image imgScalata = iconaOriginale.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            labelImmagine.setIcon(new ImageIcon(imgScalata));

            labelImmagine.setBorder(bordoNormale);
            labelImmagine.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Logica visiva e di selezione al click
            labelImmagine.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pathAvatarSelezionato = pathRisorsa;

                    // Resetta i bordi di tutte le immagini nel pannello
                    for (Component c : pannelloAvatar.getComponents()) {
                        if (c instanceof JLabel && c != pannelloAvatar.getComponent(0)) {
                            ((JLabel) c).setBorder(bordoNormale);
                        }
                    }
                    // Evidenzia quella cliccata
                    labelImmagine.setBorder(bordoSelezionato);
                }
            });
            pannelloAvatar.add(labelImmagine);
        }
        pannelloCentro.add(pannelloAvatar);
        add(pannelloCentro, BorderLayout.CENTER);

        // SUD: Bottone Conferma
        add(new JPanel(new BorderLayout()) {
            {
                add(new JPanel() {
                    {
                        setOpaque(false);
                        add(bottoneMenu);
                    }
                }, BorderLayout.EAST);
                add(new JPanel() {
                    {
                        setOpaque(false);
                        add(bottoneConferma);
                    }
                }, BorderLayout.CENTER);
            }
        }, BorderLayout.SOUTH);
    }

    // --- I GETTER ---
    // Il tuo Controller userà questi metodi per estrarre i dati quando l'utente preme "Conferma"

    /**
     * Recupera il nickname inserito dall'utente.
     * Complessità computazionale: O(1).
     *
     * @return la stringa contenente il nickname.
     */
    public String getNickname() {
        return campoNickname.getText().trim();
    }

    /**
     * Recupera il percorso dell'avatar selezionato dall'utente.
     * Complessità computazionale: O(1).
     *
     * @return il percorso dell'avatar.
     */
    public String getPathAvatarSelezionato() {
        return pathAvatarSelezionato;
    }

    /**
     * Restituisce il riferimento al bottone di conferma.
     * Complessità computazionale: O(1).
     *
     * @return il bottone per confermare e giocare.
     */
    public JButton getBottoneConferma() {
        return bottoneConferma;
    }

    /**
     * Restituisce il riferimento al bottone per tornare al menù.
     * Complessità computazionale: O(1).
     *
     * @return il bottone menù.
     */
    public JButton getBottoneMenu() {
        return bottoneMenu;
    }

    /**
     * Aggiorna lo stato del pannello ricevendo la notifica dal Model (pattern Observer).
     * Complessità computazionale: O(1).
     *
     * @param modello il modello da cui provengono gli aggiornamenti (GiocoBriscola).
     * @param arg argomenti opzionali.
     */
    @Override
    public void update(Observable modello, Object arg) {
        GiocoBriscola gioco = (GiocoBriscola) modello;
        gioco.getPartitaCorrente();

    }

}