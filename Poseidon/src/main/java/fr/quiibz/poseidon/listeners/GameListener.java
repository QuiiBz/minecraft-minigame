package fr.quiibz.poseidon.listeners;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.data.mongo.MongoManager;
import fr.quiibz.api.listeners.IListener;
import fr.quiibz.api.scoreboards.IBoardManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.accounts.Rank;
import fr.quiibz.poseidon.config.AbstractConfig;
import fr.quiibz.poseidon.config.GameConfig;
import fr.quiibz.poseidon.game.AbstractTeamGameManager;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.GameTeam;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.players.AbstractTeamPlayerData;
import fr.quiibz.poseidon.players.IPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;

public class GameListener extends IListener {

    /*
     *  FIELDS
     */

    private IGameManager gameManager;
    private IBoardManager boardManager;

    /*
     *  CONSTRUCTOR
     */

    public GameListener(IGameManager gameManager, IBoardManager boardManager) {

        this.gameManager = gameManager;
        this.boardManager = boardManager;
    }

    /*
     *  METHODS
     */

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);

        this.gameManager.handleJoin(player);
        this.boardManager.handleJoin(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        event.setQuitMessage(null);

        this.gameManager.handleLeave(player);
        this.boardManager.handleLeave(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        event.setCancelled(true);

        if(this.gameManager.getGameState().equals(GameState.WAITING)) {

            Rank rank = AccountManager.get().getAccount(player).getRank();

            Bukkit.broadcastMessage(rank.getPrefix() + player.getName() + "§7: " + rank.getChatColor() + event.getMessage());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        event.setDeathMessage(null);

        this.gameManager.handleDeath(player, true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {

        if(!this.gameManager.getGameState().equals(GameState.PLAYING))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        if(this.gameManager.getGameState().equals(GameState.WAITING)) {

            if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                event.setCancelled(true);

        } else if(this.gameManager.getGameState().equals(GameState.FINISHING))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {

        if(this.gameManager.getGameState().equals(GameState.WAITING)) {

            if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                event.setCancelled(true);

        } else if(this.gameManager.getGameState().equals(GameState.FINISHING))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if(!this.gameManager.getGameState().equals(GameState.PLAYING))
            event.setCancelled(true);
        else {

            if(event instanceof EntityDamageByEntityEvent) {

                EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;

                if(entityEvent.getEntity() instanceof Player && entityEvent.getDamager() instanceof Player && this.gameManager instanceof AbstractTeamGameManager) {

                    AbstractTeamPlayerData playerData = (AbstractTeamPlayerData) this.gameManager.getData((Player) entityEvent.getEntity());
                    AbstractTeamPlayerData damagerData = (AbstractTeamPlayerData) this.gameManager.getData((Player) entityEvent.getDamager());

                    if(!playerData.getTeam().equals(GameTeam.NONE) && !damagerData.getTeam().equals(GameTeam.NONE)) {

                        if(playerData.getTeam().equals(damagerData.getTeam())) {

                            event.setCancelled(true);
                            entityEvent.getDamager().sendMessage(Constants.PREFIX + "§cVous ne pouvez pas frapper un membre de votre équipe.");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(this.gameManager.getGameState().equals(GameState.WAITING)) {

            if(!event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))
                event.setCancelled(true);

        } else if(this.gameManager.getGameState().equals(GameState.FINISHING))
            event.setCancelled(true);
    }
}
