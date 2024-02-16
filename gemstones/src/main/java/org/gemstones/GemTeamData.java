package org.gemstones;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class GemTeamData implements Serializable {

    public HashMap<UUID, String> gemTeamMap;

    public GemTeamData() {
        this.gemTeamMap = new HashMap<UUID, String>();
    }

    public GemTeamData (HashMap<UUID, String> gemTeamMap) {
        this.gemTeamMap = gemTeamMap;
    }

    /*
     * Saves GemTeamData
     */
    public boolean saveData(
        String filePath,
        HashMap<UUID, String> gemTeamMap,
        Logger logger
    ) {
        this.gemTeamMap.putAll(gemTeamMap);
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                new GZIPOutputStream(new FileOutputStream(filePath))
            );
            out.writeObject(this);
            out.close();
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE,
                "Error occurred when attempting to save GemTeamData: " + e
            );
            return false;
        }
    }

    /*
     * Loads GemTeamData
     */
    public static GemTeamData loadData(
        String filePath,
        Logger logger
    ) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                new GZIPInputStream(new FileInputStream(filePath))
            );
            GemTeamData gTeamData = (GemTeamData) in.readObject();
            in.close();
            return gTeamData;
        } catch (FileNotFoundException e) {
            return null;
        } catch (ClassNotFoundException | IOException e) {
            logger.log(Level.SEVERE,
                "Error occurred when attempting to save GemTeamData: " + e
            );
            return null;
        }
    }
}
