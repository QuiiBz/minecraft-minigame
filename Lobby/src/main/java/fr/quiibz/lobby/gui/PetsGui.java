package fr.quiibz.lobby.gui;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.gui.AbstractListGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.lobby.pets.Pet;
import fr.quiibz.lobby.pets.PetsManager;
import fr.quiibz.commons.accounts.PetsType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PetsGui extends AbstractListGuiBuilder {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Pets";
    }

    @Override
    public int getSize() {

        return 4;
    }

    @Override
    public byte getGlassMeta() {

        return 10;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        PetsType currentPet = AccountManager.get().getAccount(player).getPet();
        int slot = 1;

        for(PetsType petsType : PetsType.values()) {

            if(!petsType.equals(PetsType.NONE))
                this.addItem(slot++, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(petsType.getOwner()).setName("§6§l" + petsType.getName()).setLore(
                        " ",
                        (currentPet.equals(petsType) ? " §f§l» §aSélectionné" : " §f§l» §eClic pour sélectionner")).toItemStack());
        }

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.startsWith("§6§l")) {

            PetsType petsType = PetsType.resolve(name.replace("§6§l", ""));

            if(petsType != null) {

                PetsManager.get().setPet(player, petsType);

                GuiManager.open(this, player);
            }
        }

        super.onClick(player, inv, item, click, slot);
    }

    @Override
    public int itemsPerPage() {

        return 5;
    }

    @Override
    public boolean hasNextPage() {

        return this.page == 1;
    }
}
