package org.gemstones;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import org.bukkit.command.Command;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

public class GemstonePowerupsPluginTest {
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

    @Test
    public void playerGemstoneCommandNoArgs() {
        PlayerMock player = server.addPlayer();
        Assertions.assertFalse(player.performCommand("gemstone"));
    }

    /*
     * Tests the choose Gem Team subcommand and ensures that the player's
     * Gem Team gets set as expected
     */
    // @Test
    // public void playerSetGemTeamCommand() {
    //     PlayerMock player = server.addPlayer();
    //     String gemstoneTeam = "gold";
    //
    //     /* Asserting that the command is valid */
    //     Assertions.assertTrue(
    //         player.performCommand("gemstones choose " + gemstoneTeam)
    //     );
    //
    //     // Command gCommand = server.getPluginCommand("gemstones");
    //     // server.executePlayer(gCommand, "choose", gemstoneTeam);
    //     // Assertions.assertEquals(
    //     //     plugin.getGemTeam(player).toString(), gemstoneTeam.toUpperCase()
    //     // );
    // }
    //

    // @Test
    // public void playerGemToggleCommand() {
    //     PlayerMock player = server.addPlayer();
    //     Assertions.assertTrue(
    //         player.performCommand("gemstones toggle off")
    //     );
    //     Assertions.assertFalse(
    //         plugin.getGemToggle(player)
    //     );
    // }

    // @Test
    // public void playerNonOPGlobalEnable() {
    //     PlayerMock player = server.addPlayer();
    //     Assertions.assertFalse(player.isOp());
    //     Assertions.assertFalse(
    //         player.performCommand("gemstones enable")
    //     );
    // }
    //
    // @Test
    // public void playerNonOPGlobalDisable() {
    //     PlayerMock player = server.addPlayer();
    //     Assertions.assertFalse(player.isOp());
    //     Assertions.assertFalse(
    //         player.performCommand("gemstones disable")
    //     );
    // }
    //
    // @Test
    // public void playerOPGlobalEnable() {
    //     PlayerMock player = server.addPlayer();
    //     player.setOp(true);
    //     Assertions.assertTrue(
    //         player.performCommand("gemstones enable")
    //     );
    // }
    //
    // @Test
    // public void playerOPGlobalDisable() {
    //     PlayerMock player = server.addPlayer();
    //     player.setOp(true);
    //     Assertions.assertTrue(
    //         player.performCommand("gemstones disable")
    //     );
    // }

}
