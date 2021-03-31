package fr.quiibz.commons.commands;

import fr.quiibz.apollon.Apollon;
import fr.quiibz.apollon.accounts.AccountProvider;
import fr.quiibz.zeus.feeders.RankFeeder;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.accounts.Rank;
import fr.quiibz.commons.topic.RankTopic;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RankCommand extends Command {

    /*
     *  CONSTRUCTOR
     */

    public RankCommand(String name) {

        super(name);
    }

    /*
     *  METHODS
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length == 3) {

            if(args[0].equals("set")) {

                String target = args[1];
                Rank rank = Rank.getRank(Integer.valueOf(args[2]));

                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(target);

                if(player != null && player.isConnected()) {

                    AccountProvider accountProvider = new AccountProvider(ProxyServer.getInstance().getPlayer(target));
                    Account targetAccount = accountProvider.getAccount();
                    targetAccount.setPower(rank.getPower());

                    new RankFeeder(new RankTopic(player.getUniqueId(), rank)).publish();

                    ProxyServer.getInstance().getScheduler().runAsync(Apollon.get(), () -> {

                        accountProvider.sendToRedis(targetAccount);
                        accountProvider.sendToDB(targetAccount);
                    });
                }
            }
        }
    }
}
