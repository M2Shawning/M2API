package com.github.m2shawning.M2API.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    // Files & File Configs
    private File file;
    private FileConfiguration fileConfig;

    // -----------------------------------------------------------------------------------------------------------------

    public ConfigManager(Plugin plugin, String fileName, File file) {

        // Assigns Extension
        if (!(fileName.contains(".yml"))) {
            fileName += (".yml");
        }

        // Creates Plugin Folder
        if (!file.exists()) {
            file.mkdir();
        }

        // Sets File Location
        this.file = new File(file, fileName);

        // Saves Default Config File
        if (fileName.equals("config.yml")) {
            if (!this.file.exists()) {
                plugin.saveDefaultConfig();
            }
        }

        // Loads Config File
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public ConfigManager(Plugin plugin, String fileName) {

        // Assigns Extension
        if (!(fileName.contains(".yml"))) {
            fileName += ".yml";
        }

        // Creates Plugin Folder
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        // Sets File Location
        this.file = new File(plugin.getDataFolder(), fileName);

        if (fileName.equals("config.yml")) {
            // Saves Default Config File
            if (!this.file.exists()) {
                plugin.saveDefaultConfig();
            }
        }

        // Loads Config File
        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return this.fileConfig;
    }

    public void saveConfig() {
        try {
            this.fileConfig.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
