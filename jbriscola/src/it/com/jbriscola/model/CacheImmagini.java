package it.com.jbriscola.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class CacheImmagini {
    // La mappa che conserva le immagini già decodificate
    private static final Map<String, BufferedImage> cache = new HashMap<>();

    public static BufferedImage getImmagine(String path) {
        if (!cache.containsKey(path)) {
            try {
                // Legge dal disco SOLO LA PRIMA VOLTA
                cache.put(path, ImageIO.read(new File(path)));
            } catch (IOException e) {
                System.err.println("Errore I/O: Impossibile caricare " + path);
                // Ritorna un buffer vuoto per evitare NullPointerException in cascata
                return new BufferedImage(125, 215, BufferedImage.TYPE_INT_ARGB);
            }
        }
        return cache.get(path);
    }
}