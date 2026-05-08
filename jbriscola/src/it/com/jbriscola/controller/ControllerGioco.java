package it.com.jbriscola.controller;

import it.com.jbriscola.model.Giocatore;
import it.com.jbriscola.model.GiocoBriscola;
import it.com.jbriscola.model.Umano;
import it.com.jbriscola.view.FinestraGioco;
import it.com.jbriscola.view.Pannello;
import it.com.jbriscola.view.Pannello.TipoPannello;

@SuppressWarnings("deprecation")
public class ControllerGioco {
    private final GiocoBriscola model;
    private final FinestraGioco vista;

    /**
     * Costruttore del Controller. Collega il Model e la View, inizializzando
     * gli observer e i listener per i bottoni dell'interfaccia grafica.
     * Implementa il pattern Observer per disaccoppiare Model e View.
     * Complessità computazionale: O(1) per l'impostazione, ma l'aggiunta di listener
     * è costante per ogni componente.
     *
     * @param gioco Il modello (Model) del gioco.
     * @param finestra La finestra principale (View) dell'applicazione.
     */
    public ControllerGioco(GiocoBriscola gioco, FinestraGioco finestra) {
        model = gioco;
        vista = finestra;

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

    private void inizializzaBottoniMenu() {
        vista.getPannelloMenu().getBottoneGioco().addActionListener(e -> {
            cambiaSchermata(TipoPannello.GIOCATORE);
        });

        vista.getPannelloMenu().getBottoneStatistiche()
                .addActionListener(e -> cambiaSchermata(TipoPannello.STATISTICHE));

    }

    private void inizializzaBottoniStatistiche() {
        vista.getPannelloStatistiche().getBottoneMenu()
                .addActionListener(e -> cambiaSchermata(TipoPannello.MENU));
    }

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

    private void inizializzaPartita(Giocatore giocatore) {

        // una eventuale partita già in corso viene sovrascritta
        vista.getPannelloGioco().ifPresent(model::deleteObserver);

        model.iniziaPartita(giocatore);
        
        // Evitiamo chiamate nude al .get() di un Optional: si usa ifPresent
        model.getPartitaCorrente().ifPresent(nuovaPartita -> {
            vista.setPannelloGioco(
                    nuovaPartita.getGiocatore(),
                    nuovaPartita.getBotNemico1(),
                    nuovaPartita.getBotAlleato(),
                    nuovaPartita.getBotNemico2()
            );

            vista.getPannelloGioco().ifPresent(nuovaPartita::addObserver);
            
            // Abilita esplicitamente il bottone se il turno iniziale è zero (Umano)
            if (nuovaPartita.getNumeroTurno() == 0) {
                vista.getPannelloGioco().ifPresent(p -> p.getBottoneConferma().setEnabled(true));
            }
            
            // Innesca i turni dei bot nel caso in cui il Random iniziale non abbia scelto l'Umano (0)
            nuovaPartita.eseguiTurniBot();
        });
    }

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
     * Complessità computazionale: O(1), le operazioni di Swing sono considerate costanti.
     *
     * @param schermata il TipoPannello del pannello da visualizzare.
     */
    public void cambiaSchermata(TipoPannello schermata) {
        vista.visualizzaPannello(schermata);
        vista.repaint();
    }
}
