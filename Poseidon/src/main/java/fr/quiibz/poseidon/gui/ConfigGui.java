package fr.quiibz.poseidon.gui;

import fr.quiibz.api.API;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerStatus;
import fr.quiibz.poseidon.config.IConfig;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGui extends AbstractGuiBuilder {

    /*
     *  FIELDS
     */

    private IGameManager gameManager;
    private IConfig config;

    /*
     *  METHODS
     */

    public ConfigGui(IGameManager gameManager) {

        this.gameManager = gameManager;
        this.config = gameManager.getConfig();
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Configuration";
    }

    @Override
    public int getSize() {

        return 6;
    }

    @Override
    public byte getGlassMeta() {

        return 1;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        this.config.getItems().forEach(item -> inv.setItem(item.getSlot(), item.toItem()));

        ServerInstance serverInstance = API.get().getServerInstance();

        inv.setItem(18, new ItemBuilder(Material.HOPPER).setName("§6§lMenu des configurations").setLore(
                " ",
                " §f§l» §eClic pour accéder").toItemStack());

        IConfig config = this.gameManager.getConfig();

        if(config.getName() != null && config.getOwner() != null) {

            List<String> lore = config.toLore();
            lore.add(" ");
            lore.add(" §f§l» §eClic-gauche : §aAccéder au menu");
            lore.add(" §f§l» §eClic-droit : §cDécharger cette configuration");

            inv.setItem(27, new ItemBuilder(Material.STORAGE_MINECART).setName("§6§lConfiguration actuelle").setLore(lore).toItemStack());

        } else
            inv.setItem(27, new ItemBuilder(Material.MINECART).setName("§c§lAucune configuration").toItemStack());

        if(serverInstance.getStatus().equals(ServerStatus.WHITELISED))
            inv.setItem(39, new ItemBuilder(Material.INK_SACK, 1, (byte) 8).setName("§6§lStatus").setLore(
                    " ",
                    Constants.ROUND + "§7Status actuel : " + serverInstance.getStatus().getName(),
                    " ",
                    " §f§l» §eClic pour passer à §aouvert").toItemStack());
        else
            inv.setItem(39, new ItemBuilder(Material.INK_SACK, 1, (byte) 10).setName("§6§lStatus").setLore(
                    " ",
                    Constants.ROUND + "§7Status actuel : " + serverInstance.getStatus().getName(),
                    " ",
                    " §f§l» §eClic pour passer à §dwhitelisté").toItemStack());

        inv.setItem(40, new ItemBuilder(Material.SLIME_BALL).setName("§a§lForcer le démarrage").setLore(
                " ",
                " §f§l» §eClic pour forcer le démarrage").toItemStack());
        inv.setItem(41, new ItemBuilder(Material.GOLD_NUGGET).setName("§6§lAnnonce sur les Lobby").setLore(
                " ",
                " §f§l» §eClic pour faire une annonce").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        ServerInstance serverInstance = API.get().getServerInstance();
        String name = item.getItemMeta().getDisplayName();

        if(name.equals("§6§lMenu des configurations"))
            GuiManager.open(new ConfigurationGui(this.gameManager), player, this);
        else if(name.equals("§6§lConfiguration actuelle")) {

            if(click.equals(ClickType.LEFT))
                GuiManager.open(new ConfigurationGui(this.gameManager), player, this);
            else if(click.equals(ClickType.RIGHT)) {

                this.config.setItems(this.gameManager);
                this.config.setName(null);
                this.config.setOwner(null);

                GuiManager.open(this, player);

                player.sendMessage(Constants.PREFIX + "§eLa configuration a bien été déchargée.");
            }

        } else if(name.equals("§6§lStatus")) {

            API.get().setUpdate(true);
            serverInstance.setStatus(serverInstance.getStatus().equals(ServerStatus.WHITELISED) ? ServerStatus.ONLINE : ServerStatus.WHITELISED);
            GuiManager.open(this, player);

        } else if(name.equals("§a§lForcer le démarrage")) {

            if(this.gameManager.getGameState().equals(GameState.WAITING)) {

                this.gameManager.getTimer().setExactSec(12);
                player.closeInventory();
            }

        } else if(name.equals("§6§lAnnonce sur les Lobby")) {

            // todo

        } else {

            this.config.getItems().forEach(current -> {

                if(current.toItem().getItemMeta().getDisplayName().equals(name))
                    current.onClick(player, this);
            });
        }

        super.onClick(player, inv, item, click, slot);
    }
}
