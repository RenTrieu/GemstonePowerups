package org.gemstones;

import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class Lapis extends Gemstone implements Listener {

    private final double expBoostFactor = 1.3;

    public Lapis(
        GemstonePowerupsPlugin plugin,
        Server server,
        GemstoneType gType
    ) {
        super(plugin, server, gType);
        server.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (plugin.getGemTeam(event.getPlayer()) == this.getGemstoneType()) {
            if (event.getAmount() > 0) {
                event.setAmount(
                    (int) Math.ceil(event.getAmount()*expBoostFactor)
                );
            }
        }
    }

}

