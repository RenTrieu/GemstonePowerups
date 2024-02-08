package org.gemstones;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.gemstones.GemstonePowerupsPlugin;
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
	public void playerGreetOnJoin() {
		PlayerMock player = server.addPlayer();
		player.reconnect();
		String greeting = player.nextMessage();
		Assertions.assertEquals(
			greeting,
			"Hello, "
			+ LegacyComponentSerializer.legacyAmpersand().serialize(
				player.name())
			+ "!"
		);
	}
}
