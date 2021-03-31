package fr.quiibz.api.feeders;

import fr.quiibz.commons.topic.TeleportTopic;

public class TeleportFeeder extends AbstractFeeder<TeleportTopic> {

    /*
     *  FIELDS
     */

    private TeleportTopic teleport;

    /*
     *  CONSTRUCTOR
     */

    public TeleportFeeder(TeleportTopic teleport) {

        this.teleport = teleport;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "teleport";
    }

    @Override
    public TeleportTopic feed() {

        return this.teleport;
    }
}
