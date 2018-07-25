package com.gmail.m2shawning.M2API.mysql;

import com.gmail.m2shawning.M2API.M2API;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public class MySQL {

    private Connection connection;
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

    // Triggers getConnection asynchronously
    public void openConnection() {

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    makeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }; runnable.runTaskAsynchronously(JavaPlugin.getPlugin(M2API.class));
    }

    // Opens mysql connection
    private void makeConnection() throws SQLException {

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
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            connection = DriverManager.getConnection("jdbc:mysql//" + hostname + ":" + port + "/" + database, username, password);
        }
    }

    // Closes mysql connection
    public void closeConnection() {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
