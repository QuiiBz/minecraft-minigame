package fr.quiibz.lobby.gui;

import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.feeders.InstanceStatusFeeder;
import fr.quiibz.api.feeders.InstanceStopFeeder;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MyHostGui extends AbstractGuiBuilder {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Mes serveurs Host";
    }

    @Override
    public int getSize() {

        return 4;
    }

    @Override
    public boolean update() {

        return true;
    }

    @Override
    public byte getGlassMeta() {

        return 5;
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

            if(serverInstance.getHost().equals(player.getName())) {

                List<String> lore = serverInstance.toLore();
                lore.add(" §f§l» §eClic-gauche : §aRejoindre");
                lore.add(" §f§l» §eClic-droit : §cSupprimer");

                inv.setItem(slot.getAndIncrement(), new ItemBuilder(serverInstance.getServerType().toItemStack(), serverInstance.getPlayers().size()).setName("§6§l" + serverInstance.getServerType().getName() + " #" + serverInstance.getServerId()).setLore(lore).toItemStack());
            }
        });

        inv.setItem(3, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(player.getName()).setName("§6§lMes Host").setLore(
                " ",
                " §f§l» §aSélectionné").toItemStack());
        inv.setItem(5, new ItemBuilder(Material.SLIME_BALL).setName("§a§lCréer un Host").setLore(
                " ",
                " §f§l» §eClic pour créer").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.equals("§a§lCréer un Host"))
            GuiManager.open(new CreateHostGui(), player, this);
        else if(name.startsWith("§6§l") && !name.equals("§6§lMes Host")) {

            ServerType serverType = ServerType.getByName(name);
            int serverId = Integer.parseInt(name.replace("§6§l" + serverType.getName() + " #", ""));

            if(click.equals(ClickType.LEFT))
                new TeleportFeeder(new TeleportTopic(player.getUniqueId(), serverType, serverId)).publish();
            else if(click.equals(ClickType.RIGHT)) {

                RedisAccess redisAccess = RedisAccess.getInstance();
                RedissonClient redissonClient = redisAccess.getClient();
                Iterable<String> keys = redissonClient.getKeys().getKeysByPattern("instance:*");

                keys.forEach(key -> {

                    RBucket<ServerInstance> rBucket = redissonClient.getBucket(key);
                    ServerInstance serverInstance = rBucket.get();

                    if(serverInstance.getServerType().equals(serverType) && serverInstance.getServerId() == serverId)
                        new InstanceStopFeeder(serverInstance.getId()).publish();
                });
            }
        }

        super.onClick(player, inv, item, click, slot);
    }
}
