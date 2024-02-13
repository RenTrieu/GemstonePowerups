package org.gemstones;

import net.md_5.bungee.api.ChatColor;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;

public class GemstonePowerupsPlugin extends JavaPlugin implements Listener {

    private BukkitScheduler scheduler;
    private FileConfiguration config;
    private ArrayList<Gemstone> gemstoneList;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        /* Loading configuration file */
        saveDefaultConfig();
        this.config = getConfig();

        /* Parsing through main configuration and storing results in gemstoneList */
        this.gemstoneList = new ArrayList<>();
        ConfigurationSection gConfig = this.config.getConfigurationSection(
            "gemstones"
        );
        Set<String> gList = gConfig.getKeys(false);
        /* Iterating through each gemstone in the config.yml */
        for (String gemstoneString : gList) {
            ConfigurationSection pSection = gConfig.getConfigurationSection(
                gemstoneString + ".potion_effects"
            );
            /* Iterating through each potion effect */
            for (String pEffectString : pSection.getKeys(false)) {
                List<Map<String, Object>> paramList = 
                    (List<Map<String, Object>>) pSection.getValues(
                        true
                    ).get(pEffectString);
                for (Map<String, Object> param : paramList) {
                    Gemstone gemstone = new Gemstone(Bukkit.getServer());
                    /* Extracting parameters */
                    int radius = (int) param.get("radius");
                    int level = (int) param.get("level");
                    List<String> matList = (List<String>) param.get("blocks");

                    NamespacedKey pNamespacedKey = new NamespacedKey(
                        NamespacedKey.MINECRAFT, pEffectString.toLowerCase()
                    );
                    Registry<PotionEffectType> reg = Bukkit.getRegistry(PotionEffectType.class);
                    PotionEffectType pType1 = reg.get(pNamespacedKey);
                    PotionEffect pEffect = new PotionEffect(
                        reg.get(pNamespacedKey), 200, level
                    );

                    Material[] matArray = new Material[matList.size()];
                    for (int i = 0; i < matList.size(); i++) {
                        matArray[i] = Material.valueOf(matList.get(i));
                    }
                    gemstone.addGemstoneEffect(
                        radius,
                        pEffect,
                        matArray
                    );
                    gemstone.runTaskTimer(this, 40L, 40L);
                    gemstoneList.add(gemstone);
                }
            }
        }
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
