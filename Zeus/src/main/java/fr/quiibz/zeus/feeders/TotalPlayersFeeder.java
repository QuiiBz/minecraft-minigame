package fr.quiibz.zeus.feeders;

import net.md_5.bungee.api.ProxyServer;

public class TotalPlayersFeeder extends AbstractFeeder<Integer> {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "totalPlayers";
    }

    @Override
    public Integer feed() {

        return ProxyServer.getInstance().getOnlineCount();
    }
}
