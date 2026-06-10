package it.com.jbriscola.model;

import javax.swing.Timer;
import java.util.*;

/**
 * Classe che rappresenta la singola partita di Briscola.
 * Gestisce lo stato del gioco, i giocatori, le carte e le regole della partita.
 */
public class PartitaBriscola extends Observable {

    public enum StatoPartita {IN_CORSO, TERMINATA, PERSA, VINTA, PAREGGIO}

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
    private boolean isAlleati;

    private List<Carta> carteSulTavolo;

    /**
     * Costruttore della classe PartitaBriscola.
     * Inizializza una nuova partita, distribuendo le carte e impostando lo stato iniziale.
     *
     * @param gioco     Il riferimento al modello principale del gioco.
     * @param giocatore L'umano che partecipa alla partita.
     */
    public PartitaBriscola(GiocoBriscola gioco, Umano giocatore) {
        this.gioco = gioco;

        // Mazzo
        mazzo = Mazzo.getMazzo();
        this.briscola = mazzo.pesca();

        // Giocatori
        this.giocatore = giocatore;
        this.botAlleato = new Bot(Utils.estraiNome());
        this.botNemico1 = new Bot(Utils.estraiNome());
        this.botNemico2 = new Bot(Utils.estraiNome());

        // Setting Partita
        stato = StatoPartita.IN_CORSO;
        numeroTurno = new Random().nextInt(4);
        carteSulTavolo = new ArrayList<>(4);

        distribuisciCarte();

    }

    /**
     * Restituisce il riferimento al modello principale del gioco.
     *
     * @return il modello del gioco.
     */
    public GiocoBriscola getGioco() {
        return gioco;
    }

    /**
     * Imposta il riferimento al modello principale del gioco.
     *
     * @param gioco il nuovo modello del gioco da impostare.
     */
    public void setGioco(GiocoBriscola gioco) {
        this.gioco = gioco;
    }

    /**
     * Restituisce lo stato attuale della partita (IN_CORSO, TERMINATA, PERSA, VINTA).
     *
     * @return lo stato corrente della partita.
     */
    public StatoPartita getStato() {
        return stato;
    }

    /**
     * Restituisce il giocatore umano.
     *
     * @return il giocatore umano.
     */
    public Giocatore getGiocatore() {
        return giocatore;
    }


    /**
     * Restituisce il bot alleato del giocatore umano.
     *
     * @return il bot alleato.
     */
    public Giocatore getBotAlleato() {
        return botAlleato;
    }


    /**
     * Restituisce il primo bot nemico.
     *
     * @return il primo bot nemico.
     */
    public Giocatore getBotNemico1() {
        return botNemico1;
    }


    /**
     * Restituisce il secondo bot nemico.
     *
     * @return il secondo bot nemico.
     */
    public Giocatore getBotNemico2() {
        return botNemico2;
    }

    /**
     * Restituisce la carta che determina il seme di briscola per la partita.
     *
     * @return La carta briscola.
     */
    public Carta getBriscola() {
        return briscola;
    }

    /**
     * Restituisce il mazzo di carte utilizzato nella partita corrente.
     *
     * @return il mazzo della partita.
     */
    public Mazzo getMazzo() {
        return mazzo;
    }

    /**
     * Restituisce la somma dei punti del giocatore.
     *
     * @return il totale dei punti accumulati.
     */
    public int getPuntiGiocatore() {
        return puntiGiocatore;
    }

    /**
     * Restituisce la somma dei punti dei nemici.
     *
     * @return il totale dei punti accumulati.
     */
    public int getPuntiNemici() {
        return puntiNemici;
    }

    /**
     * Restituisce la lista delle carte attualmente sul tavolo.
     *
     * @return la lista delle carte sul tavolo.
     */
    public List<Carta> getCarteSulTavolo() {
        return carteSulTavolo;
    }

    /**
     * Restituisce la somma dei punti delle carte scartate in una mano
     *
     * @return somma dei punti delle carte sul tavolo
     */
    private int calcolaPuntiSulTavolo() {
        return carteSulTavolo.stream().mapToInt(Carta::getPuntiCarta).sum();
    }

    /**
     * Restituisce il numero del turno corrente (0 per il giocatore umano, 1-3 per i bot).
     *
     * @return il numero del turno.
     */
    public int getNumeroTurno() {
        return numeroTurno;
    }

    /**
     * Distribuisce le carte ad ogni giocatore partendo dal giocatore che deve iniziare il turno
     * Se è il primo turno (giocatore ha mano Null) deve distribuire 3 carte altrimenti 1 sola
     */
    private void distribuisciCarte() {
        int nGiocatore = numeroTurno;
        for (int i = 0; i < 4; i++) {
            Giocatore g = getGiocatoreDalTurno(nGiocatore);
            if (g.getMano() == null || g.getMano().isEmpty()) g.setMano(mazzo.pescaCarte());
            else {
                if (!mazzo.isEmpty()) g.pesca(mazzo.pesca());
                else g.pesca(briscola);
            }
            nGiocatore = (nGiocatore + 1) % 4;
        }
    }

