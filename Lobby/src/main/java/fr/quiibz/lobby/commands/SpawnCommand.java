package fr.quiibz.lobby.commands;

import fr.quiibz.api.commands.ICommand;
import fr.quiibz.api.logger.Logger;
import fr.quiibz.api.utils.Constants;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends ICommand {

    /*
     *  METHODS
     */

    @Override
    public void perform(CommandSender sender, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            player.sendMessage(Constants.PREFIX + "§eVous avez été téléporté au spawn.");
            player.teleport(new Location(player.getWorld(), 24.5, 10, 2.5, 90, 0));
        }
    }

    @Override
    public String getCommand() {

        return "spawn";
    }
}
