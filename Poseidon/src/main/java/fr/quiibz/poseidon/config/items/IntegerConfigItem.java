package fr.quiibz.poseidon.config.items;

import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.poseidon.config.values.IConfigValue;
import fr.quiibz.poseidon.gui.IntegerConfigGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class IntegerConfigItem extends AbstractConfigItem<Integer> {

    /*
     *  CONSTRUCTOR
     */

    public IntegerConfigItem(int slot, Material material, IConfigValue<Integer> configValue) {

        super(slot, material, configValue);
    }

    public IntegerConfigItem(int slot, Material material, byte type, IConfigValue<Integer> configValue) {

        super(slot, material, type, configValue);
    }

    /*
     *  METHODS
     */

    @Override
    public void onClick(Player player, AbstractGuiBuilder lastGui) {

        GuiManager.open(new IntegerConfigGui(this.getConfig()), player, lastGui);
    }
}
