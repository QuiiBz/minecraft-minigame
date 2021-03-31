package fr.quiibz.poseidon.game;

import fr.quiibz.api.utils.Timer;
import fr.quiibz.poseidon.config.IConfig;
import fr.quiibz.poseidon.players.IPlayerData;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IGameManager {

    /*
     *  METHODS
     */

    void handleJoin(Player player);
    void handleLeave(Player player);
    void handleDeath(Player player, boolean death);
    void handleStart(List<Player> players);
    void handleStop(Player winner);
    Map<UUID, IPlayerData> getData();
    IPlayerData getData(Player player);
    GameState getGameState();
    int getMinPlayers();
    int getMaxPlayers();
    Timer getTimer();
    int getSpectators();
    IConfig generateConfig();
    IConfig getConfig();
}
