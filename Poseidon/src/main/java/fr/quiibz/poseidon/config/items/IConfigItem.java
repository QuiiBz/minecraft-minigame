package fr.quiibz.poseidon.config.items;

import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.poseidon.config.values.IConfigValue;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IConfigItem {

    /*
     *  METHODS
     */

    int getSlot();
    ItemStack toItem();
    void onClick(Player player, AbstractGuiBuilder lastGui);
    IConfigValue getConfig();
}
