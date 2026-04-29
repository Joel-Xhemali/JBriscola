package it.com.jbriscola.model;

import java.util.List;

/**
 * Classe astratta che rappresenta un giocatore che sia esso Umano o Artificiale
 */
public abstract class Giocatore {
    private String nome;
    private List<Carta> mano;

    public Giocatore(String nome, List<Carta> mano) {
        this.nome = nome;
        this.mano = mano;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Carta> getMano() {
        return mano;
    }

    public void setMano(List<Carta> mano) {
        this.mano = mano;
    }
}
