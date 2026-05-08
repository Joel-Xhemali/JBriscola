package it.com.jbriscola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * Classe che rappresenta la singola partita di Briscola.
 * Gestisce lo stato del gioco, i giocatori, le carte e le regole della partita.
 */
public class PartitaBriscola extends Observable {

    public enum StatoPartita {IN_CORSO, TERMINATA, PERSA, VINTA}

    private GiocoBriscola gioco;
    private StatoPartita stato;
    private Mazzo mazzo;
    private Carta briscola;
    private Carta cartaComanda;
    private Giocatore giocatore;
    private Giocatore botAlleato;
    private Giocatore botNemico1;
    private Giocatore botNemico2;
    private int puntiGiocatore;
    private int puntiNemici;
    private int numeroTurno;

    private List<Carta> carteSulTavolo;

    /**
     * Costruttore della classe PartitaBriscola.
     * Inizializza una nuova partita, distribuendo le carte e impostando lo stato iniziale.
     * Complessità computazionale: O(1) per l'inizializzazione e la distribuzione delle carte (numero fisso di operazioni).
     *
     * @param gioco Il riferimento al modello principale del gioco.
     * @param giocatore L'umano che partecipa alla partita.
     */
    public PartitaBriscola(GiocoBriscola gioco, Umano giocatore) {
        this.gioco = gioco;

        // Mazzo
        mazzo = Mazzo.getMazzo();
        this.briscola = mazzo.pesca();

        // Giocatori
        this.giocatore = giocatore;
        giocatore.setMano(mazzo.pescaCarte());
        this.botAlleato = new Bot(Utils.estraiNome(), mazzo.pescaCarte());
        this.botNemico1 = new Bot(Utils.estraiNome(), mazzo.pescaCarte());
        this.botNemico2 = new Bot(Utils.estraiNome(), mazzo.pescaCarte());

        // Setting Partita
        stato = StatoPartita.IN_CORSO;
        numeroTurno = new Random().nextInt(4);
        carteSulTavolo = new ArrayList<>(4);
    }

    /**
     * Restituisce il riferimento al modello principale del gioco.
     * Complessità computazionale: O(1).
     *
     * @return il modello del gioco.
     */
    public GiocoBriscola getGioco() {
        return gioco;
    }

    /**
     * Imposta il riferimento al modello principale del gioco.
     * Complessità computazionale: O(1).
     *
     * @param gioco il nuovo modello del gioco da impostare.
     */
    public void setGioco(GiocoBriscola gioco) {
        this.gioco = gioco;
    }

    /**
     * Restituisce lo stato attuale della partita (IN_CORSO, TERMINATA, PERSA, VINTA).
     * Complessità computazionale: O(1).
     *
     * @return lo stato corrente della partita.
     */
    public StatoPartita getStato() {
        return stato;
    }

    /**
     * Restituisce il giocatore umano.
     * Complessità computazionale: O(1).
     *
     * @return il giocatore umano.
     */
    public Giocatore getGiocatore() {
        return giocatore;
    }


    /**
     * Restituisce il bot alleato del giocatore umano.
     * Complessità computazionale: O(1).
     *
     * @return il bot alleato.
     */
    public Giocatore getBotAlleato() {
        return botAlleato;
    }


    /**
     * Restituisce il primo bot nemico.
     * Complessità computazionale: O(1).
     *
     * @return il primo bot nemico.
     */
    public Giocatore getBotNemico1() {
        return botNemico1;
    }


    /**
     * Restituisce il secondo bot nemico.
     * Complessità computazionale: O(1).
     *
     * @return il secondo bot nemico.
     */
    public Giocatore getBotNemico2() {
        return botNemico2;
    }

    /**
     * Restituisce la carta che determina il seme di briscola per la partita.
     * Complessità computazionale: O(1).
     *
     * @return La carta briscola.
     */
    public Carta getBriscola() {
        return briscola;
    }

    /**
     * Restituisce la somma dei punti del giocatore e dei nemici.
     * Complessità computazionale: O(1).
     *
     * @return il totale dei punti accumulati.
     */
    public int getPunti() {
        return puntiGiocatore + puntiNemici;
    }

    /**
     * Restituisce la lista delle carte attualmente sul tavolo.
     * Complessità computazionale: O(1).
     *
     * @return la lista delle carte sul tavolo.
     */
    public List<Carta> getCarteSulTavolo() {
        return carteSulTavolo;
    }

    /**
     * Restituisce il numero del turno corrente (0 per il giocatore umano, 1-3 per i bot).
     * Complessità computazionale: O(1).
     *
     * @return il numero del turno.
     */
    public int getNumeroTurno() {
        return numeroTurno;
    }

