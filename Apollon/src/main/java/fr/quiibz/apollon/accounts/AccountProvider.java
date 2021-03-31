package fr.quiibz.apollon.accounts;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.quiibz.apollon.data.mongo.MongoManager;
import fr.quiibz.apollon.data.redis.RedisAccess;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.accounts.PetsType;
import fr.quiibz.commons.accounts.Rank;
import fr.quiibz.commons.accounts.Settings;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AccountProvider {

    public static final String REDIS_KEY = "account:";

    private ProxiedPlayer player;
    private RedisAccess access;

    public AccountProvider(ProxiedPlayer player) {

        this.player = player;
        this.access = RedisAccess.getInstance();
    }

    public Account getAccount() {

        Account account = this.provideFromRedis();

        if(account == null) {

            account = this.provideFromDB();

            this.sendToRedis(account);
        }

        return account;
    }

    public void sendToRedis(Account account) {

        RedissonClient client = this.access.getClient();
        String key = REDIS_KEY + this.player.getUniqueId().toString();
        RBucket<Account> rBucket = client.getBucket(key);

        rBucket.set(account);
    }

    private Account provideFromRedis() {

        RedissonClient client = this.access.getClient();
        String key = REDIS_KEY + this.player.getUniqueId().toString();
        RBucket<Account> rBucket = client.getBucket(key);

        return rBucket.get();
    }

    private Account provideFromDB() {

        UUID uuid = this.player.getUniqueId();

        MongoCollection<Account> mongoCollection = MongoManager.get().getCollection("accounts", Account.class);
        Account account = mongoCollection.find(Filters.eq("uuid", player.getUniqueId())).first();

        if(account != null)
            return account;
        else
            return new Account(uuid, 0, 0, 0, PetsType.NONE, new HashMap<String, UUID>());
    }

    public void sendToDB(Account account) {

        MongoCollection<Account> mongoCollection = MongoManager.get().getCollection("accounts", Account.class);
        mongoCollection.deleteOne(Filters.eq("uuid", player.getUniqueId()));
        mongoCollection.insertOne(account);
    }
}
