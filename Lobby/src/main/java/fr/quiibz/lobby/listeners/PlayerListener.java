package fr.quiibz.lobby.listeners;

import fr.quiibz.api.API;
import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.listeners.IListener;
import fr.quiibz.api.scoreboards.IBoardManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.api.utils.PlayerUtils;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.accounts.PetsType;
import fr.quiibz.commons.accounts.Rank;
import fr.quiibz.lobby.Lobby;
import fr.quiibz.lobby.pets.PetsManager;
import fr.quiibz.lobby.scoreboards.LobbyBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.*;

public class PlayerListener extends IListener {

    /*
     *  FIELDS
     */

    private IBoardManager boardManager;

    /*
     *  CONSTRUCTOR
     */

    public PlayerListener() {

        this.boardManager = new LobbyBoard();
    }

    /*
     *  METHODS
     */

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);

        PlayerUtils.clear(player);

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
        player.teleport(new Location(player.getWorld(), 24.5, 10, 2.5, 90, 0));

        this.giveItems(player);

        this.boardManager.handleJoin(player);

        Bukkit.getScheduler().runTaskLater(Lobby.get(), () -> {

            Account account = AccountManager.get().getAccount(player);
            Rank rank =  account.getRank();

            if(!account.getPet().equals(PetsType.NONE))
                PetsManager.get().setPet(player, account.getPet());

            if(rank.getPower() >= 50) {

                player.setAllowFlight(true);
                Bukkit.broadcastMessage(rank.getPrefix() + player.getName() + " §7a rejoint le Lobby !");
            }

        }, 20);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        event.setQuitMessage(null);

        Player player = event.getPlayer();

        this.boardManager.handleLeave(player);

        PetsManager.get().remove(player);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {

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

        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {

        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }

    private void giveItems(Player player) {

        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setName("§a§lMenu principal" + Constants.RCLICK).toItemStack());
        player.getInventory().setItem(1, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§e§lProfil" + Constants.RCLICK).setSkullOwner(player.getName()).toItemStack());
        player.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK, 1, (byte) 10).setName("§9§lVisibilité" + Constants.RCLICK).toItemStack());
        player.getInventory().setItem(8, new ItemBuilder(Material.ENDER_CHEST).setName("§d§lLobby" + Constants.RCLICK).toItemStack());
    }
}
