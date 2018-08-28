package com.github.m2shawning.M2API.databases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;

public class MySQL {

    private Connection connection = null;
    private String hostname, database, username, password;
    private int port;

    public MySQL (String hostname, String database, String username, String password, int port) {

        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }



    // Why couldn't the Java developer speak? Null Pointer Exception.
    // -----------------------------------------------------------------------------------------------------------------

    // Catches errors to from opening mySQL connection
    public void openConnection() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "M2API: Connecting To Database");
        try {
            makeConnection();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "M2API: Connection Failed");
        }
    }

    // Opens MySQL connection
    private void makeConnection() throws SQLException, NullPointerException {

        // Checks if connection is already open
        if (connection != null && !connection.isClosed()) {
            return;
        }

        // Only can run method on one thread
        synchronized (this) {

            // Checks if connection is already open
            if (connection != null && !connection.isClosed()) {
                return;
            }

            // Creates connection
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "M2API: Connected");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "M2API: Connection Failed");
            }
        }
    }

    // Closes MySQL connection
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
