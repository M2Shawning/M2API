package com.github.m2shawning.M2API;

import com.github.m2shawning.M2API.arena.Arena;
import com.github.m2shawning.M2API.arena.ArenaListener;
import com.github.m2shawning.M2API.team.Team;
import com.github.m2shawning.M2API.team.TeamListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class M2API extends JavaPlugin {

    public static ArrayList<Team> TeamArrayList = new ArrayList<>();
    public static ArrayList<Arena> ArenaArrayList = new ArrayList<>();

    public void onEnable() {

        if (!(Bukkit.getVersion().contains("MC: 1.13"))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Unsupported Version]");
            Bukkit.shutdown();
        }

        getServer().getPluginManager().registerEvents(new ArenaListener(), this);
        getServer().getPluginManager().registerEvents(new TeamListener(), this);

    }
}
