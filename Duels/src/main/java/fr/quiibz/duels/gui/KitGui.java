package fr.quiibz.duels.gui;

import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.duels.players.DuelsKit;
import fr.quiibz.duels.players.DuelsPlayerData;
import fr.quiibz.poseidon.players.IPlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class KitGui extends AbstractGuiBuilder<DuelsPlayerData> {

    /*
     *  CONSTRUCTOR
     */

    public KitGui(IPlayerData playerData) {

        super((DuelsPlayerData) playerData);
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Kit";
    }

    @Override
    public int getSize() {

        return 3;
    }

    @Override
    public byte getGlassMeta() {

        return -1;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        AtomicInteger slot = new AtomicInteger(0);

        for(DuelsKit kit : DuelsKit.values())
            inv.setItem(slot.getAndIncrement(), kit.toItemStack(this.getSettings()));

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.startsWith("§6§l")) {

            DuelsKit clicked = DuelsKit.resolve(name.replace("§6§l", ""));

            this.getSettings().setKit(clicked);

            player.closeInventory();
            player.sendMessage(Constants.PREFIX + "§eVous avez sélectionné le Kit §6§l" + clicked.getName());
        }

        super.onClick(player, inv, item, click, slot);
    }
}
