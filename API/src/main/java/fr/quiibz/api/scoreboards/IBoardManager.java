package fr.quiibz.api.scoreboards;

import org.bukkit.entity.Player;

public interface IBoardManager {

    void handleJoin(Player player);
    void handleLeave(Player player);
    String getBoardName();
    String[] getLines(Player player);
}
