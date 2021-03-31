package fr.quiibz.api.gui;

import fr.quiibz.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractGuiBuilder<T> {

    /*
     *  FIELDS
     */

    protected final int slots = this.getSize() * 9 - 1;
    private AbstractGuiBuilder lastGui;
    private T settings;

    /*
     *  CONSTRUCTOR
     */

    public AbstractGuiBuilder() {

        this(null);
    }

    public AbstractGuiBuilder(T settings) {

        this.settings = settings;
    }

    /*
     *  METHODS
     */

    public void setContent(Player player, Inventory inv) {

        byte glassMeta = this.getGlassMeta();

        if(glassMeta != -1) {

            ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, glassMeta).setName(" ").toItemStack();
            inv.setItem(0, glass);
            inv.setItem(1, glass);
            inv.setItem(7, glass);
            inv.setItem(8, glass);
            inv.setItem(9, glass);
            inv.setItem(17, glass);
            inv.setItem(this.slots - 17, glass);
            inv.setItem(this.slots - 9, glass);
            inv.setItem(this.slots - 8, glass);
            inv.setItem(this.slots - 7, glass);
            inv.setItem(this.slots - 1, glass);
            inv.setItem(this.slots, glass);
        }

        if(this.hasBack()) {

            if(this.lastGui == null)
                inv.setItem(this.slots - 4, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).setName("§cFermer").setLore(
                        " ",
                        " §f§l» §eClic pour fermer").toItemStack());
            else
                inv.setItem(this.slots - 4, new ItemBuilder(Material.ARROW).setName("§6Retour").setLore(
                        " ",
                        " §f§l» §eClic pour retourner en arrière").toItemStack());
        }
    }

    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        if(slot == this.slots - 4) {

            if(this.lastGui == null)
                player.closeInventory();
            else
                GuiManager.open(this.lastGui, player, null);
        }
    }

    public boolean update() {

        return false;
    }

    public byte getGlassMeta() {

        return -1;
    }

    public boolean hasBack() {

        return true;
    }

    public T getSettings() {

        return this.settings;
    }

    public AbstractGuiBuilder getLastGui() {

        return this.lastGui;
    }

    public AbstractGuiBuilder setLastGui(AbstractGuiBuilder lastGui) {

        this.lastGui = lastGui;

        return this;
    }

    public abstract String getName();
    public abstract int getSize();
}