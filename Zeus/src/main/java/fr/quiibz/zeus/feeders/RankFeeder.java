package fr.quiibz.zeus.feeders;

import fr.quiibz.commons.topic.RankTopic;

public class RankFeeder extends AbstractFeeder<RankTopic> {

    /*
     *  METHODS
     */

    private RankTopic topic;

    /*
     *  CONSTRUCTOR
     */

    public RankFeeder(RankTopic topic) {

        this.topic = topic;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "rank";
    }

    @Override
    public RankTopic feed() {

        return this.topic;
    }
}
