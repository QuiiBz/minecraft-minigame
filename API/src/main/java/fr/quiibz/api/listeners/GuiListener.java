package fr.quiibz.api.listeners;

import fr.quiibz.api.gui.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GuiListener extends IListener {

    /*
     *  METHODS
     */

    @EventHandler
    public void onCloseGui(InventoryCloseEvent event) {

        GuiManager.onClose((Player) event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        GuiManager.onClose(event.getPlayer());
    }
}
