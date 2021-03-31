package fr.quiibz.commons.topic;

import fr.quiibz.commons.instances.ServerType;

import java.util.UUID;

public class CreateInstanceTopic {

    /*
     *  FIELDS
     */

    private UUID uuid;
    private ServerType serverType;

    /*
     *  CONSTRUCTOR
     */

    public CreateInstanceTopic() { }

    public CreateInstanceTopic(UUID uuid, ServerType serverType) {

        this.uuid = uuid;
        this.serverType = serverType;
    }

    /*
     *  METHODS
     */

    public UUID getUUID() {

        return this.uuid;
    }

    public ServerType getServerType() {

        return this.serverType;
    }
}
