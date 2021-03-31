package fr.quiibz.lobby.gui;

import com.avaje.ebeaninternal.server.lib.sql.Prefix;
import fr.quiibz.api.API;
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

import java.util.concurrent.atomic.AtomicInteger;

public class LobbyGui extends AbstractGuiBuilder {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Lobby";
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

        return 2;
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

            if(serverInstance.getServerType().equals(ServerType.LOBBY))
                inv.setItem(slot.getAndIncrement(), new ItemBuilder(Material.BANNER, serverInstance.getPlayers().size(), (byte) serverInstance.getStatus().getColor()).setName("§6§lLobby #" + serverInstance.getServerId()).setLore(
                        " ",
                        Constants.ROUND + "§7Status : " + serverInstance.getStatus().getName(),
                        Constants.ROUND + "§7Connectés : §a" + serverInstance.getPlayers().size(),
                        " ",
                        ((API.get().getServerInstance().getServerId() == serverInstance.getServerId()) ? " §f§l» §aTu es connecté" : " §f§l» §eClic pour rejoindre")).toItemStack());
        });

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.startsWith("§6§lLobby #")) {

            int lobbyId = Integer.parseInt(name.replace("§6§lLobby #", ""));

            new TeleportFeeder(new TeleportTopic(player.getUniqueId(), ServerType.LOBBY, lobbyId)).publish();
        }

        super.onClick(player, inv, item, click, slot);
    }
}