    /**
     * Un giocatore scarta una carta. La carta viene aggiunta al tavolo, il turno avanza,
     * e se ci sono 4 carte sul tavolo, la presa viene risolta.
     * Complessità computazionale: O(1) per le operazioni di lista e aggiornamento stato.
     *
     * @param giocatore il giocatore che scarta la carta.
     * @param carta la carta che viene scartata.
     */
    public void scarta(Giocatore giocatore, Carta carta) {
        giocatore.getMano().remove(carta);
        carteSulTavolo.add(carta);

        if (cartaComanda == null || carta.getPuntiCarta() > cartaComanda.getPuntiCarta()) cartaComanda = carta;


        // Se la carta scartata è di briscola allora si aggiunge 1 al punteggio del giocatore
        // Esempio valore 4 ha punti 0.002 passa a 1.002 che è maggiore di qualsiasi altro valore non briscola
        // Puo essere superato però da una briscola più alta tipo valore 7 ha punti 0.005 passa a 1.005
        double puntiCarta = carta.getSeme() == briscola.getSeme() ? carta.getPuntiCarta() + 1 : carta.getPuntiCarta();
        giocatore.setPuntiTavolo(puntiCarta);

        // 1. Avanza il turno in modo circolare tra i 4 giocatori (0, 1, 2, 3, poi torna a 0)
        numeroTurno = (numeroTurno + 1) % 4;

        setChanged();
        notifyObservers();

        // 2. Controlla se la presa è conclusa (4 carte sul tavolo)
        if (carteSulTavolo.size() == 4) {
            risolviPresa();
        } else {
            // 3. Se la presa non è finita e il turno corrente NON è dell'umano (0), fai giocare i bot
            if (numeroTurno != 0 && stato == StatoPartita.IN_CORSO) {
                eseguiTurniBot();
            }
        }
    }

    /**
     * Manda in esecuzione i turni sequenziali dei bot finché non torna il turno dell'umano
     * o finché la presa non è terminata.
     * Complessità computazionale: O(1) in quanto il numero massimo di bot è fisso (3).
     */
    public void eseguiTurniBot() {
        // Utilizziamo un ciclo: in giochi a turni, se ci sono più bot consecutivi,
        // questo ciclo li farà giocare in automatico in frazioni di secondo.
        while (numeroTurno != 0 && carteSulTavolo.size() < 4) {
            Giocatore botDiTurno = getGiocatoreDalTurno(numeroTurno);

            // Intelligenza Artificiale base: gioca la prima carta disponibile nella mano
            if (!botDiTurno.getMano().isEmpty()) {

                Carta cartaScelta = botDiTurno.getMano().getFirst();
                // Il bot scarta la sua carta. Questo invocherà nuovamente scarta() in modo ricorsivo/sequenziale
                scarta(botDiTurno, cartaScelta);
            }
        }
    }

    /**
     * Restituisce il giocatore corrispondente al turno attuale utilizzando
     * lo switch expression di Java 21.
     * Complessità computazionale: O(1).
     *
     * @param turno il numero del turno (0-3).
     * @return il giocatore associato al turno.
     * @throws IllegalStateException se il valore del turno non è valido.
     */
    private Giocatore getGiocatoreDalTurno(int turno) {
        return switch (turno) {
            case 0 -> giocatore;
            case 1 -> botNemico1;
            case 2 -> botAlleato;
            case 3 -> botNemico2;
            default -> throw new IllegalStateException("Valore turno non valido: " + turno);
        };
    }

    /**
     * Risolve la presa corrente quando ci sono 4 carte sul tavolo.
     * Stabilisce il vincitore, assegna i punti e ripulisce il tavolo.
     * Complessità computazionale: O(1) in quanto opera su un numero fisso di carte (4).
     */
    private void risolviPresa() {
        double maxPunti = 0;
        int giocatoreWin = 0;
        for (int i = 0; i < 4; i++) {
            double giocatorePunti = getGiocatoreDalTurno(i).getPuntiTavolo();
            if (maxPunti < giocatorePunti) {
                maxPunti = giocatorePunti;
                giocatoreWin = i;
            }
        }

        carteSulTavolo.clear();
        numeroTurno = giocatoreWin;

        if (mazzo.isEmpty()) {
            if (puntiNemici > puntiGiocatore) stato = StatoPartita.PERSA;
            else stato = StatoPartita.VINTA;
        }

        setChanged();
        notifyObservers();

        // Se il giocatore che ha vinto la presa (che deve iniziare la mano successiva) 
        // è un bot, inneschiamo immediatamente le বহুম turni
        if (numeroTurno != 0 && stato == StatoPartita.IN_CORSO) {
            eseguiTurniBot();
        }
    }
}
