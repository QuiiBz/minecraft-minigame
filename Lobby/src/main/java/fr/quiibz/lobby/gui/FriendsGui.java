package fr.quiibz.lobby.gui;

import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.gui.AbstractGuiBuilder;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.accounts.Account;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.atomic.AtomicInteger;

public class FriendsGui extends AbstractGuiBuilder {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Amis";
    }

    @Override
    public int getSize() {

        return 5;
    }

    @Override
    public byte getGlassMeta() {

        return 9;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        Account account = AccountManager.get().getAccount(player);

        AtomicInteger slot = new AtomicInteger(11);

        account.getFriends().forEach((friend, uuid) -> {

            inv.setItem(slot.getAndIncrement(), new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§6§l" + friend).setSkullOwner(friend).toItemStack());
        });

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        super.onClick(player, inv, item, click, slot);
    }
}
