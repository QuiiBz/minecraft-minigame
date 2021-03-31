package fr.quiibz.apollon.accounts;

import fr.quiibz.commons.accounts.Account;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountManager {

    /*
     *  FIELDS
     */

    private static AccountManager instance;

    private List<Account> accounts;

    /*
     *  CONSTRUCTOR
     */

    public AccountManager() {

        instance = this;

        this.accounts = new ArrayList<Account>();
    }

    /*
     *  METHODS
     */

    public void addAccount(Account account) {

        this.accounts.add(account);
    }

    public void removeAccount(UUID uuid) {

        Account account = this.getAccount(uuid);

        /*RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();

        RBucket<Account> rBucket = redissonClient.getBucket("account:" + uuid.toString());
        rBucket.set(account);*/

        this.accounts.remove(account);
    }

    public Account getAccount(ProxiedPlayer player) {

        return this.getAccount(player.getUniqueId());
    }

    public Account getAccount(UUID uuid) {

        return this.accounts.stream().filter(current -> current.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public static AccountManager get() {

        return instance;
    }
}
