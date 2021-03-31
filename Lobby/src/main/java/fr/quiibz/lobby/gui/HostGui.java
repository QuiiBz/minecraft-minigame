package fr.quiibz.lobby.gui;

import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.commons.topic.TeleportTopic;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HostGui extends AbstractGuiBuilder<ServerType> {

    /*
     *  CONSTRUCTOR
     */

    public HostGui(ServerType serverType) {

        super(serverType);
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Serveurs Host" + (this.getSettings() != null ? " (" + this.getSettings().getName() + ")" : "");
    }

    @Override
    public int getSize() {

        return 5;
    }

    @Override
    public boolean update() {

        return true;
    }

    @Override
    public byte getGlassMeta() {

        return 3;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern("instance:*");

        int serverSlot = 2;

        for(ServerType serverType : ServerType.values()) {

            if(!serverType.equals(ServerType.LOBBY))
                inv.setItem(serverSlot++, new ItemBuilder(serverType.toItemStack()).setName("§6§l" + serverType.getName()).setLore(
                        " ",
                        (this.getSettings() != null && this.getSettings().equals(serverType) ? " §f§l» §aSélectionné" : " §f§l» §eClic pour filtrer")).toItemStack());
        }

        AtomicInteger slot = new AtomicInteger(11);

        keys.forEach(key -> {

            RBucket<ServerInstance> rBucket = redissonClient.getBucket(key);
            ServerInstance serverInstance = rBucket.get();

            if(!serverInstance.getHost().equals("console")) {

                List<String> lore = serverInstance.toLore();
                lore.add(" §f§l» §eClic pour rejoindre");

                if(this.getSettings() != null && serverInstance.getServerType().equals(this.getSettings()))
                    inv.setItem(slot.getAndIncrement(), new ItemBuilder(Material.BANNER, serverInstance.getPlayers().size(), (byte) serverInstance.getStatus().getColor()).setName("§6§l" + serverInstance.getServerType().getName() + " #" + serverInstance.getServerId()).setLore(lore).toItemStack());
                else if(this.getSettings() == null)
                    inv.setItem(slot.getAndIncrement(), new ItemBuilder(serverInstance.getServerType().toItemStack(), serverInstance.getPlayers().size()).setName("§6§l" + serverInstance.getServerType().getName() + " #" + serverInstance.getServerId()).setLore(lore).toItemStack());
            }
        });

        inv.setItem(19, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(player.getName()).setName("§6§lMes Host").setLore(
                " ",
                " §f§l» §eClic pour accéder").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(slot >= 2 && slot <= 6) {

            ServerType serverType = ServerType.getByName(name.replace("§6§l", ""));
            GuiManager.open(new HostGui(serverType), player, this.getLastGui());

        } else if(name.equals("§6§lMes Host"))
            GuiManager.open(new MyHostGui(), player, this);
        else if(name.startsWith("§6§l")) {

            ServerType serverType;
            String server = name.replace("§6§l", "");

            if(this.getSettings() != null)
                serverType = this.getSettings();
            else
                serverType = ServerType.getByName(server);

            int serverId = Integer.parseInt(server.replace(serverType.getName(), "").replace(" #",  ""));

            new TeleportFeeder(new TeleportTopic(player.getUniqueId(), serverType, serverId)).publish();
        }

        super.onClick(player, inv, item, click, slot);
    }
}
