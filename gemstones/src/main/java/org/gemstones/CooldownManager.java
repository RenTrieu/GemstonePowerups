package org.gemstones;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

public class CooldownManager {

    private long cooldown;

    public CooldownManager(long cooldown) {
        this.cooldown = cooldown;
    }

    /*
     * Sets the Gemstone cooldown for a player
     */
    public void startCooldown(Player player) {
        UUID pUUID = player.getUniqueId();
        GemstonePowerupsPlugin.gemCooldownMap.put(
            pUUID, System.currentTimeMillis()
        );
    }

    /*
     * Resets the Gemstone cooldown for a player
     */
    public void resetCooldown(Player player) {
        GemstonePowerupsPlugin.gemCooldownMap.remove(player.getUniqueId());
    }

    /*
     * Gets the Gemstone cooldown for a player
     */
    public long getTimeElapsed(Player player) {
        if (!GemstonePowerupsPlugin.gemCooldownMap.containsKey(
            player.getUniqueId())
        ) {
            return 0L;
        }
        return System.currentTimeMillis() 
            - GemstonePowerupsPlugin.gemCooldownMap.get(player.getUniqueId());
    }

    /*
     * Returns whether or not a player's cooldown has elapsed
     */
    public boolean getCooldownElapsed(Player player) {
        if (!GemstonePowerupsPlugin.gemCooldownMap.containsKey(
                player.getUniqueId())
        ) {
            return true;
        }
        long cooldownMillis = TimeUnit.SECONDS.toMillis(cooldown);
        return System.currentTimeMillis() 
            - GemstonePowerupsPlugin.gemCooldownMap.get(player.getUniqueId())
            > cooldownMillis;
    }

}
