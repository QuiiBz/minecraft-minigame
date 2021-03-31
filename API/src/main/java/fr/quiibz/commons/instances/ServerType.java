package fr.quiibz.commons.instances;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ServerType {

    LOBBY("Lobby", null),
    DUELS("Duels", new ItemStack(Material.IRON_SWORD)),
    SHOOTER("Shooter", new ItemStack(Material.FIREWORK)),
    CTF("CaptureTheFlag", new ItemStack(Material.BANNER, 1, (byte) 12)),
    ;

    /*
     *  FIELDS
     */

    private String name;
    private ItemStack item;

    /*
     *  CONSTRUCTOR
     */

    ServerType(String name, ItemStack item) {

        this.name = name;
        this.item = item;
    }

    /*
     *  METHODS
     */

    public String getName() {

        return this.name;
    }

    public ItemStack toItemStack() {

        return this.item;
    }

    public static ServerType getByName(String name) {

        for(ServerType current : values()) {

            if(name.contains(current.getName()))
                return current;
        }

        return null;
    }
}
