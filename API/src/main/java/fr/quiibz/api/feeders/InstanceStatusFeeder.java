package fr.quiibz.api.feeders;

import fr.quiibz.commons.instances.ServerInstance;

public class InstanceStatusFeeder extends AbstractFeeder<ServerInstance> {

    /*
     *  FIELDS
     */

    private ServerInstance serverInstance;

    /*
     *  CONSTRUCTOR
     */

    public InstanceStatusFeeder(ServerInstance serverInstance) {

        this.serverInstance = serverInstance;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "instanceStatus";
    }

    @Override
    public ServerInstance feed() {

        return this.serverInstance;
    }
}
