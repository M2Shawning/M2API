package com.gmail.m2shawning.M2API.Arena;

import com.gmail.m2shawning.M2API.M2API;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ArenaListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if ((event.getDamager() instanceof Player) && (event.getEntity() instanceof Player)) {
            for (Arena arena : M2API.ArenaArrayList) {
                if ((arena.isPlayerInArea((Player)event.getEntity())) && (arena.isPlayerInArea((Player)event.getDamager()))) {
                    if (!(arena.playerDamage)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {

        for (Arena arena : M2API.ArenaArrayList) {
            if (arena.isPlayerInArea(event.getPlayer())) {
                if (!(arena.blockInteract)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        for (Arena arena : M2API.ArenaArrayList) {
            if (arena.isPlayerInArea(event.getPlayer())) {
                if (!(arena.blockPlace)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        for (Arena arena : M2API.ArenaArrayList) {
            if (arena.isPlayerInArea(event.getPlayer())) {
                if (!(arena.blockBreak)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
