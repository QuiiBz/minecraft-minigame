package fr.quiibz.lobby.pets;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.accounts.PetsType;
import org.bukkit.entity.Player;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PetsManager {

    /*
     *  FIELDS
     */

    private static PetsManager instance;
    private Map<UUID, Pet> pets;

    /*
     *  CONSTRUCTOR
     */

    public PetsManager() {

        instance = this;
        this.pets = new HashMap<UUID, Pet>();
    }

    /*
     *  METHODS
     */

    public void update(Player player) {

        if(this.hasPet(player)) {

            Pet pet = this.pets.get(player.getUniqueId());

            pet.updateLocation(player.getLocation());
        }
    }

    public Pet getPet(Player player) {

        return this.pets.get(player.getUniqueId());
    }

    public void setPet(Player player, PetsType petsType) {

        this.remove(player);

        Account account = AccountManager.get().getAccount(player);
        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        RBucket<Account> rBucket = redissonClient.getBucket("account:" + player.getUniqueId().toString());

        account.setPet(petsType);
        rBucket.set(account);

        this.pets.put(player.getUniqueId(), new Pet(petsType, player.getLocation()));
    }

    private boolean hasPet(Player player) {

        return this.pets.containsKey(player.getUniqueId());
    }

    public void remove(Player player) {

        Pet pet = this.pets.remove(player.getUniqueId());

        if(pet != null)
            pet.delete();
    }

    public static PetsManager get() {

        return instance;
    }
}
