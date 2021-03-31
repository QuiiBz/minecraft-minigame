package fr.quiibz.zeus.commands;

import fr.quiibz.apollon.data.redis.RedisAccess;
import fr.quiibz.apollon.utils.Constants;
import fr.quiibz.zeus.instances.InstanceManager;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.List;

public class InstanceCommand extends Command {

    /*
     *  FIELDS
     */

    private InstanceManager instanceManager;

    /*
     *  CONSTRUCTOR
     */

    public InstanceCommand(String name) {

        super(name);

        this.instanceManager = InstanceManager.get();
    }

    /*
     *  METHODS
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length == 2 || args.length == 3) {

            if(args[0].equals("create")) {

                ServerType serverType = ServerType.getByName(args[1]);

                if(serverType != null) {

                    sender.sendMessage(Constants.PREFIX + "§eCréation d'une nouvelle instance...");

                    this.instanceManager.createInstance(sender, serverType, (args.length == 3 ? args[2] : "console"));

                } else {

                    sender.sendMessage(Constants.PREFIX + "§cCe type d'instance n'existe pas.");
                }

            } else if(args[0].equals("delete") && args.length == 2) {

                this.instanceManager.deleteInstance(sender, args[1]);
            }

        } else if(args.length == 1) {

            if(args[0].equals("list")) {

                sender.sendMessage(Constants.LINE);
                sender.sendMessage(" ");
                sender.sendMessage(Constants.PREFIX + " §eListe des §6§linstance §e:");
                sender.sendMessage(" ");

                RedisAccess redisAccess = RedisAccess.getInstance();
                RedissonClient redissonClient = redisAccess.getClient();
                Iterable<String> keys = redissonClient.getKeys().getKeysByPattern("instance:*");

                keys.forEach(key -> {

                    RBucket<ServerInstance> rBucket = redissonClient.getBucket(key);
                    ServerInstance instance = rBucket.get();

                    sender.sendMessage(Constants.ROUND + "§d§l" + instance.getServerType().getName() + " #" + instance.getServerId() + " §7:");
                    sender.sendMessage(" " + Constants.ROUND + "Status : " + instance.getStatus().getName());
                    sender.sendMessage(" " + Constants.ROUND + "ID : §f" + instance.getId());
                    sender.sendMessage(" " + Constants.ROUND + "Host : §f" + instance.getHost());
                    sender.sendMessage(" " + Constants.ROUND + "Connectés : §a" + instance.getPlayers().size());
                });

                sender.sendMessage(" ");
                sender.sendMessage(Constants.LINE);
            }
        }
    }
}
