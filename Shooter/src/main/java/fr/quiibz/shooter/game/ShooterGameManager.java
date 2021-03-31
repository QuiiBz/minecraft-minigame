package fr.quiibz.shooter.game;

import fr.quiibz.api.API;
import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.feeders.InstanceStopFeeder;
import fr.quiibz.api.feeders.TeleportFeeder;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.api.utils.PlayerFeatures;
import fr.quiibz.api.utils.PlayerUtils;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.commons.topic.TeleportTopic;
import fr.quiibz.poseidon.Poseidon;
import fr.quiibz.poseidon.config.IConfig;
import fr.quiibz.poseidon.config.values.IConfigValue;
import fr.quiibz.poseidon.config.values.IntegerConfigValue;
import fr.quiibz.poseidon.game.AbstractGameManager;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.SpawnPoint;
import fr.quiibz.poseidon.players.IPlayerData;
import fr.quiibz.shooter.config.ShooterConfig;
import fr.quiibz.shooter.players.ShooterPlayerData;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class ShooterGameManager extends AbstractGameManager {

    /*
     *  FIELDS
     */

    private List<SpawnPoint> spawns;
    private Map<UUID, UUID> kills;

    /*
     *  CONSTRUCTOR
     */

    public ShooterGameManager() {

        this.spawns = Arrays.asList(
                new SpawnPoint(-213, 19, -9, -45, 0),
                new SpawnPoint(-197, 14, 28, -180, 0),
                new SpawnPoint(-163, 19, 6, 90, 0),
                new SpawnPoint(-197, 14, 29, -145, 0),
                new SpawnPoint(-176, 9, -8, 0, 0),
                new SpawnPoint(-170, 14, 28, 145, 0));
        this.kills = new HashMap<UUID, UUID>();
    }

    /*
     *  METHODS
     */

    @Override
    public void handleLeave(Player player) {

        this.handleDeath(player, false);

        super.handleLeave(player);
    }

    @Override
    public void handleDeath(Player player, boolean death) {

        if(death) {

            if(player.getGameMode().equals(GameMode.SPECTATOR))
                return;

            IPlayerData playerData = this.getData(player);
            playerData.addDeath();

            Player killer = Bukkit.getPlayer(this.kills.get(player.getUniqueId()));

            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1F, 1F);

            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);

            this.broadcast("§c" + player.getName() + " §7a été tué par §a" + killer.getName());
            killer.sendMessage("§e+10 §7coins");

            playerData = this.getData(killer);
            playerData.addCoins(10);
            playerData.addKill();
            ((ShooterPlayerData) playerData).tryUpdate();

            IntegerConfigValue kills = (IntegerConfigValue) this.getConfig().getValue("Kills");

            if(playerData.getKills() >= kills.get()) {

                this.handleStop(killer);
                return;
            }

            new BukkitRunnable() {

                int time = 3;

                @Override
                public void run() {

                    if(time > 0)
                        PlayerFeatures.sendTitle(player, 0, 20, 20, "", "§7Réapparition dans §e" + time);
                    else {

                        if(getGameState().equals(GameState.PLAYING)) {

                            teleport(player);

                            getData(player).handleStart(ShooterGameManager.this);
                        }

                        this.cancel();
                    }

                    time--;
                }

            }.runTaskTimer(Poseidon.get(), 0, 20);

        } else if(this.getGameState().equals(GameState.PLAYING)) {

            if(Bukkit.getOnlinePlayers().size() <= 2)
                this.handleStop(new ArrayList<Player>(Bukkit.getOnlinePlayers()).get(0));
        }
    }

    @Override
    public void handleStart(List<Player> players) {

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = (board.getTeam("shooter") == null ? board.registerNewTeam("shooter") : board.getTeam("shooter"));
        team.setPrefix("§c");

        players.forEach(player -> {

            team.addEntry(player.getName());

            this.teleport(player);

            getData(player).handleStart(this);
        });
    }

    @Override
    public void handleStop(Player winner) {

        this.setGameState(GameState.FINISHING);

        IPlayerData winnerData = this.getData(winner);
        winnerData.addCoins(100);

        Bukkit.getOnlinePlayers().forEach(current -> {

            if(!current.equals(winner))
                this.setSpectator(current, true, false);

            IPlayerData currentData = this.getData(current);

            current.sendMessage(Constants.LINE);
            current.sendMessage(Constants.PREFIX + "§eLe Shooter est terminé :");
            current.sendMessage(Constants.ROUND + "§b" + winner.getName() + " §7est le gagnant avec §6§l" + winnerData.getKills() + " §7tués !");
            current.sendMessage(" ");
            current.sendMessage(Constants.PREFIX + "§eRécapitulatif :");
            current.sendMessage(Constants.ROUND + "§c" + currentData.getKills() + " §7tués - §c" + currentData.getDeaths() + " §7morts");
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

        return new ShooterPlayerData(player.getUniqueId());
    }

    @Override
    public IConfig generateConfig() {

        return new ShooterConfig(this);
    }

    public void shoot(Player player) {

        boolean canShoot = true;

        if(player.hasMetadata("cooldown")) {

            MetadataValue metadata = player.getMetadata("cooldown").get(0);

            if(metadata.asLong() > System.currentTimeMillis()) {

                player.sendMessage(Constants.PREFIX + "§cVous êtes en cooldown.");
                canShoot = false;
            }
        }

        if(!canShoot)
            return;

        long cooldown = ((ShooterPlayerData) this.getData(player)).getCooldown();
        player.setMetadata("cooldown", new FixedMetadataValue(Poseidon.get(), System.currentTimeMillis() + cooldown));

        Location loc = player.getEyeLocation();
        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1F, 1F));

        new BukkitRunnable() {

            int time = 0;
            Vector vector = loc.getDirection().normalize();
            List<Player> killed = new ArrayList<Player>();

            @Override
            public void run() {

                time += 2;

                double x = vector.getX() * time;
                double y = vector.getY() * time;
                double z = vector.getZ() * time;

                loc.add(x, y, z);

                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0F, 0F, 0F, 0F, 2);
                PlayerUtils.sendPacket(packet);

                killed.addAll(loc.getWorld().getNearbyEntities(loc, 1, 1, 1).stream().filter(current -> current instanceof Player && !current.equals(player) && player.hasLineOfSight(current)).map(current -> (Player) current).collect(Collectors.toList()));

                killed.forEach(current -> {

                    if(!ShooterGameManager.this.kills.containsKey(current.getUniqueId())) {

                        ShooterGameManager.this.kills.put(current.getUniqueId(), player.getUniqueId());
                        handleDeath(current, true);
                    }
                });

                loc.subtract(x, y, z);

                if(time > 40) {

                    killed.forEach(current -> ShooterGameManager.this.kills.remove(current.getUniqueId()));

                    this.cancel();
                }
            }

        }.runTaskTimer(Poseidon.get(), 0, 1);
    }

    public List<String> getTop() {

        List<IPlayerData> playersData = this.datas.values().stream().sorted(Comparator.comparingInt(IPlayerData::getKills)).limit(3).collect(Collectors.toList());

        Collections.reverse(playersData);

        List<String> toReturn = playersData.stream().map(current -> "§f" + Bukkit.getPlayer(current.getUUID()).getName() + ": §6" + current.getKills()).collect(Collectors.toList());

        while(toReturn.size() < 3)
            toReturn.add("§cAucun");

        return toReturn;
    }

    private void teleport(Player player) {

        SpawnPoint spawn = this.spawns.get(new Random().nextInt(this.spawns.size()));

        player.teleport(new Location(player.getLocation().getWorld(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), spawn.getPitch()));
    }
}
