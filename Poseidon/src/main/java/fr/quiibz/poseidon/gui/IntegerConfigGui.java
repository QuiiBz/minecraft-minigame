package fr.quiibz.poseidon.gui;

import fr.quiibz.api.API;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.poseidon.config.values.IConfigValue;
import fr.quiibz.poseidon.config.values.IntegerConfigValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class IntegerConfigGui<T> extends AbstractGuiBuilder<IConfigValue<T>> {

    /*
     *  CONSTRUCTOR
     */

    public IntegerConfigGui(IConfigValue<T> configValue) {

        super(configValue);
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Configuration " + this.getSettings().getName();
    }

    @Override
    public int getSize() {

        return 4;
    }

    @Override
    public byte getGlassMeta() {

        return 9;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        inv.setItem(10, new ItemBuilder(Material.BANNER).setBannerMinus().setName("§c-10").toItemStack());
        inv.setItem(11, new ItemBuilder(Material.BANNER).setBannerMinus().setName("§c-5").toItemStack());
        inv.setItem(12, new ItemBuilder(Material.BANNER).setBannerMinus().setName("§c-1").toItemStack());
        inv.setItem(13, new ItemBuilder(Material.STONE_BUTTON).setName("§6§l" + this.getSettings().get()).toItemStack());
        inv.setItem(14, new ItemBuilder(Material.BANNER).setBannerPlus().setName("§a+1").toItemStack());
        inv.setItem(15, new ItemBuilder(Material.BANNER).setBannerPlus().setName("§a+5").toItemStack());
        inv.setItem(16, new ItemBuilder(Material.BANNER).setBannerPlus().setName("§a+10").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        IntegerConfigValue configValue = (IntegerConfigValue) this.getSettings();

        switch (slot) {

            case 10:
                configValue.set(configValue.get() - 10);
                break;
            case 11:
                configValue.set(configValue.get() - 5);
                break;
            case 12:
                configValue.set(configValue.get() - 1);
                break;
            case 14:
                configValue.set(configValue.get() + 1);
                break;
            case 15:
                configValue.set(configValue.get() + 5);
                break;
            case 16:
                configValue.set(configValue.get() + 10);
                break;
            default:
                break;
        }

        inv.setItem(13, new ItemBuilder(Material.STONE_BUTTON).setName("§6§l" + configValue.get()).toItemStack());

        if(configValue.getName().equals("Slots")) {

            API.get().setMaxPlayers(configValue.get());
        }

        super.onClick(player, inv, item, click, slot);
    }
}
