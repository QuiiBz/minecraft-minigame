package fr.quiibz.api.utils;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketHelper {

    /*
     *  METHODS
     */

    public static void sendPacket(String player, Packet packet) {

        sendPacket(Bukkit.getPlayer(player), packet);
    }

    public static void sendPacket(Player player, Packet packet) {

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendPacket(Packet packet) {

        for(Player player : Bukkit.getOnlinePlayers())
            sendPacket(player, packet);
    }
}
