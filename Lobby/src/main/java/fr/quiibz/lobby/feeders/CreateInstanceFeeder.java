package fr.quiibz.lobby.feeders;

import fr.quiibz.api.feeders.AbstractFeeder;
import fr.quiibz.commons.topic.CreateInstanceTopic;

public class CreateInstanceFeeder extends AbstractFeeder<CreateInstanceTopic> {

    /*
     *  FIELDS
     */

    private CreateInstanceTopic instanceTopic;

    /*
     *  CONSTRUCTOR
     */

    public CreateInstanceFeeder(CreateInstanceTopic instanceTopic) {

        this.instanceTopic = instanceTopic;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "createInstance";
    }

    @Override
    public CreateInstanceTopic feed() {

        return this.instanceTopic;
    }
}
