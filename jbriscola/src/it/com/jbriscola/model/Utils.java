package it.com.jbriscola.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {
    public static final String PATH_AVATAR = "assets/avatar/";
    public static final String PATH_NOMI = "assets/nomiBot.txt";
    public static final String PATH_RETRO_CARTA = "assets/retro_carta.png";

    /**
     * Metodo che Legge la cartella "asset/avatar"
     * @return il path di un Avatar random
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
                .findFirst().get();     // Ritorna il
    }

    /**
     * Metodo per estrarre casualmente il nome del bot caricato da file
     *
     * @return Il nome del bot
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
