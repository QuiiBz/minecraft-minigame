package fr.quiibz.poseidon.scoreboards;

import fr.quiibz.api.API;
import fr.quiibz.api.scoreboards.BoardManager;
import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class AbstractGameBoardManager extends BoardManager {

    /*
     *  FIELDS
     */

    protected IGameManager gameManager;

    /*
     *  CONSTRUCTOR
     */

    public AbstractGameBoardManager(IGameManager gameManager) {

        this.gameManager = gameManager;
    }

    /*
     *  METHODS
     */

    @Override
    public String[] getLines(Player player) {

        int players = Bukkit.getOnlinePlayers().size();

        if(players >= this.gameManager.getMinPlayers())
            return new String[] {
                    API.get().getServerInfo(),
                    " ",
                    "§7Joueurs : §a" + Bukkit.getOnlinePlayers().size() + "§7/" + this.gameManager.getConfig().getValue("Slots").get(),
                    "§7Carte : §6" + API.get().getServerInstance().getMap(),
                    " ",
                    "§7Départ : §e" + this.gameManager.getTimer().toString(),
                    " ",
                    "§eplay.gemcube.fr"};
        else
            return new String[] {
                    API.get().getServerInfo(),
                    " ",
                    "§7Joueurs : §a" + Bukkit.getOnlinePlayers().size() + "§7/" + this.gameManager.getConfig().getValue("Slots").get(),
                    "§7Carte : §6" + API.get().getServerInstance().getMap(),
                    " ",
                    "§7Manque §e" + (this.gameManager.getMinPlayers() - players) + " §7joueurs",
                    " ",
                    "§eplay.gemcube.fr"};
    }
}
