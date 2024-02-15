package org.gemstones;

import java.io.FileInputStream;
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

    public GemTeamData (HashMap<UUID, String> gemTeamMap) {
        this.gemTeamMap = gemTeamMap;
    }

    /*
     * Saves GemTeamData
     */
    public boolean saveData(
        String filePath,
        HashMap<UUID, String> gemTeamMap
    ) {
        this.gemTeamMap = gemTeamMap;
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                new GZIPOutputStream(new FileOutputStream(filePath))
            );
            out.writeObject(this);
            out.close();
            return true;
        } catch (IOException e) {
            System.err.println(
                "Error occurred when attempting to save GemTeamData: " + e
            );
            return false;
        }
    }

    /*
     * Loads GemTeamData
     */
    public GemTeamData loadData(String filePath) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(new FileInputStream(filePath))
                );
            GemTeamData gTeamData = (GemTeamData) in.readObject();
            in.close();
            return gTeamData;
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(
                "Error occurred when attempting to save GemTeamData: " + e
            );
            return null;
        }
    }
}
