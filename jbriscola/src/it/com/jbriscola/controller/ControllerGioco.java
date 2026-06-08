package it.com.jbriscola.controller;

import it.com.jbriscola.model.*;
import it.com.jbriscola.view.FinestraGioco;
import it.com.jbriscola.view.Pannello;
import it.com.jbriscola.view.Pannello.TipoPannello;

import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class ControllerGioco implements Observer {
    private final GiocoBriscola model;
    private final FinestraGioco vista;

    /**
     * Costruttore del Controller. Collega il Model e la View, inizializzando
     * gli observer e i listener per i bottoni dell'interfaccia grafica.
     * Implementa il pattern Observer per disaccoppiare Model e View.
     *
     * @param gioco    Il modello (Model) del gioco.
     * @param finestra La finestra principale (View) dell'applicazione.
     */
    public ControllerGioco(GiocoBriscola gioco, FinestraGioco finestra) {
        model = gioco;
        vista = finestra;
        try {
            AudioManager.getInstance().play("assets/1950s Jazz Classics.wav");
        } catch (Exception e) {
            System.out.println("Error playing Jazz Classics.wav");
        }

        /*
         * il pannello statistiche deve osservare il modello del gioco per avere
         * conteggi aggiornati di partite vinte e giocate
         */
        model.addObserver(vista.getPannelloStatistiche());
        model.addObserver(vista.getPannelloGiocatore());

        inizializzaBottoniMenu();
        inizializzaBottoniStatistiche();
        inizializzaBottoniGiocatore();
    }

    /**
     * Metodo di inizializzazione bottoni pannello menù
     */
    private void inizializzaBottoniMenu() {
        vista.getPannelloMenu().getBottoneGioco().addActionListener(e -> {
            cambiaSchermata(TipoPannello.GIOCATORE);
        });

        vista.getPannelloMenu().getBottoneStatistiche()
                .addActionListener(e -> cambiaSchermata(TipoPannello.STATISTICHE));

    }

    /**
     * Metodo di inizializzazione bottoni pannello statistiche
     */
    private void inizializzaBottoniStatistiche() {
        vista.getPannelloStatistiche().getBottoneMenu()
                .addActionListener(e -> cambiaSchermata(TipoPannello.MENU));
    }

    /**
     * Metodo di inizializzazione bottoni pannello giocatore
     */
    private void inizializzaBottoniGiocatore() {
        vista.getPannelloGiocatore().getBottoneMenu()
                .addActionListener(e -> cambiaSchermata(TipoPannello.MENU));

        vista.getPannelloGiocatore().getBottoneConferma()
                .addActionListener(e -> {
                    var giocatore = new Umano(vista.getPannelloGiocatore().getNickname(), vista.getPannelloGiocatore().getPathAvatarSelezionato());
                    inizializzaPartita(giocatore);
                    inizializzaBottoniGioco();
                    cambiaSchermata(TipoPannello.GIOCO);
                });
    }

    /**
     * Metodo di inizializzazione Partita (Model e View)
     */
    private void inizializzaPartita(Giocatore giocatore) {

        // una eventuale partita già in corso viene sovrascritta
        vista.getPannelloGioco().ifPresent(model::deleteObserver);

        model.iniziaPartita(giocatore);

        model.getPartitaCorrente().ifPresent(nuovaPartita -> {
            vista.setPannelloGioco(
                    nuovaPartita.getGiocatore(),
                    nuovaPartita.getBotNemico1(),
                    nuovaPartita.getBotAlleato(),
                    nuovaPartita.getBotNemico2()
            );

            vista.getPannelloGioco().ifPresent(p -> {
                // Aggiunge l'observer
                nuovaPartita.addObserver(p);

                // Aggiungiamo anche il Controller stesso come observer per monitorare la fine della partita
                nuovaPartita.addObserver(this);

                // Forza l'aggiornamento manuale della View per mostrare SUBITO la briscola e il mazzo a inizio partita
                p.update(nuovaPartita, null);

                // Abilita esplicitamente il bottone se il turno iniziale è zero (Umano)
                if (nuovaPartita.getNumeroTurno() == 0) {
                    p.getBottoneConferma().setEnabled(true);
                }
            });

            // Innesca i turni dei bot nel caso in cui il Random iniziale non abbia scelto l'Umano (0)
            nuovaPartita.eseguiTurniBot();
        });
    }

    /**
     * Metodo di inizializzazione bottoni pannello gioco
     */
    private void inizializzaBottoniGioco() {

        /*
         * Eseguiamo il blocco in sicurezza usando l'operatore funzionale ifPresent
         */
        vista.getPannelloGioco().ifPresent(p -> {
            p.getBottoneMenu().addActionListener(e -> cambiaSchermata(Pannello.TipoPannello.MENU));

            p.getBottoneConferma().addActionListener(e -> {
                var cartaSelezionata = p.getCartaSelezionata();
                if (cartaSelezionata != null) {
                    model.getPartitaCorrente().ifPresent(partita -> {
                        // Disabilita immediatamente il bottone per impedire input doppi 
                        // mentre il model esegue lo scarto e passa il turno al botNemico1
                        p.getBottoneConferma().setEnabled(false);

                        partita.scarta(partita.getGiocatore(), cartaSelezionata);
                    });
                }
            });
        });

    }

    /**
     * Metodo per visualizzare una determinata schermata del Gioco.
     * Utilizza il CardLayout della FinestraGioco per mostrare il pannello corretto.
     *
     * @param schermata il TipoPannello del pannello da visualizzare.
     */
    public void cambiaSchermata(TipoPannello schermata) {
        vista.visualizzaPannello(schermata);
        vista.repaint();
    }

    /**
     * Il controller ascolta i cambiamenti della partita per poter reagire
     * ad un eventuale stato di VINTA o PERSA e reindirizzare alla view Statistiche.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof PartitaBriscola partita) {
            if (partita.getStato() == PartitaBriscola.StatoPartita.VINTA ||
                    partita.getStato() == PartitaBriscola.StatoPartita.PERSA) {

                // La partita è finita, segnaliamo al modello principale (GiocoBriscola) di aggiornare le stats
                model.terminaPartita();

                // Usiamo un piccolo ritardo per far vedere l'ultima presa prima di cambiare schermata
                javax.swing.Timer timer = new javax.swing.Timer(1500, e -> {
                    cambiaSchermata(TipoPannello.STATISTICHE);
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
}
