package fr.quiibz.lobby.gui;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.accounts.Account;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class VisibilityGui extends AbstractGuiBuilder {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Visibilité";
    }

    @Override
    public int getSize() {

        return 5;
    }

    @Override
    public byte getGlassMeta() {

        return 11;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        Account account = AccountManager.get().getAccount(player);

        inv.setItem(12, new ItemBuilder(Material.BANNER, 1, (byte) 10).setName("§aTout le monde").toItemStack());
        inv.setItem(13, new ItemBuilder(Material.BANNER, 1, (byte) 13).setName("§aMes amis").toItemStack());
        inv.setItem(14, new ItemBuilder(Material.BANNER, 1, (byte) 1).setName("§cPersonne").toItemStack());
        inv.setItem(21, new ItemBuilder(Material.INK_SACK, 1, (byte) (account.getVisibility() == 0 ? 10 : 8)).setName((account.getVisibility() == 0 ? "§a§lActivé" : "§c§lDésactivé")).setLore(" ", " §f§l» §eClic pour activer").toItemStack());
        inv.setItem(22, new ItemBuilder(Material.INK_SACK, 1, (byte) (account.getVisibility() == 1 ? 10 : 8)).setName((account.getVisibility() == 1 ? "§a§lActivé" : "§c§lDésactivé")).setLore(" ", " §f§l» §eClic pour activer").toItemStack());
        inv.setItem(23, new ItemBuilder(Material.INK_SACK, 1, (byte) (account.getVisibility() == 2 ? 10 : 8)).setName((account.getVisibility() == 2 ? "§a§lActivé" : "§c§lDésactivé")).setLore(" ", " §f§l» §eClic pour activer").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        if(item.getItemMeta().getDisplayName().equals("§a§lLobby")) {

            player.chat("/spawn");
            player.closeInventory();

        } else if(item.getType().equals(Material.INK_SACK)) {

            Account account = AccountManager.get().getAccount(player);
            int newVisibility = (slot - 21);

            account.setVisibility(newVisibility);

            RedisAccess redisAccess = RedisAccess.getInstance();
            RedissonClient redissonClient = redisAccess.getClient();
            RBucket<Account> rBucket = redissonClient.getBucket("account:" + player.getUniqueId().toString());
            rBucket.set(account);

            GuiManager.open(new VisibilityGui(), player);
        }

        super.onClick(player, inv, item, click, slot);
    }
}
