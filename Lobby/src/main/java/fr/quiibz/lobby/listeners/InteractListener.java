package fr.quiibz.lobby.listeners;

import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.listeners.IListener;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemUtils;
import fr.quiibz.lobby.gui.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener extends IListener {

    /*
     *  METHODS
     */

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(ItemUtils.hasMeta(item)) {

            switch (item.getItemMeta().getDisplayName()) {

                case "§a§lMenu principal" + Constants.RCLICK:

                    GuiManager.open(new MenuGui(), player);
                    break;

                case "§e§lProfil" + Constants.RCLICK:

                    GuiManager.open(new ProfileGui(), player);
                    break;

                case "§9§lVisibilité" + Constants.RCLICK:

                    GuiManager.open(new VisibilityGui(), player);
                    break;

                case "§d§lLobby" + Constants.RCLICK:

                    GuiManager.open(new LobbyGui(), player);
                    break;

                default:
                    break;
            }
        }
    }
}
