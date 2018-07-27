package com.gitlab.m2shawning.M2API.team;

import com.gitlab.m2shawning.M2API.M2API;
import com.gitlab.m2shawning.M2API.utils.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class Team {

    public String teamName;
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
    protected boolean friendlyFire;
    protected boolean blockInteract;
    protected boolean blockPlace;
    protected boolean blockBreak;



    // Configuration Manager
    // -----------------------------------------------------------------------------------------------------------------

    // Saves the data file
    public void saveFile() {

        // Adds playerUUIDArrayList to file in format of a String
        // UUIDs separated by spaces and ArrayList brackets are removed
        configManager.getConfig().set(teamName + ".playerUUIDs", playerUUIDArrayList.toString()
                .replaceAll("\\[", "").replaceAll("\\]", "") + " ");

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

            e.printStackTrace();
        }

        playerRead: try {

            // Breaks if null
            if (configManager.getConfig().getString(teamName + ".playerUUIDs") == null) {
                break playerRead;
            }

            // Clears playerUUIDArrayList
            playerUUIDArrayList.clear();

            // Loops through Array of playerUUIDs that have been gathered from a string and split up by spaces
            for (String playerUUID : configManager.getConfig().getString(teamName + ".playerUUIDs").split(" ")) {

                // Adds playerUUID to playerUUIDArrayList
                playerUUIDArrayList.add(UUID.fromString(playerUUID));
            }
        } catch (NullPointerException e) {

            e.printStackTrace();
        }
    }

    // Returns FileConfiguration for the data file
    public FileConfiguration getFile() {

        return configManager.getConfig();
    }



    // team Players
    // -----------------------------------------------------------------------------------------------------------------

    // Adds player to team
    // team file nodes created if not made
    public void addToTeam(UUID playerUUID) {

        if (!(playerUUIDArrayList.contains(playerUUID))) {
            playerUUIDArrayList.add(playerUUID);
        }

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

    // Returns the team said player is on
    public static Team teamPlayerIsOn(Player player) {

        for (Team team : M2API.TeamArrayList) {
            if (team.isPlayerOnTeam(player)) {
                return team;
            }
        }

        return null;
    }



    // team Permissions
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
