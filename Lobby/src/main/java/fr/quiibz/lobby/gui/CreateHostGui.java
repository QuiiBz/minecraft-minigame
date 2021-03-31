package fr.quiibz.lobby.gui;

import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.commons.topic.CreateInstanceTopic;
import fr.quiibz.commons.topic.TeleportTopic;
import fr.quiibz.lobby.feeders.CreateInstanceFeeder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.atomic.AtomicInteger;

public class CreateHostGui extends AbstractGuiBuilder {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Créer un serveur Host";
    }

    @Override
    public int getSize() {

        return 4;
    }

    @Override
    public byte getGlassMeta() {

        return 5;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        int serverSlot = 11;

        for(ServerType serverType : ServerType.values()) {

            if(!serverType.equals(ServerType.LOBBY))
                inv.setItem(serverSlot++, new ItemBuilder(serverType.toItemStack()).setName("§6§l" + serverType.getName()).setLore(
                        " ",
                        " §f§l» §eClic pour choisir ce type d'Host").toItemStack());
        }

        inv.setItem(3, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(player.getName()).setName("§6§lMes Host").setLore(
                " ",
                " §f§l» §eClic pour accéder").toItemStack());
        inv.setItem(5, new ItemBuilder(Material.SLIME_BALL).setName("§a§lCréer un Host").setLore(
                " ",
                " §f§l» §aSélectionné").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.equals("§6§lMes Host"))
            GuiManager.open(new MyHostGui(), player, this.getLastGui().getLastGui());
        else if(name.startsWith("§6§l")) {

            ServerType serverType = ServerType.getByName(name.replace("§6§l", ""));

            new CreateInstanceFeeder(new CreateInstanceTopic(player.getUniqueId(), serverType)).publish();

            GuiManager.open(new MyHostGui(), player);
        }

        super.onClick(player, inv, item, click, slot);
    }
}
