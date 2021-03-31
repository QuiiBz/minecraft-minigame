package fr.quiibz.ctf.config;

import fr.quiibz.poseidon.config.AbstractTeamConfig;
import fr.quiibz.poseidon.config.items.AbstractConfigItem;
import fr.quiibz.poseidon.config.items.IConfigItem;
import fr.quiibz.poseidon.config.items.IntegerConfigItem;
import fr.quiibz.poseidon.config.values.IntegerConfigValue;
import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class CTFConfig extends AbstractTeamConfig {

    /*
     *  CONSTRUCTOR
     */

    public CTFConfig(IGameManager gameManager) {

        super(gameManager);
    }

    /*
     *  METHODS
     */

    @Override
    public List<IConfigItem> setItems(IGameManager gameManager) {

        List<IConfigItem> items = new ArrayList<IConfigItem>();
        items.add(new IntegerConfigItem(13, Material.SKULL_ITEM, (byte) 3, new IntegerConfigValue(this, "Slots", gameManager.getMaxPlayers(), gameManager.getMinPlayers(), gameManager.getMaxPlayers())));
        items.add(new IntegerConfigItem(21, Material.EYE_OF_ENDER, new IntegerConfigValue(this, "Points", 5, 1, 10)));

        return items;
    }
}
