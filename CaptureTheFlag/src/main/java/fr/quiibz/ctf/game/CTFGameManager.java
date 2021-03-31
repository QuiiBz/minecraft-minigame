package fr.quiibz.ctf.game;

import fr.quiibz.api.API;
import fr.quiibz.api.feeders.InstanceStopFeeder;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.api.utils.PlayerFeatures;
import fr.quiibz.api.utils.PlayerUtils;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.commons.topic.TeleportTopic;
import fr.quiibz.ctf.config.CTFConfig;
import fr.quiibz.ctf.players.CTFPlayerData;
import fr.quiibz.poseidon.Poseidon;
import fr.quiibz.poseidon.config.IConfig;
import fr.quiibz.poseidon.game.AbstractTeamGameManager;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.GameTeam;
import fr.quiibz.poseidon.players.AbstractTeamPlayerData;
import fr.quiibz.poseidon.players.IPlayerData;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CTFGameManager extends AbstractTeamGameManager {

    /*
     *  FIELDS
     */

    public int bluePoints;
    public int redPoints;

    /*
     *  CONSTRUCTOR
     */

    public CTFGameManager() {

        this.bluePoints = 0;
        this.redPoints = 0;
    }

    /*
     *  METHODS
     */

    @Override
    public void handleJoin(Player player) {

        super.handleJoin(player);

        player.getInventory().setItem(4, new ItemBuilder(Material.NAME_TAG).setName("§6§lKit" + Constants.RCLICK).toItemStack());
    }

    @Override
    public void handleLeave(Player player) {

        this.handleDeath(player, false);

        super.handleLeave(player);
    }

    @Override
    public void handleDeath(Player player, boolean death) {

        super.handleDeath(player, death);

        CTFPlayerData playerData = (CTFPlayerData) this.getData(player);

        if(playerData.hasFlag()) {

            playerData.setHasFlag(false);
            Bukkit.broadcastMessage(Constants.PREFIX + "§eL'Equipe " + playerData.getTeam().getName() + " §ea perdu le drapeau " + playerData.getTeam().getName());
        }

        if(death) {

            playerData.addDeath();

            Player killer = player.getKiller();
            AbstractTeamPlayerData killerData = (AbstractTeamPlayerData) this.getData(killer);
            killerData.addCoins(10);

            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1F, 1F);

            this.broadcast(playerData.getTeam().getName() + " " + player.getName() + " §7a été tué par " + killerData.getTeam().getName() + " " + killer.getName());
            killer.sendMessage("§e+10 §7coins");

            new BukkitRunnable() {

                int time = 3;

                @Override
                public void run() {

                    if(time > 0)
                        PlayerFeatures.sendTitle(player, 0, 20, 20, "", "§7Réapparition dans §e" + time);
                    else {

                        if(getGameState().equals(GameState.PLAYING)) {

                            teleport(player);
                            getData(player).handleStart(CTFGameManager.this);
                        }

                        this.cancel();
                    }

                    time--;
                }

            }.runTaskTimer(Poseidon.get(), 0, 20);
        }
    }

    @Override
    public void handleStart(List<Player> players) {

        super.handleStart(players);

        players.forEach(player -> {

            this.teleport(player);
            this.getData(player).handleStart(CTFGameManager.this);
        });
    }

    private void teleport(Player player) {

        AbstractTeamPlayerData playerData = (AbstractTeamPlayerData) this.getData(player);

        if(playerData.getTeam().equals(GameTeam.BLUE))
            player.teleport(new Location(player.getWorld(), -139.5, 78, -35.5, -100, 0));
        else if(playerData.getTeam().equals(GameTeam.RED))
            player.teleport(new Location(player.getWorld(), 38.5, 78, -36.5, 120, 0));
    }

    @Override
    public IPlayerData toPlayerData(Player player) {

        return new CTFPlayerData(player.getUniqueId());
    }

    @Override
    public void handleStop(Player winner) {

        GameTeam winnerTeam = GameTeam.NONE;
        int points = (int) this.getConfig().getValue("Points").get();

        if(this.bluePoints >= points)
            winnerTeam = GameTeam.BLUE;
        else if(this.redPoints >= points)
            winnerTeam = GameTeam.RED;

        if(!winnerTeam.equals(GameTeam.NONE)) {

            this.setGameState(GameState.FINISHING);

            GameTeam finalWinnerTeam = winnerTeam;
            Bukkit.getOnlinePlayers().forEach(current -> {

                AbstractTeamPlayerData winnerData = (AbstractTeamPlayerData) this.getData(current);
                winnerData.addCoins(100);

                current.sendMessage(Constants.LINE);
                current.sendMessage(Constants.PREFIX + "§eLe CaptureTheFlag est terminé :");
                current.sendMessage(Constants.ROUND + "§7L'Equipe " + finalWinnerTeam.getName() + " §7est la gagnante avec §6§l" + points + " §7points !");
                current.sendMessage(" ");
                current.sendMessage(Constants.PREFIX + "§eRécapitulatif :");
                current.sendMessage(Constants.ROUND + "§c" + winnerData.getKills() + " §7tués - §c" + winnerData.getDeaths() + " §7morts");
                current.sendMessage(" ");
                current.sendMessage(" §b§k!§c§k!§r §a§l+" + winnerData.getCoins() + " §7coins §c§k!§b§k!");
                current.sendMessage(" ");
                current.sendMessage(Constants.ROUND + "§6§l/re §7pour re-jouer");
                current.sendMessage(Constants.LINE);

                PlayerUtils.clear(current);
                current.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).setName("§6§lRe-jouer" + Constants.RCLICK).toItemStack());
                current.getInventory().setItem(8, new ItemBuilder(Material.BED).setName("§c§lQuitter" + Constants.RCLICK).toItemStack());

                if(finalWinnerTeam.getName().equals(winnerData.getTeam().getName())) {

                    Firework firework = current.getWorld().spawn(current.getLocation(), Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    FireworkEffect.Builder builder = FireworkEffect.builder();
                    meta.addEffect(builder.flicker(true).withColor(Color.ORANGE).build());
                    meta.addEffect(builder.trail(true).build());
                    meta.addEffect(builder.withFade(Color.YELLOW).build());
                    firework.setFireworkMeta(meta);

                    PlayerFeatures.sendTitle(current, 0, 100, 20, "", "§6§lVictoire !");
                }
            });

            Bukkit.getScheduler().runTaskLater(API.get(), () -> {

                Bukkit.getOnlinePlayers().forEach(current -> new TeleportFeeder(new TeleportTopic(current.getUniqueId(), ServerType.LOBBY, -1)).publish());

                new InstanceStopFeeder(API.get().getServerInstance().getId()).publish();

            }, 20 * 10);
        }
    }

    @Override
    public int getMinPlayers() {

        return 2;
    }

    @Override
    public int getMaxPlayers() {

        return 32;
    }

    @Override
    public IConfig generateConfig() {

        return new CTFConfig(this);
    }

    public void checkCapture(Player player, Location to) {

        CTFPlayerData playerData = (CTFPlayerData) this.getData(player);

        if(playerData.hasFlag() && this.isInZone(playerData.getTeam(), to)) {

            Bukkit.getOnlinePlayers().forEach(current -> {

                AbstractTeamPlayerData currentData = (AbstractTeamPlayerData) this.getData(current);

                current.playSound(current.getLocation(), (currentData.getTeam().getName().equals(playerData.getTeam().getName()) ? Sound.LEVEL_UP : Sound.ANVIL_BREAK), 1F, 1F);
            });

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(Constants.PREFIX + playerData.getTeam().getName() + " " + player.getName() + " §ea marqué un point !");
            Bukkit.broadcastMessage(" ");

            playerData.setHasFlag(false);

            if(playerData.getTeam().equals(GameTeam.BLUE))
                this.bluePoints++;
            else
                this.redPoints++;

            teleport(player);
            getData(player).handleStart(CTFGameManager.this);

            this.handleStop(null);

        } else if(!playerData.hasFlag() && this.isInZone(playerData.getTeam().inverse(), to)) {

            Bukkit.getOnlinePlayers().forEach(current -> {

                AbstractTeamPlayerData currentData = (AbstractTeamPlayerData) this.getData(current);

                current.playSound(current.getLocation(), (currentData.getTeam().getName().equals(playerData.getTeam().getName()) ? Sound.LEVEL_UP : Sound.ANVIL_BREAK), 1F, 1F);
            });

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(Constants.PREFIX + playerData.getTeam().getName() + " " + player.getName() + " §ea récupéré le drapeau adverse !");
            Bukkit.broadcastMessage(" ");

            playerData.setHasFlag(true);
        }
    }

    private boolean isInZone(GameTeam team, Location loc) {

        if(team.equals(GameTeam.RED))
            return loc.getBlockX() >= 27 && loc.getBlockX() <= 29 && loc.getBlockZ() <= -43 && loc.getBlockZ() >= -45;
        else if(team.equals(GameTeam.BLUE))
            return loc.getBlockX() >= -130 && loc.getBlockX() <= -128 && loc.getBlockZ() >= -44 && loc.getBlockZ() <= -42;

        return true;
    }
}
