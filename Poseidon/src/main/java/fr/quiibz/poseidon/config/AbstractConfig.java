package fr.quiibz.poseidon.config;

import fr.quiibz.api.API;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.poseidon.config.items.IConfigItem;
import fr.quiibz.poseidon.config.items.IntegerConfigItem;
import fr.quiibz.poseidon.config.values.IConfigValue;
import fr.quiibz.poseidon.config.values.IntegerConfigValue;
import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.Material;

import java.util.*;

public abstract class AbstractConfig implements IConfig {

    /*
     *  FIELDS
     */

    private List<IConfigItem> items;
    private ServerType serverType;
    private UUID owner;
    private String name;

    /*
     *  CONSTRUCTOR
     */

    public AbstractConfig(IGameManager gameManager) {

        this.items = this.setItems(gameManager);
        this.serverType = API.get().getServerInstance().getServerType();
        this.owner = null;
        this.name = null;

        this.updateConfig();
    }

    /*
     *  METHODS
     */

    @Override
    public void updateConfig() {

        Map<String, String> config = new HashMap<String, String>();

        this.items.forEach(current -> {

            config.put(current.getConfig().getName(), String.valueOf(current.getConfig().get()));
        });

        API.get().setConfig(config);
    }

    @Override
    public IConfigValue getValue(String key) {

        Optional<IConfigItem> configItem = this.items.stream().filter(current -> current.getConfig().getName().equals(key)).findFirst();

        return (configItem.isPresent() ? configItem.get().getConfig() : null);
    }

    @Override
    public List<IConfigItem> setItems(IGameManager gameManager) {

        List<IConfigItem> items = new ArrayList<IConfigItem>();
        items.add(new IntegerConfigItem(13, Material.SKULL_ITEM, (byte) 3, new IntegerConfigValue(this, \"Slots", gameManager.getMaxPlayers(), gameManager.getMinPlayers(), gameManager.getMaxPlayers())));

        return items;
    }

    @Override
    public List<IConfigItem> getItems() {

        return this.items;
    }

    @Override
    public ServerType getServerType() {

        return this.serverType;
    }

    @Override
    public UUID getOwner() {

        return this.owner;
    }

    @Override
    public void setOwner(UUID owner) {

        this.owner = owner;
    }

    @Override
    public String getName() {

        return this.name;
    }

    @Override
    public void setName(String name) {

        this.name = name;
    }

    @Override
    public void load(GameConfig config) {

        this.setName(config.getName());
        this.setOwner(config.getOwner());

        List<IConfigItem> items = new ArrayList<IConfigItem>(this.items);

        config.getItems().forEach((key, value) -> {

            IConfigItem item = items.stream().filter(current -> current.getConfig().getName().equals(key)).findFirst().get();
            item.getConfig().fromInt(value);
        });

        this.items = items;

        updateConfig();
    }

    @Override
    public GameConfig save() {

        Map<String, Integer> items = new HashMap<String, Integer>();

        this.items.forEach(entry -> {

            items.put(entry.getConfig().getName(), entry.getConfig().toInt());
        });

        return new GameConfig(items, this.getServerType(), this.getOwner(), this.getName());
    }

    @Override
    public List<String> toLore() {

        List<String> lore = new ArrayList<String>();
        lore.add(" ");
        lore.add(Constants.ROUND + "§7Jeu : §6§l" + this.getServerType().getName());
        lore.add(Constants.ROUND + "§7Nom : §a" + this.getName());
        lore.add(" ");
        lore.add(Constants.PREFIX + "§eConfiguration :");

        this.items.forEach(current -> {

            lore.add(Constants.ROUND + "§7" + current.getConfig().getName() + " : §f" + current.getConfig().get());
        });

        return lore;
    }
}
