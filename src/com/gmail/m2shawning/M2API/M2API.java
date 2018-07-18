package com.gmail.m2shawning.M2API;

import com.gmail.m2shawning.M2API.Arena.Arena;
import com.gmail.m2shawning.M2API.Arena.ArenaListener;
import com.gmail.m2shawning.M2API.Team.Team;
import com.gmail.m2shawning.M2API.Team.TeamListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class M2API extends JavaPlugin {

    /*
    public static Arena getMiniTools() {
        return new Arena();
    }
    */

    public static ArrayList<Team> TeamArrayList = new ArrayList<>();
    public static ArrayList<Arena> ArenaArrayList = new ArrayList<>();

    public void onEnable() {

        if (!(Bukkit.getVersion().contains("MC: 1.12.2"))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Unsupported Version]");
        }

        getServer().getPluginManager().registerEvents(new ArenaListener(), this);
        getServer().getPluginManager().registerEvents(new TeamListener(), this);


        /*
        String name;
        for (int i = 0; i < 16; i++) {
            Arena[] n = new Arena[4];
            n[i] = new Arena("Test");
            n[1].saveFile();
        }*/
    }
}
