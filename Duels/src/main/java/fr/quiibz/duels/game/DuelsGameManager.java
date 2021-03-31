package fr.quiibz.duels.game;

import fr.quiibz.api.API;
import fr.quiibz.api.feeders.InstanceStopFeeder;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.api.utils.PlayerFeatures;
import fr.quiibz.api.utils.PlayerUtils;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.commons.topic.TeleportTopic;
import fr.quiibz.duels.config.DuelsConfig;
import fr.quiibz.duels.players.DuelsPlayerData;
import fr.quiibz.poseidon.config.IConfig;
import fr.quiibz.poseidon.game.AbstractGameManager;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.players.IPlayerData;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class DuelsGameManager extends AbstractGameManager {

    /*
     *  FIELDS
     */

    private List<Duel> duels;
    private List<UUID> waiting;

    /*
     *  CONSTRUCTOR
     */

    public DuelsGameManager() {

        this.duels = new ArrayList<Duel>();
        this.waiting = new ArrayList<UUID>();
    }

    /*
     *  METHODS
     */

    @Override
    public void handleJoin(Player player) {

        super.handleJoin(player);

        player.getInventory().setItem(0, new ItemBuilder(Material.NAME_TAG).setName("§6§lKit" + Constants.RCLICK).toItemStack());
    }

    @Override
    public void handleLeave(Player player) {

        this.waiting.remove(player.getUniqueId());

        this.handleDeath(player, false);

        super.handleLeave(player);
    }

    @Override
    public void handleDeath(Player player, boolean death) {

        super.handleDeath(player, death);

        // Pas en attente
        if(!this.waiting.contains(player.getUniqueId())) {

            Duel duel = this.getDuel(player);

            // Si en duel
            if(duel != null) {

                Player winner = Bukkit.getPlayer(duel.getOpponent(player));
                duel.setWinner(winner.getUniqueId());

                if(death) {

                    this.broadcast("§c" + player.getName() + " §7a été tué par §a" + winner.getName());
                    winner.sendMessage("§e+10 §7coins");

                    IPlayerData playerData = this.getData(winner);
                    playerData.addCoins(10);
                }

                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

                if(this.isSomeoneWaiting()) {

                    Player opponent = Bukkit.getPlayer(this.waiting.remove(0));

                    this.duels.remove(duel);

                    duel = new Duel(winner.getUniqueId(), opponent.getUniqueId());
                    duel.start(this);

                    this.duels.add(duel);

                } else {

                    if(!this.getGameState().equals(GameState.PLAYING))
                        return;

                    if(players.stream().filter(current -> current.getGameMode().equals(GameMode.SURVIVAL)).count() == 1)
                        this.handleStop(players.stream().filter(current -> current.getGameMode().equals(GameMode.SURVIVAL)).findFirst().get());
                    else {

                        this.waiting.add(winner.getUniqueId());
                        winner.sendMessage(Constants.PREFIX + "§eRecherche d'un adversaire en cours...");
                    }
                }
            }
        }
    }

    @Override
    public void handleStart(List<Player> players) {

        while(players.size() >= 2)
            this.duels.add(new Duel(players.remove(0).getUniqueId(), players.remove(0).getUniqueId()));

        if(players.size() == 1) {

            Player waiting = players.remove(0);

            this.waiting.add(waiting.getUniqueId());

            this.setSpectator(waiting, true, false);
        }

        this.duels.forEach(duel -> duel.start(this));
    }

    @Override
    public void handleStop(Player winner) {

        this.setGameState(GameState.FINISHING);

        IPlayerData winnerData = this.getData(winner);
        winnerData.addCoins(100);

        Bukkit.getOnlinePlayers().forEach(current -> {

            IPlayerData currentData = this.getData(current);

            current.sendMessage(Constants.LINE);
            current.sendMessage(Constants.PREFIX + "§eLe Duels sont terminés :");
            current.sendMessage(Constants.ROUND + "§b" + winner.getName() + " §7est le gagnant avec §6§l" + winnerData.getKills() + " §7tués !");
            current.sendMessage(" ");
            current.sendMessage(Constants.PREFIX + "§eRécapitulatif :");
            current.sendMessage(Constants.ROUND + "§c" + currentData.getKills() + " §7tués");
            current.sendMessage(" ");
            current.sendMessage(" §b§k!§c§k!§r §a§l+" + currentData.getCoins() + " §7coins §c§k!§b§k!");
            current.sendMessage(" ");
            current.sendMessage(Constants.ROUND + "§6§l/re §7pour re-jouer");
            current.sendMessage(Constants.LINE);
        });

        Firework firework = winner.getWorld().spawn(winner.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Builder builder = FireworkEffect.builder();
        meta.addEffect(builder.flicker(true).withColor(Color.ORANGE).build());
        meta.addEffect(builder.trail(true).build());
        meta.addEffect(builder.withFade(Color.YELLOW).build());
        firework.setFireworkMeta(meta);

        PlayerUtils.clear(winner);
        winner.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).setName("§6§lRe-jouer" + Constants.RCLICK).toItemStack());
        winner.getInventory().setItem(8, new ItemBuilder(Material.BED).setName("§c§lQuitter" + Constants.RCLICK).toItemStack());

        PlayerFeatures.sendTitle(winner, 0, 100, 20, "", "§6§lVictoire !");

        Bukkit.getScheduler().runTaskLater(API.get(), () -> {

            Bukkit.getOnlinePlayers().forEach(current -> new TeleportFeeder(new TeleportTopic(current.getUniqueId(), ServerType.LOBBY, -1)).publish());

            new InstanceStopFeeder(API.get().getServerInstance().getId()).publish();

        }, 20 * 10);
    }

    @Override
    public int getMinPlayers() {

        return 2;
    }

    @Override
    public int getMaxPlayers() {

        return 16;
    }

    @Override
    public IPlayerData toPlayerData(Player player) {

        return new DuelsPlayerData(player.getUniqueId());
    }

    @Override
    public IConfig generateConfig() {

        return new DuelsConfig(this);
    }

    private boolean isSomeoneWaiting() {

        return this.waiting.size() >= 1;
    }

    public Duel getDuel(Player player) {

        return this.duels.stream().filter(current -> current.toFirstPlayer().equals(player) || current.toSecondPlayer().equals(player)).findFirst().orElse(null);
    }
}
