package it.com.jbriscola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Classe che rappresenta la singola partita
 */
public class PartitaBriscola extends Observable {

    public enum StatoPartita {IN_CORSO, TERMINATA, PERSA, VINTA}

    private GiocoBriscola gioco;
    private StatoPartita stato;
    private Mazzo mazzo;
    private Giocatore giocatore;
    private Giocatore botAlleato;
    private Giocatore botNemico1;
    private Giocatore botNemico2;
    private int punti;

    private List<Carta> carteSulTavolo;

    public PartitaBriscola(GiocoBriscola gioco, Umano giocatore) {
        this.gioco = gioco;

        // Mazzo
        mazzo = Mazzo.getMazzo();

        // Giocatori
        this.giocatore = giocatore;
        giocatore.setMano(mazzo.pescaCarte());
        this.botAlleato = new Bot(Utils.estraiNome(), mazzo.pescaCarte());
        this.botNemico1 = new Bot(Utils.estraiNome(), mazzo.pescaCarte());
        this.botNemico2 = new Bot(Utils.estraiNome(), mazzo.pescaCarte());

        // Setting Partita
        stato = StatoPartita.IN_CORSO;
        punti = 0;
        carteSulTavolo = new ArrayList<>(4);
    }

    public GiocoBriscola getGioco() {
        return gioco;
    }

    public void setGioco(GiocoBriscola gioco) {
        this.gioco = gioco;
    }

    public StatoPartita getStato() {
        return stato;
    }

    public Giocatore getGiocatore() {
        return giocatore;
    }


    public Giocatore getBotAlleato() {
        return botAlleato;
    }


    public Giocatore getBotNemico1() {
        return botNemico1;
    }


    public Giocatore getBotNemico2() {
        return botNemico2;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }

    public void scarta(Giocatore giocatore, Carta carta){
        giocatore.getMano().remove(carta);
        carteSulTavolo.add(carta);

        // [Qui in futuro implementerai la logica: siamo a 4 carte? Chi vince la presa?]

        setChanged();
        notifyObservers();
    }
}
