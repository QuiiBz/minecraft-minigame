package fr.quiibz.lobby.listeners;

import fr.quiibz.api.listeners.IListener;
import fr.quiibz.lobby.pets.PetsManager;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PetsListener extends IListener {

    /*
     *  FIELDS
     */

    private PetsManager petsManager;

    /*
     *  CONSTRUCTOR
     */

    public PetsListener() {

        this.petsManager = PetsManager.get();
    }

    /*
     *  METHODS
     */

    @EventHandler
    public void onInteractPets(PlayerInteractAtEntityEvent event) {

        if(event.getRightClicked() instanceof ArmorStand)
            event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {

            Player player = event.getPlayer();
            this.petsManager.update(player);
        }
    }
}
