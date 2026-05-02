package it.com.jbriscola.model;

import java.util.List;

public class Umano extends Giocatore{
    public Umano(String nome, String avatar, List<Carta> mano) {
        super(nome, avatar, mano);
    }

    public Umano(String nome, String avatar){
        this(nome, avatar,null);
    }
}
