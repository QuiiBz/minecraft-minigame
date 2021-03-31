package fr.quiibz.ctf.players;

import fr.quiibz.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum CTFKit {

    GUERRIER("Guerrier", Material.IRON_SWORD),
    ARCHER("Archer", Material.BOW),
    RUNNER("Coureur", Material.FEATHER),
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

    CTFKit(String name, Material material) {

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

                player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
                player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
                break;

            case ARCHER:

                player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                player.getInventory().setItem(0, new ItemStack(Material.WOOD_SWORD));
                player.getInventory().setItem(1, new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).addEnchant(Enchantment.ARROW_DAMAGE, 2).toItemStack());
                player.getInventory().setItem(2, new ItemStack(Material.ARROW, 1));
                break;

            case RUNNER:

                player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                player.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
                player.getInventory().setItem(1, new ItemStack(Material.POTION, 2, (short) 8226));
                break;

            case MAGICIEN:

                player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                player.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
                player.getInventory().setItem(1, new ItemStack(Material.POTION, 2, (short) 16421));
                break;

            default:
                break;
        }

        player.getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 8));
    }

    public static CTFKit resolve(String name) {

        for(CTFKit kit : values()) {

            if(name.equals(kit.getName()))
                return kit;
        }

        return null;
    }

    public ItemStack toItemStack(CTFPlayerData playerData) {

        ItemBuilder itemBuilder = new ItemBuilder(this.material).setName("§6§l" + this.name).setLore(
                " ",
                (this.equals(playerData.getKit()) ? " §f§l» §aSélectionné" : " §f§l» §eClic pour sélectionner"));

        return itemBuilder.toItemStack();
    }
}
