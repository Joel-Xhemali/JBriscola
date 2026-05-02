package it.com.jbriscola.controller;

import it.com.jbriscola.model.*;
import it.com.jbriscola.view.FinestraGioco;
import it.com.jbriscola.view.Pannello;
import it.com.jbriscola.view.Pannello.TipoPannello;
import it.com.jbriscola.view.PannelloGioco;

import java.util.List;
import java.util.Optional;

public class ControllerGioco {
    private GiocoBriscola model;
    private FinestraGioco vista;

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
                    Giocatore giocatore = new Umano(vista.getPannelloGiocatore().getNickname(), vista.getPannelloGiocatore().getPathAvatarSelezionato());
                    inizializzaPartita(giocatore);
                    inizializzaBottoniGioco();
                    cambiaSchermata(TipoPannello.GIOCO);
                });
    }

    private void inizializzaPartita(Giocatore giocatore) {

        // una eventuale partita già in corso viene sovrascritta
        Optional<PannelloGioco> vecchio = vista.getPannelloGioco();
        if (vecchio.isPresent()) {
            model.deleteObserver(vecchio.get());
//            model.deleteObserver(vecchio.get().getPannelloVittoria());
//            model.deleteObserver(vecchio.get().getPannelloSconfitta());
        }

        model.iniziaPartita(giocatore);
        PartitaBriscola nuovaPartita = model.getPartitaCorrente().get();
        vista.setPannelloGioco(model.getPartitaCorrente().get().getGiocatore(),
                model.getPartitaCorrente().get().getBotAlleato(),
                model.getPartitaCorrente().get().getBotNemico1(),
                model.getPartitaCorrente().get().getBotNemico2());

        /*
         * il pannello della partita e i pannelli per mostrare il suo esito (vittoria o
         * sconfitta) sono registrati come observer del modello del gioco (i pannelli
         * esito riportano le statistiche delle partite precedenti)
         */
//        model.addObserver(vista.getPannelloGioco().get());
        nuovaPartita.addObserver(vista.getPannelloGioco().get());
//        model.addObserver(vista.getPannelloGioco().get().getPannelloVittoria());
//        model.addObserver(vista.getPannelloGioco().get().getPannelloSconfitta());
    }

    private void inizializzaBottoniGioco() {

        /*
         * qui non ci si preoccupa di gestire eventuali errori di Optional vuoto perché
         * il metodo è privato e chiamato solo dopo l'inizializzazione di una partita (e
         * la creazione di un pannello gioco)
         */
        PannelloGioco p = vista.getPannelloGioco().get();

        p.getBottoneMenu().addActionListener(e -> cambiaSchermata(Pannello.TipoPannello.MENU));

        p.getBottoneConferma().addActionListener(e -> {
            if (p.getCartaSelezionata() != null) {
                // 1. Il controller passa l'ordine alla partita corrente
                PartitaBriscola partita = model.getPartitaCorrente().get();

                partita.scarta(partita.getGiocatore(), p.getCartaSelezionata());
            }
        });
        /*
         * al clic di un bottone con una lettera, essa viene aggiunta alle lettere usate
         * nel modello
         */
//        p.getBottoniLettere().forEach(
//                (c, b) -> b.addActionListener(e -> modello.getPartitaCorrente().get().aggiungiLetteraUsata(c)));

        /*
         * al clic del bottone "Nuova parola" in un pannello esito (vittoria /
         * sconfitta) si crea una nuova partita e un nuovo pannello gioco
         */
//        for (PannelloEsito x : List.of(p.getPannelloVittoria(), p.getPannelloSconfitta())) {
//            x.getBottoneNuovaParola().addActionListener(e -> {
//                inizializzaPartita();
//                inizializzaBottoniGioco();
//                cambiaSchermata(Pannello.TipoPannello.GIOCO);
//            });
//        }
    }

    /**
     * Metodo per visualizzare una determinata schermata del Gioco
     *
     * @param schermata del pannello da visualizzare
     */
    public void cambiaSchermata(TipoPannello schermata) {
        vista.visualizzaPannello(schermata);
        vista.repaint();
    }
}
