package fr.quiibz.poseidon.players;

import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IPlayerData {

    /*
     *  METHODS
     */

    void handleStart(IGameManager gameManager);
    void setUUID(UUID uuid);
    UUID getUUID();
    void addCoins(int coins);
    int getCoins();
    void addKill();
    int getKills();
    void addDeath();
    int getDeaths();
    Player toPlayer();
}
