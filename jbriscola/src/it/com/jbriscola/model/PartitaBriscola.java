package it.com.jbriscola.model;

/**
 * Classe che rappresenta la singola partita
 */
public class PartitaBriscola {
    enum StatoPartita { IN_CORSO, TERMINATA}

    private GiocoBriscola gioco;
    private StatoPartita stato;
    private Mazzo mazzo;
    private Giocatore giocatore;
    private Giocatore botAlleato;
    private Giocatore botNemico1;
    private Giocatore botNemico2;
    private int punti;


}
