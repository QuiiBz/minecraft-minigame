package fr.quiibz.zeus.feeders.listeners;

import fr.quiibz.commons.topic.TeleportTopic;
import fr.quiibz.zeus.instances.InstanceManager;
import fr.quiibz.zeus.instances.QueueManager;

public class TeleportFeedListener extends AbstractFeedListener<TeleportTopic> {

    /*
     *  FIELDS
     */

    private QueueManager queueManager;

    /*
     *  CONSTRUCTOR
     */

    public TeleportFeedListener(QueueManager queueManager) {

        this.queueManager = queueManager;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "teleport";
    }

    @Override
    public void listen(TeleportTopic object) {

        this.queueManager.teleport(object);
    }
}
