package fr.quiibz.api.listeners;

import fr.quiibz.api.npc.NPC;
import fr.quiibz.api.utils.Reflection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCListener extends IListener {

    /*
     *  METHODS
     */

    @EventHandler
    public void onNPCInteract(PlayerInteractEntityEvent event) {

        if(Reflection.getHandle(event.getRightClicked()) instanceof NPC) {

            NPC npc = (NPC) Reflection.getHandle(event.getRightClicked());
            npc.runCallback(event.getPlayer());
        }
    }
}
