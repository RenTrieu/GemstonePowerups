package org.gemstones;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Server;


public class Gemstone extends BukkitRunnable {

    /* Stores which Gemstone team each player is on */
    private static HashMap<String, Player> gTeamMap;
    /* 
     * Stores vectors to blocks within a sphere of a given radius,
     * relative to the origin 
     */
    private static HashMap<Integer, ArrayList<Vector>> 
        sphereVectors = new HashMap<>();
    /* Stores the mapping from potion effects to radii and material */
    private HashMap<PotionEffect, HashMap<Integer, Material[]>>
        effectMap = new HashMap<>();
    private GemstoneType gType = null;
    public Server server;

    public Gemstone(Server server) {
        this.server = server;
    }

    public void run() {
        for (Player player : this.server.getOnlinePlayers()) {
            if (gType == null) {
                // TODO: Add check for if player is in the right gemstone category
                applyEffects(player);
            }
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
        if (!this.effectMap.containsKey(pEffect)) {
            this.effectMap.put(
                pEffect, new HashMap<Integer, Material[]>()
            );
        }
        HashMap<Integer, Material[]> radiusMap = this.effectMap.get(pEffect);
        if (!radiusMap.containsKey(radius)) {
            radiusMap.put(radius, matList);
        }
        else {
            Material[] prevMatList = radiusMap.get(radius);
            HashSet<Material> matSet = new HashSet<>();
            for (Material mat : prevMatList) {
                matSet.add(mat);
            }
            for (Material mat : matList) {
                matSet.add(mat);
            }
            radiusMap.remove(radius);
            radiusMap.put(radius, (Material[]) matSet.toArray());
        }
    }

    /*
     * Applies the applicable potion effects to the player based off of their
     * Location and conditions
     */
    public void applyEffects(
        Player player
    ) {
        for (PotionEffect pEffect : effectMap.keySet()) {
            HashMap<Integer, Material[]> radiusMap = effectMap.get(pEffect);
            boolean detectedBlock = false;
            for (Integer radius : radiusMap.keySet()) {
                Material[] matList = radiusMap.get(radius);
                detectedBlock = scanBlockProximity(
                    player, matList, radius
                ).size() > 0;
            }
            if (detectedBlock) {
                // TODO: Add check to see if player already has potion effect of same or higher level applied
                // If so, don't apply again
                pEffect.apply(player);
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
