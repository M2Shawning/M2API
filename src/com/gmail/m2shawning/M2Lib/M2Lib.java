package com.gmail.m2shawning.M2Lib;

import com.gmail.m2shawning.M2Lib.Tools.MiniTools;
import org.bukkit.plugin.java.JavaPlugin;

public class M2Lib extends JavaPlugin {

    public static MiniTools getMiniTools() {
        return new MiniTools();
    }
}
