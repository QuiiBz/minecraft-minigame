package fr.quiibz.ctf.commands;

import fr.quiibz.api.commands.ICommand;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.ctf.gui.KitGui;
import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand extends ICommand {

    /*
     *  FIELDS
     */

    private IGameManager gameManager;

    /*
     *  CONSTRUCTOR
     */

    public KitCommand(IGameManager gameManager) {

        this.gameManager = gameManager;
    }

    /*
     *  METHODS
     */

    @Override
    public void perform(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        GuiManager.open(new KitGui(this.gameManager.getData(player)), player);
    }

    @Override
    public String getCommand() {

        return "kit";
    }
}
