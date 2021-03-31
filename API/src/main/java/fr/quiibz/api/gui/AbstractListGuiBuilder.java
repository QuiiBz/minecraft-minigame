package fr.quiibz.api.gui;

import fr.quiibz.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractListGuiBuilder<T> extends AbstractGuiBuilder<T> {

    /*
     *  FIELDS
     */

    protected int page;
    private List<ItemStack> items;

    /*
     *  CONSTRUCTOR
     */

    public AbstractListGuiBuilder() {

        this.items = new ArrayList<ItemStack>();
    }

    /*
     *  METHODS
     */

    public void addItem(int slot, ItemStack item) {

        if(slot == 1)
            this.items.clear();

        if(slot > this.itemsPerPage() * this.page - this.itemsPerPage() && slot <= this.itemsPerPage() * this.page)
            items.add(item);
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        AtomicInteger slot = new AtomicInteger(11);

        this.items.forEach(current -> {

            inv.setItem(slot.getAndIncrement(), current);
        });

        if(this.page > 1)
            inv.setItem(this.slots - 5, new ItemBuilder(Material.FEATHER).setName("§6Page précédente").setLore(
                    " ",
                    " §f§l» §eClic pour accéder").toItemStack());

        if(this.hasNextPage())
            inv.setItem(this.slots - 3, new ItemBuilder(Material.FEATHER).setName("§6Page suivante").setLore(
                    " ",
                    " §f§l» §eClic pour accéder").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.equals("§6Page précédente"))
            GuiManager.open(this, player, this.page - 1);
        else if(name.equals("§6Page suivante"))
            GuiManager.open(this, player, this.page + 1);

        super.onClick(player, inv, item, click, slot);
    }

    public void setPage(int page) {

        this.page = page;
    }

    public boolean hasNextPage() {

        return false;
    }

    public abstract int itemsPerPage();
}