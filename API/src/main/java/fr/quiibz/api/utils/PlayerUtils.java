package fr.quiibz.api.utils;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils {

    /*
     *  METHODS
     */

    public static boolean exist(String player) {

        return exist(Bukkit.getPlayer(player));
    }

    public static boolean exist(Player player) {

        return player != null && player.isOnline();
    }

    public static void clear(Player player) {

        clearArmor(player);

        player.setLevel(0);
        player.setExp(0);
        player.setFoodLevel(20);
        player.setHealth(20);
        player.getActivePotionEffects().clear();
        player.getInventory().clear();

        player.setGameMode(GameMode.SURVIVAL);
    }

    public static void clearArmor(Player player) {

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
    }

    public static void sendPacket(Packet packet) {

        Bukkit.getOnlinePlayers().forEach(current -> sendPacket(current, packet));
    }

    public static void sendPacket(Player player, Packet packet) {

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
