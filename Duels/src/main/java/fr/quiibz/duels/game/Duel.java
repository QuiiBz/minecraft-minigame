package fr.quiibz.duels.game;

import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class Duel {

    /*
     *  FIELDS
     */

    private UUID firstPlayer;
    private UUID secondPlayer;
    private UUID winner;

    /*
     *  CONSTRUCTOR
     */

    public Duel(UUID firstPlayer, UUID secondPlayer) {

        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    /*
     *  METHODS
     */

    public void start(DuelsGameManager gameManager) {

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = (board.getTeam("duel") == null ? board.registerNewTeam("duel") : board.getTeam("duel"));
        team.setPrefix("§c");

        Player firstPlayer = this.toFirstPlayer();
        Player secondPlayer = this.toSecondPlayer();

        gameManager.setSpectator(firstPlayer, false, false);
        gameManager.setSpectator(secondPlayer, false, false);

        gameManager.getData(firstPlayer).handleStart(gameManager);
        gameManager.getData(secondPlayer).handleStart(gameManager);

        firstPlayer.setHealth(20);
        firstPlayer.sendMessage(" ");
        firstPlayer.sendMessage(Constants.PREFIX + "§eDébut du §6§lDuel §e:");
        firstPlayer.sendMessage(Constants.ROUND + "§7Adversaire : §c" + secondPlayer.getName());
        firstPlayer.sendMessage(" ");

        team.addEntry(firstPlayer.getName());

        secondPlayer.setHealth(20);
        secondPlayer.sendMessage(" ");
        secondPlayer.sendMessage(Constants.PREFIX + "§eDébut du §6§lDuel §e:");
        secondPlayer.sendMessage(Constants.ROUND + "§7Adversaire : §c" + firstPlayer.getName());
        secondPlayer.sendMessage(" ");

        team.addEntry(secondPlayer.getName());
    }

    public UUID getOpponent(Player player) {

        return (this.toFirstPlayer().equals(player) ? this.secondPlayer : this.firstPlayer);
    }

    public Player toFirstPlayer() {

        return Bukkit.getPlayer(this.firstPlayer);
    }

    public Player toSecondPlayer() {

        return Bukkit.getPlayer(this.secondPlayer);
    }

    public void setWinner(UUID uuid) {

        this.winner = uuid;
    }
}
