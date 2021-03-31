package fr.quiibz.shooter;

import fr.quiibz.api.API;
import fr.quiibz.api.scoreboards.IBoardManager;
import fr.quiibz.poseidon.AbstractGame;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.gui.ConfigGui;
import fr.quiibz.shooter.gui.ShooterConfigGui;
import fr.quiibz.shooter.listeners.GameListener;
import fr.quiibz.shooter.game.ShooterGameManager;
import fr.quiibz.shooter.scoreboards.ShooterBoard;

public class Shooter extends AbstractGame {

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

        return new ShooterGameManager();
    }

    @Override
    public IBoardManager getBoardManager() {

        return new ShooterBoard(this.gameManager);
    }

    @Override
    public ConfigGui getConfigGui() {

        return new ShooterConfigGui(this.gameManager);
    }
}
