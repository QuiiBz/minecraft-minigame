package fr.quiibz.lobby.scoreboards;

import fr.quiibz.api.API;
import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.scoreboards.BoardManager;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.accounts.Rank;
import fr.quiibz.lobby.Lobby;
import org.bukkit.entity.Player;

public class LobbyBoard extends BoardManager {

    /*
     *  METHODS
     */

    @Override
    public String getBoardName() {

        return "§6§lGEMCUBE";
    }

    @Override
    public String[] getLines(Player player) {

        Account account = AccountManager.get().getAccount(player);
        Rank rank = account.getRank();

        return new String[] {
                " ",
                "§7Grade : " + rank.getColor() + (rank.getName() == null ? "Joueur" : rank.getName()),
                "§7Coins : §e" + account.getCoins(),
                " ",
                "§7Connectés : §a" + Lobby.get().getTotalPlayers(),
                "§7Lobby §6#" + API.get().getServerInstance().getServerId(),
                " ",
                "§eplay.gemcube.fr"};
    }
}
