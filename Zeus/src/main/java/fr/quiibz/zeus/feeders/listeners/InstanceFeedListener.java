package fr.quiibz.zeus.feeders.listeners;

import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.zeus.instances.InstanceManager;

public class InstanceFeedListener extends AbstractFeedListener<ServerInstance> {

    /*
     *  FIELDS
     */

    private InstanceManager instanceManager;

    /*
     *  CONSTRUCTOR
     */

    public InstanceFeedListener(InstanceManager instanceManager) {

        this.instanceManager = instanceManager;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "instanceStatus";
    }

    @Override
    public void listen(ServerInstance object) {

        this.instanceManager.updateInstance(object);
    }
}
