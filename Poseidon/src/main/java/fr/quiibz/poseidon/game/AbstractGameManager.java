package fr.quiibz.poseidon.game;

import fr.quiibz.api.API;
import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.feeders.InstanceStopFeeder;
import fr.quiibz.api.utils.*;
import fr.quiibz.api.utils.Timer;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.instances.ServerStatus;
import fr.quiibz.poseidon.config.IConfig;
import fr.quiibz.poseidon.players.AbstractPlayerData;
import fr.quiibz.poseidon.players.IPlayerData;
import org.apache.logging.log4j.core.helpers.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.*;

public abstract class AbstractGameManager implements IGameManager {

    /*
     *  FIELDS
     */

    private AbstractGameManager instance;
    private IConfig config;
    protected Map<UUID, IPlayerData> datas;
    private GameState gameState;
    private Timer timer;
    private List<UUID> spectators;

    /*
     *  CONSTRUCTOR
     */

    public AbstractGameManager() {

        instance = this;

        this.config = this.generateConfig();
        this.datas = new HashMap<UUID, IPlayerData>();
        this.gameState = GameState.WAITING;
        this.timer = new Timer(0, 30);
        this.spectators = new ArrayList<UUID>();

        Bukkit.getScheduler().runTaskTimer(API.get(), () -> {

            if(Bukkit.getOnlinePlayers().size() >= this.getMinPlayers() && this.gameState.equals(GameState.WAITING)) {

                this.timer.removeSec();

                Bukkit.getOnlinePlayers().forEach(current -> current.setLevel(this.timer.getExactSec()));

                if(this.timer.getExactMin() == 0) {

                    int sec = this.timer.getExactSec();

                    if(sec == 45 || sec == 30 || sec == 15 || (sec <= 5 && sec >= 1))
                        this.showStart();
                    else if(sec == 0)
                        this.start();
                }

            } else if(this.gameState.equals(GameState.PLAYING))
                this.timer.addSec();

        }, 20, 20);
    }

    /*
     *  METHODS
     */

    @Override
    public void handleJoin(Player player) {

        PlayerUtils.clear(player);
        player.getInventory().setItem(8, new ItemBuilder(Material.BED).setName("§c§lQuitter" + Constants.RCLICK).toItemStack());

        if(API.get().getServerInstance().getHost().equals(player.getName()))
            player.getInventory().setItem(1, new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§6§lConfiguration" + Constants.RCLICK).toItemStack());

        this.datas.put(player.getUniqueId(), this.toPlayerData(player));

        if(this.gameState.equals(GameState.WAITING)) {

            player.setLevel(this.timer.getExactSec());

            int players = Bukkit.getOnlinePlayers().size();

            Bukkit.getOnlinePlayers().forEach(current -> {

                PlayerFeatures.sendActionBar(current, "§e" + player.getName() + " §7a rejoint la partie (" + players + "/" + this.getMaxPlayers() + ")");
            });
        }
    }

    @Override
    public void handleLeave(Player player) {

        this.datas.remove(player.getUniqueId()).getCoins();

        int players = Bukkit.getOnlinePlayers().size() - 1;

        if(this.gameState.equals(GameState.WAITING)) {

            if(players < this.getMinPlayers())
                this.timer = new Timer(0, 30);

            Bukkit.getOnlinePlayers().forEach(current -> {

                PlayerFeatures.sendActionBar(current, "§e" + player.getName() + " §7a quitté la partie (" + players + "/" + this.getMaxPlayers() + ")");
            });

        } else if(this.gameState.equals(GameState.PLAYING)) {

            if(players <= 0)
                new InstanceStopFeeder(API.get().getServerInstance().getId()).publish();
        }

        this.spectators.remove(player.getUniqueId());
    }

    @Override
    public void handleDeath(Player player, boolean death) {

        if(this.getGameState().equals(GameState.PLAYING)) {

            this.setSpectator(player, true, false);

            Player killer = player.getKiller();

            if(killer != null) {

                IPlayerData playerData = this.getData(killer);
                playerData.addKill();
            }
        }
    }

    @Override
    public Map<UUID, IPlayerData> getData() {

        return this.datas;
    }

    @Override
    public IPlayerData getData(Player player) {

        return this.datas.get(player.getUniqueId());
    }

    @Override
    public GameState getGameState() {

        return this.gameState;
    }

    @Override
    public Timer getTimer() {

        return this.timer;
    }

    @Override
    public IConfig getConfig() {

        return this.config;
    }

    public void setSpectator(Player player, boolean spec, boolean msg) {

        if(spec) {

            if(msg) {

                player.sendMessage(Constants.LINE);
                player.sendMessage(" ");
                player.sendMessage(Constants.PREFIX + "§eTu es §6§lspectateur §e:");
                player.sendMessage(Constants.ROUND + "§7Utilise la boussole pour te téléporter aux joueurs");
                player.sendMessage(Constants.ROUND + "§7Utilise l'étoile du Nether pour re-jouer");
                player.sendMessage(Constants.ROUND + "§7Utilise le lit pour quitter");
                player.sendMessage(" ");
                player.sendMessage(Constants.LINE);

                player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setName("§b§lMenu spectateur" + Constants.RCLICK).toItemStack());
                player.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).setName("§6§lRe-jouer" + Constants.RCLICK).toItemStack());
                player.getInventory().setItem(8, new ItemBuilder(Material.BED).setName("§c§lQuitter" + Constants.RCLICK).toItemStack());
                player.updateInventory();

                this.spectators.add(player.getUniqueId());
            }

            PlayerUtils.clear(player);
            player.setGameMode(GameMode.SPECTATOR);

        } else {

            if(this.spectators.contains(player.getUniqueId()))
                PlayerUtils.clear(player);

            this.spectators.remove(player.getUniqueId());
        }
    }

    private void showStart() {

        Bukkit.getOnlinePlayers().forEach(current -> {

            current.playSound(current.getLocation(), Sound.NOTE_PIANO, 1F, 1F);
            PlayerFeatures.sendTitle(current, 0, 20, 20, "", "§7Début dans §e" + this.timer.getExactSec() + " secondes");
        });
    }

    private void start() {

        this.gameState = GameState.PLAYING;

        API.get().getServerInstance().setStatus(ServerStatus.PLAYING);
        API.get().setUpdate(true);

        this.handleStart(new ArrayList<>(Bukkit.getOnlinePlayers()));

        Bukkit.getOnlinePlayers().forEach(current -> {

            current.playSound(current.getLocation(), Sound.NOTE_PIANO, 1F, 1F);
            PlayerFeatures.sendTitle(current, 0, 20, 20, "", "§eDébut de la partie !");
        });
    }

    public void broadcast(String message) {

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public void setGameState(GameState gameState) {

        this.gameState = gameState;
    }

    @Override
    public int getSpectators() {

        return this.spectators.size();
    }

    public abstract IPlayerData toPlayerData(Player player);

    public AbstractGameManager get() {

        return this.instance;
    }
}
