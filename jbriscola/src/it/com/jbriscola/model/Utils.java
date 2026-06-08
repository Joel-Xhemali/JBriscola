package it.com.jbriscola.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    public static final String PATH_AVATAR = "assets/avatar/";
    public static final String PATH_NOMI = "assets/nomiBot.txt";

    /**
     * Metodo che legge la cartella "assets/avatar" e restituisce un avatar casuale.
     * Utilizza Java Streams per estrarre casualmente un percorso valido.
     * Complessità computazionale: O(F) dove F è il numero di file presenti nella directory.
     *
     * @return il path di un Avatar generato casualmente.
     */
    public static String getPathAvatar(){
        File folder = new File(PATH_AVATAR);
        List<String> avatars = new ArrayList<>();

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                avatars.add(PATH_AVATAR + "/" + file.getName());
            }
        }

        Random random = new Random();

        return random.ints(0, avatars.size()) // Genera un flusso infinito di indici
                .distinct()              // Assicura che siano unici
                .mapToObj(avatars::get)  // Prendi il nome corrispondente
                .findFirst().get();     // Ritorna il primo
    }

    /**
     * Metodo per estrarre casualmente un nome da assegnare al bot.
     * Legge un file di testo contenente i nomi utilizzando il costrutto try-with-resources
     * e un flusso Stream.
     * Complessità computazionale: O(N) dove N è il numero di righe all'interno del file.
     *
     * @return Il nome estratto per il bot (o un nome di fallback in caso di errore I/O).
     */
    public static String estraiNome() {
        List<String> nomiBot;
        String nome;
        /*
         * costrutto "try with resources", si assicura che il file venga chiuso dopo
         * l'utilizzo
         */
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_NOMI))) {

            /*
             * uso di stream per convertire tutte le lettere in minuscolo
             */
            nomiBot = br.lines().map(String::toLowerCase).toList();

            Random random = new Random();

            nome = random.ints(0, nomiBot.size()) // Genera un flusso infinito di indici
                    .distinct()              // Assicura che siano unici
                    .mapToObj(nomiBot::get)  // Prendi il nome corrispondente (Accesso Diretto)
                    .findFirst().get();

        } catch (IOException e) {
            e.printStackTrace();
            nome = "Karen";
        }

        return nome;
    }

}
