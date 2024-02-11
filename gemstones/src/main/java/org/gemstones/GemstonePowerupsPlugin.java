package org.gemstones;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;

public class GemstonePowerupsPlugin extends JavaPlugin implements Listener {

    private BukkitScheduler scheduler;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Gemstone gTest = new Gemstone(Bukkit.getServer());
        PotionEffect pEffect = new PotionEffect(
            PotionEffectType.GLOWING,
            200,
            1
        );
        gTest.addGemstoneEffect(
            5, pEffect, new Material[]{Material.DIAMOND_BLOCK}
        );
        gTest.runTaskTimer(this, (long) 40.0, (long) 40.0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(
            Component.text("Hello, " + event.getPlayer().getName() + "!")
        );
    }

    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        if (command.getName().equalsIgnoreCase("gemstones")) {
            sender.sendMessage(ChatColor.GREEN + "Command received!");
        }
        return true;
    }
}
