package fr.quiibz.api.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    /*
     *  METHODS
     */

    public static boolean notNull(ItemStack item) {

        return item != null && !item.getType().equals(Material.AIR);
    }

    public static boolean hasMeta(ItemStack item) {

        return notNull(item) && item.hasItemMeta() && item.getItemMeta().hasDisplayName();
    }
}
