package org.gemstones;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
     * TODO: Implement when MockBukkit is updated with potion effect
     * detection
     */
    // @Test
    // public void testApplyEffects() {
    //     WorldMock testWorld = new WorldMock(Material.BEDROCK, 0, 256, 3);
    //     Location blockLoc = new Location(testWorld, 0.0, 4.0, 0.0);
    //     testWorld.setBlockData(
    //         blockLoc, Material.DIAMOND_BLOCK.createBlockData()
    //     );
    //     server.addWorld(testWorld);
    //     PlayerMock player = server.addPlayer();
    //     player.setLocation(blockLoc);
    //
    //     Gemstone gTest = new Gemstone();
    //     PotionEffect pEffect = new PotionEffect(
    //         PotionEffectType.GLOWING,
    //         200,
    //         1
    //     );
    //     gTest.addGemstoneEffect(
    //         5, pEffect, new Material[]{Material.DIAMOND_BLOCK}
    //     );
    //     gTest.applyEffects(player);
    //     Assertions.assertTrue(player.isGlowing());
    // }

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

}
