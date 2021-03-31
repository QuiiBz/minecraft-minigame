package fr.quiibz.apollon.commands;

import fr.quiibz.apollon.friends.FriendsManager;
import fr.quiibz.apollon.utils.Constants;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FriendsCommand extends Command {

    /*
     *  FIELDS
     */

    private FriendsManager friendsManager;

    /*
     *  CONSTRUCTOR
     */

    public FriendsCommand(FriendsManager friendsManager) {

        super("friends", null, "friend", "f");

        this.friendsManager = friendsManager;
    }

    /*
     *  METHODS
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(args.length == 1) {

            if(args[0].equals("list"))
                this.friendsManager.sendList(player);
            else
                this.sendHelp(player);

        } else if(args.length == 2) {

            if(args[0].equals("add"))
                this.friendsManager.add(player, args[1]);
            else if(args[0].equals("remove"))
                this.friendsManager.remove(player, args[1]);
            else
                this.sendHelp(player);

        } else
            this.sendHelp(player);
    }

    private void sendHelp(ProxiedPlayer player) {

        player.sendMessage(" ");
        player.sendMessage(Constants.PREFIX + "§eAide pour les §6§lAmis §e:");
        player.sendMessage(" ");
        player.sendMessage(Constants.ROUND + "§b/f list §f§l» §7Voir sa liste d'amis");
        player.sendMessage(Constants.ROUND + "§b/f add <joueur> §f§l» §7Ajouter un ami");
        player.sendMessage(Constants.ROUND + "§b/f remove <joueur> §f§l» §7Supprimer un ami");
        player.sendMessage(" ");
    }
}
