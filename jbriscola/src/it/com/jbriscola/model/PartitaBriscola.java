package it.com.jbriscola.model;

/**
 * Classe che rappresenta la singola partita
 */
public class PartitaBriscola {

    public enum StatoPartita {IN_CORSO, TERMINATA, PERSA, VINTA}

    private GiocoBriscola gioco;
    private StatoPartita stato;
    private Mazzo mazzo;
    private Giocatore giocatore;
    private Giocatore botAlleato;
    private Giocatore botNemico1;
    private Giocatore botNemico2;
    private int punti;

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

    public void setStato(StatoPartita stato) {
        this.stato = stato;
    }

    public Mazzo getMazzo() {
        return mazzo;
    }

    public void setMazzo(Mazzo mazzo) {
        this.mazzo = mazzo;
    }

    public Giocatore getGiocatore() {
        return giocatore;
    }

    public void setGiocatore(Giocatore giocatore) {
        this.giocatore = giocatore;
    }

    public Giocatore getBotAlleato() {
        return botAlleato;
    }

    public void setBotAlleato(Giocatore botAlleato) {
        this.botAlleato = botAlleato;
    }

    public Giocatore getBotNemico1() {
        return botNemico1;
    }

    public void setBotNemico1(Giocatore botNemico1) {
        this.botNemico1 = botNemico1;
    }

    public Giocatore getBotNemico2() {
        return botNemico2;
    }

    public void setBotNemico2(Giocatore botNemico2) {
        this.botNemico2 = botNemico2;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }

    public String getParola() {
        return null;
    }

}
