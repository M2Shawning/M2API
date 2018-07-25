package com.gmail.m2shawning.M2API.stats;

import com.gmail.m2shawning.M2API.M2API;
import com.gmail.m2shawning.M2API.mysql.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stats {

    private MySQL mySQL;
    private Connection connection;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    private String table;
    private String output;

    public Stats(MySQL mySQL, String table) {

        this.mySQL = mySQL;
        this.table = table;

        connection = mySQL.getConnection();
    }

    // THIS IS SUCH A MESS
    // Sets Stat
    public void setStat(Player player, String statName, String statValue) {

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                try {

                    if (getStat(player, statName) != null) {

                        statement = connection.prepareStatement("UPDATE " + table + " SET (" + statName + ")=(?) WHERE playerUUID=" + player.getUniqueId() + ";");
                        statement.setString(1, statValue);

                    } else {

                        statement = connection.prepareStatement("INSERT INTO " + table + " (playerUUID, " + statName + " ) VALUES (?, ?);");
                        statement.setString(1, player.getUniqueId().toString());
                        statement.setString(2, statValue);
                    }

                    statement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();

                } finally {
                    try {
                        if (connection != null) {
                            statement.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }; runnable.runTaskAsynchronously(JavaPlugin.getPlugin(M2API.class));
    }

    // Gets Stat
    public String getStat(Player player, String statName) {

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                try {

                    statement = connection.prepareStatement("SELECT " + statName + " FROM " + table + " WHERE playerUUID=" + player.getUniqueId());
                    resultSet = statement.executeQuery();

                    resultSet.next();
                    output = resultSet.getString(statName);

                } catch (SQLException e) {
                    e.printStackTrace();

                } finally {
                    mySQL.closeResources(resultSet, statement);
                }
            }
        }; runnable.runTaskAsynchronously(JavaPlugin.getPlugin(M2API.class));

        return output;
    }
}