    /**
     * Un giocatore scarta una carta. La carta viene aggiunta al tavolo, il turno avanza,
     * e se ci sono 4 carte sul tavolo, la presa viene risolta.
     *
     * @param giocatore il giocatore che scarta la carta.
     * @param carta     la carta che viene scartata.
     */
    public void scarta(Giocatore giocatore, Carta carta) {
        giocatore.getMano().remove(carta);
        carteSulTavolo.add(carta);
        giocatore.setCartaScartata(carta);

        // Determina se la carta giocata supera la carta attualmente in testa
        boolean supera = (cartaComanda == null) ||
                (carta.getSeme() == briscola.getSeme() && (cartaComanda.getSeme() != briscola.getSeme() || carta.getForzaCarta() > cartaComanda.getForzaCarta())) ||
                (carta.getSeme() == cartaComanda.getSeme() && carta.getForzaCarta() > cartaComanda.getForzaCarta());
        if (supera) {
            cartaComanda = carta;
            isAlleati = isAlleato(giocatore);
        }

        // Avanza il turno in modo circolare tra i 4 giocatori (0, 1, 2, 3, poi torna a 0)
        numeroTurno = (numeroTurno + 1) % 4;

        setChanged();
        notifyObservers();

        // Controlla se la presa è conclusa (4 carte sul tavolo)
        if (carteSulTavolo.size() == 4) {
            // Utilizziamo un javax.swing.Timer al posto di Thread.sleep per evitare di bloccare l'interfaccia grafica
            Timer t = new Timer(2000, e -> risolviPresa());
            t.setRepeats(false);
            t.start();
        } else {
            // Se la presa non è finita e il turno corrente NON è dell'umano (0), fai giocare i bot
            if (numeroTurno != 0 && stato == StatoPartita.IN_CORSO) {
                eseguiTurniBot();
            }
        }
    }

