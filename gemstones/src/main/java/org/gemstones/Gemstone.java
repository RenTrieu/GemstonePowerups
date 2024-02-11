package org.gemstones;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.lang.Math;
import java.util.AbstractMap.SimpleEntry;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Server;

import org.gemstones.GemstoneType;


public class Gemstone extends BukkitRunnable {

    /* 
     * Stores vectors to blocks within a sphere of a given radius,
     * relative to the origin 
     */
    private static HashMap<Integer, ArrayList<Vector>> 
        sphereVectors = new HashMap<>();
    /* Stores the mapping from radii to potion effects and material */
    private HashMap<Integer, HashMap<PotionEffect, Material[]>>
        radiusMap = new HashMap<>();
    private GemstoneType gType = null;
    private Server server;

    public Gemstone(Server server) {
        this.server = server;
    }

    public void run() {
        for (Player player : this.server.getOnlinePlayers()) {
            applyEffects(player);
        }
    }

    /*
     * Adds a Gemstone effect that will apply in the given radius when
     * the player is in proximity to the given materials
     *
     * If the potion effect for the given radius already has an array of
     * materials, then the original and new array will be combined
     */
    public void addGemstoneEffect(
        int radius,
        PotionEffect pEffect,
        Material[] matList
    ) {
        if (!radiusMap.containsKey(radius)) {
            radiusMap.put(
                radius, new HashMap<PotionEffect, Material[]>()
            );
        }
        HashMap<PotionEffect, Material[]> effectMap = radiusMap.get(radius);
        if (!effectMap.containsKey(pEffect)) {
            effectMap.put(pEffect, matList);
        }
        else {
            Material[] prevMatList = effectMap.get(pEffect);
            HashSet<Material> matSet = new HashSet<>();
            for (Material mat : prevMatList) {
                matSet.add(mat);
            }
            for (Material mat : matList) {
                matSet.add(mat);
            }
            effectMap.remove(pEffect);
            effectMap.put(pEffect, (Material[]) matSet.toArray());
        }
    }

    /*
     * Applies the applicable potion effects to the player based off of their
     * Location and conditions
     */
    public void applyEffects(
        Player player
    ) {
        for (Integer radius : radiusMap.keySet()) {
            HashMap<PotionEffect, Material[]> effectMap = radiusMap.get(radius);
            for (PotionEffect pEffect : effectMap.keySet()) {
                Material[] matList = effectMap.get(pEffect);
                ArrayList<Location> detectList = scanBlockProximity(
                    player,
                    matList,
                    radius
                );
                if (detectList.size() > 0) {
                    pEffect.apply(player);
                }
            }
        }
    }

    /* 
     * Scans for the location of certain blocks in a given proximity to a
     * player 
     */
    public ArrayList<Location> scanBlockProximity(
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
