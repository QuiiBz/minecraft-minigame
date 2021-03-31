package fr.quiibz.lobby.gui;

import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.utils.Constants;
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

public class ServersGui extends AbstractGuiBuilder<ServerType> {

    /*
     *  CONSTRUCTOR
     */

    public ServersGui(ServerType serverType) {

        super(serverType);
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Serveurs de " + this.getSettings().getName();
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

        return 13;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern("instance:*");

        AtomicInteger slot = new AtomicInteger(11);

        keys.forEach(key -> {

            RBucket<ServerInstance> rBucket = redissonClient.getBucket(key);
            ServerInstance serverInstance = rBucket.get();

            if(serverInstance.getServerType().equals(this.getSettings())) {

                List<String> lore = serverInstance.toLore();
                lore.add(" §f§l» §eClic pour rejoindre");

                inv.setItem(slot.getAndIncrement(), new ItemBuilder(Material.BANNER, serverInstance.getPlayers().size(), (byte) serverInstance.getStatus().getColor()).setName("§6§l" + this.getSettings().getName() + " #" + serverInstance.getServerId()).setLore(lore).toItemStack());
            }
        });

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.startsWith("§6§l" + this.getSettings().getName() + " #")) {

            int serverId = Integer.parseInt(name.replace("§6§l" + this.getSettings().getName() + " #", ""));

            new TeleportFeeder(new TeleportTopic(player.getUniqueId(), this.getSettings(), serverId)).publish();
        }

        super.onClick(player, inv, item, click, slot);
    }
}
