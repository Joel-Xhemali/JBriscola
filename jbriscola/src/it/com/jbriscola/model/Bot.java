package it.com.jbriscola.model;

import java.util.List;

/**
 * Classe
 */
public class Bot extends Giocatore {
    public Bot(String nome, String avatar, List<Carta> mano) {
        super(nome, avatar, mano);
    }

    public Bot(String nome, List<Carta> mano) {
        this(nome, Utils.getPathAvatar(), mano);
    }

}
