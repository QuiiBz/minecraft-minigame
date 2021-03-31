package fr.quiibz.poseidon.config;

import fr.quiibz.poseidon.config.items.AbstractConfigItem;
import fr.quiibz.poseidon.config.items.IConfigItem;
import fr.quiibz.poseidon.config.items.IntegerConfigItem;
import fr.quiibz.poseidon.config.values.IntegerConfigValue;
import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractTeamConfig extends AbstractConfig {

    /*
     *  CONSTRUCTOR
     */

    public AbstractTeamConfig(IGameManager gameManager) {

        super(gameManager);
    }

    /*
     *  METHODS
     */

    @Override
    public List<IConfigItem> setItems(IGameManager gameManager) {

        List<IConfigItem> previous = super.setItems(gameManager);
        previous.addAll(Arrays.asList(
                new IntegerConfigItem(21, Material.BANNER, (byte) 15, new IntegerConfigValue(this, "Teams", 2, 2, gameManager.getMaxPlayers() / 2))));

        return previous;
    }
}
