package fr.quiibz.duels.players;

import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.players.AbstractPlayerData;

import java.util.UUID;

public class DuelsPlayerData extends AbstractPlayerData {

    /*
     *  FIELDS
     */

    private DuelsKit duelsKit;

    /*
     *  CONSTRUCTOR
     */

    public DuelsPlayerData(UUID uuid) {

        super(uuid);

        this.duelsKit = DuelsKit.GUERRIER;
    }

    /*
     *  METHODS
     */

    @Override
    public void handleStart(IGameManager gameManager) {

        this.duelsKit.give(this.toPlayer());
    }

    public DuelsKit getKit() {

        return this.duelsKit;
    }

    public void setKit(DuelsKit duelsKit) {

        this.duelsKit = duelsKit;
    }
}
