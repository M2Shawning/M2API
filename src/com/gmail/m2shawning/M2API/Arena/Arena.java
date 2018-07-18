package com.gmail.m2shawning.M2API.Arena;

import com.gmail.m2shawning.M2API.M2API;
import com.gmail.m2shawning.M2API.Utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class Arena {

    private String arenaName;
    private ConfigManager configManager;

    public Arena(String arenaName, Plugin plugin, String fileName) {

        this.arenaName = arenaName;

        M2API.ArenaArrayList.add(this);

        configManager = new ConfigManager(plugin, fileName);
        setDefaults();
        loadFile();
    }

    public Arena(String arenaName, Plugin plugin, String fileName, File file) {

        this.arenaName = arenaName;

        M2API.ArenaArrayList.add(this);

        configManager = new ConfigManager(plugin, fileName, file);
        setDefaults();
        loadFile();
    }

    // Sets default file values
    private void setDefaults() {

        configManager.getConfig().addDefault(arenaName + ".worldName", "world");
        configManager.getConfig().addDefault(arenaName + ".minX", 0);
        configManager.getConfig().addDefault(arenaName + ".minY", 0);
        configManager.getConfig().addDefault(arenaName + ".minZ", 0);
        configManager.getConfig().addDefault(arenaName + ".maxX", 0);
        configManager.getConfig().addDefault(arenaName + ".maxY", 0);
        configManager.getConfig().addDefault(arenaName + ".maxZ", 0);

        configManager.getConfig().addDefault(arenaName + ".friendlyFire", false);
        configManager.getConfig().addDefault(arenaName + ".worldInteraction", false);
        configManager.getConfig().addDefault(arenaName + ".blockPlace", false);
        configManager.getConfig().addDefault(arenaName + ".blockBreak", false);
    }

    private int minX, minY, minZ;
    private int maxX, maxY, maxZ;
    private String worldName;
    public boolean playerDamage;
    public boolean blockInteract;
    public boolean blockPlace;
    public boolean blockBreak;
    private ArrayList<String> coordArrayList = new ArrayList<>();



    // Configuration Manager
    // -----------------------------------------------------------------------------------------------------------------

    // Saves the data file
    public void saveFile() {

        configManager.getConfig().set(arenaName + ".worldName", worldName);
        configManager.getConfig().set(arenaName + ".minX", minX);
        configManager.getConfig().set(arenaName + ".minY", minY);
        configManager.getConfig().set(arenaName + ".minZ", minZ);
        configManager.getConfig().set(arenaName + ".maxX", maxX);
        configManager.getConfig().set(arenaName + ".maxY", maxY);
        configManager.getConfig().set(arenaName + ".maxZ", maxZ);

        configManager.getConfig().set(arenaName + ".playerDamage", playerDamage);
        configManager.getConfig().set(arenaName + ".blockInteract", blockInteract);
        configManager.getConfig().set(arenaName + ".blockPlace", blockPlace);
        configManager.getConfig().set(arenaName + ".blockBreak", blockBreak);

        configManager.saveConfig();
        loadFile();
    }

    // Loads the data file
    public void loadFile() {

        try {

            // Read general location data from the data file
            minX = configManager.getConfig().getInt(arenaName + ".minX");
            minY = configManager.getConfig().getInt(arenaName + ".minY");
            minZ = configManager.getConfig().getInt(arenaName + ".minZ");
            maxX = configManager.getConfig().getInt(arenaName + ".maxX");
            maxY = configManager.getConfig().getInt(arenaName + ".maxY");
            maxZ = configManager.getConfig().getInt(arenaName + ".maxZ");
            worldName = configManager.getConfig().getString(arenaName + ".worldName");

            // Read property data from the data file
            playerDamage = configManager.getConfig().getBoolean(arenaName + ".playerDamage");
            blockInteract = configManager.getConfig().getBoolean(arenaName + ".blockInteract");
            blockPlace = configManager.getConfig().getBoolean(arenaName + ".blockPlace");
            blockBreak = configManager.getConfig().getBoolean(arenaName + ".blockBreak");

        } catch (NullPointerException e) {

        }

        try {

            // Read coordinates from the data file
            coordArrayList.clear();
            for (String key : configManager.getConfig().getConfigurationSection(arenaName + ".SpawnPoints").getKeys(false)) {
                coordArrayList.add(configManager.getConfig().getString(arenaName + ".SpawnPoints." + key + ".spawnCoord"));

            }

        } catch (NullPointerException e) {

        }
    }

    // Returns FileConfiguration for the data file
    public FileConfiguration getFile() {

        return configManager.getConfig();
    }



    // Arena Creation
    // -----------------------------------------------------------------------------------------------------------------

    // Generates a the given game area
    // Returns false if locations are not in the same world
    public void createArea(Location loc1, Location loc2) {

        if (!(loc1.getWorld().equals(loc2.getWorld()))) {
            return;
        }

        minX = loc1.getBlockX();
        minY = loc1.getBlockY();
        minZ = loc1.getBlockZ();
        maxX = loc2.getBlockX();
        maxY = loc2.getBlockY();
        maxZ = loc2.getBlockZ();
        worldName = loc1.getWorld().getName();
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

        saveFile();
    }



    // Area Deletion
    // -----------------------------------------------------------------------------------------------------------------

    // Deletes a given game area
    public void deleteArea() {

        configManager.getConfig().set(arenaName, null);

        saveFile();
    }



    // Spawn Point Creation
    // -----------------------------------------------------------------------------------------------------------------

    // Creates player spawn points
    public void createPlayerSpawnPoint(String pointName, Location loc) {

        String coordString = "";
        coordString += Integer.toString(loc.getBlockX());
        coordString += (" " + Integer.toString(loc.getBlockY()));
        coordString += (" " + Integer.toString(loc.getBlockZ()));
        coordString += (" " + Integer.toString((int)loc.getYaw()));
        coordString += (" " + Float.toString((int)loc.getPitch()));

        configManager.getConfig().set(arenaName + ".SpawnPoints." + pointName + ".spawnCoord", coordString);

        saveFile();
    }



    // Spawn Point Deletion
    // -----------------------------------------------------------------------------------------------------------------

    // Deletes a given spawn point
    public void deletePlayerSpawnPoint(String pointName) {

        configManager.getConfig().set(arenaName + ".SpawnPoints." + pointName, null);

        saveFile();
    }



    // Player In Area Checking
    // -----------------------------------------------------------------------------------------------------------------

    // Determines if player is within the given area
    public boolean isPlayerInArea(Player player) {

        if (player.getWorld().getName().equals(worldName)) {

            if ((player.getLocation().getBlockX() >= minX) && (player.getLocation().getBlockX() <= maxX)) {

                if ((player.getLocation().getBlockY() >= minY) && (player.getLocation().getBlockY() <= maxY)) {

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
    public ArrayList<UUID> getPlayersUUIDListInArea(Collection<? extends Player> playerCollection) {

        if (playerCollection.isEmpty()) {
            return null;
        }

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
    public void spawnAtRandom(ArrayList<UUID> playerUUIDArrayList) {

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
    public void spawnAtRandom(UUID playerUUID) {

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
    public void randomSpawnAtSpawnPoints(ArrayList<UUID> playerUUIDArrayList) {

        // Random Number Gen
        Random rand = new Random();

        // Declare Variables
        String coordString;
        int[] coordInt;

        // Create temporary Player UUID ArrayList
        ArrayList<UUID>tempPlayerUUIDArrayList = new ArrayList<>(playerUUIDArrayList);
        Collections.shuffle(tempPlayerUUIDArrayList, new Random());

        // Cycle through playerUUID ArrayList and 'spawn' players
        int counter = 0;
        for (UUID playerUUID : tempPlayerUUIDArrayList) {

            coordString = coordArrayList.get(counter);
            coordInt = Arrays.stream(coordString.split(" ")).mapToInt(Integer::parseInt).toArray();

            Location loc = new Location(Bukkit.getWorld(worldName),
                    (coordInt[0] + 0.5), coordInt[1], (coordInt[2] + 0.5), coordInt[3], coordInt[4]);

            Bukkit.getPlayer(playerUUID).teleport(loc);
            counter++;
        }
    }

    // Randomly spawns a player at a predefined spawn point
    public void randomSpawnAtSpawnPoints(UUID playerUUID) {

        Random rand = new Random();

        String coordString;
        int[] coordInt;

        coordString = coordArrayList.get(rand.nextInt(coordArrayList.size()));
        coordInt = Arrays.stream(coordString.split(" ")).mapToInt(Integer::parseInt).toArray();

        Location loc = new Location(Bukkit.getWorld(worldName), (coordInt[0] + 0.5), coordInt[1], (coordInt[2] + 0.5), coordInt[3], coordInt[4]);

        Bukkit.getPlayer(playerUUID).teleport(loc);
    }

    // Spawns a player at a predefined spawn point
    public void specificSpawnAtSpawnPoint(String spawnPointName, UUID playerUUID) {

        String coordString;
        int[] coordInt;

        coordString = configManager.getConfig().getString(arenaName + ".SpawnPoints." + spawnPointName + ".spawnCoord");
        coordInt = Arrays.stream(coordString.split(" ")).mapToInt(Integer::parseInt).toArray();

        Location loc = new Location(Bukkit.getWorld(worldName), (coordInt[0] + 0.5), coordInt[1], (coordInt[2] + 0.5), coordInt[3], coordInt[4]);

        Bukkit.getPlayer(playerUUID).teleport(loc);
    }



    // Area Permissions
    // -----------------------------------------------------------------------------------------------------------------

    // Changes friendly fire
    public void setCanPlayerDamage(Boolean canPlayerDamage) {

        playerDamage = canPlayerDamage;
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
