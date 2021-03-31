package fr.quiibz.api.commands;

import fr.quiibz.api.registers.CommandRegistrable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class ICommand extends CommandRegistrable implements CommandExecutor {

    /*
     *  METHODS
     */

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // TODO check permission
        this.perform(sender, args);

        return false;
    }

    @Override
    public CommandExecutor getExecutor() {

        return this;
    }

    public abstract void perform(CommandSender sender, String[] args);
}
