package fr.quiibz.poseidon.gui;

import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.api.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class SpectatorGui extends AbstractGuiBuilder {

    @Override
    public String getName() {

        return "§8Menu spectateur";
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
    public boolean update() {

        return true;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        AtomicInteger slot = new AtomicInteger(0);

        Bukkit.getOnlinePlayers().forEach(current -> {

            if(!current.getGameMode().equals(GameMode.SPECTATOR))
                inv.setItem(slot.getAndIncrement(), new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§6§l" + current.getName()).setSkullOwner(current.getName()).setLore(
                        " ",
                        " §f§l» §eClic pour te téléporter").toItemStack());
        });

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.startsWith("§6§l")) {

            String toTeleport = name.replace("§6§l", "");

            if(PlayerUtils.exist(toTeleport)) {

                player.teleport(Bukkit.getPlayer(toTeleport).getLocation());
                player.closeInventory();
                player.sendMessage(Constants.PREFIX + "§eVous avez été téléporté à §6§l" + toTeleport);

            } else
                player.sendMessage(Constants.PREFIX + "§cCe joueur n'existe plus.");
        }

        super.onClick(player, inv, item, click, slot);
    }
}
