package fr.quiibz.poseidon.config;

import fr.quiibz.commons.instances.ServerType;

import java.util.Map;
import java.util.UUID;

public class GameConfig {

    /*
     *  FIELDS
     */

    private Map<String, Integer> items;
    private ServerType serverType;
    private UUID owner;
    private String name;

   /*
    *  CONSTRUCTOR
    */

    public GameConfig() {}

    public GameConfig(Map<String, Integer> items, ServerType serverType, UUID owner, String name) {

        this.items = items;
        this.serverType = serverType;
        this.owner = owner;
        this.name = name;
    }

    /*
     *  METHODS
     */

    public Map<String, Integer> getItems() {

        return this.items;
    }

    public void setItems(Map<String, Integer> items) {

        this.items = items;
    }

    public ServerType getServerType() {

        return this.serverType;
    }

    public void setServerType(ServerType serverType) {

        this.serverType = serverType;
    }

    public UUID getOwner() {

        return this.owner;
    }

    public void setOwner(UUID owner) {

        this.owner = owner;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
