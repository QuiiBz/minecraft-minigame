package fr.quiibz.ctf.listeners;

import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.listeners.IListener;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemUtils;
import fr.quiibz.ctf.game.CTFGameManager;
import fr.quiibz.ctf.gui.KitGui;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.players.AbstractTeamPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class GameListener extends IListener {

    /*
     *  FIELDS
     */

    private IGameManager gameManager;

    /*
     *  CONSTRUCTOR
     */

    public GameListener(IGameManager gameManager) {

        this.gameManager = gameManager;
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

                case "§6§lKit" + Constants.RCLICK:

                    GuiManager.open(new KitGui(this.gameManager.getData(player)), player);
                    break;

                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onCapture(PlayerMoveEvent event) {

        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getBlockX() != to.getBlockX() || to.getBlockZ() != to.getBlockZ()) {

            Player player = event.getPlayer();
            CTFGameManager ctfGameManager = (CTFGameManager) this.gameManager;

            ctfGameManager.checkCapture(player, to);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if(!this.gameManager.getGameState().equals(GameState.WAITING)) {

            Player player = event.getPlayer();
            AbstractTeamPlayerData playerData = (AbstractTeamPlayerData) this.gameManager.getData(player);

            Bukkit.broadcastMessage(playerData.getTeam().getName() + " " + player.getName() + "§7: " + event.getMessage());
        }
    }
}
