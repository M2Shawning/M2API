package com.gmail.m2shawning.M2API.Team;

import com.gmail.m2shawning.M2API.M2API;
import com.gmail.m2shawning.M2API.Utils.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class Team {

    private String teamName;
    private ConfigManager configManager;

    public Team(String teamName, Plugin plugin, String fileName) {

        this.teamName = teamName;

        M2API.TeamArrayList.add(this);

        configManager = new ConfigManager(plugin, fileName);
        setDefaults();
        loadFile();
    }

    public Team(String teamName, Plugin plugin, String fileName, File file) {

        this.teamName = teamName;

        M2API.TeamArrayList.add(this);

        configManager = new ConfigManager(plugin, fileName, file);
        setDefaults();
        loadFile();
    }

    // Sets default file values
    private void setDefaults() {

        configManager.getConfig().addDefault(teamName + ".friendlyFire", false);
        configManager.getConfig().addDefault(teamName + ".worldInteraction", false);
        configManager.getConfig().addDefault(teamName + ".blockPlace", false);
        configManager.getConfig().addDefault(teamName + ".blockBreak", false);
    }

    private ArrayList<UUID> playerUUIDArrayList = new ArrayList<>();
    public boolean friendlyFire;
    public boolean blockInteract;
    public boolean blockPlace;
    public boolean blockBreak;



    // Configuration Manager
    // -----------------------------------------------------------------------------------------------------------------

    // Saves the data file
    public void saveFile() {

        configManager.getConfig().set(teamName + ".playerUUIDs", playerUUIDArrayList.toString());
        configManager.getConfig().set(teamName + ".friendlyFire", friendlyFire);
        configManager.getConfig().set(teamName + ".blockInteract", blockInteract);
        configManager.getConfig().set(teamName + ".blockPlace", blockPlace);
        configManager.getConfig().set(teamName + ".blockBreak", blockBreak);

        configManager.saveConfig();
        loadFile();
    }

    // Loads the data file
    public void loadFile() {

        try {

            friendlyFire = configManager.getConfig().getBoolean(teamName + ".friendlyFire");
            blockInteract = configManager.getConfig().getBoolean(teamName + ".blockInteract");
            blockPlace = configManager.getConfig().getBoolean(teamName + ".blockPlace");
            blockBreak = configManager.getConfig().getBoolean(teamName + ".blockBreak");

        } catch (NullPointerException e) {

        }

        try {

            playerUUIDArrayList.clear();
            for (String string : configManager.getConfig().getStringList(teamName + ".playerUUIDs")) {

                playerUUIDArrayList.add(UUID.fromString(string));
            }

        } catch (NullPointerException e) {

        }
    }

    // Returns FileConfiguration for the data file
    public FileConfiguration getFile() {

        return configManager.getConfig();
    }



    // Team Players
    // -----------------------------------------------------------------------------------------------------------------

    // Adds player to team
    // Team file nodes created if not made
    public void addToTeam(UUID playerUUID) {

        playerUUIDArrayList.add(playerUUID);
        saveFile();
    }

    // Removes player from team
    public void removeFromTeam(UUID playerUUID) {

        playerUUIDArrayList.remove(playerUUID);
        saveFile();
    }

    // Checks if player is on team
    public boolean isPlayerOnTeam(Player player) {

        return playerUUIDArrayList.contains(player.getUniqueId());
    }

    // Return list of player on team
    public ArrayList<UUID> playersOnTeam() {

        return playerUUIDArrayList;
    }



    // Team Permissions
    // -----------------------------------------------------------------------------------------------------------------

    // Changes friendly fire
    public void setCanFriendlyFire(Boolean canFriendlyFire) {

        friendlyFire = canFriendlyFire;
    }

    // Changes world interaction
    public void setCanBlockInteract(Boolean canBlockInteract) {

        blockInteract = canBlockInteract;
    }

    // Changes if players can place blocks
    public void setCanBlockPlace(Boolean canBlockPlace) {

        blockPlace = canBlockPlace;
    }

    // Changes if players can break blocks
    public void setCanBlockBreak(Boolean canBlockBreak) {

        blockBreak = canBlockBreak;
    }

}
