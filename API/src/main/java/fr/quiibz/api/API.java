package fr.quiibz.api;

import com.google.common.collect.Maps;
import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.commands.LagCommand;
import fr.quiibz.api.data.redis.RedisAccess;
import fr.quiibz.api.feeders.InstanceStatusFeeder;
import fr.quiibz.api.feeders.listeners.FriendsFeedListener;
import fr.quiibz.api.feeders.listeners.RankFeedListener;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.listeners.DefaultListener;
import fr.quiibz.api.listeners.GuiListener;
import fr.quiibz.api.listeners.NPCListener;
import fr.quiibz.api.logger.Logger;
import fr.quiibz.api.npc.NPCManager;
import fr.quiibz.api.registers.Registrable;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerStatus;
import fr.quiibz.commons.instances.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class API extends JavaPlugin {

    /*
     *  FIELDS
     */

    private static API instance;

    private boolean update;
    private boolean disabling;
    private ServerInstance serverInstance;
    private String serverInfo;
    private NPCManager npcManager;

    /*
     *  METHODS
     */

    @Override
    public void onEnable() {

        Logger.log("Loading GemAPI...");

        instance = this;

        RedisAccess.init();

        new AccountManager();

        this.register(this, new GuiManager());
        this.register(this, new DefaultListener(this));
        this.register(this, new GuiListener());
        this.register(this, new NPCListener());
        this.register(this, new LagCommand());

        this.update = true;
        this.disabling = false;

        this.serverInstance = new ServerInstance(
                ServerType.getByName(System.getenv("SERVERTYPE")),
                Integer.parseInt(System.getenv("SERVERID")),
                System.getenv("ID"),
                this.findIp(),
                Integer.parseInt(System.getenv("SERVERPORT")),
                ServerStatus.ONLINE,
                Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList()),
                Bukkit.getServer().getMaxPlayers(),
                System.getenv("MAP"),
                System.getenv("HOST"),
                Maps.newHashMap());

        if(!this.serverInstance.getHost().equals("console"))
            this.serverInstance.setStatus(ServerStatus.WHITELISED);

        this.serverInfo = "§7" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + " §8" + (this.serverInstance.getServerType().equals(ServerType.CTF) ? "CTF" : this.serverInstance.getServerType().getName()) + " #" + this.serverInstance.getServerId();
        this.npcManager = new NPCManager();

        new RankFeedListener();
        new FriendsFeedListener();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {

            if(!this.disabling && this.update) {

                this.serverInstance.setPlayers(Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList()));

                new InstanceStatusFeeder(this.serverInstance).publish();

                this.setUpdate(false);
            }
        }, 0, 20);

        Logger.log("Successfully loaded GemAPI");
    }

    @Override
    public void onDisable() {

        this.disabling = true;

        this.serverInstance.setStatus(ServerStatus.CLOSING);

        new InstanceStatusFeeder(this.serverInstance).publish();

        RedisAccess.close();
    }

    public void register(JavaPlugin plugin, Registrable registrable) {

        registrable.register(plugin);
    }

    private String findIp() {

        try {

            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            return in.readLine();

        } catch (IOException e) {

            return null;
        }
    }

    public void setUpdate(boolean update) {

        this.update = update;
    }

    public void setMaxPlayers(int maxPlayers) {

        this.serverInstance.setMaxPlayers(maxPlayers);
        this.setUpdate(true);
    }

    public void setConfig(Map<String, String> config) {

        this.serverInstance.setConfig(config);
        this.setUpdate(true);
    }

    public ServerInstance getServerInstance() {

        return this.serverInstance;
    }

    public String getServerInfo() {

        return this.serverInfo;
    }

    public NPCManager getNPCManager() {

        return this.npcManager;
    }

    public static API get() {

        return instance;
    }
}
