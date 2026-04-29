package it.com.jbriscola.model;

import java.util.Observable;
import java.util.Optional;

public class GiocoBriscola extends Observable {
    private int partiteGiocate;
    private int partiteVinte;
    private Optional<PartitaBriscola> partitaCorrente;
}
