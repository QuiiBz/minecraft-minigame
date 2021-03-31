package fr.quiibz.poseidon.config;

import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.poseidon.config.items.IConfigItem;
import fr.quiibz.poseidon.config.values.IConfigValue;
import fr.quiibz.poseidon.game.IGameManager;

import java.util.List;
import java.util.UUID;

public interface IConfig {

    /*
     *  METHODS
     */

    void updateConfig();
    IConfigValue getValue(String key);
    List<IConfigItem> setItems(IGameManager gameManager);
    List<IConfigItem> getItems();
    ServerType getServerType();
    UUID getOwner();
    void setOwner(UUID owner);
    String getName();
    void setName(String name);
    void load(GameConfig config);
    GameConfig save();
    List<String> toLore();
}
