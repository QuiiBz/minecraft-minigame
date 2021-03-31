package fr.quiibz.ctf.players;

import fr.quiibz.api.utils.PlayerUtils;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.players.AbstractTeamPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class CTFPlayerData extends AbstractTeamPlayerData {

    /*
     *  FIELDS
     */

    private CTFKit ctfKit;
    private boolean hasFlag;

    /*
     *  CONSTRUCTOR
     */

    public CTFPlayerData(UUID uuid) {

        super(uuid);

        this.ctfKit = CTFKit.GUERRIER;
        this.hasFlag = false;
    }

    /*
     *  METHODS
     */

    @Override
    public void handleStart(IGameManager gameManager) {

        Player player = this.toPlayer();

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = (board.getTeam(this.getTeam().getName()) == null ? board.registerNewTeam(this.getTeam().getName()) : board.getTeam(this.getTeam().getName()));
        team.setPrefix(this.getTeam().getName() + " ");
        team.addEntry(player.getName());

        PlayerUtils.clear(player);

        this.ctfKit.give(player);
    }

    public CTFKit getKit() {

        return this.ctfKit;
    }

    public void setKit(CTFKit ctfKit) {

        this.ctfKit = ctfKit;
    }

    public boolean hasFlag() {

        return this.hasFlag;
    }

    public void setHasFlag(boolean hasFlag) {

        this.hasFlag = hasFlag;

        if(this.hasFlag) {

            Player player = this.toPlayer();
            PlayerUtils.clearArmor(player);
            player.getInventory().clear();

            for(int i = 0; i < 9; i++)
                player.getInventory().setItem(i, new ItemStack(Material.BANNER, 1, (byte) this.getTeam().inverse().getColor()));
        }
    }
}
