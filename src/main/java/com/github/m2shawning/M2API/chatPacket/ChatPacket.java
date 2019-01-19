package com.github.m2shawning.M2API.chatPacket;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

public class ChatPacket {

    public static void sendTitlePackets(String displayText, ArrayList<UUID> playerUUIDs) {

        PacketPlayOutTitle display = new PacketPlayOutTitle(EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" +  displayText + "\"}"), 0, 0, 0);

        for (UUID playerUUID : playerUUIDs) {
            ((CraftPlayer) Bukkit.getServer().getPlayer(playerUUID))
                    .getHandle().playerConnection.sendPacket(display);
        }
    }

    public static void sendActionPackets(String displayText, ArrayList<UUID> playerUUIDs) {

        PacketPlayOutTitle display = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" +  displayText + "\"}"), 0, 0, 0);

        for (UUID playerUUID : playerUUIDs) {
            ((CraftPlayer) Bukkit.getServer().getPlayer(playerUUID))
                    .getHandle().playerConnection.sendPacket(display);
        }
    }
}
