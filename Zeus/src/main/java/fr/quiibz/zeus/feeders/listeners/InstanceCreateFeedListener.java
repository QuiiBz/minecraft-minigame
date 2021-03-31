package fr.quiibz.zeus.feeders.listeners;

import fr.quiibz.commons.topic.CreateInstanceTopic;
import fr.quiibz.zeus.instances.InstanceManager;
import net.md_5.bungee.api.ProxyServer;

public class InstanceCreateFeedListener extends AbstractFeedListener<CreateInstanceTopic> {

    /*
     *  FIELDS
     */

    private InstanceManager instanceManager;

    /*
     *  CONSTRUCTOR
     */

    public InstanceCreateFeedListener(InstanceManager instanceManager) {

        this.instanceManager = instanceManager;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "createInstance";
    }

    @Override
    public void listen(CreateInstanceTopic object) {

        this.instanceManager.createInstance(ProxyServer.getInstance().getConsole(), object.getServerType(), ProxyServer.getInstance().getPlayer(object.getUUID()).getName());
    }
}
