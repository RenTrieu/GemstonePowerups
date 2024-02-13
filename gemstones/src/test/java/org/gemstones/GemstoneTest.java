package org.gemstones;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.WorldMock;

import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GemstoneTest {
    private ServerMock server;
    private GemstonePowerupsPlugin plugin;

    @BeforeEach
    public void setUp() {
        /* Starting the mock server */
        server = MockBukkit.mock();
        /* Loading the Gemstones plugin */
        plugin = MockBukkit.load(GemstonePowerupsPlugin.class);
    }

    @AfterEach
    public void tearDown() {
        /* Stop the mock server */
        MockBukkit.unmock();
    }

    /*
     * Case where the gemstone block is right by the player
     * 
     * Testing for whether or not the player receives the corresponding
     * potion effect
     */
    @Test
    public void checkApplyEffectsAdjacent() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location blockLoc = new Location(testWorld, 0.0, 4.0, 0.0);
        testWorld.setBlockData(
            blockLoc, Material.DIAMOND_BLOCK.createBlockData()
        );
        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(blockLoc);

        Gemstone gTest = new Gemstone(server);
        PotionEffect pEffect = new PotionEffect(
            PotionEffectType.GLOWING,
            200,
            1
        );
        gTest.addGemstoneEffect(
            5, pEffect, new Material[]{Material.DIAMOND_BLOCK}
        );
        gTest.applyEffects(player);
        Assertions.assertTrue(player.hasPotionEffect(pEffect.getType()));
    }

    /*
     * Case where two blocks with two different potion effects are specified
     * and are by the player
     * 
     * Testing for whether or not the player receives the corresponding
     * potion effect
     */
    @Test
    public void checkApplyTwoEffectsAdjacent() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location blockLoc1 = new Location(testWorld, 0.0, 4.0, 0.0);
        Location blockLoc2 = new Location(testWorld, 0.0, 4.0, -2.0);
        testWorld.setBlockData(
            blockLoc1, Material.DIAMOND_BLOCK.createBlockData()
        );
        testWorld.setBlockData(
            blockLoc2, Material.GOLD_BLOCK.createBlockData()
        );

        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        Location playerLoc = new Location(testWorld, 0.0, 5.0, 0.0);
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        PotionEffect pEffect1 = new PotionEffect(
            PotionEffectType.DAMAGE_RESISTANCE, 200, 1
        );
        PotionEffect pEffect2 = new PotionEffect(
            PotionEffectType.FIRE_RESISTANCE, 200, 1
        );

        gTest.addGemstoneEffect(
            5, pEffect1, new Material[]{Material.DIAMOND_BLOCK}
        );
        gTest.addGemstoneEffect(
            5, pEffect2, new Material[]{Material.GOLD_BLOCK}
        );

        gTest.applyEffects(player);

        Assertions.assertTrue(player.hasPotionEffect(pEffect1.getType()));
        Assertions.assertTrue(player.hasPotionEffect(pEffect2.getType()));
    }

    /*
     * Testing for whether or not the player receives the higher level of
     * potion effect when two of the same potion effect are applied
     */
    @Test
    public void checkApplyHigherLevelEffect() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location blockLoc1 = new Location(testWorld, 0.0, 4.0, 0.0);
        Location blockLoc2 = new Location(testWorld, 0.0, 4.0, -2.0);
        testWorld.setBlockData(
            blockLoc1, Material.DIAMOND_BLOCK.createBlockData()
        );
        testWorld.setBlockData(
            blockLoc2, Material.GOLD_BLOCK.createBlockData()
        );

        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        Location playerLoc = new Location(testWorld, 0.0, 5.0, 0.0);
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        PotionEffect pEffect1 = new PotionEffect(
            PotionEffectType.DAMAGE_RESISTANCE, 200, 2
        );
        PotionEffect pEffect2 = new PotionEffect(
            PotionEffectType.DAMAGE_RESISTANCE, 200, 1
        );

        gTest.addGemstoneEffect(
            5, pEffect1, new Material[]{Material.DIAMOND_BLOCK}
        );
        gTest.addGemstoneEffect(
            5, pEffect2, new Material[]{Material.GOLD_BLOCK}
        );

        gTest.applyEffects(player);

        Assertions.assertTrue(player.hasPotionEffect(pEffect1.getType()));
    }

    /*
     * Testing that when the player leaves the radius of a block that the
     * potion effect wears off in the expected amount of time
     */
    // @Test
    // public void checkApplyEffectExpires() {
    //     WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
    //     Location blockLoc = new Location(testWorld, 0.0, 4.0, 0.0);
    //     Location playerLoc = new Location(testWorld, 0.0, 4.0, 1.0);
    //     int radius = 5;
    //     testWorld.setBlockData(
    //         blockLoc, Material.DIAMOND_BLOCK.createBlockData()
    //     );
    //     server.addWorld(testWorld);
    //     PlayerMock player = server.addPlayer();
    //     player.setLocation(playerLoc);
    //
    //     Gemstone gTest = new Gemstone(server);
    //     PotionEffect pEffect = new PotionEffect(
    //         PotionEffectType.DAMAGE_RESISTANCE, 15, 1
    //     );
    //     gTest.addGemstoneEffect(
    //         radius, pEffect, new Material[]{Material.DIAMOND_BLOCK}
    //     );
    //     gTest.applyEffects(player);
    //     //gTest.runTaskTimer(this.plugin, 0L, 40L);
    //
    //     //player.setLocation(playerLoc.add(new Vector((double) radius, 0.0, 0.0)));
    //     //Assertions.assertFalse(player.hasPotionEffect(pEffect.getType()));
    //     // server.getScheduler().performTicks(10L);
    //     // Assertions.assertTrue(player.hasPotionEffect(pEffect.getType()));
    //     // server.getScheduler().performTicks(20L);
    //     // Assertions.assertFalse(player.hasPotionEffect(pEffect.getType()));
    //
    //     //server.getScheduler().performTicks(40L);
    //     //player.setLocation(playerLoc.add(new Vector((double) radius, 0.0, 0.0)));
    //     //player.setLocation(new Location(testWorld, 10.0, 4.0, 20.0));
    //     // System.out.println("pLoc: " + player.getLocation());
    //     // Assertions.assertTrue(player.hasPotionEffect(pEffect.getType()));
    //     // server.getScheduler().performTicks(100L);
    //     // System.out.println("pLoc: " + player.getLocation());
    //     // Assertions.assertTrue(player.hasPotionEffect(pEffect.getType()));
    //     // server.getScheduler().performTicks(110L);
    //     // System.out.println("pLoc: " + player.getLocation());
    //     // Assertions.assertFalse(player.hasPotionEffect(pEffect.getType()));
    // }

    /*
     * Case where multiple blocks are specified and
     * are adjacent to the player
     * 
     * Testing for whether or not the player receives the corresponding
     * potion effect
     */
    @Test
    public void checkApplyEffectsAdjacentMultipleBlocks() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location blockLoc1 = new Location(testWorld, 0.0, 4.0, 0.0);
        testWorld.setBlockData(
            blockLoc1, Material.DIAMOND_BLOCK.createBlockData()
        );
        Location blockLoc2 = new Location(testWorld, 0.0, 4.0, 1.0);
        testWorld.setBlockData(
            blockLoc2, Material.DIAMOND_ORE.createBlockData()
        );

        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        Location playerLoc = new Location(testWorld, 0.0, 5.0, 0.0);
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        PotionEffect pEffect = new PotionEffect(
            PotionEffectType.GLOWING,
            200,
            1
        );
        gTest.addGemstoneEffect(
            5, pEffect, new Material[]{
                Material.DIAMOND_BLOCK,
                Material.DIAMOND_ORE
            }
        );
        gTest.applyEffects(player);
        Assertions.assertTrue(player.hasPotionEffect(pEffect.getType()));
    }


    /*
     * Case where the block we are scanning for is right by the player
     */
    @Test
    public void checkScanBlockAtOrigin() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location blockLoc = new Location(testWorld, 0.0, 4.0, 0.0);
        testWorld.setBlockData(
            blockLoc, Material.DIAMOND_BLOCK.createBlockData()
        );
        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(blockLoc);

        Gemstone gTest = new Gemstone(server);
        ArrayList<Location> locList = gTest.scanBlockProximity(
            player,
            new Material[]{Material.DIAMOND_BLOCK},
            5
        );

        ArrayList<Location> expectedList = new ArrayList<>();
        expectedList.add(blockLoc);

        Assertions.assertTrue(
            locList.containsAll(expectedList)
            && expectedList.containsAll(locList)
        );
    }

    /*
     * Case where the block we are scanning for is outside the border in the
     * x direction
     */
    @Test
    public void checkScanBlockOutsideX() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location playerLoc = new Location(testWorld, 0.0, 4.0, 0.0);
        Location blockLoc = new Location(testWorld, 6.0, 4.0, 0.0);
        testWorld.setBlockData(
            blockLoc, Material.DIAMOND_BLOCK.createBlockData()
        );
        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        ArrayList<Location> locList = gTest.scanBlockProximity(
            player,
            new Material[]{Material.DIAMOND_BLOCK},
            5
        );

        ArrayList<Location> expectedList = new ArrayList<>();
        expectedList.add(blockLoc);

        Assertions.assertFalse(
            locList.containsAll(expectedList)
            && expectedList.containsAll(locList)
        );
    }

    /*
     * Case where the block we are scanning for is outside the border in the
     * y direction
     */
    @Test
    public void checkScanBlockOutsideY() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location playerLoc = new Location(testWorld, 0.0, 4.0, 0.0);
        Location blockLoc = new Location(testWorld, 0.0, 10.0, 0.0);
        testWorld.setBlockData(
            blockLoc, Material.DIAMOND_BLOCK.createBlockData()
        );
        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        ArrayList<Location> locList = gTest.scanBlockProximity(
            player,
            new Material[]{Material.DIAMOND_BLOCK},
            5
        );

        ArrayList<Location> expectedList = new ArrayList<>();
        expectedList.add(blockLoc);

        Assertions.assertFalse(
            locList.containsAll(expectedList)
            && expectedList.containsAll(locList)
        );
    }

    /*
     * Case where the block we are scanning for is outside the border in the
     * z direction
     */
    @Test
    public void checkScanBlockOutsideZ() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location playerLoc = new Location(testWorld, 0.0, 4.0, 0.0);
        Location blockLoc = new Location(testWorld, 0.0, 4.0, 7.0);
        testWorld.setBlockData(
            blockLoc, Material.DIAMOND_BLOCK.createBlockData()
        );
        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        ArrayList<Location> locList = gTest.scanBlockProximity(
            player,
            new Material[]{Material.DIAMOND_BLOCK},
            5
        );

        ArrayList<Location> expectedList = new ArrayList<>();
        expectedList.add(blockLoc);

        Assertions.assertFalse(
            locList.containsAll(expectedList)
            && expectedList.containsAll(locList)
        );
    }

    /*
     * Case where the block we are scanning for is right on the border
     * of the scanning radius
     */
    @Test
    public void checkScanBlockOnBorder() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location playerLoc = new Location(testWorld, 0.0, 4.0, 5.0);
        Location blockLoc = new Location(testWorld, 0.0, 4.0, 0.0);
        testWorld.setBlockData(
            blockLoc, Material.DIAMOND_BLOCK.createBlockData()
        );
        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        ArrayList<Location> locList = gTest.scanBlockProximity(
            player,
            new Material[]{Material.DIAMOND_BLOCK},
            5
        );

        ArrayList<Location> expectedList = new ArrayList<>();
        expectedList.add(blockLoc);

        Assertions.assertFalse(
            locList.containsAll(expectedList)
            && expectedList.containsAll(locList)
        );
    }

    /*
     * Case where the block we are scanning for is right within the border
     * of the scanning radius
     */
    @Test
    public void checkScanBlockInsideBorder() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location playerLoc = new Location(testWorld, 0.0, 4.0, 4.0);
        Location blockLoc = new Location(testWorld, 0.0, 4.0, 0.0);
        testWorld.setBlockData(
            blockLoc, Material.DIAMOND_BLOCK.createBlockData()
        );
        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        ArrayList<Location> locList = gTest.scanBlockProximity(
            player,
            new Material[]{Material.DIAMOND_BLOCK},
            5
        );

        ArrayList<Location> expectedList = new ArrayList<>();
        expectedList.add(blockLoc);

        Assertions.assertTrue(
            locList.containsAll(expectedList)
            && expectedList.containsAll(locList)
        );
    }

    /*
     * Case with some blocks within the border and some outside the border
     * of the scanning radius
     */
    @Test
    public void checkScanVariedBlocks() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location playerLoc = new Location(testWorld, 0.0, 4.0, 0.0);

        /* Locations within the radius */
        ArrayList<Location> expectedSuccess = new ArrayList<>();
        expectedSuccess.add(new Location(testWorld, 0.0, 4.0, 0.0));
        expectedSuccess.add(new Location(testWorld, 2.0, 4.0, -3.0));
        expectedSuccess.add(new Location(testWorld, -2.0, 5.0, 1.0));
        expectedSuccess.add(new Location(testWorld, 0.0, 7.0, 0.0));

        /* Locations outside of the radius */
        ArrayList<Location> expectedFailure = new ArrayList<>();
        expectedFailure.add(new Location(testWorld, -5.0, 4.0, 6.0));
        expectedFailure.add(new Location(testWorld, 0.0, 10.0, 0.0));
        expectedFailure.add(new Location(testWorld, -1.0, 14.0, 2.0));

        /* Setting all locations to diamond block */
        for (Location blockLoc : expectedSuccess) {
            testWorld.setBlockData(
                blockLoc, Material.DIAMOND_BLOCK.createBlockData()
            );
        }
        for (Location blockLoc : expectedFailure) {
            testWorld.setBlockData(
                blockLoc, Material.DIAMOND_BLOCK.createBlockData()
            );
        }

        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        ArrayList<Location> locList = gTest.scanBlockProximity(
            player,
            new Material[]{Material.DIAMOND_BLOCK},
            5
        );

        Assertions.assertTrue(
            locList.containsAll(expectedSuccess)
            && expectedSuccess.containsAll(locList)
        );
        for (Location loc : expectedFailure) {
            Assertions.assertFalse(locList.contains(loc));
        }
    }

    /*
     * Case with multiple types of blocks specified and within the border
     */
    @Test
    public void checkScanMultipleBlockTypes() {
        WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
        Location playerLoc = new Location(testWorld, 0.0, 4.0, 0.0);
        Location blockLoc1 = new Location(testWorld, 0.0, 4.0, -2.0);
        Location blockLoc2 = new Location(testWorld, 1.0, 4.0, -1.0);

        testWorld.setBlockData(
            blockLoc1, Material.DIAMOND_BLOCK.createBlockData()
        );

        testWorld.setBlockData(
            blockLoc2, Material.DIAMOND_ORE.createBlockData()
        );

        server.addWorld(testWorld);
        PlayerMock player = server.addPlayer();
        player.setLocation(playerLoc);

        Gemstone gTest = new Gemstone(server);
        ArrayList<Location> locList = gTest.scanBlockProximity(
            player,
            new Material[]{
                Material.DIAMOND_BLOCK,
                Material.DIAMOND_ORE
            },
            5
        );

        ArrayList<Location> expectedSuccess = new ArrayList<>();
        expectedSuccess.add(blockLoc1);
        expectedSuccess.add(blockLoc2);

        Assertions.assertTrue(
            locList.containsAll(expectedSuccess)
            && expectedSuccess.containsAll(locList)
        );
    }
}
