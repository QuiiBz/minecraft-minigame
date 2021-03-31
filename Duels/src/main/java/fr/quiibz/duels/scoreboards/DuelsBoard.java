package fr.quiibz.duels.scoreboards;

import fr.quiibz.api.API;
import fr.quiibz.duels.game.Duel;
import fr.quiibz.duels.game.DuelsGameManager;
import fr.quiibz.duels.players.DuelsPlayerData;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.scoreboards.AbstractGameBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DuelsBoard extends AbstractGameBoardManager {

    /*
     *  CONSTRUCTOR
     */

    public DuelsBoard(IGameManager gameManager) {

        super(gameManager);
    }

    /*
     *  METHODS
     */

    @Override
    public String getBoardName() {

        return "§6§lDUELS";
    }

    @Override
    public String[] getLines(Player player) {

        if(this.gameManager.getGameState().equals(GameState.WAITING))
            return super.getLines(player);
        else {

            DuelsPlayerData playerData = (DuelsPlayerData) this.gameManager.getData(player);

            return new String[] {
                    API.get().getServerInfo(),
                    " ",
                    "§7Joueurs : §a" + (Bukkit.getOnlinePlayers().size() - this.gameManager.getSpectators()),
                    "§7Carte : §6" + API.get().getServerInstance().getMap(),
                    " ",
                    "§7Kit : §b" + playerData.getKit().getName(),
                    "§7Tués : §c" + playerData.getKills(),
                    " ",
                    "§7Durée : §e" + this.gameManager.getTimer().toString(),
                    " ",
                    "§eplay.gemcube.fr"};
        }
    }
}
