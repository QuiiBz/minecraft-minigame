package fr.quiibz.lobby.feeders.listener;

import fr.quiibz.api.feeders.listeners.AbstractFeedListener;
import fr.quiibz.lobby.Lobby;

public class TotalPlayersFeedListener extends AbstractFeedListener<Integer> {

    /*
     *  FIELDS
     */

    private Lobby lobby;

    /*
     *  CONSTRUCTOR
     */

    public TotalPlayersFeedListener(Lobby lobby) {

        this.lobby = lobby;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "totalPlayers";
    }

    @Override
    public void listen(Integer object) {

        this.lobby.setTotalPlayers(object);
    }
}
