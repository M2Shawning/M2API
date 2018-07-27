package com.gmail.m2shawning.M2API.arena;

import com.gmail.m2shawning.M2API.M2API;
import com.gmail.m2shawning.M2API.team.Team;
import com.gmail.m2shawning.M2API.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class Arena {

    public String arenaName;
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
    protected boolean playerDamage;
    protected boolean blockInteract;
    protected boolean blockPlace;
    protected boolean blockBreak;

    private ArrayList<String> coordNameList = new ArrayList<>();
    private ArrayList<List<Integer>> coordArrayList = new ArrayList<>();

    private ArrayList<String> teamWithCoordNameList = new ArrayList<>();
    private ArrayList<ArrayList<List<Integer>>> teamsCoordArrayList = new ArrayList<>();



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

        // Deals with arena variables
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

            e.printStackTrace();
        }

        // Deals with standard Spawn Point variables
        stdSpawnPoints: try {

            // Breaks if null
            if (!configManager.getConfig().contains(arenaName + ".SpawnPoints")) {
                break stdSpawnPoints;
            }

            // Clears Variables
            coordNameList.clear();
            coordArrayList.clear();

            // Loops through Spawn Points
            for (String key : configManager.getConfig().getConfigurationSection(arenaName + ".SpawnPoints").getKeys(false)) {

                // Sets Spawn Point Name and Spawn Point Integer ArrayList to variables
                coordNameList.add(key);
                coordArrayList.add(configManager.getConfig().getIntegerList(arenaName + ".SpawnPoints." + key + ".spawnCoord"));
            }

        } catch (NullPointerException e) {

            e.printStackTrace();
        }

        // Deals with team Spawn Point variables
        teamSpawnPoints: try {

            // Breaks if null
            if (M2API.TeamArrayList == null || !configManager.getConfig().contains(arenaName + ".TeamSpawnPoints")) {
                break teamSpawnPoints;
            }

            // Creates temporary variable
            ArrayList<List<Integer>> tempArrayOfIntegerList = new ArrayList<>();

            // Clears Variables
            teamWithCoordNameList.clear();
            teamsCoordArrayList.clear();

            // Loops through teams
            for (Team team : M2API.TeamArrayList) {

                // Clears temporary variable for each team and adds team Name to ArrayList
                tempArrayOfIntegerList.clear();
                teamWithCoordNameList.add(team.teamName);

                // Loops through each teams Spawn Points
                for (String key : configManager.getConfig().getConfigurationSection(arenaName + ".TeamSpawnPoints." + team.teamName).getKeys(false)) {

                    // Adds Spawn Point Integer ArrayList to temporary ArrayList
                    tempArrayOfIntegerList.add(configManager.getConfig().getIntegerList(arenaName + ".TeamSpawnPoints." + team.teamName + "." + key + ".spawnCoord"));
                }

                // Adds temporary ArrayList to full ArrayList
                teamsCoordArrayList.add(tempArrayOfIntegerList);
            }


        } catch (NullPointerException e){

            e.printStackTrace();
        }
    }

    // Returns FileConfiguration for the data file
    public FileConfiguration getFile() {

        return configManager.getConfig();
    }



    // arena Creation
    // -----------------------------------------------------------------------------------------------------------------

    // Generates a the given game area
    // Returns false if locations are not in the same world
    public void createArena(Location loc1, Location loc2) {

        // Makes sure locations are in the same world
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



    // arena Deletion
    // -----------------------------------------------------------------------------------------------------------------

    // Deletes a given game area
    public void deleteArena() {

        configManager.getConfig().set(arenaName, null);
        configManager.saveConfig();

        // Loads file before executing save method to also wipe variables
        loadFile();

        saveFile();
    }



    // Spawn Point Creation
    // -----------------------------------------------------------------------------------------------------------------

    // Creates player spawn point
    public void createPlayerSpawnPoint(String pointName, Location loc) {

        ArrayList<Integer> coordIntegerArrayList = new ArrayList<>();
        coordIntegerArrayList.add(loc.getBlockX());
        coordIntegerArrayList.add(loc.getBlockY());
        coordIntegerArrayList.add(loc.getBlockZ());
        coordIntegerArrayList.add((int)loc.getYaw());
        coordIntegerArrayList.add((int)loc.getPitch());

        configManager.getConfig().set(arenaName + ".SpawnPoints." + pointName + ".spawnCoord", coordIntegerArrayList);

        saveFile();
    }

    // Creates team spawn point
    public void createTeamSpawnPoint(String pointName, String teamName, Location loc) {

        ArrayList<Integer> coordIntegerArrayList = new ArrayList<>();
        coordIntegerArrayList.add(loc.getBlockX());
        coordIntegerArrayList.add(loc.getBlockY());
        coordIntegerArrayList.add(loc.getBlockZ());
        coordIntegerArrayList.add((int)loc.getYaw());
        coordIntegerArrayList.add((int)loc.getPitch());

        configManager.getConfig().set(arenaName + ".TeamSpawnPoints." + teamName +  "." + pointName + ".spawnCoord", coordIntegerArrayList);

        saveFile();
    }



    // Spawn Point Deletion
    // -----------------------------------------------------------------------------------------------------------------

    // Deletes a given player spawn point
    public void deletePlayerSpawnPoint(String pointName) {

        configManager.getConfig().set(arenaName + ".SpawnPoints." + pointName, null);
        configManager.saveConfig();

        // Loads file before executing save method to also wipe variables
        loadFile();

        saveFile();
    }

    // Deletes all player spawn points for a given arena
    public void deleteAllPlayerSpawnPoints() {

        configManager.getConfig().set(arenaName + ".SpawnPoints", null);
        configManager.saveConfig();

        // Loads file before executing save method to also wipe variables
        loadFile();

        saveFile();
    }

    // Deletes a given team spawn point
    public void deleteTeamSpawnPoint(String pointName, String teamName) {

        configManager.getConfig().set(arenaName + ".TeamSpawnPoints." + teamName +  "." + pointName + ".spawnCoord", null);
        configManager.saveConfig();

        // Loads file before executing save method to also wipe variables
        loadFile();

        saveFile();
    }

    // Deletes all team spawn points for a given area
    public void deleteAllTeamSpawnPoints(String teamName) {

        configManager.getConfig().set(arenaName + ".TeamSpawnPoints." + teamName, null);
        configManager.saveConfig();

        // Loads file before executing save method to also wipe variables
        loadFile();

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
        int timeoutCounter = 0;

        Player player;
        World world = Bukkit.getWorld(worldName);
        Random rand = new Random();

        // Loops through given PlayerUUID ArrayList
        for (int counter = 0; counter < playerUUIDArrayList.size(); counter++) {

            // Sets the given player, randomizes an x, z, and yaw all in their respective ranges
            player = Bukkit.getPlayer(playerUUIDArrayList.get(counter));
            x = (rand.nextInt((maxX - minX) + 1) + minX);
            z = (rand.nextInt((maxZ - minZ) + 1) + minZ);
            yaw = (rand.nextInt(360));

            // Loops through y within arena's range
            for (y = minY; y <= maxY; y++) {

                // Checks if the player can safely be teleported to a target location
                if ((world.getBlockAt(x, y, z).getType().toString().equals("AIR")) && (world.getBlockAt(x, (y + 1), z)
                        .getType().toString().equals("AIR")) && (world.getBlockAt(x, (y - 1), z).getType().isSolid())) {

                    player.teleport(new Location(world, (x + 0.5), y, (z + 0.5), yaw, 0));

                    // Resets timeoutCounter
                    timeoutCounter = 0;

                    // Adds to the counter to counteract the negative counter and stops search
                    counter++;
                    break;
                }
            }

            // Protects invalid arena size from lagging server
            timeoutCounter++;
            if (timeoutCounter >= 100) {
                player.sendMessage(ChatColor.RED + "[M2API] Failed To Find Suitable Location");
                player.sendMessage(ChatColor.RED + "[M2API] Contact Staff");

                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[M2API] Timeout Protector");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[M2API] Failed To Find Suitable Location For: " + player.getName());
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[M2API] Check arena Configuration");

                continue;
            }

            // In the event that a location is not found, this keeps the player selected for another loop
            counter--;
        }
    }

    // Randomly spawns a player within a defined area
    public void spawnAtRandom(UUID playerUUID) {

        int x, y, z, yaw;
        int timeoutCounter = 0;

        Random rand = new Random();
        World world = Bukkit.getWorld(worldName);
        boolean foundLocation = false;

        while (!(foundLocation)) {

            // Sets the given player, randomizes an x, z, and yaw all in their respective ranges
            x = (rand.nextInt((maxX - minX) + 1) + minX);
            z = (rand.nextInt((maxZ - minZ) + 1) + minZ);
            yaw = (rand.nextInt(360));

            // Checks if the player can safely be teleported to a target location
            for (y = minY; y <= maxY; y++) {
                if ((world.getBlockAt(x, y, z).getType().toString().equals("AIR")) && (world.getBlockAt(x, (y + 1), z)
                        .getType().toString().equals("AIR")) && (world.getBlockAt(x, (y - 1), z).getType().isSolid())) {

                    Bukkit.getPlayer(playerUUID).teleport(new Location(world, (x + 0.5), y, (z + 0.5), yaw, 0));

                    timeoutCounter = 0;

                    // Stops looping through both the y and the random x, z coordinates
                    foundLocation = true;
                    break;
                }
            }

            // Protects invalid arena size from lagging server
            timeoutCounter++;
            if (timeoutCounter >= 100) {
                Bukkit.getPlayer(playerUUID).sendMessage(ChatColor.RED + "[M2API] Failed To Find Suitable Location");
                Bukkit.getPlayer(playerUUID).sendMessage(ChatColor.RED + "[M2API] Contact Staff");

                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[M2API] Timeout Protector");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[M2API] Failed To Find Suitable Location For: "
                        + Bukkit.getPlayer(playerUUID).getName());
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[M2API] Check arena Configuration");

                break;
            }
        }
    }

    // Randomly spawns players at predefined spawn points
    public void randomSpawnAtSpawnPoints(ArrayList<UUID> playerUUIDArrayList) {

        Random rand = new Random();

        // Creates temporary Player UUID ArrayList and randomizes said ArrayList
        ArrayList<UUID>tempPlayerUUIDArrayList = new ArrayList<>(playerUUIDArrayList);
        Collections.shuffle(tempPlayerUUIDArrayList, new Random());

        // Creates intArray for coordinates
        Integer[] intArray = new Integer[coordArrayList.get(0).size()];

        // Cycles through playerUUID ArrayList and 'spawn' players
        int counter = 0;
        for (UUID playerUUID : tempPlayerUUIDArrayList) {

            // Sequentially grabs coordinates
            intArray = coordArrayList.get(counter).toArray(intArray);

            Location loc = new Location(Bukkit.getWorld(worldName), intArray[0], intArray[1], intArray[2], intArray[3], intArray[4]);
            Bukkit.getPlayer(playerUUID).teleport(loc);

            counter++;
        }
    }

    // Randomly spawns a player at a predefined spawn point
    public void randomSpawnAtSpawnPoints(UUID playerUUID) {

        Random rand = new Random();

        // Creates intArray for coordinates and randomly selects a coordinate for said ArrayList
        Integer[] intArray = new Integer[coordArrayList.get(0).size()];
        intArray = coordArrayList.get(rand.nextInt(coordArrayList.size())).toArray(intArray);

        Location loc = new Location(Bukkit.getWorld(worldName), intArray[0], intArray[1], intArray[2], intArray[3], intArray[4]);

        Bukkit.getPlayer(playerUUID).teleport(loc);
    }

    // Spawns a player at a predefined spawn point
    public void specificSpawnAtSpawnPoint(String spawnPointName, UUID playerUUID) {

        // Creates intArray for coordinates and locates index of target spawn point
        Integer[] intArray = new Integer[coordArrayList.get(0).size()];
        intArray = coordArrayList.get(coordNameList.indexOf(spawnPointName)).toArray(intArray);

        Location loc = new Location(Bukkit.getWorld(worldName), intArray[0], intArray[1], intArray[2], intArray[3], intArray[4]);

        Bukkit.getPlayer(playerUUID).teleport(loc);
    }



    // team Spawning
    // -----------------------------------------------------------------------------------------------------------------

    // Spawn a team of players at a specified spawn point
    public void spawnTeamAtSpawnPoints(ArrayList<UUID> playerUUIDArrayList) {

        // Copies given ArrayList for local use
        ArrayList<UUID> tempPlayerUUIDArrayList = new ArrayList<>(playerUUIDArrayList);

        // Indexs allow both the current team and current spawn point to be selected
        int currentTeamIndex, currentSpawnPointIndex;
        int x, y, z, pitch, yaw;

        // Loops through teams
        for (Team team : M2API.TeamArrayList) {

            // Sets the currentTeamIndex to that which aligns with the team
            // Starts the currentSpawnPointIndex at 0 for each team
            currentTeamIndex = teamWithCoordNameList.indexOf(team.teamName);
            currentSpawnPointIndex = 0;

            // Loops through players
            for (UUID playerUUID : tempPlayerUUIDArrayList) {

                // Checks if the player is on this team
                if (team.isPlayerOnTeam(Bukkit.getPlayer(playerUUID))) {

                    // Sets coordinates
                    x = teamsCoordArrayList.get(currentTeamIndex).get(currentSpawnPointIndex).get(0);
                    y = teamsCoordArrayList.get(currentTeamIndex).get(currentSpawnPointIndex).get(1);
                    z = teamsCoordArrayList.get(currentTeamIndex).get(currentSpawnPointIndex).get(2);
                    pitch = teamsCoordArrayList.get(currentTeamIndex).get(currentSpawnPointIndex).get(3);
                    yaw = teamsCoordArrayList.get(currentTeamIndex).get(currentSpawnPointIndex).get(4);

                    Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, pitch, yaw);
                    Bukkit.getPlayer(playerUUID).teleport(loc);

                    // Removes player from ArrayList and goes onto next spawn point
                    tempPlayerUUIDArrayList.remove(playerUUID);
                    currentSpawnPointIndex++;
                }
            }
        }
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
