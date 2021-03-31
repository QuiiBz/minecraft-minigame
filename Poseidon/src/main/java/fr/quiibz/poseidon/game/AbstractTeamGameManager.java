package fr.quiibz.poseidon.game;

import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.poseidon.players.AbstractTeamPlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class AbstractTeamGameManager extends AbstractGameManager {

    /*
     *  METHODS
     */

    @Override
    public void handleJoin(Player player) {

        super.handleJoin(player);

        player.getInventory().setItem(0, new ItemBuilder(Material.BANNER,   1, (byte) 15).setName("§6§lEquipe" + Constants.RCLICK).toItemStack());
    }

    @Override
    public void handleStart(List<Player> players) {

        players.forEach(player -> {

            AbstractTeamPlayerData playerData = (AbstractTeamPlayerData) this.getData(player);

            if(playerData.getTeam().equals(GameTeam.NONE))
                playerData.setTeam(this.findTeam());
        });
    }

    private GameTeam findTeam() {

        Map<GameTeam, List<String>> teams = this.toTeams();

        return teams.entrySet().stream().min(Comparator.comparingInt(current -> current.getValue().size())).get().getKey();
    }

    public Map<GameTeam, List<String>> toTeams() {

        Map<GameTeam, List<String>> teams = new HashMap<GameTeam, List<String>>();

        this.getData().forEach((uuid, data) -> {

            AbstractTeamPlayerData playerData = (AbstractTeamPlayerData) data;
            GameTeam team = playerData.getTeam();

            if(!team.equals(GameTeam.NONE)) {

                if(teams.containsKey(team)) {

                    List<String> previous = teams.remove(team);
                    previous.add(playerData.toPlayer().getName());
                    teams.put(team, previous);

                } else {

                    List<String> newList = new ArrayList<String>();
                    newList.add(playerData.toPlayer().getName());

                    teams.put(team, newList);
                }
            }
        });

        for(GameTeam team : GameTeam.values()) {

            if(!team.equals(GameTeam.NONE) && !teams.containsKey(team))
                teams.put(team, new ArrayList<String>());
        }

        return teams;
    }
}
