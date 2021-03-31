package fr.quiibz.ctf.scoreboards;

import fr.quiibz.api.API;
import fr.quiibz.ctf.game.CTFGameManager;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.players.AbstractTeamPlayerData;
import fr.quiibz.poseidon.scoreboards.AbstractGameBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CTFBoard extends AbstractGameBoardManager {

    /*
     *  CONSTRUCTOR
     */

    public CTFBoard(IGameManager gameManager) {

        super(gameManager);
    }

    /*
     *  METHODS
     */

    @Override
    public String getBoardName() {

        return "§6§lCAPTURETHEFLAG";
    }

    @Override
    public String[] getLines(Player player) {

        if(this.gameManager.getGameState().equals(GameState.WAITING))
            return super.getLines(player);
        else {

            AbstractTeamPlayerData playerData = (AbstractTeamPlayerData) this.gameManager.getData(player);

            int bluePoints = ((CTFGameManager) this.gameManager).bluePoints;
            int redPoints = ((CTFGameManager) this.gameManager).redPoints;
            int points = (int) this.gameManager.getConfig().getValue("Points").get();

            return new String[]{
                    API.get().getServerInfo(),
                    " ",
                    "§9Bleu §7: §a" + bluePoints + "§7/" + points,
                    "§cRouge §7: §a" + redPoints + "§7/" + points,
                    " ",
                    "§7Equipe : " + playerData.getTeam().getName(),
                    "§7Tués : §c" + playerData.getKills() + " §7- Morts : §c" + playerData.getDeaths(),
                    " ",
                    "§7Durée : §e" + this.gameManager.getTimer().toString(),
                    " ",
                    "§eplay.gemcube.fr"};
        }
    }
}
