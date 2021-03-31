package fr.quiibz.apollon.listeners;

import fr.quiibz.apollon.Apollon;
import fr.quiibz.apollon.accounts.AccountManager;
import fr.quiibz.apollon.accounts.AccountProvider;
import fr.quiibz.apollon.utils.Constants;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.accounts.Rank;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    /*
     *  METHODS
     */

    @EventHandler
    public void onJoin(PostLoginEvent event) {

        ProxiedPlayer player = event.getPlayer();

        if(!Apollon.get().getMaintenanceManager().canJoin(player))
            player.disconnect(new TextComponent("§c§lErreur\n\n§7Le serveur est actuellement en §nmaintenance§r.\n§7Malheuresement, vous n'êtes pas autorisé à rejoindre le serveur."));

        ProxyServer.getInstance().getScheduler().runAsync(Apollon.get(), () -> {

            AccountProvider accountProvider = new AccountProvider(player);
            Account account = accountProvider.getAccount();

            accountProvider.sendToRedis(account);
            Rank rank = account.getRank();

            player.setTabHeader(new TextComponent("\n§6§lGEMCUBE\n§eplay.gemcube.fr\n"), new TextComponent("\n§7Besoin d'aide ? §f§l➥ /help\n"));

            Title title = ProxyServer.getInstance().createTitle()
                    .title(new TextComponent("§6§lGemCube"))
                    .subTitle(new TextComponent("§a§k!§d§k!§r §eBon jeu §d§k!§a§k!"));

            title.send(player);

            player.sendMessage(Constants.LINE);
            player.sendMessage(" ");
            player.sendMessage(Constants.PREFIX + "§eBienvenue sur §6§lGemCube §7(Alpha)§e, " + rank.getPrefix() + " " + player.getName());
            player.sendMessage(" ");
            player.sendMessage(Constants.ROUND + "§7Il y a actuellement §a§l" + ProxyServer.getInstance().getOnlineCount() + " §7connectés !");
            player.sendMessage(" ");
            player.sendMessage(Constants.LINE);

            AccountManager.get().addAccount(account);
        });
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {

        ProxiedPlayer player = event.getPlayer();

        ProxyServer.getInstance().getScheduler().runAsync(Apollon.get(), () -> {

            AccountProvider accountProvider = new AccountProvider(player);
            Account account = accountProvider.getAccount();

            accountProvider.sendToDB(account);

            AccountManager.get().removeAccount(player.getUniqueId());
        });
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {

        ServerPing serverPing = event.getResponse();
        serverPing.setDescription("       &f&l»&e&l» &6&lGEMCUBE &8- &7Mini-Jeux &afun &7et &bdélire &e&l«&f&l«\n          &c&lEn développement &8-  &e&ngemcube.fr".replaceAll("&", "§"));
        serverPing.setVersion(new ServerPing.Protocol(Apollon.get().getMaintenanceManager().isMaintenanceEnabled() ? "§4Maintenance en cours" : "§41.8 §7» §41.15", serverPing.getVersion().getProtocol()));

        event.setResponse(serverPing);
    }
}
