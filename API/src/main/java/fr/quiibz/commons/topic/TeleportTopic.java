package fr.quiibz.commons.topic;

import fr.quiibz.commons.instances.ServerType;

import java.util.UUID;

public class TeleportTopic {

    /*
     *  FIELDS
     */

    private UUID uuid;
    private ServerType serverType;
    private int serverId;

    /*
     *  CONSTRUCTOR
     */

    public TeleportTopic() { }

    public TeleportTopic(UUID uuid, ServerType serverType, int serverId) {

        this.uuid = uuid;
        this.serverType = serverType;
        this.serverId = serverId;
    }
}
