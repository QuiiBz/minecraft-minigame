package fr.quiibz.zeus.instances;

import fr.quiibz.apollon.utils.Constants;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerStatus;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.commons.topic.TeleportTopic;
import fr.quiibz.zeus.Zeus;
import fr.quiibz.zeus.feeders.listeners.TeleportFeedListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueManager {

    /*
     *  FIELDS
     */

    public InstanceManager instanceManager;
    private List<TeleportTopic> queue;

    /*
     *  CONSTRUCTOR
     */

    public QueueManager() {

        this.instanceManager = InstanceManager.get();
        this.queue = new ArrayList<TeleportTopic>();

        ProxyServer.getInstance().getScheduler().schedule(Zeus.get(), () -> {

            new TeleportFeedListener(this);

        }, 2, TimeUnit.SECONDS);

        ProxyServer.getInstance().getScheduler().schedule(Zeus.get(), this::update, 1, 1, TimeUnit.SECONDS);
    }

    /*
     *  METHODS
     */

    private void update() {

        Iterator<TeleportTopic> iterator = this.queue.iterator();
        Set<TeleportTopic> removeSet = new HashSet<TeleportTopic>();

        while(iterator.hasNext()) {

            TeleportTopic teleportTopic = iterator.next();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(teleportTopic.getUUID());
            ServerType serverType = teleportTopic.getServerType();
            long waiting = this.getWaiting(serverType);

            if(this.tryConnect(teleportTopic))
                removeSet.add(teleportTopic);
            else
                player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eFile d'attente en §a" + serverType.getName() + " §7(" + this.getPosition(player) + "/" + waiting + ")"));
        }

        this.queue.removeAll(removeSet);
    }

    private long getWaiting(ServerType serverType) {

        return this.queue.stream().filter(current -> current.getServerType().equals(serverType)).count();
    }

    private int getPosition(ProxiedPlayer player) {

        AtomicInteger position = new AtomicInteger(0);

        this.queue.stream().peek(x -> position.incrementAndGet()).filter(current -> current.getUUID().equals(player.getUniqueId())).findFirst().get();

        return position.get();
    }

    public void teleport(TeleportTopic teleportTopic) {

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(teleportTopic.getUUID());
        ServerType serverType = teleportTopic.getServerType();

        if(serverType.equals(ServerType.LOBBY))
            this.tryConnect(teleportTopic);
        else {

            if(this.getWaiting(serverType) >= 1)
                this.joinQueue(player, teleportTopic);
            else
                this.tryConnect(teleportTopic);
        }
    }

    public void joinQueue(ProxiedPlayer player, TeleportTopic teleportTopic) {

        if(this.queue.stream().noneMatch(current -> current.getUUID().equals(player.getUniqueId()))) {

            long waiting = this.getWaiting(teleportTopic.getServerType());

            this.queue.add(teleportTopic);
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTu as rejoint la file d'attente en §a" + teleportTopic.getServerType().getName() + " §7(" + this.getPosition(player) + "/" + (waiting + 1) + ")"));
        }
    }

    public void leaveQueue(ProxiedPlayer player) {

        TeleportTopic teleportTopic = this.queue.stream().filter(current -> current.getUUID().equals(player.getUniqueId())).findFirst().orElse(null);

        if(teleportTopic != null)
            this.queue.remove(teleportTopic);
    }

    public boolean tryConnect(TeleportTopic teleportTopic) {

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(teleportTopic.getUUID());
        ServerType serverType = teleportTopic.getServerType();
        boolean teleported = false;

        if(this.instanceManager.getInstancesByServer(serverType).size() >= 1) {

            ServerInstance instance = null;

            if(teleportTopic.getServerId() == -1) {

                if(serverType.equals(ServerType.LOBBY))
                    instance = this.instanceManager.getRandomServer(serverType);
                else
                    instance = this.instanceManager.getBestInstanceByServer(serverType);

                if(!instance.getStatus().equals(ServerStatus.BOOTING) &&
                        !instance.getStatus().equals(ServerStatus.CLOSING) &&
                        !instance.getStatus().equals(ServerStatus.PLAYING) &&
                        instance.getPlayers().size() < instance.getMaxPlayers() &&
                        !player.getServer().getInfo().getName().equals(instance.getName())) {

                    if(!instance.getStatus().equals(ServerStatus.WHITELISED) || (instance.getStatus().equals(ServerStatus.WHITELISED) && instance.getHost().equals(player.getName()))) {

                        this.leaveQueue(player);

                        player.connect(instance.toServerInfo());
                        teleported = true;
                    }
                }

                if(!teleported)
                    this.joinQueue(player, teleportTopic);

            } else {

                instance = this.instanceManager.getInstanceByServerAndId(serverType, teleportTopic.getServerId());

                if(instance.getStatus().equals(ServerStatus.BOOTING))
                    player.sendMessage(Constants.PREFIX + "§cCe serveur n'est pas encore démarré.");
                else if(instance.getStatus().equals(ServerStatus.CLOSING))
                    player.sendMessage(Constants.PREFIX + "§cCe serveur est entrain de s'arrêter.");
                else if(instance.getStatus().equals(ServerStatus.PLAYING))
                    player.sendMessage(Constants.PREFIX + "§cCe serveur est déjà en jeu.");
                else if(instance.getPlayers().size() >= instance.getMaxPlayers())
                    player.sendMessage(Constants.PREFIX + "§cCe serveur est déjà plein.");
                else if(player.getServer().getInfo().getName().equals(instance.getName()))
                    player.sendMessage(Constants.PREFIX + "§cTu es déjà connecté à ce serveur.");
                else {

                    if(!instance.getStatus().equals(ServerStatus.WHITELISED) || (instance.getStatus().equals(ServerStatus.WHITELISED) && instance.getHost().equals(player.getName()))) {

                        this.leaveQueue(player);

                        player.connect(instance.toServerInfo());
                        teleported = true;

                    } else
                        player.sendMessage(Constants.PREFIX + "§cTu n'es pas l'hôte de ce serveur.");
                }
            }

        } else
            this.joinQueue(player, teleportTopic);

        return teleported;
    }
}
