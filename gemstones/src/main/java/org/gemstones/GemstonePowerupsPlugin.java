package org.gemstones;

import net.md_5.bungee.api.ChatColor;

import java.util.Set;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import org.apache.commons.lang3.EnumUtils;

public class GemstonePowerupsPlugin extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private ArrayList<Gemstone> gemstoneList;

    /* Maps Player UUID to Gemstone team */
    private static HashMap<UUID, String> gemTeamMap;
    /* 
     * Tracks whether or not the Gemstone effects are enabled for a given player
     */
    private static HashMap<UUID, Boolean> gemToggleMap;
    private final String gemTeamFilePath = getDataFolder() + "/gemstones.gzip";
    private GemData gData;
    /* Gemstone subcommands */
    private final ArrayList<String> COMMANDS = new ArrayList<>(Arrays.asList(
        "choose",
        "toggle",
        "show"
    ));
    /* Subcommands that need GemstoneType enums as suggestions */
    private final ArrayList<String> GTYPE_COMMANDS = new ArrayList<>(
        Arrays.asList("choose")
    );
    /* Gemstone admin subcommands */
    private final ArrayList<String> ADMIN_COMANDS = new ArrayList<>(
        Arrays.asList("enable", "disable")
    );
    /* Potion effects only run when this is true */
    private boolean potionsEnabled = true;

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
                    Gemstone gemstone = new Gemstone(
                        this,
                        Bukkit.getServer(),
                        GemstoneType.valueOf(gemstoneString.toUpperCase())
                    );
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

        /* Loading in saved gem data if exists */
        GemData gData = GemData.loadData(
            gemTeamFilePath, Bukkit.getLogger()
        );
        if (gData == null) {
            gData = new GemData();
            gData.saveData(
                gemTeamFilePath,
                gData.gemTeamMap,
                gData.gemToggleMap,
                getLogger() 
            );
        }
        this.gData = gData;
        GemstonePowerupsPlugin.gemTeamMap = this.gData.gemTeamMap;
        GemstonePowerupsPlugin.gemToggleMap = this.gData.gemToggleMap;
    }


    @Override
    public List<String> onTabComplete(
        CommandSender sender,
        Command command,
        String alias,
        String[] args) {
        ArrayList<String> completions = new ArrayList<>();
        List<String> argList = Arrays.asList(args);
        if (argList.contains("toggle")) {
            completions.add("on");
            completions.add("off");
        }
        else if (argList.contains("choose")) {
            ArrayList<String> gemstoneTypes = new ArrayList<>();
            for (GemstoneType gType : GemstoneType.values()) {
                completions.add(gType.toString().toLowerCase());
            }
        }
        else {
            for (String arg : args) {
                StringUtil.copyPartialMatches(arg, this.COMMANDS, completions);
            }
            if (sender.hasPermission("gemstones.admin")) {
                for (String arg : args) {
                    StringUtil.copyPartialMatches(
                        arg, this.ADMIN_COMANDS, completions
                    );
                }
            }
        }
        return completions;
    }

    /*
     * Accessor method for potionsEnabled
     */
    public boolean isPotionsEnabled() {
        return this.potionsEnabled;
    }

    /*
     * Sets the given player's Gem toggle status
     */
    private void setGemToggle(Player player, Boolean toggle) {
        UUID pUUID = player.getUniqueId();
        if (GemstonePowerupsPlugin.gemToggleMap.containsKey(pUUID)) {
            GemstonePowerupsPlugin.gemToggleMap.remove(pUUID);
        }
        GemstonePowerupsPlugin.gemToggleMap.put(pUUID, toggle);
        this.gData.saveData(
            gemTeamFilePath,
            gData.gemTeamMap,
            GemstonePowerupsPlugin.gemToggleMap,
            getLogger()
        );
    }

    /*
     * Returns the given player's Gem toggle status
     */
    public Boolean getGemToggle(Player player) {
        if (!gemToggleMap.containsKey(player.getUniqueId())) {
            return true;
        }
        return GemstonePowerupsPlugin.gemToggleMap.get(player.getUniqueId());
    }

    /*
     * Sets the given player's Gem Team
     */
    private void setGemTeam(Player player, GemstoneType gemType) {
        UUID pUUID = player.getUniqueId();
        if (GemstonePowerupsPlugin.gemTeamMap.containsKey(pUUID)) {
            GemstonePowerupsPlugin.gemTeamMap.remove(pUUID);
        }
        GemstonePowerupsPlugin.gemTeamMap.put(pUUID, gemType.toString());
        this.gData.saveData(
            gemTeamFilePath,
            GemstonePowerupsPlugin.gemTeamMap,
            gData.gemToggleMap, 
            getLogger()
        );
    }

    /*
     * Returns the given player's Gem Team
     */
    public GemstoneType getGemTeam(Player player) {
        if (!GemstonePowerupsPlugin.gemTeamMap.containsKey(
                player.getUniqueId())
        ) {
            return null;
        }
        return GemstoneType.valueOf(
            GemstonePowerupsPlugin.gemTeamMap.get(player.getUniqueId())
        );
    }

    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        Boolean rc = false;
        if (command.getName().equalsIgnoreCase("gemstones")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    return false;
                }
                if (!player.hasPermission("gemstones.user")) {
                    return false;
                }
                String subcommand = args[0];
                switch (subcommand) {
                    case "choose": {
                        if (args.length < 2) {
                            rc = false;
                        }
                        else if (EnumUtils.isValidEnum(
                            GemstoneType.class, args[1].toUpperCase()
                        )) {
                            setGemTeam(
                                player,
                                GemstoneType.valueOf(args[1].toUpperCase())
                            );
                            player.sendMessage(
                                ChatColor.GREEN
                                + "Gemstone set to: "
                                + getGemTeam(player).toString()
                            );
                            rc = true;
                        }
                    }
                    break;
                    case "show": {
                        GemstoneType curGem = getGemTeam(player);
                        String gemString = "None";
                        if (curGem != null) {
                            gemString = curGem.toString();
                        }
                        player.sendMessage(
                            ChatColor.GOLD
                            + "Current Gemstone: "
                            + gemString
                        );
                        player.sendMessage(
                            ChatColor.GOLD
                            + "Gemstones Active: "
                            + getGemToggle(player).toString()
                        );
                        rc = true;
                    }
                    break;
                    case "toggle": {
                        if (args.length < 2) {
                            rc = false;
                        }
                        else {
                            Boolean toggle;
                            switch (args[1]) {
                                case "on": toggle = true;
                                break;
                                case "off": toggle = false;
                                break;
                                default: toggle = true;
                            }
                            setGemToggle(player, toggle);
                            player.sendMessage(
                                ChatColor.GOLD
                                + "Gemstones Status set to: "
                                + toggle.toString()
                            );
                            rc = true;
                        }
                    }
                    break;
                    case "enable": {
                        if (player.hasPermission("gemstones.admin")) {
                            this.potionsEnabled = true;
                            rc = true;
                        }
                        else {
                            rc = false;
                        }
                    }
                    break;
                    case "disable": {
                        if (player.hasPermission("gemstones.admin")) {
                            this.potionsEnabled = false;
                            rc = true;
                        }
                        else {
                            rc = false;
                        }
                    }
                    break;
                }
            }
        }
        return rc;
    }
}
