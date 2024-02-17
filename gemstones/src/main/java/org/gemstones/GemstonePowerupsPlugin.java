package org.gemstones;

import net.md_5.bungee.api.ChatColor;

import java.util.Set;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

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
import org.bukkit.util.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import org.apache.commons.lang3.EnumUtils;

public class GemstonePowerupsPlugin extends JavaPlugin implements Listener {

    private BukkitScheduler scheduler;
    private FileConfiguration config;
    private ArrayList<Gemstone> gemstoneList;

    /* Maps Player UUID to Gemstone team */
    private HashMap<UUID, String> gemTeamMap;
    private final String gemTeamFilePath = getDataFolder() + "/gemTeam.gzip";
    private GemTeamData gData;

    /* Gemstone subcommands */
    private final ArrayList<String> COMMANDS = new ArrayList<>(Arrays.asList(
        "choose",
        "toggle"
    ));

    /* Gemstone admin subcommands */
    private final ArrayList<String> ADMIN_COMANDS = new ArrayList<>(
        Arrays.asList("globalToggle")
    );

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

        /* Loading in saved gemTeamMap data if exists */
        GemTeamData gData = GemTeamData.loadData(
            gemTeamFilePath, Bukkit.getLogger()
        );
        if (gData == null) {
            gData = new GemTeamData();
            gData.saveData(
                gemTeamFilePath, gData.gemTeamMap, getLogger() 
            );
        }
        this.gData = gData;
        this.gemTeamMap = gData.gemTeamMap;
    }


    @Override
    public List<String> onTabComplete(
        CommandSender sender,
        Command command,
        String alias,
        String[] args) {
        ArrayList<String> completions = new ArrayList<>();
        for (String arg : args) {
            StringUtil.copyPartialMatches(arg, this.COMMANDS, completions);
        }
        return completions;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(
            Component.text("Hello, " + event.getPlayer().getName() + "!")
        );
        this.gemTeamMap.put(event.getPlayer().getUniqueId(), "GOLD");
        this.gData.saveData(
            gemTeamFilePath, gemTeamMap, getLogger()
        );
    }

    /*
     * Sets the given player's Gem Team
     */
    private void setGemTeam(Player player, GemstoneType gemType) {
        UUID pUUID = player.getUniqueId();
        if (gemTeamMap.containsKey(pUUID)) {
            gemTeamMap.remove(pUUID);
        }
        gemTeamMap.put(pUUID, gemType.toString());
    }

    /*
     * Gets the given player's Gem Team
     */
    public GemstoneType getGemTeam(Player player) {
        return GemstoneType.valueOf(gemTeamMap.get(player.getUniqueId()));
    }

    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        if (command.getName().equalsIgnoreCase("gemstones")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    return false;
                }
                String subcommand = args[1];
                switch (subcommand) {
                    case "choose": if (args.length < 2) {
                        return false;
                    }
                    else if (EnumUtils.isValidEnum(
                        GemstoneType.class, args[2].toUpperCase()
                    )) {
                        setGemTeam(
                            player, GemstoneType.valueOf(args[2].toUpperCase())
                        );
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Command received!");
            }
        }
        return true;
    }
}
