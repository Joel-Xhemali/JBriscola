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

    private static final String nomeSchermata = "Giocatore";
    private static String indicazioneMenu = "Menù";
    // Path Avatar da scegliere
    private static final String PATH = "assets/avatar/";
    private static final String[] FILE_AVATAR = {"gamer.png", "girl.png", "cat.png", "meerkat.png", "panda.png", "rabbit.png"};

    /**
     * Costruttore base di PannelloGiocatore.
     * Utilizza la grafica di default e inizializza i componenti grafici.
     */
    public PannelloGiocatore() {
        this(GRAFICA_DEFAULT);
    }

    /**
     * Costruttore che accetta una grafica personalizzata.
     * Inizializza i componenti per la creazione del profilo utente.
     *
     * @param grafica la grafica da applicare al pannello.
     */
    public PannelloGiocatore(GraficaPannello grafica) {
        super(new BorderLayout(20, 20), grafica);
        titolo = grafica.creaTitolo(nomeSchermata);
        campoNickname = grafica.creaCampoTesto("Nickname");
        bottoneConferma = grafica.creaBottone("Gioca");
        bottoneMenu = grafica.creaBottone(indicazioneMenu);

        inizializzaPannello();
    }

    /**
     * Inizializza il pannello del Giocatore settando i sotto pannelli per il NickName, l'Avatar e il pulsanti Conferma/Menù
     */
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

        // SOUTH: bottoni Conferma e Menù
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints disp = GraficaPannello.generaDisposizione(4, 0, 2, 1, GridBagConstraints.CENTER);
        p.add(bottoneConferma, disp);
        disp = GraficaPannello.generaDisposizione(4, 0, 1, 1, GridBagConstraints.EAST);
        p.add(bottoneMenu, disp);
        add(p, BorderLayout.SOUTH);
    }

    /**
     * Recupera il nickname inserito dall'utente.
     *
     * @return la stringa contenente il nickname.
     */
    public String getNickname() {
        return campoNickname.getText().trim();
    }

    /**
     * Recupera il percorso dell'avatar selezionato dall'utente.
     *
     * @return il percorso dell'avatar.
     */
    public String getPathAvatarSelezionato() {
        return pathAvatarSelezionato;
    }

    /**
     * Restituisce il riferimento al bottone di conferma.
     *
     * @return il bottone per confermare e giocare.
     */
    public JButton getBottoneConferma() {
        return bottoneConferma;
    }

    /**
     * Restituisce il riferimento al bottone per tornare al menù.
     *
     * @return il bottone menù.
     */
    public JButton getBottoneMenu() {
        return bottoneMenu;
    }

    /**
     * Aggiorna lo stato del pannello ricevendo la notifica dal Model (pattern Observer).
     *
     * @param modello il modello da cui provengono gli aggiornamenti (GiocoBriscola).
     * @param arg     argomenti opzionali.
     */
    @Override
    public void update(Observable modello, Object arg) {
        GiocoBriscola gioco = (GiocoBriscola) modello;
        gioco.getPartitaCorrente();

    }

}