package fr.quiibz.api.registers;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ListenerRegistrable implements Registrable {

    /*
     *  METHODS
     */

    public void register(JavaPlugin plugin) {

        plugin.getServer().getPluginManager().registerEvents(this.getListener(), plugin);
    }

    public abstract Listener getListener();
}
