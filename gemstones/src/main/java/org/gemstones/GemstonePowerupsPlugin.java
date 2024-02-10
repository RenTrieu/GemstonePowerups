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

public class GemstonePowerupsPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
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
            Gemstone gTest = new Gemstone();
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ArrayList<Location> locList = gTest.scanBlockProximity(
                    player,
                    new Material[]{Material.DIAMOND_BLOCK},
                    5
                );
                for (Location loc : locList) {
                    sender.sendMessage("loc: " + loc.toString());
                }
            }
        }
        return true;
    }
}
