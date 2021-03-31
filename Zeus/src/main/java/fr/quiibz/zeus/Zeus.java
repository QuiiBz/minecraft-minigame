package fr.quiibz.zeus;

import fr.quiibz.apollon.commands.MaintenanceCommand;
import fr.quiibz.apollon.listeners.JoinListener;
import fr.quiibz.commons.commands.RankCommand;
import fr.quiibz.zeus.commands.InstanceCommand;
import fr.quiibz.zeus.commands.HubCommand;
import fr.quiibz.zeus.instances.InstanceManager;
import fr.quiibz.zeus.instances.QueueManager;
import fr.quiibz.zeus.listeners.PlayerListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Zeus extends Plugin {

    /*
     *  FIELDS
     */

    private static Zeus instance;

    /*
     *  METHODS
     */

    @Override
    public void onEnable() {

        instance = this;

        new InstanceManager();
        QueueManager queueManager = new QueueManager();

        PluginManager pluginManager = this.getProxy().getPluginManager();
        pluginManager.registerCommand(this, new InstanceCommand("instance"));
        pluginManager.registerCommand(this, new HubCommand("hub"));
        pluginManager.registerListener(this, new PlayerListener(queueManager));
        pluginManager.registerCommand(this, new RankCommand("rank"));
    }

    public static Zeus get() {

        return instance;
    }
}
