package fr.quiibz.api.scoreboards;

import fr.quiibz.api.API;
import fr.quiibz.api.scoreboards.fastboard.FastBoard;
import fr.quiibz.api.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class BoardManager implements IBoardManager {

    private Map<UUID, FastBoard> boards;

    public BoardManager() {

        this.boards = new HashMap<UUID, FastBoard>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(API.get(), this::updateBoards, 20, 20);
    }

    private void updateBoards() {

        List<Player> toRemove = new ArrayList<Player>();

        this.boards.forEach((uuid, board) -> {

            Player player = Bukkit.getPlayer(uuid);

            if(!PlayerUtils.exist(player))
                toRemove.add(player);
            else {

                String[] lines = this.getLines(player);

                if(lines != null)
                    board.updateLines(lines);
                else
                    toRemove.add(player);
            }
        });

        toRemove.forEach(this::handleLeave);
    }

    public void handleJoin(Player player) {

        FastBoard board = new FastBoard(player);
        board.updateTitle(this.getBoardName());

        this.boards.put(player.getUniqueId(), board);
    }

    public void handleLeave(Player player) {

        FastBoard board = this.boards.remove(player.getUniqueId());

        if(board != null)
            board.delete();
    }
}
