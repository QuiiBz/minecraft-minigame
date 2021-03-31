package fr.quiibz.duels;

import fr.quiibz.api.API;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.scoreboards.IBoardManager;
import fr.quiibz.duels.gui.DuelsConfigGui;
import fr.quiibz.duels.listeners.GameListener;
import fr.quiibz.duels.scoreboards.DuelsBoard;
import fr.quiibz.duels.game.DuelsGameManager;
import fr.quiibz.poseidon.AbstractGame;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.gui.ConfigGui;

public class Duels extends AbstractGame {

    /*
     *  FIELDS
     */

    /*
     *  METHODS
     */

    @Override
    public void enable(API api) {

        api.register(this, new GameListener(this.gameManager));
    }

    @Override
    public IGameManager getGameManager() {

        return new DuelsGameManager();
    }

    @Override
    public IBoardManager getBoardManager() {

        return new DuelsBoard(this.gameManager);
    }

    @Override
    public ConfigGui getConfigGui() {

        return new DuelsConfigGui(this.gameManager);
    }
}
