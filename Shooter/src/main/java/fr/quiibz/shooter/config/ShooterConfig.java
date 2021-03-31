package fr.quiibz.shooter.config;

import fr.quiibz.poseidon.config.*;
import fr.quiibz.poseidon.config.items.IConfigItem;
import fr.quiibz.poseidon.config.items.IntegerConfigItem;
import fr.quiibz.poseidon.config.values.IntegerConfigValue;
import fr.quiibz.poseidon.game.IGameManager;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class ShooterConfig extends AbstractConfig {

    /*
     *  CONSTRUCTOR
     */

    public ShooterConfig(IGameManager gameManager) {

        super(gameManager);
    }

    /*
     *  METHODS
     */

    @Override
    public List<IConfigItem> setItems(IGameManager gameManager) {

        List<IConfigItem> previous = super.setItems(gameManager);
        previous.addAll(Arrays.asList(
                new IntegerConfigItem(21, Material.REDSTONE, new IntegerConfigValue(this, "Kills", 20, 1, 50)),
                new IntegerConfigItem(23, Material.FEATHER, new IntegerConfigValue(this, "Vitesse", 4, 2, 8))));

        return previous;
    }
}
