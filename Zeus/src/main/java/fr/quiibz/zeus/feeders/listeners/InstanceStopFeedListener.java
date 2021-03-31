package fr.quiibz.zeus.feeders.listeners;

import fr.quiibz.zeus.instances.InstanceManager;
import net.md_5.bungee.api.ProxyServer;

public class InstanceStopFeedListener extends AbstractFeedListener<String> {

    /*
     *  FIELDS
     */

    private InstanceManager instanceManager;

    /*
     *  CONSTRUCTOR
     */

    public InstanceStopFeedListener(InstanceManager instanceManager) {

        this.instanceManager = instanceManager;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "instanceStop";
    }

    @Override
    public void listen(String object) {

        this.instanceManager.deleteInstance(ProxyServer.getInstance().getConsole(), object);
    }
}
