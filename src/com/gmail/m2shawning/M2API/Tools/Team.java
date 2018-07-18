package com.gmail.m2shawning.M2API.Tools;

import com.gmail.m2shawning.M2API.Utils.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Team {

    private String teamName;
    private ConfigManager configManager;

    public Team(String teamName, Plugin plugin, String fileName) {
        this.teamName = teamName;

        configManager = new ConfigManager(plugin, fileName);
    }

    public Team(String teamName, Plugin plugin, String fileName, File file) {
        this.teamName = teamName;

        configManager = new ConfigManager(plugin, fileName, file);
    }

    // Configuration Manager
    // -----------------------------------------------------------------------------------------------------------------

    // Saves the data file
    public void saveFile() {

        configManager.saveConfig();
        loadFile();
    }

    // Loads the data file
    public void loadFile() {


    }

    // Returns FileConfiguration for the data file
    public FileConfiguration getFile() {

        return configManager.getConfig();
    }



    // Team Addition
    // -----------------------------------------------------------------------------------------------------------------

    // Adds player to team
    // Team created if not made
    public void


}
