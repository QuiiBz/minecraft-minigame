package fr.quiibz.poseidon.commands;

import fr.quiibz.api.API;
import fr.quiibz.api.commands.ICommand;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.commons.topic.TeleportTopic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReCommand extends ICommand {

    /*
     *  METHODS
     */

    @Override
    public void perform(CommandSender sender, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            new TeleportFeeder(new TeleportTopic(player.getUniqueId(), API.get().getServerInstance().getServerType(), -1)).publish();
        }
    }

    @Override
    public String getCommand() {

        return "re";
    }
}
