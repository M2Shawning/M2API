package com.github.m2shawning.M2API.stats;

import com.github.m2shawning.M2API.mysql.MySQL;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stats {

    private MySQL mySQL;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private String table;
    private String output;

    public Stats(MySQL mySQL, String table) {

        this.mySQL = mySQL;
        this.table = table;
    }

    // Sets Stat
    public void setStat(Player player, String statName, String statValue) {

        connection = mySQL.getConnection();

        try {

            if (getStat(player, statName) != null) {

                preparedStatement = connection.prepareStatement("UPDATE " + table + " SET " + statName + "=(?) WHERE playerUUID='" + player.getUniqueId() + "';");
                preparedStatement.setString(1, statValue);

                preparedStatement.executeUpdate();

            } else {

                preparedStatement = connection.prepareStatement("INSERT INTO " + table + " (playerUUID, " + statName + " ) VALUES (?, ?);");
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, statValue);

                preparedStatement.executeUpdate();

            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            mySQL.closeResources(null, preparedStatement);
        }
    }


    // Gets Stat
    public String getStat(Player player, String statName) {

        connection = mySQL.getConnection();

        try {

            preparedStatement = connection.prepareStatement("SELECT " + statName + " FROM " + table + " WHERE playerUUID='" + player.getUniqueId() + "';");
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            output = resultSet.getString(statName);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            mySQL.closeResources(resultSet, preparedStatement);
        }

        return output;
    }
}