    /**
     * Manda in esecuzione i turni sequenziali dei bot finché non torna il turno dell'umano
     * o finché la presa non è terminata.
     */
    public void eseguiTurniBot() {
        // Utilizzo un Timer di Swing per non bloccare l'interfaccia grafica.
        // In questo modo, diamo tempo all'utente di vedere le mosse e la View può aggiornarsi.
        Timer timer = new Timer(2000, e -> {
            if (numeroTurno != 0 && carteSulTavolo.size() < 4) {
                Giocatore botDiTurno = getGiocatoreDalTurno(numeroTurno);

                Carta cartaScelta = sceltaCarta(botDiTurno);
                scarta(botDiTurno, cartaScelta);

            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Metodo che gestisce la scelta della carta da scartare per il Bot secondo una "Intelligenza Artificiale" base.
     * A seconda della carta che comanda sul tavolo e alle carte che ha in mano il bot,
     * la scelta della carta da scartare può essere tra "Liscio", "Carico", "Briscola grande", "Briscola piccola" oppure
     * una carta dello stesso seme ma con una forza maggiore.
     *
     * @param botDiTurno Giocatore che deve scartare la carta
     * @return Carta scartata dal bot
     */
    private Carta sceltaCarta(Giocatore botDiTurno) {
        List<Carta> mano = botDiTurno.getMano();
        if (mano.isEmpty()) return null;

        // Ordino la mano in base alla forza
        List<Carta> manoOrdinata = mano.stream()
                .sorted(Comparator.comparingInt(Carta::getForzaCarta))
                .toList();

        List<Carta> carichi = new ArrayList<>();
        List<Carta> punti = new ArrayList<>();
        List<Carta> lisci = new ArrayList<>();
        List<Carta> briscoloneUtili = new ArrayList<>();
        List<Carta> briscolineUtili = new ArrayList<>();
        List<Carta> briscoleInutili = new ArrayList<>();
        List<Carta> superaComanda = new ArrayList<>();

        // Per ogni carta in mano la inserisco in una delle liste qui sopra
        for (Carta c : manoOrdinata) {
            boolean isBriscola = c.getSeme() == briscola.getSeme();
            int puntiCarta = c.getPuntiCarta();

            boolean batteComanda = cartaComanda == null ||
                    (isBriscola && cartaComanda.getSeme() != briscola.getSeme()) ||
                    (c.getSeme() == cartaComanda.getSeme() && c.getForzaCarta() > cartaComanda.getForzaCarta());

            if (isBriscola) {
                if (batteComanda) {
                    if (puntiCarta >= 10) briscoloneUtili.add(c);
                    else briscolineUtili.add(c);
                } else briscoleInutili.add(c);
            } else {
                if (puntiCarta >= 10) carichi.add(c);
                else if (puntiCarta > 0) punti.add(c);
                else lisci.add(c);

                if (cartaComanda != null && c.getSeme() == cartaComanda.getSeme() && batteComanda) {
                    superaComanda.add(c);
                }
            }
        }

        boolean giocaAFavore = (isAlleato(botDiTurno) && isAlleati) || (!isAlleato(botDiTurno) && !isAlleati);
        int puntiTavolo = calcolaPuntiSulTavolo();

        boolean tavolataPesante = puntiTavolo >= 9 && !giocaAFavore;
        boolean tavolataMedia = (puntiTavolo >= 5 && puntiTavolo < 9) && !giocaAFavore;

        boolean caricaSicura = false;
        boolean puntiSicuri = false;

        // Se il bot è l'ultimo del turno e la giocata è a favore allora posso caricare o mettere punti
        if (cartaComanda != null && giocaAFavore) {
            caricaSicura = (carteSulTavolo.size() == 3) ||
                    (cartaComanda.getSeme() == briscola.getSeme() && cartaComanda.getForzaCarta() >= 6);
            puntiSicuri = (carteSulTavolo.size() == 3) ||
                    (cartaComanda.getSeme() == briscola.getSeme() && cartaComanda.getForzaCarta() < 6);
        }

        // Se la mano non è di alleati ma posso superare la carta
        if (!giocaAFavore && !superaComanda.isEmpty()) {
            return superaComanda.getLast();
        }
        // Se posso caricare sicuro ed ho un carico in mano
        if (caricaSicura && !carichi.isEmpty()) {
            return carichi.getLast();
        }
        // Se ci sono molti punti sul tavolo ed ho una briscola grande
        if (tavolataPesante && !briscoloneUtili.isEmpty()) {
            return briscoloneUtili.getLast();
        }
        // Se c'è qualche punto sul tabolo ed ho una briscola piccola
        if (tavolataMedia && !briscolineUtili.isEmpty()) {
            return briscolineUtili.getFirst();
        }
        if ((tavolataMedia || tavolataPesante) && cartaComanda.getSeme() != briscola.getSeme()) {
            if (!briscoleInutili.isEmpty()) return briscoleInutili.getFirst();
            else if (!briscolineUtili.isEmpty()) return briscolineUtili.getFirst();
            else if (!briscoloneUtili.isEmpty()) return briscoloneUtili.getFirst();
        }
        // Se sono l'ultimo a giocare con la mano di alleati ed ho punti in mano
        if (puntiSicuri && !punti.isEmpty()) {
            return punti.getLast();
        }

        // Nel caso nessuno delle precedenti condizioni è vera allora devo scartare la carta con il minimo danno
        if (!lisci.isEmpty()) return lisci.getFirst();
        if (!punti.isEmpty()) return punti.getFirst();
        if (!briscoleInutili.isEmpty()) return briscoleInutili.getFirst();
        if (!briscolineUtili.isEmpty()) return briscolineUtili.getFirst();
        if (!briscoloneUtili.isEmpty()) return briscoloneUtili.getFirst();
        if (!carichi.isEmpty()) return carichi.getFirst();

        return null;
    }

    /**
     * Restituisce il giocatore corrispondente al turno attuale
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
     * Restituisce True o False a seconda se il giocatore è un alleato
     *
     * @param giocatore
     * @return True o False
     */
    private boolean isAlleato(Giocatore giocatore) {
        return giocatore.equals(botAlleato) || giocatore.equals(this.giocatore);
    }

    /**
     * Risolve la presa corrente quando ci sono 4 carte sul tavolo.
     * Stabilisce il vincitore, assegna i punti e ripulisce il tavolo.
     */
    private void risolviPresa() {
        int giocatoreWin = 0;
        int sommaPunti = 0;

        // trovo il giocatore che ha vinto la mano
        for (int i = 0; i < 4; i++) {
            Carta cartaScartata = getGiocatoreDalTurno(i).getCartaScartata();
            sommaPunti += cartaScartata.getPuntiCarta();
            if (cartaComanda.equals(cartaScartata)) {
                giocatoreWin = i;
                break;
            }
        }

        // Calcolo i putni
        if (giocatoreWin == 0 || giocatoreWin == 2) this.puntiGiocatore += sommaPunti;
        else this.puntiNemici += sommaPunti;

        carteSulTavolo.clear();
        cartaComanda = null;
        numeroTurno = giocatoreWin;

        // Controllo se la partita è finita
        if (mazzo.isEmpty()) {
            if (getGiocatoreDalTurno(numeroTurno).getMano().isEmpty()) {
                Mazzo.close();
                if ((puntiGiocatore > 60 || puntiNemici > 60 || (puntiGiocatore == 60 && puntiNemici == 60))) {
                    if (puntiNemici > puntiGiocatore) stato = StatoPartita.PERSA;
                    else if (puntiNemici == puntiGiocatore) stato = StatoPartita.PAREGGIO;
                    else stato = StatoPartita.VINTA;
                    gioco.terminaPartita();
                } else {
                    // Cambio il mazzo
                    mazzo = Mazzo.getMazzo();
                    this.briscola = mazzo.pesca();

                    distribuisciCarte();

                    numeroTurno = (numeroTurno + 1) % 4;
                }
            }
        } else distribuisciCarte();


        setChanged();
        notifyObservers();

        // Se il giocatore che ha vinto la presa (che deve iniziare la mano successiva) 
        // è un bot, inneschiamo immediatamente i suoi turni
        if (numeroTurno != 0 && stato == StatoPartita.IN_CORSO) {
            eseguiTurniBot();
        }
    }
}