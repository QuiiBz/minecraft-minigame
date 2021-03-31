package fr.quiibz.apollon.feeders;

import fr.quiibz.commons.topic.FriendsTopic;
import fr.quiibz.zeus.feeders.AbstractFeeder;

public class FriendsFeeder extends AbstractFeeder<FriendsTopic> {

    /*
     *  FIELDS
     */

    private FriendsTopic topic;

    /*
     *  CONSTRUCTOR
     */

    public FriendsFeeder(FriendsTopic topic) {

        this.topic = topic;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "friends";
    }

    @Override
    public FriendsTopic feed() {

        return this.topic;
    }
}
