package com.github.m2shawning.M2API.databases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLite {

    // TODO CLEAN UP FILE AND PERSONALIZE IT

    private Connection connection = null;
    private String fileName;
    private Plugin plugin;



    // Why couldn't the Java developer speak? Null Pointer Exception.
    // -----------------------------------------------------------------------------------------------------------------

    public SQLite(Plugin plugin, String fileName){

        this.plugin = plugin;
        this.fileName = fileName;
    }

    // SQL creation stuff, You can leave the blow stuff untouched.
    public void openConnection() {

        File dataFolder = new File(plugin.getDataFolder(), fileName +".db");

        if (!dataFolder.exists()){

            try {

                dataFolder.createNewFile();

            } catch (IOException e) {

                plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "M2API: Failed To Create File");

            }
        }

        try {

            if(connection != null && !connection.isClosed()){
                return;
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);

        } catch (SQLException ex) {

            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "M2API: SQLite Exception On Initialize");

        } catch (ClassNotFoundException ex) {

            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "M2API: No JDBC Library Detected");

        }
    }

    // Closes databases connection
    public void closeConnection() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "M2API: Closing Connection To Database");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "M2API: Connection Closed");

        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "M2API: Failed To Close");
        }
    }

    // Gets connection
    public Connection getConnection() {
        return connection;
    }

    // Closes PreparedStatement and ResultSet
    public void closeResources(ResultSet resultSet, PreparedStatement statement) {

        if (resultSet != null) {

            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {

            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}