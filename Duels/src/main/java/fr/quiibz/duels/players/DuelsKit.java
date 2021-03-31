package fr.quiibz.duels.players;

import fr.quiibz.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum DuelsKit {

    GUERRIER("Guerrier", Material.IRON_SWORD),
    ARCHER("Archer", Material.BOW),
    MAGICIEN("Magicien", Material.CAULDRON_ITEM),
    ;

    /*
     *  FIELDS
     */

    private String name;
    private Material material;

    /*
     *  CONSTRUCTOR
     */

    DuelsKit(String name, Material material) {

        this.name = name;
        this.material = material;
    }
    /*
     *  METHODS
     */

    public String getName() {

        return this.name;
    }

    public void give(Player player) {

        switch (this) {

            case GUERRIER:

                player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
                player.getInventory().setItem(1, new ItemStack(Material.GOLDEN_APPLE, 2));
                break;

            case ARCHER:

                player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
                player.getInventory().setItem(1, new ItemStack(Material.BOW));
                player.getInventory().setItem(2, new ItemStack(Material.ARROW, 16));
                break;

            case MAGICIEN:

                player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
                player.getInventory().setItem(1, new ItemStack(Material.POTION, 1, (byte) 16421));
                player.getInventory().setItem(2, new ItemStack(Material.POTION, 1, (byte) 16421));
                player.getInventory().setItem(3, new ItemStack(Material.POTION, 1, (byte) 16421));
                player.getInventory().setItem(4, new ItemStack(Material.POTION, 1, (byte) 16385));
                player.getInventory().setItem(5, new ItemStack(Material.POTION, 1, (byte) 16426));
                break;

            default:
                break;
        }

        player.getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 8));
    }

    public static DuelsKit resolve(String name) {

        for(DuelsKit kit : values()) {

            if(name.equals(kit.getName()))
                return kit;
        }

        return null;
    }

    public ItemStack toItemStack(DuelsPlayerData playerData) {

        ItemBuilder itemBuilder = new ItemBuilder(this.material).setName("§6§l" + this.name).setLore(
                " ",
                (this.equals(playerData.getKit()) ? " §f§l» §aSélectionné" : " §f§l» §eClic pour sélectionner"));

        return itemBuilder.toItemStack();
    }
}
