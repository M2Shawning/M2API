package com.gmail.m2shawning.M2Lib.Tools;

import com.gmail.m2shawning.M2Lib.Utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public class MiniTools {

    // Area Creation
    // -----------------------------------------------------------------------------------------------------------------

    // Generates A Game Area
    // Returns false if locations are not in the same world
    public boolean createArea(ConfigManager configManager, String areaName, Location loc1, Location loc2) {

        if (!(loc1.getWorld().equals(loc2.getWorld()))) {
            return false;
        }

        double minX = loc1.getX(), minY = loc1.getY(), minZ = loc1.getZ();
        double maxX = loc2.getX(), maxY = loc2.getY(), maxZ = loc2.getZ();
        double temp;

        if (minX > maxX) {
            temp = minX;
            minX = maxX;
            maxX = temp;
        }
        if (minY > maxY) {
            temp = minY;
            minY = maxY;
            maxY = temp;
        }
        if (minZ > maxZ) {
            temp = minZ;
            minZ = maxZ;
            maxZ = temp;
        }

        configManager.getConfig().set(areaName + ".worldName", loc1.getWorld().getName());
        configManager.getConfig().set(areaName + ".minX", minX);
        configManager.getConfig().set(areaName + ".minY", minY);
        configManager.getConfig().set(areaName + ".minZ", minZ);
        configManager.getConfig().set(areaName + ".maxX", maxX);
        configManager.getConfig().set(areaName + ".maxY", maxY);
        configManager.getConfig().set(areaName + ".maxZ", maxZ);

        configManager.saveConfig();

        return true;
    }



    // Spawn Point Creation
    // -----------------------------------------------------------------------------------------------------------------

    // Creates player spawn points
    public void createPlayerSpawnPoint(ConfigManager configManager, String areaName) {}

    // Creates multiple team spawn points
    public void createTeamSpawnPoint() {}



    // Player In Area Checking
    // -----------------------------------------------------------------------------------------------------------------

    // Determines if player is within the given area
    public boolean isPlayerInArea(ConfigManager configManager, String areaName, Player player) {

        String worldName = configManager.getConfig().getString(areaName + ".worldName");
        if (player.getWorld().getName().equals(worldName)) {

            int minX = configManager.getConfig().getInt(areaName + ".minX");
            int maxX = configManager.getConfig().getInt(areaName + ".maxX");
            if ((player.getLocation().getX() >= minX) && (player.getLocation().getX() <= maxX)) {

                int minY = configManager.getConfig().getInt(areaName + ".minY");
                int maxY = configManager.getConfig().getInt(areaName + ".maxY");
                if ((player.getLocation().getY() >= minY) && (player.getLocation().getY() <= maxY)) {

                    int minZ = configManager.getConfig().getInt(areaName + ".minZ");
                    int maxZ = configManager.getConfig().getInt(areaName + ".maxZ");
                    if ((player.getLocation().getZ() >= minZ) && (player.getLocation().getZ() <= maxZ)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Returns an ArrayList of all player UUIDs that are within the specified area
    // Returns null if no players in playerCollection
    public ArrayList<UUID> getPlayersUUIDListInArea(ConfigManager configManager, String areaName,
                                                    Collection<? extends Player> playerCollection) {

        if (playerCollection.isEmpty()) {
            return null;
        }

        String worldName = configManager.getConfig().getString(areaName + ".worldName");
        int minX = configManager.getConfig().getInt(areaName + ".minX");
        int minY = configManager.getConfig().getInt(areaName + ".minY");
        int minZ = configManager.getConfig().getInt(areaName + ".minZ");
        int maxX = configManager.getConfig().getInt(areaName + ".maxX");
        int maxY = configManager.getConfig().getInt(areaName + ".maxY");
        int maxZ = configManager.getConfig().getInt(areaName + ".maxZ");

        ArrayList<UUID> tempPlayersUUIDList = new ArrayList<>();

        for (Player player : playerCollection) {
            if (player.getWorld().getName().equals(worldName)) {
                if ((player.getLocation().getX() >= minX) && (player.getLocation().getX() <= maxX)) {
                    if ((player.getLocation().getY() >= minY) && (player.getLocation().getY() <= maxY)) {
                        if ((player.getLocation().getZ() >= minZ) && (player.getLocation().getZ() <= maxZ)) {
                            tempPlayersUUIDList.add(player.getUniqueId());
                        }
                    }
                }
            }
        }

        return tempPlayersUUIDList;
    }



    // Player/Team Spawning
    // -----------------------------------------------------------------------------------------------------------------

    // Randomly spawns a player within a defined area
    public void spawnPlayerAtRandom(ConfigManager configManager, String areaName, ArrayList<UUID> playerUUIDArrayList) {

        String worldName = configManager.getConfig().getString(areaName + ".worldName");
        int minX = configManager.getConfig().getInt(areaName + ".minX");
        int minY = configManager.getConfig().getInt(areaName + ".minY");
        int minZ = configManager.getConfig().getInt(areaName + ".minZ");
        int maxX = configManager.getConfig().getInt(areaName + ".maxX");
        int maxY = configManager.getConfig().getInt(areaName + ".maxY");
        int maxZ = configManager.getConfig().getInt(areaName + ".maxZ");
        int x, y, z, yaw;

        Player player;
        World world = Bukkit.getWorld(worldName);
        Random rand = new Random();

        for (int counter = 0; counter < playerUUIDArrayList.size(); counter++) {

            player = Bukkit.getPlayer(playerUUIDArrayList.get(counter));
            x = (rand.nextInt((maxX - minX) + 1) + minX);
            z = (rand.nextInt((maxZ - minZ) + 1) + minZ);
            yaw = (rand.nextInt(360));

            for (y = minY; y <= maxY; y++) {
                if ((world.getBlockAt(x, y, z).getType().toString().equals("AIR")) && (world.getBlockAt(x, (y + 1), z)
                        .getType().toString().equals("AIR")) && (world.getBlockAt(x, (y - 1), z).getType().isSolid())) {

                    player.teleport(new Location(world, (x + 0.5), y, (z + 0.5), yaw, 0));
                    counter++;
                    break;
                }
            }

            counter--;
        }
    }

    // Spawns a player at predefined spawn points
    public void spawnPlayerAtSpawnPoint() {}

    // Spawns multiple teams of players a predefined spawn points
    public void spawnTeamAtSpawnPoint() {}
}
