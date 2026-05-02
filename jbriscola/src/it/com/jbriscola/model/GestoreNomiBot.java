package it.com.jbriscola.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe che gestisce i nominativi dei bot
 */
public class GestoreNomiBot {

    private final String PERCORSO = "/assets/nomiBot.txt";
    private final List<String> nomiInMemoria;

    public GestoreNomiBot() {
        this.nomiInMemoria = new ArrayList<>();
        caricaDalClasspath();
    }

    /**
     * Legge e salva i nomi dal file assets/nomiBot.txt
     */
    private void caricaDalClasspath() {
        try (InputStream is = getClass().getResourceAsStream(PERCORSO)) {

            if (is == null) {
                // Se is è null, il file non è stato trovato nel classpath.
                throw new IllegalArgumentException("Risorsa non trovata nel classpath: " + PERCORSO);
            }

            // Usiamo InputStreamReader per convertire i byte in testo e BufferedReader per leggere le righe
            try (BufferedReader lettore = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = lettore.readLine()) != null) {
                    if (!linea.trim().isEmpty()) { // Ignoro righe vuote
                        nomiInMemoria.add(linea.trim());
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Errore critico durante la lettura del file dei nomi bot", e);
        }

        if (nomiInMemoria.isEmpty()) {
            throw new IllegalStateException("Il file dei nomi è stato trovato ma è vuoto. Impossibile avviare il gioco.");
        }
    }

    /**
     * Restituisce e rimuove un nome dall'elenco dei giocatori
     */
    public String ottieniNomeCasuale() {
        Random rand = new Random();
        int indiceCasuale = rand.nextInt(nomiInMemoria.size());
        return nomiInMemoria.remove(indiceCasuale);
    }
}
