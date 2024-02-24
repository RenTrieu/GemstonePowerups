package org.gemstones;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Amethyst extends Gemstone {

    public Amethyst(
        GemstonePowerupsPlugin plugin,
        Server server,
        GemstoneType gType
    ) {
        super(plugin, server, gType);
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
                if (player.isSneaking()) {
                    removeEffects(player);
                }
            }
        }
    }

}
