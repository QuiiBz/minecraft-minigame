package fr.quiibz.poseidon.players;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.poseidon.Poseidon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.UUID;

public abstract class AbstractPlayerData implements IPlayerData {

    /*
     *  FIELDS
     */

    private UUID uuid;
    private int coins;
    private int kills;
    private int deaths;

    /*
     *  CONSTRUCTOR
     */

    public AbstractPlayerData(UUID uuid) {

        this.uuid = uuid;
        this.coins =  0;
        this.kills = 0;
        this.deaths = 0;
    }

    /*
     *  METHODS
     */

    @Override
    public void setUUID(UUID uuid) {

        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {

        return this.uuid;
    }

    @Override
    public void addCoins(int coins) {

        this.coins += coins;

        Bukkit.getScheduler().runTaskAsynchronously(Poseidon.get(), () -> {

            Player player = this.toPlayer();

            Account account = AccountManager.get().getAccount(player);
            RedisAccess redisAccess = RedisAccess.getInstance();
            RedissonClient redissonClient = redisAccess.getClient();
            RBucket<Account> rBucket = redissonClient.getBucket("account:" + player.getUniqueId().toString());

            account.setCoins(account.getCoins() + coins);
            rBucket.set(account);
        });
    }

    @Override
    public int getCoins() {

        return this.coins;
    }

    @Override
    public void addKill() {

        this.kills++;
    }

    @Override
    public int getKills() {

        return this.kills;
    }

    @Override
    public void addDeath() {

        this.deaths++;
    }

    @Override
    public int getDeaths() {

        return this.deaths;
    }

    @Override
    public Player toPlayer() {

        return Bukkit.getPlayer(this.uuid);
    }
}
