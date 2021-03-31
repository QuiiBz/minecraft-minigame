package fr.quiibz.commons.topic;

import fr.quiibz.commons.accounts.Rank;

import java.util.UUID;

public class RankTopic {

    /*
     *  FIELDS
     */

    private UUID uuid;
    private Rank rank;

    /*
     *  CONSTRUCTOR
     */

    public RankTopic() { }

    public RankTopic(UUID uuid, Rank rank) {

        this.uuid = uuid;
        this.rank = rank;
    }

    /*
     *  METHODS
     */

    public UUID getUUID() {

        return this.uuid;
    }

    public Rank getRank() {

        return this.rank;
    }
}
