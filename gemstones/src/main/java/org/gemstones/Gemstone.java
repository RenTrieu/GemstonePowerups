package org.gemstones;

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
    public GemstonePowerupsPlugin plugin;

    public Gemstone(
        GemstonePowerupsPlugin plugin,
        Server server,
        GemstoneType gType
    ) {
        this.plugin = plugin;
        this.server = server;
        this.gType = gType;
    }

    public void run() {
        if (!plugin.isPotionsEnabled()) {
            return;
        }
        for (Player player : this.server.getOnlinePlayers()) {
            if (gType == null || gType == plugin.getGemTeam(player)) {
                if (plugin.getGemToggle(player)) {
                    applyEffects(player);
                }
            }
        }
    }

    /*
     * Getter method for Gemstone Type
     */
    public GemstoneType getGemstoneType() {
        return this.gType;
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
                // TODO: Add check to see if player already has potion effect
                // of same or higher level applied
                // If so, don't apply again
                pEffect.apply(player);
            }
        }
    }

    /*
     * Removes the applicable potion effects from the player
     */
    public void removeEffects(
        Player player
    ) {
        for (PotionEffect pEffect : effectMap.keySet()) {
            player.removePotionEffect(pEffect.getType());
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
            Vector origin = new Vector(0.0, 0.0, 0.0);
            for (int x = 0; x < radius; x++) {
                double scaledXSquared = ((double) x) / radius;
                scaledXSquared = scaledXSquared * scaledXSquared;
                for (int y = 0;
                     y < Math.ceil(
                             radius * Math.sqrt(1-scaledXSquared)
                         ) + 1;
                     y++
                ) {
                    double scaledYSquared = ((double) y) / radius;
                    scaledYSquared = scaledYSquared * scaledYSquared;
                    for (int z = 0;
                         z < Math.ceil(radius * Math.sqrt(
                                 1-scaledYSquared-scaledXSquared)
                             ) + 1;
                         z++
                    ) {
                        Vector curVec = new Vector(x, y, z);
                        double distance = origin.distance(curVec);
                        if (distance < radius) {
                            sphereVectors.get(radius).add(curVec);
                            if (!(x == 0 && y == 0 && z == 0)) {
                                sphereVectors.get(radius).add(
                                    new Vector(x, y, -z)
                                );
                                sphereVectors.get(radius).add(
                                    new Vector(x, -y, z)
                                );
                                sphereVectors.get(radius).add(
                                    new Vector(x, -y, -z)
                                );
                                sphereVectors.get(radius).add(
                                    new Vector(-x, y, z)
                                );
                                sphereVectors.get(radius).add(
                                    new Vector(-x, y, -z)
                                );
                                sphereVectors.get(radius).add(
                                    new Vector(-x, -y, z)
                                );
                                sphereVectors.get(radius).add(
                                    new Vector(-x, -y, -z)
                                );
                            }
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
