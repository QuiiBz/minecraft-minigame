package fr.quiibz.poseidon.players;

import fr.quiibz.poseidon.game.GameTeam;

import java.util.UUID;

public abstract class AbstractTeamPlayerData extends AbstractPlayerData {

    /*
     *  FIELDS
     */

    private GameTeam team;

    /*
     *  CONSTRUCTOR
     */

    public AbstractTeamPlayerData(UUID uuid) {

        super(uuid);

        this.team = GameTeam.NONE;
    }

    /*
     *  METHODS
     */

    public GameTeam getTeam() {

        return this.team;
    }

    public void setTeam(GameTeam team) {

        this.team = team;
    }
}
