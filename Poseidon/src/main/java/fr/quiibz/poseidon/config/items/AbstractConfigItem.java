package fr.quiibz.poseidon.config.items;

import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.poseidon.config.values.IConfigValue;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractConfigItem<T> implements IConfigItem {

    /*
     *  FIELDS
     */

    private int slot;
    private Material material;
    private byte type;
    private IConfigValue<T> configValue;

    /*
     *  CONSTRUCTOR
     */

    public AbstractConfigItem(int slot, Material material, IConfigValue<T> configValue) {

       this(slot, material, (byte) 0, configValue);
    }

    public AbstractConfigItem(int slot, Material material, byte type, IConfigValue<T> configValue) {

        this.slot = slot;
        this.material = material;
        this.type = type;
        this.configValue = configValue;
    }

    /*
     *  METHODS
     */

    @Override
    public int getSlot() {

        return this.slot;
    }

    @Override
    public ItemStack toItem() {

        return new ItemBuilder(this.material,1, this.type).setName("§6§l" + this.configValue.getName()).setLore(
                " ",
                Constants.ROUND + "§7Valeur : §a" + this.configValue.get(),
                " ",
                " §f§l» §eClic pour modifier").toItemStack();
    }

    @Override
    public IConfigValue<T> getConfig() {

        return this.configValue;
    }
}
