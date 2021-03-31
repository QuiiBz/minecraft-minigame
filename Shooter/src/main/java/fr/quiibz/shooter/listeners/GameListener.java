package fr.quiibz.shooter.listeners;

import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.listeners.IListener;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemUtils;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.shooter.game.ShooterGameManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

                case "§6§lShooter" + Constants.RCLICK:

                    ((ShooterGameManager) this.gameManager).shoot(player);
                    break;

                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if(!this.gameManager.getGameState().equals(GameState.WAITING)) {

            Player player = event.getPlayer();

            Bukkit.broadcastMessage(Bukkit.getScoreboardManager().getMainScoreboard().getTeam("shooter").getPrefix() + player.getName() + "§7: " + event.getMessage());
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {

        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        event.setCancelled(true);
    }
}
