package fr.quiibz.lobby.listeners;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.listeners.IListener;
import fr.quiibz.commons.accounts.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends IListener {

    /*
     *  METHODS
     */

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        Rank rank = AccountManager.get().getAccount(player).getRank();

        event.setCancelled(true);

        Bukkit.broadcastMessage(rank.getPrefix() + player.getName() + "§7: " + rank.getChatColor() + event.getMessage());
    }
}
