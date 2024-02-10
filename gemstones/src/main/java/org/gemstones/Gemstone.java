package org.gemstones;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.lang.Math;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.Material;

import org.gemstones.GemstoneType;


public class Gemstone {

    private BukkitScheduler scheduler;
    /* 
     * Stores vectors to blocks within a sphere of a given radius,
     * relative to the origin 
     */
    private static HashMap<Integer, ArrayList<Vector>> 
        sphereVectors = new HashMap<>();

    public Gemstone() {
        this.scheduler = Bukkit.getScheduler();
    }

    /*
     * Returns true if the distance between origin and loc is less than
     * the specified distance
     */
    public static boolean withinDistance(
        Location origin,
        Location loc,
        int distance
    ) {
        
        return false;
    }

    /* 
     * Scans for the location of certain blocks in a given proximity to a
     * player 
     */
    ArrayList<Location> scanBlockProximity(
        Player player,
        Material[] materials,
        int radius
    ) {
        ArrayList<Location> nearBlocks = new ArrayList<>();
        Location pLocation = player.getLocation();

        /* Computing sphere vectors for the given radius */
        if (!sphereVectors.containsKey(radius)) {
            sphereVectors.put(radius, new ArrayList<Vector>());
            ArrayList<Vector> sphereVecs = new ArrayList<>();
            Vector origin = new Vector(0.0, 0.0, 0.0);
            for (int x = -radius; x < radius; x++) {
                for (int y = -radius; y < radius; y++) {
                    for (int z = -radius; z < radius; z++) {
                        Vector curVec = new Vector(
                            (double) x, (double) y, (double) z
                        );
                        double distance = origin.distance(curVec);
                        if (distance < radius) {
                            sphereVectors.get(radius).add(curVec);
                        }
                    }
                }
            }
        }

        /* 
         * For each block/location that matches a material in the materials array,
         * add to nearBlocks 
         */
        for (Vector sVector : sphereVectors.get(radius)) {
            Location curLoc = pLocation.clone().add(sVector);
            Material curMat = curLoc.getBlock().getType();
            boolean match = false;
            for (int i = 0; i < materials.length; i++) {
                if (curMat.compareTo(materials[i]) == 0) {
                    match = true;
                }
            }
            if (match) {
                nearBlocks.add(curLoc);
            }
        }
        return nearBlocks;
    }

}
