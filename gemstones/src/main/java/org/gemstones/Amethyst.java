package org.gemstones;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class Amethyst extends Gemstone implements Listener {

    public Amethyst(
        GemstonePowerupsPlugin plugin,
        Server server,
        GemstoneType gType
    ) {
        super(plugin, server, gType);
        server.getPluginManager().registerEvents(this, plugin);
    }

    public void run() {
        if (!plugin.isPotionsEnabled()) {
            return;
        }
        for (Player player : super.server.getOnlinePlayers()) {
            if (super.getGemstoneType() == null 
                || super.getGemstoneType() == plugin.getGemTeam(player)) {
                if (plugin.getGemToggle(player)
                    && !player.isSneaking()) {
                    applyEffects(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            removeEffects(player);
        }
    }
}
