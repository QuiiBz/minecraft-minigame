package fr.quiibz.zeus.listeners;

import fr.quiibz.apollon.Apollon;
import fr.quiibz.apollon.accounts.AccountProvider;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.zeus.instances.QueueManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    /*
     *  FIELDS
     */

    private QueueManager queueManager;

    /*
     *  CONSTRUCTOR
     */

    public PlayerListener(QueueManager queueManager) {

        this.queueManager = queueManager;
    }

    /*
     *  METHODS
     */

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {

        ProxiedPlayer player = event.getPlayer();

        this.queueManager.leaveQueue(player);
    }
}
