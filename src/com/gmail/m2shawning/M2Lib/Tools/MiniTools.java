package com.gmail.m2shawning.M2Lib.Tools;

import com.gmail.m2shawning.M2Lib.Utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class MiniTools {

    private ConfigManager configManager;
    public MiniTools(ConfigManager configManager, String areaName) {
        this.configManager = configManager;
    }

    // Area Creation
    // -----------------------------------------------------------------------------------------------------------------

    // Generates a the given game area
    // Returns false if locations are not in the same world
    public boolean createArea(String areaName, Location loc1, Location loc2) {

        if (!(loc1.getWorld().equals(loc2.getWorld()))) {
            return false;
        }

        int minX = loc1.getBlockX(), minY = loc1.getBlockY(), minZ = loc1.getBlockZ();
        int maxX = loc2.getBlockX(), maxY = loc2.getBlockY(), maxZ = loc2.getBlockZ();
        int temp;

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

        configManager.getConfig().set("Areas." + areaName + ".worldName", loc1.getWorld().getName());
        configManager.getConfig().set("Areas." + areaName + ".minX", minX);
        configManager.getConfig().set("Areas." + areaName + ".minY", minY);
        configManager.getConfig().set("Areas." + areaName + ".minZ", minZ);
        configManager.getConfig().set("Areas." + areaName + ".maxX", maxX);
        configManager.getConfig().set("Areas." + areaName + ".maxY", maxY);
        configManager.getConfig().set("Areas." + areaName + ".maxZ", maxZ);

        configManager.saveConfig();

        return true;
    }



    // Area Deletion
    // -----------------------------------------------------------------------------------------------------------------

    // Deletes a given game area
    public void deleteArea(String areaName) {

        configManager.getConfig().set("Areas." + areaName, null);
    }



    // Spawn Point Creation
    // -----------------------------------------------------------------------------------------------------------------

    // Creates player spawn points
    public void createPlayerSpawnPoint(String areaName, String pointName, Location loc) {

        String coordString = "";
        coordString += Integer.toString(loc.getBlockX());
        coordString += (" " + Integer.toString(loc.getBlockY()));
        coordString += (" " + Integer.toString(loc.getBlockZ()));
        coordString += (" " + Integer.toString((int)loc.getYaw()));
        coordString += (" " + Float.toString((int)loc.getPitch()));

        configManager.getConfig().set("SpawnPoints." + areaName + "." + pointName + ".spawnCoord", coordString);
    }



    // Spawn Point Deletion
    // -----------------------------------------------------------------------------------------------------------------

    // Deletes a given spawn point
    public void deletePlayerSpawnPoint(String areaName, String pointName) {

        configManager.getConfig().set("SpawnPoints." + areaName + "." + pointName, null);
    }



    // Player In Area Checking
    // -----------------------------------------------------------------------------------------------------------------

    // Determines if player is within the given area
    public boolean isPlayerInArea(String areaName, Player player) {

        String worldName = configManager.getConfig().getString("Areas." + areaName + ".worldName");
        if (player.getWorld().getName().equals(worldName)) {

            int minX = configManager.getConfig().getInt("Areas." + areaName + ".minX");
            int maxX = configManager.getConfig().getInt("Areas." + areaName + ".maxX");
            if ((player.getLocation().getBlockX() >= minX) && (player.getLocation().getBlockX() <= maxX)) {

                int minY = configManager.getConfig().getInt("Areas." + areaName + ".minY");
                int maxY = configManager.getConfig().getInt("Areas." + areaName + ".maxY");
                if ((player.getLocation().getBlockY() >= minY) && (player.getLocation().getBlockY() <= maxY)) {

                    int minZ = configManager.getConfig().getInt("Areas." + areaName + ".minZ");
                    int maxZ = configManager.getConfig().getInt("Areas." + areaName + ".maxZ");
                    if ((player.getLocation().getBlockZ() >= minZ) && (player.getLocation().getBlockZ() <= maxZ)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Returns an ArrayList of all player UUIDs that are within the specified area
    // Returns null if no players in playerCollection
    public ArrayList<UUID> getPlayersUUIDListInArea(String areaName, Collection<? extends Player> playerCollection) {

        if (playerCollection.isEmpty()) {
            return null;
        }

        String worldName = configManager.getConfig().getString("Areas." + areaName + ".worldName");
        int minX = configManager.getConfig().getInt("Areas." + areaName + ".minX");
        int minY = configManager.getConfig().getInt("Areas." + areaName + ".minY");
        int minZ = configManager.getConfig().getInt("Areas." + areaName + ".minZ");
        int maxX = configManager.getConfig().getInt("Areas." + areaName + ".maxX");
        int maxY = configManager.getConfig().getInt("Areas." + areaName + ".maxY");
        int maxZ = configManager.getConfig().getInt("Areas." + areaName + ".maxZ");

        ArrayList<UUID> tempPlayersUUIDList = new ArrayList<>();

        for (Player player : playerCollection) {
            if (player.getWorld().getName().equals(worldName)) {
                if ((player.getLocation().getBlockX() >= minX) && (player.getLocation().getBlockX() <= maxX)) {
                    if ((player.getLocation().getBlockY() >= minY) && (player.getLocation().getBlockY() <= maxY)) {
                        if ((player.getLocation().getBlockZ() >= minZ) && (player.getLocation().getBlockZ() <= maxZ)) {
                            tempPlayersUUIDList.add(player.getUniqueId());
                        }
                    }
                }
            }
        }

        return tempPlayersUUIDList;
    }



    // Player Spawning
    // -----------------------------------------------------------------------------------------------------------------

    // Randomly spawns players within a defined area
    public void spawnAtRandom(String areaName, ArrayList<UUID> playerUUIDArrayList) {

        String worldName = configManager.getConfig().getString("Areas." + areaName + ".worldName");
        int minX = configManager.getConfig().getInt("Areas." + areaName + ".minX");
        int minY = configManager.getConfig().getInt("Areas." + areaName + ".minY");
        int minZ = configManager.getConfig().getInt("Areas." + areaName + ".minZ");
        int maxX = configManager.getConfig().getInt("Areas." + areaName + ".maxX");
        int maxY = configManager.getConfig().getInt("Areas." + areaName + ".maxY");
        int maxZ = configManager.getConfig().getInt("Areas." + areaName + ".maxZ");
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

    // Randomly spawns a player within a defined area
    public void spawnAtRandom(String areaName, UUID playerUUID) {

        String worldName = configManager.getConfig().getString("Areas." + areaName + ".worldName");
        int minX = configManager.getConfig().getInt("Areas." + areaName + ".minX");
        int minY = configManager.getConfig().getInt("Areas." + areaName + ".minY");
        int minZ = configManager.getConfig().getInt("Areas." + areaName + ".minZ");
        int maxX = configManager.getConfig().getInt("Areas." + areaName + ".maxX");
        int maxY = configManager.getConfig().getInt("Areas." + areaName + ".maxY");
        int maxZ = configManager.getConfig().getInt("Areas." + areaName + ".maxZ");
        int x, y, z, yaw;

        Random rand = new Random();
        World world = Bukkit.getWorld(worldName);
        boolean foundLocation = false;

        while (!(foundLocation)) {

            x = (rand.nextInt((maxX - minX) + 1) + minX);
            z = (rand.nextInt((maxZ - minZ) + 1) + minZ);
            yaw = (rand.nextInt(360));

            for (y = minY; y <= maxY; y++) {
                if ((world.getBlockAt(x, y, z).getType().toString().equals("AIR")) && (world.getBlockAt(x, (y + 1), z)
                        .getType().toString().equals("AIR")) && (world.getBlockAt(x, (y - 1), z).getType().isSolid())) {

                    Bukkit.getPlayer(playerUUID).teleport(new Location(world, (x + 0.5), y, (z + 0.5), yaw, 0));
                    foundLocation = true;
                    break;
                }
            }
        }
    }

    // Randomly spawns players at predefined spawn points
    public void randomSpawnAtSpawnPoints(String areaName, ArrayList<UUID> playerUUIDArrayList) {

        // Random Number Gen
        Random rand = new Random();

        // Declare Variables
        String coordString;
        int[] coordInt;
        int yaw = rand.nextInt(360);

        // Create temporary Player UUID ArrayList
        ArrayList<UUID>tempPlayerUUIDArrayList = new ArrayList<>(playerUUIDArrayList);
        Collections.shuffle(tempPlayerUUIDArrayList, new Random());

        // Read coordinates from the storage file
        ArrayList<String> coordArrayList = new ArrayList<>();
        for (String key : configManager.getConfig().getConfigurationSection("SpawnPoints." + areaName).getKeys(false)) {
            coordArrayList.add(configManager.getConfig().getString("SpawnPoints." + areaName + "." + key + ".spawnCoord"));
        }
        Collections.shuffle(coordArrayList, new Random());

        // Cycle through playerUUID ArrayList and 'spawn' players
        int counter = 0;
        for (UUID playerUUID : tempPlayerUUIDArrayList) {

            coordString = coordArrayList.get(counter);
            coordInt = Arrays.stream(coordString.split(" ")).mapToInt(Integer::parseInt).toArray();

            Location loc = new Location(Bukkit.getWorld(configManager.getConfig().getString("Areas." + areaName + ".worldName")),
                    coordInt[0] + 0.5, coordInt[1], coordInt[2] + 0.5, yaw, 0);

            Bukkit.getPlayer(playerUUID).teleport(loc);
            counter++;
        }
    }

    // Randomly spawns a player at a predefined spawn point
    public void randomSpawnAtSpawnPoints(String areaName, UUID playerUUID) {

        Random rand = new Random();

        String coordString;
        int[] coordInt;

        // Read coordinates from the storage file
        ArrayList<String> coordArrayList = new ArrayList<>();
        for (String key : configManager.getConfig().getConfigurationSection("SpawnPoints." + areaName).getKeys(false)) {
            coordArrayList.add(configManager.getConfig().getString("SpawnPoints." + areaName + "." + key + ".spawnCoord"));
        }
        Collections.shuffle(coordArrayList, new Random());

        coordString = coordArrayList.get(rand.nextInt(coordArrayList.size()));
        coordInt = Arrays.stream(coordString.split(" ")).mapToInt(Integer::parseInt).toArray();

        Location loc = new Location(Bukkit.getWorld(configManager.getConfig().getString("Areas." + areaName +
                ".worldName")), coordInt[0] + 0.5, coordInt[1], coordInt[2] + 0.5, coordInt[3], coordInt[4]);

        Bukkit.getPlayer(playerUUID).teleport(loc);
    }

    // Spawns a player at a predefined spawn point
    public void specificSpawnAtSpawnPoint(String areaName, String spawnPointName, UUID playerUUID) {

        String coordString;
        int[] coordInt;

        coordString = configManager.getConfig().getString("SpawnPoints." + areaName + "." + spawnPointName + ".spawnCoord");
        coordInt = Arrays.stream(coordString.split(" ")).mapToInt(Integer::parseInt).toArray();

        Location loc = new Location(Bukkit.getWorld(configManager.getConfig().getString("Areas." + areaName +
                ".worldName")), coordInt[0] + 0.5, coordInt[1], coordInt[2] + 0.5, coordInt[3], coordInt[4]);

        Bukkit.getPlayer(playerUUID).teleport(loc);
    }
}
