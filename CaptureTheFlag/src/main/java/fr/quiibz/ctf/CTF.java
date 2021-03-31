package fr.quiibz.ctf;

import fr.quiibz.api.API;
import fr.quiibz.api.scoreboards.IBoardManager;
import fr.quiibz.ctf.commands.KitCommand;
import fr.quiibz.ctf.game.CTFGameManager;
import fr.quiibz.ctf.gui.CTFConfigGui;
import fr.quiibz.ctf.listeners.GameListener;
import fr.quiibz.ctf.scoreboards.CTFBoard;
import fr.quiibz.poseidon.AbstractGame;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.gui.ConfigGui;

public class CTF extends AbstractGame {

    /*
     *  METHODS
     */

    @Override
    public void enable(API api) {

        api.register(this, new GameListener(this.gameManager));
        api.register(this, new KitCommand(this.gameManager));
    }

    @Override
    public IGameManager getGameManager() {

        return new CTFGameManager();
    }

    @Override
    public IBoardManager getBoardManager() {

        return new CTFBoard(this.gameManager);
    }

    @Override
    public ConfigGui getConfigGui() {

        return new CTFConfigGui(this.gameManager);
    }
}
