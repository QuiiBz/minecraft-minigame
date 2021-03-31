package fr.quiibz.zeus.instances;

import com.google.common.collect.Maps;
import fr.quiibz.apollon.data.redis.RedisAccess;
import fr.quiibz.apollon.utils.Constants;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerStatus;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.commons.topic.TeleportTopic;
import fr.quiibz.zeus.Zeus;
import fr.quiibz.zeus.feeders.TotalPlayersFeeder;
import fr.quiibz.zeus.feeders.listeners.InstanceCreateFeedListener;
import fr.quiibz.zeus.feeders.listeners.InstanceFeedListener;
import fr.quiibz.zeus.feeders.listeners.InstanceStopFeedListener;
import fr.quiibz.zeus.feeders.listeners.TeleportFeedListener;
import fr.quiibz.zeus.reconnect.ServerReconnectHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InstanceManager {

    /*
     *  FIELDS
     */

    private static InstanceManager instance;

    private List<ServerInstance> serverInstances;

    /*
     *  CONSTRUCTOR
     */

    public InstanceManager() {

        instance = this;

        this.serverInstances = new ArrayList<ServerInstance>();

        ProxyServer.getInstance().getScheduler().schedule(Zeus.get(), () -> {

            this.fetchInstance();

            new InstanceFeedListener(this);
            new InstanceStopFeedListener(this);
            new InstanceCreateFeedListener(this);

        }, 2, TimeUnit.SECONDS);

        ProxyServer.getInstance().getScheduler().schedule(Zeus.get(), this::feedTotalPlayers, 2, 1, TimeUnit.SECONDS);
        ProxyServer.getInstance().setReconnectHandler(new ServerReconnectHandler());
    }

    /*
     *  METHODS
     */

    private void fetchInstance() {

        ProxyServer.getInstance().getScheduler().runAsync(Zeus.get(), () -> {

            RedisAccess redisAccess = RedisAccess.getInstance();
            RedissonClient redissonClient = redisAccess.getClient();
            Iterable<String> keys = redissonClient.getKeys().getKeysByPattern("instance:*");

            keys.forEach(key -> {

                RBucket<ServerInstance> rBucket = redissonClient.getBucket(key);
                ServerInstance serverInstance = rBucket.get();

                this.addInstance(serverInstance, ServerStatus.ONLINE);
            });
        });
    }

    public void createInstance(CommandSender sender, ServerType serverType, String host) {

        String server = serverType.getName();

        ProxyServer.getInstance().getScheduler().runAsync(Zeus.get(), () -> {

            String id = String.valueOf(new Random().nextInt((9999 - 1111) + 1) + 1111);
            int serverId = this.getNextId(serverType);
            int port = this.findPort();
            String map = serverType.findMap();

            this.addInstance(new ServerInstance(serverType, serverId, id, null, port, ServerStatus.BOOTING, new ArrayList<UUID>(), 0, map, host, Maps.newHashMap()), ServerStatus.BOOTING);

            try {

               Runtime.getRuntime().exec("docker service create" +
                        " --publish mode=host,target=25565,published=" + port +
                        " --name " +  server + id +
                        " -e SERVERTYPE=" + server +
                        " -e SERVERID=" + serverId +
                        " -e ID=" + id +
                        " -e SERVERPORT=" + port +
                        " -e MAP=" + map +
                        " -e HOST=" + host +
                        " -e WORLD=https://kerion.s3.eu-west-3.amazonaws.com/" + server.toLowerCase() + "-" + map + ".zip" +
                        " -e EULA=true" +
                        " -e TYPE=spigot" +
                        " -e VERSION=1.8.8-R0.1-SNAPSHOT-latest" +
                        " myregistry.com:5000/" + server.toLowerCase());

                sender.sendMessage(Constants.PREFIX + "§eInstance §6§l" + server + " §ecréée avec l'ID §f" + id);

            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    private int getNextId(ServerType serverType) {

        AtomicInteger id = new AtomicInteger(1);

        while(this.serverInstances.stream().anyMatch(current -> current.getServerType().equals(serverType) && current.getServerId() == id.get()))
            id.incrementAndGet();

        return id.get();
    }

    private int findPort() {

        return 25580 + this.serverInstances.size() + 1;
    }

    private void addInstance(ServerInstance serverInstance, ServerStatus serverStatus) {

        serverInstance.setStatus(serverStatus);

        this.serverInstances.add(serverInstance);

        if(serverInstance.getIp() != null)
            ProxyServer.getInstance().getServers().put(serverInstance.getName(), serverInstance.toServerInfo());

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        RBucket<ServerInstance> rBucket = redissonClient.getBucket("instance:" + serverInstance.getName());
        rBucket.set(serverInstance);
    }

    public void deleteInstance(CommandSender sender, String id) {

        ServerInstance instance = this.getInstanceById(id);

        if(instance != null) {

            ProxyServer.getInstance().getScheduler().runAsync(Zeus.get(), () -> {

                try {

                    Runtime.getRuntime().exec("docker service rm " + instance.getName());

                    sender.sendMessage(Constants.PREFIX + "§eInstance §6§l" + instance.getName() + " §esupprimée.");

                } catch (IOException e) {

                    e.printStackTrace();
                }
            });

        } else {

            sender.sendMessage(Constants.PREFIX + "§cAucune instance pour cet ID trouvée.");
        }
    }

    public void removeInstance(ServerInstance instance) {

        this.serverInstances.remove(instance);
        ProxyServer.getInstance().getServers().remove(instance.getName());

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();
        RBucket<ServerInstance> rBucket = redissonClient.getBucket("instance:" + instance.getName());
        rBucket.delete();
    }

    public void updateInstance(ServerInstance serverInstance) {

        ServerInstance instance = this.getInstanceById(serverInstance.getId());
        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();

        if(instance.getStatus().equals(ServerStatus.CLOSING))
            this.removeInstance(instance);
        else {

            if(instance.getIp() == null) {

                instance.setIp(serverInstance.getIp());
                ProxyServer.getInstance().getServers().put(serverInstance.getName(), serverInstance.toServerInfo());
            }

            instance.setStatus(serverInstance.getStatus());
            instance.setPlayers(serverInstance.getPlayers());
            instance.setMaxPlayers(serverInstance.getMaxPlayers());
            instance.setConfig(serverInstance.getConfig());

            RBucket<ServerInstance> rBucket = redissonClient.getBucket("instance:" + instance.getName());
            rBucket.set(instance);
        }

        if(!instance.getServerType().equals(ServerType.LOBBY)) {

            if(this.getInstancesStartedOrBootingByServer(instance.getServerType()).size() == 0)
                this.createInstance(ProxyServer.getInstance().getConsole(), instance.getServerType(), "console");
        }
    }

    private void feedTotalPlayers() {

        new TotalPlayersFeeder().publish();

        ServerInstance toRemove = this.serverInstances.stream().filter(instance -> instance.getStatus().equals(ServerStatus.CLOSING)).findFirst().orElse(null);

        if(toRemove != null)
            this.removeInstance(toRemove);
    }

    public ServerInstance getInstanceById(String id) {

        return this.serverInstances.stream().filter(current -> current.getId().equals(id)).findFirst().orElse(null);
    }

    public List<ServerInstance> getInstancesByServer(ServerType serverType) {

        return this.serverInstances.stream().filter(current -> current.getServerType().equals(serverType)).collect(Collectors.toList());
    }

    public List<ServerInstance> getInstancesStartedByServer(ServerType serverType) {

        return this.serverInstances.stream().filter(current -> current.getServerType().equals(serverType) && current.getStatus().equals(ServerStatus.ONLINE)).collect(Collectors.toList());
    }

    public List<ServerInstance> getInstancesStartedOrBootingByServer(ServerType serverType) {

        return this.serverInstances.stream().filter(current -> current.getServerType().equals(serverType) && (current.getStatus().equals(ServerStatus.ONLINE) || current.getStatus().equals(ServerStatus.BOOTING))).collect(Collectors.toList());
    }

    public ServerInstance getInstanceByServerAndId(ServerType serverType, int serverId) {

        return this.serverInstances.stream().filter(current -> current.getServerType().equals(serverType) && current.getServerId() == serverId).findFirst().orElse(null);
    }

    public ServerInstance getBestInstanceByServer(ServerType serverType) {

        return this.serverInstances.stream().filter(current -> (current.getStatus().equals(ServerStatus.ONLINE) || current.getStatus().equals(ServerStatus.BOOTING)) && current.getServerType().equals(serverType)).max(Comparator.comparingInt(current -> current.getPlayers().size())).get();
    }

    public ServerInstance getRandomServer(ServerType serverType) {

        List<ServerInstance> servers = this.getInstancesByServer(serverType);

        return servers.get(new Random().nextInt(servers.size()));
    }

    public static InstanceManager get() {

        return instance;
    }
}
