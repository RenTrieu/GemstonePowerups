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

public class GemData implements Serializable {

    public HashMap<UUID, String> gemTeamMap;
    public HashMap<UUID, Boolean> gemToggleMap;
    public HashMap<UUID, Long> gemCooldownMap;

    public GemData() {
        this.gemTeamMap = new HashMap<UUID, String>();
        this.gemToggleMap = new HashMap<UUID, Boolean>();
        this.gemCooldownMap = new HashMap<UUID, Long>();
    }

    public GemData (
        HashMap<UUID, String> gemTeamMap,
        HashMap<UUID, Boolean> gemToggleMap,
        HashMap<UUID, Long> gemCooldownMap
    ) {
        this.gemTeamMap = gemTeamMap;
        this.gemToggleMap = gemToggleMap;
        this.gemCooldownMap = new HashMap<UUID, Long>();
    }

    /*
     * Saves GemData
     */
    public boolean saveData(
        String filePath,
        HashMap<UUID, String> gemTeamMap,
        HashMap<UUID, Boolean> gemToggleMap,
        HashMap<UUID, Long> gemCooldownMap,
        Logger logger
    ) {
        this.gemTeamMap.putAll(gemTeamMap);
        this.gemToggleMap.putAll(gemToggleMap);
        this.gemCooldownMap.putAll(gemCooldownMap);
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                new GZIPOutputStream(new FileOutputStream(filePath))
            );
            out.writeObject(this);
            out.close();
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE,
                "Error occurred when attempting to save GemData: " + e
            );
            return false;
        }
    }

    /*
     * Loads GemData
     */
    public static GemData loadData(
        String filePath,
        Logger logger
    ) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                new GZIPInputStream(new FileInputStream(filePath))
            );
            GemData gData = (GemData) in.readObject();
            in.close();
            return gData;
        } catch (FileNotFoundException e) {
            return null;
        } catch (ClassNotFoundException | IOException e) {
            logger.log(Level.SEVERE,
                "Error occurred when attempting to save GemData: " + e
            );
            return null;
        }
    }
}
