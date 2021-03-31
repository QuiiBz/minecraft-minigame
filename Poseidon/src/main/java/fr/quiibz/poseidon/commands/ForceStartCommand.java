package fr.quiibz.poseidon.commands;

import fr.quiibz.api.commands.ICommand;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.command.CommandSender;

public class ForceStartCommand extends ICommand {

    /*
     *  FIELDS
     */

    private IGameManager gameManager;

    /*
     *  CONSTRUCTOR
     */

    public ForceStartCommand(IGameManager gameManager) {

        this.gameManager = gameManager;
    }

    /*
     *  METHODS
     */

    @Override
    public void perform(CommandSender sender, String[] args) {

        if(this.gameManager.getGameState().equals(GameState.WAITING))
            this.gameManager.getTimer().setExactSec(5);
    }

    @Override
    public String getCommand() {

        return "forcestart";
    }
}
