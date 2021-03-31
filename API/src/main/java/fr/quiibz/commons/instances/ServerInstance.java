package fr.quiibz.commons.instances;

import fr.quiibz.api.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerInstance {

    /*
     *  FIELDS
     */

    private ServerType serverType;
    private int serverId;
    private String id;
    private String ip;
    private int port;
    private ServerStatus status;
    private List<UUID> players;
    private int maxPlayers;
    private String map;
    private String host;
    private Map<String, String> config;

    /*
     *  CONSTRUCTOR
     */

    public ServerInstance() { }

    public ServerInstance(ServerType serverType, int serverId, String id, String ip, int port, ServerStatus status, List<UUID> players, int maxPlayers, String map, String host, Map<String, String> config) {

        this.serverType = serverType;
        this.serverId = serverId;
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.status = status;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.map = map;
        this.host = host;
        this.config = config;
    }

    /*
     *  METHODS
     */

    public ServerType getServerType() {

        return this.serverType;
    }

    public int getServerId() {

        return this.serverId;
    }

    public String getId() {

        return this.id;
    }

    public String getIp() {

        return this.ip;
    }

    public int getPort() {

        return this.port;
    }

    public String getName() {

        return this.serverType.getName() + this.id;
    }

    public ServerStatus getStatus() {

        return this.status;
    }

    public void setStatus(ServerStatus status) {

        this.status = status;
    }

    public List<UUID> getPlayers() {

        return this.players;
    }

    public void setPlayers(List<UUID> players) {

        this.players = players;
    }

    public int getMaxPlayers() {

        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {

        this.maxPlayers = maxPlayers;
    }

    public String getMap() {

        return this.map;
    }

    public void setMap(String map) {

        this.map = map;
    }

    public String getHost() {

        return this.host;
    }

    public Map<String, String> getConfig() {

        return this.config;
    }

    public void setConfig(Map<String, String> config) {

        this.config = config;
    }

    public List<String> toLore() {

        List<String> lore = new ArrayList<String>();
        lore.add(" ");

        if(!this.host.equals("console")) {

            lore.add(Constants.ROUND + "§7Jeu : §6§l" + this.getServerType().getName());
            lore.add(Constants.ROUND + "§7Host : §f" + this.getHost());
            lore.add(" ");

        }

        lore.add(Constants.ROUND + "§7Carte : §6" + this.getMap());
        lore.add(Constants.ROUND + "§7Status : " + this.getStatus().getName());
        lore.add(Constants.ROUND + "§7Connectés : §a" + this.getPlayers().size() + "§7/" + this.getMaxPlayers());

        if(!this.host.equals("console")) {

            lore.add(" ");
            lore.add(Constants.PREFIX + "§eConfiguration :");

            this.getConfig().forEach((name, value) -> lore.add(Constants.ROUND + "§7" + name + " : §f" + value));
        }

        lore.add(" ");

        return lore;
    }
}
