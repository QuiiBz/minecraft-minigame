package fr.quiibz.lobby.gui;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.accounts.Account;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ProfileGui extends AbstractGuiBuilder {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Profil";
    }

    @Override
    public int getSize() {

        return 5;
    }

    @Override
    public byte getGlassMeta() {

        return 4;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        Account account = AccountManager.get().getAccount(player);

        inv.setItem(12, new ItemBuilder(Material.MONSTER_EGG, 1, (byte) 90).setName("§6§lPets").setLore(
                " ",
                " §f§l» §eClic pour accéder").toItemStack());
        inv.setItem(13, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(player.getName()).setName("§6§lProfil").setLore(
                " ",
                Constants.ROUND + "§7Pseudo : §f" + player.getName(),
                Constants.ROUND + "§7Grade : " + account.getRank().getColor() + account.getRank().getName(),
                Constants.ROUND + "§7Coins : §e" + account.getCoins()).toItemStack());
        inv.setItem(14, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§6§lAmis").setLore(
                " ",
                " §f§l» §eClic pour accéder").toItemStack());
        inv.setItem(21, new ItemBuilder(Material.ITEM_FRAME).setName("§6§lStatistiques").setLore(
                " ",
                " §f§l» §eClic pour accéder").toItemStack());
        inv.setItem(22, new ItemBuilder(Material.STORAGE_MINECART).setName("§6§lMes Host").setLore(
                " ",
                " §f§l» §eClic pour accéder").toItemStack());
        inv.setItem(23, new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§6§lOptions").setLore(
                " ",
                " §f§l» §eClic pour accéder").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.equals("§6§lPets"))
            GuiManager.open(new PetsGui(), player, this);
        else if(name.equals("§6§lAmis"))
            GuiManager.open(new FriendsGui(), player, this);
        else if(name.equals("§6§lMes Host"))
            GuiManager.open(new MyHostGui(), player, this);

        super.onClick(player, inv, item, click, slot);
    }
}
