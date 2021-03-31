package fr.quiibz.apollon.friends;

import fr.quiibz.apollon.accounts.AccountManager;
import fr.quiibz.apollon.accounts.AccountProvider;
import fr.quiibz.apollon.data.redis.RedisAccess;
import fr.quiibz.apollon.feeders.FriendsFeeder;
import fr.quiibz.apollon.utils.Constants;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.topic.FriendsTopic;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FriendsManager {

    /*
     *  FIELDS
     */

    private Map<UUID, UUID> requests;

    /*
     *  CONSTRUCTOR
     */

    public FriendsManager() {

        this.requests = new HashMap<UUID, UUID>();
    }

    /*
     *  METHODS
     */

    public void sendList(ProxiedPlayer player) {

        Account account = AccountManager.get().getAccount(player);
        Map<String, UUID> friends = account.getFriends();

        player.sendMessage(" ");
        player.sendMessage(Constants.PREFIX + "§eListe de tes §6§lAmis §e:");
        player.sendMessage(" ");

        if(friends.isEmpty())
            player.sendMessage(Constants.ROUND + "§cTu n'as pas encore d'amis ! §7Fait §f/friend add <joueur> §7pour en ajouter un.");
        else {

            friends.forEach((pseudo, uuid) -> {

                ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(uuid);

                if(friend != null && friend.isConnected())
                    player.sendMessage(Constants.ROUND + "§b" + friend.getName() + " " + Constants.ALLOW);
                else
                    player.sendMessage(Constants.ROUND + "§b" + pseudo + " " + Constants.DENY);
            });
        }

        player.sendMessage(" ");
    }

    public void add(ProxiedPlayer player, String target) {

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(target);

        if(friend != null && friend.isConnected()) {

            if(this.requests.containsKey(player.getUniqueId())) {

                UUID lastRequest = this.requests.get(player.getUniqueId());

                if(friend.getUniqueId().equals(lastRequest)) {

                    this.requests.remove(player.getUniqueId());

                    Account account = AccountManager.get().getAccount(player);
                    account.getFriends().put(friend.getName(), friend.getUniqueId());
                    RBucket<Account> rBucket = redissonClient.getBucket("account:" + player.getUniqueId().toString());
                    rBucket.set(account);

                    account = AccountManager.get().getAccount(friend);
                    account.getFriends().put(player.getName(), player.getUniqueId());
                    rBucket = redissonClient.getBucket("account:" + friend.getUniqueId().toString());
                    rBucket.set(account);

                    new FriendsFeeder(new FriendsTopic(player.getName(), player.getUniqueId(), friend.getName(), friend.getUniqueId(), 0)).publish();
                    new FriendsFeeder(new FriendsTopic(friend.getName(), friend.getUniqueId(), player.getName(), player.getUniqueId(), 0)).publish();

                    player.sendMessage(" ");
                    player.sendMessage(Constants.PREFIX + "§eVous êtes maintenant ami avec §a" + friend.getName());
                    player.sendMessage(" ");

                    friend.sendMessage(" ");
                    friend.sendMessage(Constants.PREFIX + "§eVous êtes maintenant ami avec §a" + player.getName());
                    friend.sendMessage(" ");

                } else
                    player.sendMessage(Constants.PREFIX + "§cCe joueur a déjà une demande d'amis en attente.");

            } else if(!this.hasFriend(player, friend.getUniqueId())) {

                this.requests.put(friend.getUniqueId(), player.getUniqueId());

                player.sendMessage(" ");
                player.sendMessage(Constants.PREFIX + "§eVous avez envoyé une demande d'amis à §b" + friend.getName());
                player.sendMessage(" ");

                friend.sendMessage(" ");
                friend.sendMessage(Constants.PREFIX + "§eVous avez reçu une demande d'amis de §b" + friend.getName() + " §e:");
                friend.sendMessage(" ");
                friend.sendMessage(Constants.ROUND + "§7Fait §f/friend add " + player.getName() + " §7pour accepter");
                friend.sendMessage(" ");

            } else
                player.sendMessage(Constants.PREFIX + "§cCe joueur est déjà amis avec vous.");

        } else
            player.sendMessage(Constants.PREFIX + "§cCe joueur n'est pas connecté.");
    }

    public void remove(ProxiedPlayer player, String target) {

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(target);

        if(friend != null && friend.isConnected()) {

            if(this.hasFriend(player, friend.getUniqueId())) {

                Account account = AccountManager.get().getAccount(player);
                account.getFriends().remove(friend.getName());
                RBucket<Account> rBucket = redissonClient.getBucket("account:" + player.getUniqueId().toString());
                rBucket.set(account);

                account = AccountManager.get().getAccount(friend);
                account.getFriends().remove(player.getName());
                rBucket = redissonClient.getBucket("account:" + friend.getUniqueId().toString());
                rBucket.set(account);

                new FriendsFeeder(new FriendsTopic(player.getName(), player.getUniqueId(), friend.getName(), friend.getUniqueId(), 1)).publish();
                new FriendsFeeder(new FriendsTopic(friend.getName(), friend.getUniqueId(), player.getName(), player.getUniqueId(), 1)).publish();

                player.sendMessage(Constants.PREFIX + "§eVous n'êtes plus amis avec §c" + friend.getName());

                friend.sendMessage(Constants.PREFIX + "§c" + friend.getName() + " §evous a retiré de sa liste d'amis");

            } else
                player.sendMessage(Constants.PREFIX + "§cCe joueur n'est pas votre amis.");

        } else
            player.sendMessage(Constants.PREFIX + "§cCe joueur n'est pas connecté.");
    }

    private boolean hasFriend(ProxiedPlayer player, UUID target) {

        return AccountManager.get().getAccount(player).getFriends().containsValue(target);
    }
}
