package fr.quiibz.zeus.commands;

import fr.quiibz.apollon.utils.Constants;
import fr.quiibz.zeus.instances.InstanceManager;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

    /*
     *  FIELDS
     */

    private InstanceManager instanceManager;

    /*
     *  CONSTRUCTOR
     */

    public HubCommand(String name) {

        super(name);

        this.instanceManager = InstanceManager.get();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            int lobbys = this.instanceManager.getInstancesStartedByServer(ServerType.LOBBY).size();
            ServerInstance instance = this.instanceManager.getRandomServer(ServerType.LOBBY);

            if(lobbys > 1) {

                while(player.getServer().getInfo().getName().equals(instance.getName()))
                    instance = this.instanceManager.getRandomServer(ServerType.LOBBY);

                player.sendMessage(Constants.PREFIX + "§eTéléportation vers le §6§lLobby #" + instance.getServerId());
                player.connect(instance.toServerInfo());

            } else {

                if(player.getServer().getInfo().getName().equals(instance.getName())) {

                    player.sendMessage(Constants.PREFIX + "§cVous êtes déjà connecté au seul Lobby disponible.");

                } else {

                    player.sendMessage(Constants.PREFIX + "§eTéléportation vers le §6§lLobby #" + instance.getServerId());
                    player.connect(instance.toServerInfo());
                }
            }
        }
    }
}
