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
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;

public class MenuGui extends AbstractGuiBuilder {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Menu principal";
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

        return 5;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern("instance:*");
        List<ServerInstance> instances = new ArrayList<ServerInstance>();

        keys.forEach(key -> {

            RBucket<ServerInstance> rBucket = redissonClient.getBucket(key);
            instances.add(rBucket.get());
        });

        inv.setItem(12, new ItemBuilder(Material.IRON_SWORD).setName("§6§lDuels").setLore(
                "§8PvP",
                " ",
                "§7Choisissez un kit de",
                "§7départ, puis combattez",
                "§7vos adversaires.",
                " ",
                Constants.ROUND + "§7Connectés : §a" + instances.stream().filter(instance -> instance.getServerType().equals(ServerType.DUELS)).mapToInt(instance -> instance.getPlayers().size()).sum(),
                " ",
                " §f§l» §eClic-gauche : §aJouer",
                " §f§l» §eClic-droit : §bOuvrir le menu").toItemStack());
        inv.setItem(21, new ItemBuilder(Material.FIREWORK).setName("§6§lShooter").setLore(
                "§8Fun",
                " ",
                "§7Lancez des rockettes contre",
                "§7vos enemis pour être le premier",
                "§7à atteindre 20 tués.",
                " ",
                Constants.ROUND + "§7Connectés : §a" + instances.stream().filter(instance -> instance.getServerType().equals(ServerType.SHOOTER)).mapToInt(instance -> instance.getPlayers().size()).sum(),
                " ",
                " §f§l» §eClic-gauche : §aJouer",
                " §f§l» §eClic-droit : §bOuvrir le menu").toItemStack());
        inv.setItem(13, new ItemBuilder(Material.BANNER, 1, (byte) 12).setName("§6§lCaptureTheFlag").setLore(
                "§8PvP",
                " ",
                "§7Récupérez le drapeau adverse",
                "§7pour le ramener à votre base.",
                " ",
                Constants.ROUND + "§7Connectés : §a" + instances.stream().filter(instance -> instance.getServerType().equals(ServerType.CTF)).mapToInt(instance -> instance.getPlayers().size()).sum(),
                " ",
                " §f§l» §eClic-gauche : §aJouer",
                " §f§l» §eClic-droit : §bOuvrir le menu").toItemStack());

        inv.setItem(19, new ItemBuilder(Material.BED).setName("§a§lLobby").setLore(
                " ",
                " §f§l» §eClic-gauche : §aRevenir au spawn",
                " §f§l» §eClic-droit : §bVoir les lobby").toItemStack());

        inv.setItem(39, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(player.getName()).setName("§e§lProfil").setLore(
                " ",
                " §f§l» §eClic pour ouvrir").toItemStack());

        inv.setItem(41, new ItemBuilder(Material.STORAGE_MINECART).setName("§6§lServeurs Host").setLore(
                " ",
                " §f§l» §eClic pour ouvrir").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.equals("§a§lLobby")) {

            if(click.equals(ClickType.LEFT)) {

                player.chat("/spawn");
                player.closeInventory();

            } else if(click.equals(ClickType.RIGHT))
                GuiManager.open(new LobbyGui(), player, this);

        } else if(name.equals("§6§lDuels"))
            this.processGame(click, ServerType.DUELS, player);
        else if(name.equals("§6§lShooter"))
            this.processGame(click, ServerType.SHOOTER, player);
        else if(name.equals("§6§lCaptureTheFlag"))
            this.processGame(click, ServerType.CTF, player);
        else if(name.equals("§6§lServeurs Host"))
            GuiManager.open(new HostGui(null), player, this);
        else if(name.equals("§e§lProfil"))
            GuiManager.open(new ProfileGui(), player, this);

        super.onClick(player, inv, item, click, slot);
    }

    private void processGame(ClickType click, ServerType serverType, Player player) {

        if(click.equals(ClickType.LEFT)) {

            player.closeInventory();
            new TeleportFeeder(new TeleportTopic(player.getUniqueId(), serverType, -1)).publish();

        } else if (click.equals(ClickType.RIGHT))
            GuiManager.open(new PlayGui(serverType), player, this);
    }
}
