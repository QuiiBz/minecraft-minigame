package fr.quiibz.poseidon.listeners;

import fr.quiibz.api.API;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.listeners.IListener;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemUtils;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.commons.topic.TeleportTopic;
import fr.quiibz.poseidon.game.AbstractTeamGameManager;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.gui.ConfigGui;
import fr.quiibz.poseidon.gui.SpectatorGui;
import fr.quiibz.poseidon.gui.TeamGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener extends IListener {

    /*
     *  FIELDS
     */

    private IGameManager gameManager;
    private ConfigGui configGui;

    /*
     *  CONSTRUCTOR
     */

    public InteractListener(IGameManager gameManager, ConfigGui configGui) {

        this.gameManager = gameManager;
        this.configGui = configGui;
    }

    /*
     *  METHODS
     */

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(ItemUtils.hasMeta(item)) {

            switch (item.getItemMeta().getDisplayName()) {

                case "§b§lMenu spectateur" + Constants.RCLICK:

                    GuiManager.open(new SpectatorGui(), player);
                    break;

                case "§6§lRe-jouer" + Constants.RCLICK:

                    new TeleportFeeder(new TeleportTopic(player.getUniqueId(), API.get().getServerInstance().getServerType(), -1)).publish();
                    break;

                case "§6§lConfiguration" + Constants.RCLICK:

                    GuiManager.open(this.configGui, player);
                    break;

                case "§6§lEquipe" + Constants.RCLICK:

                    GuiManager.open(new TeamGui((AbstractTeamGameManager) this.gameManager), player);
                    break;

                case "§c§lQuitter" + Constants.RCLICK:

                    new TeleportFeeder(new TeleportTopic(player.getUniqueId(), ServerType.LOBBY, -1)).publish();
                    break;

                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if(ItemUtils.hasMeta(item)) {

            switch (item.getItemMeta().getDisplayName()) {

                case "§b§lMenu spectateur" + Constants.RCLICK:

                    GuiManager.open(new SpectatorGui(), player);
                    break;

                case "§6§lRe-jouer" + Constants.RCLICK:

                    new TeleportFeeder(new TeleportTopic(player.getUniqueId(), API.get().getServerInstance().getServerType(), -1)).publish();
                    break;

                case "§c§lQuitter" + Constants.RCLICK:

                    new TeleportFeeder(new TeleportTopic(player.getUniqueId(), ServerType.LOBBY, -1)).publish();
                    break;

                default:
                    break;
            }
        }
    }
}
