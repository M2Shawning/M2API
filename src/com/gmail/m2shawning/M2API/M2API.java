package com.gmail.m2shawning.M2API;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class M2API extends JavaPlugin {

    /*
    public static Arena getMiniTools() {
        return new Arena();
    }
    */

    public void onEnable() {

        if (!(Bukkit.getVersion().contains("MC: 1.12.2"))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Unsupported Version]");
        }

        /*
        String name;
        for (int i = 0; i < 16; i++) {
            Arena[] n = new Arena[4];
            n[i] = new Arena("Test");
            n[1].saveFile();
        }*/
    }
}
