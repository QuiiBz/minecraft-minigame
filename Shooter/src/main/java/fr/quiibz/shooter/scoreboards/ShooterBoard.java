package fr.quiibz.shooter.scoreboards;

import fr.quiibz.api.API;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.players.IPlayerData;
import fr.quiibz.poseidon.scoreboards.AbstractGameBoardManager;
import fr.quiibz.shooter.game.ShooterGameManager;
import fr.quiibz.shooter.players.ShooterPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.swing.text.NumberFormatter;
import java.util.List;

public class ShooterBoard extends AbstractGameBoardManager {

    /*
     *  CONSTRUCTOR
     */

    public ShooterBoard(IGameManager gameManager) {

        super(gameManager);
    }

    /*
     *  METHODS
     */

    @Override
    public String getBoardName() {

        return "§6§lSHOOTER";
    }

    @Override
    public String[] getLines(Player player) {

        if(this.gameManager.getGameState().equals(GameState.WAITING))
            return super.getLines(player);
        else {

            ShooterPlayerData playerData = (ShooterPlayerData) this.gameManager.getData(player);

            List<String> best = ((ShooterGameManager) this.gameManager).getTop();

            return new String[] {
                    API.get().getServerInfo(),
                    " ",
                    "§7Joueurs : §a" + (Bukkit.getOnlinePlayers().size() - this.gameManager.getSpectators()),
                    " ",
                    "§7Objectif : §e" + this.gameManager.getConfig().getValue("Kills").get() + " tués",
                    "§7Tués : §c" + playerData.getKills() + " §7- Morts : §c" + playerData.getDeaths(),
                    "§7Tier : §a" + playerData.getTier() + " §7(" + (String.format("%.2f", (float) playerData.getCooldown() / 1000)) + "s)",
                    " ",
                    best.get(0),
                    best.get(1),
                    best.get(2),
                    " ",
                    "§7Durée : §e" + this.gameManager.getTimer().toString(),
                    " ",
                    "§eplay.gemcube.fr"};
        }
    }
}
