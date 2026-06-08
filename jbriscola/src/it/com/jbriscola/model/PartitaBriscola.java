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
     * Complessità computazionale: O(1) per l'inizializzazione e la distribuzione delle carte (numero fisso di operazioni).
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
     * Restituisce il mazzo di carte utilizzato nella partita corrente.
     * Complessità computazionale: O(1).
     *
     * @return il mazzo della partita.
     */
    public Mazzo getMazzo() {
        return mazzo;
    }

    /**
     * Restituisce la somma dei punti del giocatore.
     * Complessità computazionale: O(1).
     *
     * @return il totale dei punti accumulati.
     */
    public int getPuntiGiocatore() {
        return puntiGiocatore;
    }

    /**
     * Restituisce la somma dei punti dei nemici.
     * Complessità computazionale: O(1).
     *
     * @return il totale dei punti accumulati.
     */
    public int getPuntiNemici() {
        return puntiNemici;
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

    private int calcolaPuntiSulTavolo() {
        return carteSulTavolo.stream().mapToInt(Carta::getPuntiCarta).sum();
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
     * Complessità computazionale: O(1) per le operazioni di lista e aggiornamento stato.
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

        // 1. Avanza il turno in modo circolare tra i 4 giocatori (0, 1, 2, 3, poi torna a 0)
        numeroTurno = (numeroTurno + 1) % 4;

        setChanged();
        notifyObservers();

        // 2. Controlla se la presa è conclusa (4 carte sul tavolo)
        if (carteSulTavolo.size() == 4) {
            // Utilizziamo un javax.swing.Timer al posto di Thread.sleep per evitare di bloccare l'interfaccia grafica
            Timer t = new Timer(2000, e -> risolviPresa());
            t.setRepeats(false);
            t.start();
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
        // Utilizziamo un Timer di Swing per non bloccare l'interfaccia grafica.
        // In questo modo, diamo tempo all'utente di vedere le mosse e la View può aggiornarsi.
        Timer timer = new Timer(2000, e -> {
            if (numeroTurno != 0 && carteSulTavolo.size() < 4) {
                Giocatore botDiTurno = getGiocatoreDalTurno(numeroTurno);

                Carta cartaScelta = sceltaCarta(botDiTurno);
                // Il bot scarta la sua carta. Questo invocherà nuovamente scarta()
                scarta(botDiTurno, cartaScelta);

            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private Carta sceltaCarta(Giocatore botDiTurno) {
        Carta cartaScelta = null;
        // Intelligenza Artificiale base: gioca la prima carta disponibile nella mano
        if (!botDiTurno.getMano().isEmpty()) {

            boolean giocaAFavore = (isAlleato(botDiTurno) && isAlleati) || (!isAlleato(botDiTurno) && !isAlleati);
            boolean briscolona = calcolaPuntiSulTavolo() >= 9 && !giocaAFavore;
            boolean briscolina = (5 <= calcolaPuntiSulTavolo() && calcolaPuntiSulTavolo() < 9) && !giocaAFavore;
            boolean carico = false;
            boolean punti = false;

            List<Carta> cartaSuperaComanda = null;
            if (cartaComanda != null) {
                carico = (carteSulTavolo.size()== 3 && giocaAFavore) ||
                            (cartaComanda.getSeme() == briscola.getSeme() && cartaComanda.getForzaCarta()>=6) && giocaAFavore;
                punti = (carteSulTavolo.size()== 3 && giocaAFavore) ||
                        (cartaComanda.getSeme() == briscola.getSeme() && cartaComanda.getForzaCarta()<6) && giocaAFavore;

                cartaSuperaComanda = botDiTurno.getMano().stream()
                        .filter(c -> c.getSeme() == cartaComanda.getSeme() && c.getSeme() != briscola.getSeme())
                        .filter(c -> c.getForzaCarta() > cartaComanda.getForzaCarta())
                        .sorted(Comparator.comparingInt(Carta::getForzaCarta))
                        .toList();
            }

            // Logica per identificare se il bot possiede carichi (punti >= 10: Assi o Tre) o briscole
            var carichiInMano = botDiTurno.getMano().stream()
                    .filter(c -> c.getPuntiCarta() >= 10)
                    .filter(c -> c.getSeme() != briscola.getSeme())
                    .sorted(Comparator.comparingInt(Carta::getForzaCarta))
                    .toList();

            var puntiInMano = botDiTurno.getMano().stream()
                    .filter(c -> c.getPuntiCarta() > 0 && c.getPuntiCarta() < 10)
                    .filter(c -> c.getSeme() != briscola.getSeme())
                    .sorted(Comparator.comparingInt(Carta::getForzaCarta))
                    .toList();

            var briscoloneInMano = botDiTurno.getMano().stream()
                    .filter(c -> c.getSeme() == briscola.getSeme())
                    .filter(c -> c.getPuntiCarta() >= 10)
                    .sorted(Comparator.comparingInt(Carta::getForzaCarta))
                    .toList();

            var briscolineInMano = botDiTurno.getMano().stream()
                    .filter(c -> c.getSeme() == briscola.getSeme())
                    .filter(c -> c.getPuntiCarta() < 10)
                    .sorted(Comparator.comparingInt(Carta::getForzaCarta))
                    .toList();

            var lisciInMano = botDiTurno.getMano().stream()
                    .filter(c -> c.getPuntiCarta() == 0)
                    .filter(c -> c.getSeme() != briscola.getSeme())
                    .sorted(Comparator.comparingInt(Carta::getForzaCarta))
                    .toList();

            if (!giocaAFavore && cartaSuperaComanda != null && !cartaSuperaComanda.isEmpty()) {
                // Se il bot ha una carta dello stesso seme ma con valore più grande
                cartaScelta = cartaSuperaComanda.getLast();
            } else if (carico && !carichiInMano.isEmpty()) {
                // Se il bot ha un carico e la situazione è favorevole (alleato comanda o colpo sicuro)
                cartaScelta = carichiInMano.getLast();
            } else if (briscolona && !briscoloneInMano.isEmpty()) {
                // Se ci sono molti punti a terra e non comanda l'alleato, prova a prendere con una briscola forte
                cartaScelta = briscoloneInMano.getLast();
            } else if (briscolina && !briscoloneInMano.isEmpty()) {
                // Se ci sono alcuni punti a terra e non comanda l'alleato, prova a prendere con una briscola piccola
                cartaScelta = briscolineInMano.getFirst();
            } else if (punti && !puntiInMano.isEmpty()) {
                // Se c'è una briscolina per terra e la situazione è favorevole
                cartaScelta = puntiInMano.getLast();
            } else {
                // Se non ho altra scelta procedo in ordine di grandezza
                if (!lisciInMano.isEmpty()) cartaScelta = lisciInMano.getFirst();
                else if (!puntiInMano.isEmpty()) cartaScelta = puntiInMano.getFirst();
                else if (!briscolineInMano.isEmpty()) cartaScelta = briscolineInMano.getFirst();
                else if (!briscoloneInMano.isEmpty()) cartaScelta = briscoloneInMano.getFirst();
                else if (!carichiInMano.isEmpty()) cartaScelta = carichiInMano.getFirst();
            }

        }
        return cartaScelta;
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

    private boolean isAlleato(Giocatore giocatore) {
        return giocatore.equals(botAlleato) || giocatore.equals(this.giocatore);
    }

    /**
     * Risolve la presa corrente quando ci sono 4 carte sul tavolo.
     * Stabilisce il vincitore, assegna i punti e ripulisce il tavolo.
     * Complessità computazionale: O(1) in quanto opera su un numero fisso di carte (4).
     */
    private void risolviPresa() {
        int giocatoreWin = 0;
        int sommaPunti = 0;
        for (int i = 0; i < 4; i++) {
            Carta cartaScartata = getGiocatoreDalTurno(i).getCartaScartata();
            sommaPunti += cartaScartata.getPuntiCarta();
            if (cartaComanda.equals(cartaScartata)) {
                giocatoreWin = i;
                break;
            }
        }

        if (giocatoreWin == 0 || giocatoreWin == 2) this.puntiGiocatore += sommaPunti;
        else this.puntiNemici += sommaPunti;

        carteSulTavolo.clear();
        cartaComanda = null;
        numeroTurno = giocatoreWin;

        if (mazzo.isEmpty()) {
            if (getGiocatoreDalTurno(numeroTurno).getMano().isEmpty()) {
                Mazzo.close();
                if ((puntiGiocatore > 60 || puntiNemici > 60 || (puntiGiocatore == 60 && puntiNemici == 60))) {
                    if (puntiNemici > puntiGiocatore) stato = StatoPartita.PERSA;
                    else if (puntiNemici == puntiGiocatore) stato = StatoPartita.PAREGGIO;
                    else stato = StatoPartita.VINTA;
                    gioco.terminaPartita();
                }else{
                    // Mazzo
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