package fr.quiibz.apollon.maintenance;

import java.util.List;

public class Maintenance {

    /*
     *  FIELDS
     */

    private boolean enabled;
    private List<String> players;

    /*
     *  CONSTRUCTOR
     */

    public Maintenance(boolean enabled, List<String> players) {

        this.enabled = enabled;
        this.players = players;
    }

    /*
     *  METHODS
     */

    public boolean isEnabled() {

        return this.enabled;
    }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

    public boolean contains(String player) {

        return this.players.contains(player);
    }

    public void addPlayer(String player) {

        if(!this.contains(player))
            this.players.add(player);
    }

    public void removePlayer(String player) {

        if(this.contains(player))
            this.players.remove(player);
    }
}
