package fr.quiibz.api.listeners;

import fr.quiibz.api.API;
import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.instances.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.UUID;

public class DefaultListener extends IListener {

    /*
     *  FIELDS
     */

    private API api;

    /*
     *  CONSTRUCTOR
     */

    public DefaultListener(API api) {

        this.api = api;
    }

    /*
     *  METHODS
     */

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        this.api.getNPCManager().onJoin(player);
        this.api.setUpdate(true);

        Bukkit.getScheduler().runTask(API.get(), () -> {

            RedisAccess redisAccess = RedisAccess.getInstance();
            RedissonClient redissonClient = redisAccess.getClient();

            RBucket<Account> rBucket = redissonClient.getBucket("account:" + player.getUniqueId().toString());
            Account account = rBucket.get();

            AccountManager.get().addAccount(account);

            String name = account.getRank().getOrder();

            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            Team team = (board.getTeam(name) == null ? board.registerNewTeam(name) : board.getTeam(name));
            team.setPrefix(account.getRank().getPrefix());
            team.addEntry(player.getName());

            // Cancel hide if it's now a lobby
            if(!API.get().getServerInstance().getServerType().equals(ServerType.LOBBY))
                return;

            account.setVisibility(account.getVisibility());

            Bukkit.getOnlinePlayers().forEach(current -> {

                Account playerAccount = AccountManager.get().getAccount(current);

                if(playerAccount.getVisibility() == 0)
                    current.showPlayer(player);
                    /// else if(visibility == 1) TODO friend
                else if(playerAccount.getVisibility() == 2)
                    current.hidePlayer(player);
            });
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent event) {

        API.get().setUpdate(true);

        UUID uuid = event.getPlayer().getUniqueId();

        AccountManager.get().removeAccount(uuid);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {

        if(event.toWeatherState())
            event.setCancelled(true);
    }
}
