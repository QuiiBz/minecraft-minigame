package fr.quiibz.lobby.gui;

import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
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
import org.redisson.api.RedissonClient;;import java.util.ArrayList;
import java.util.List;

public class PlayGui extends AbstractGuiBuilder<ServerType> {

    /*
     *  CONSTRUCTOR
     */

    public PlayGui(ServerType serverType) {

        super(serverType);
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Jouer en " + this.getSettings().getName();
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

        return 1;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern("instance:*");
        List<ServerInstance> instances = new ArrayList<ServerInstance>();

        keys.forEach(key -> {

            RBucket<ServerInstance> rBucket = redissonClient.getBucket(key);
            ServerInstance instance = rBucket.get();

            if(instance.getServerType().equals(this.getSettings()))
                instances.add(instance);
        });

        inv.setItem(13, new ItemBuilder(Material.NETHER_STAR).setName("§6§lJOUER").setLore(
                " ",
                Constants.ROUND + "§7Connectés : §a" + instances.stream().mapToInt(instance -> instance.getPlayers().size()).sum(),
                " ",
                " §f§l» §eClic pour jouer rapidement").toItemStack());
        inv.setItem(22, new ItemBuilder(Material.MAP).setName("§e§lServeurs").setLore(
                " ",
                Constants.ROUND + "§7Serveurs : §e" + instances.size(),
                " ",
                " §f§l» §eClic-gauche : §aJouer",
                " §f§l» §eClic-droit : §bVoir les serveurs").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.startsWith("§6§lJOUER") || (name.startsWith("§e§lServeurs") && click.equals(ClickType.LEFT))) {

            player.closeInventory();
            new TeleportFeeder(new TeleportTopic(player.getUniqueId(), this.getSettings(), -1)).publish();

        } else
            GuiManager.open(new ServersGui(this.getSettings()), player, this);

        super.onClick(player, inv, item, click, slot);
    }
}
