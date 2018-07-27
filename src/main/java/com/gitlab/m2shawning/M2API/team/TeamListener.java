package com.gitlab.m2shawning.M2API.team;

import com.gitlab.m2shawning.M2API.M2API;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeamListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if ((event.getDamager() instanceof Player) && (event.getEntity() instanceof Player)) {
            for (Team team : M2API.TeamArrayList) {
                if ((team.isPlayerOnTeam((Player)event.getDamager())) && (team.isPlayerOnTeam((Player)event.getEntity()))) {
                    if (!(team.friendlyFire)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {

        for (Team team : M2API.TeamArrayList) {
            if (team.isPlayerOnTeam(event.getPlayer())) {
                if (!(team.blockInteract)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        for (Team team : M2API.TeamArrayList) {
            if (team.isPlayerOnTeam(event.getPlayer())) {
                if (!(team.blockPlace)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        for (Team team : M2API.TeamArrayList) {
            if (team.isPlayerOnTeam(event.getPlayer())) {
                if (!(team.blockBreak)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
