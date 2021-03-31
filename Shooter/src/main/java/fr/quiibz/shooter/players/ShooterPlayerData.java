package fr.quiibz.shooter.players;

import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.api.utils.PlayerUtils;
import fr.quiibz.poseidon.config.values.IntegerConfigValue;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.players.AbstractPlayerData;
import fr.quiibz.poseidon.players.IPlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ShooterPlayerData extends AbstractPlayerData {

    /*
     *  FIELDS
     */

    private int tier;

    /*
     *  CONSTRUCTOR
     */

    public ShooterPlayerData(UUID uuid) {

        super(uuid);

        this.tier = 1;
    }

    /*
     *  METHODS
     */

    @Override
    public void handleStart(IGameManager gameManager) {

        Player player = this.toPlayer();

        IntegerConfigValue value = (IntegerConfigValue) gameManager.getConfig().getValue("Vitesse");

        PlayerUtils.clear(player);
        player.setWalkSpeed(Float.parseFloat(String.format("%.1f", (float) value.get() / 10)));
        player.getInventory().setItem(0, new ItemBuilder(this.toMaterial()).setName("§6§lShooter" + Constants.RCLICK).toItemStack());
    }

    public int getTier() {

        return this.tier;
    }

    public void tryUpdate() {

        Player player = this.toPlayer();
        int newTier = this.tier;

        if(this.getKills() >= 15 && tier < 4)
            newTier = 4;
        else if(this.getKills() >= 10 && tier < 3)
            newTier = 3;
        else if(this.getKills() >= 5 && tier < 2)
            newTier = 2;

        if(newTier != this.tier) {

            this.tier = newTier;
            player.getInventory().setItem(0, new ItemBuilder(this.toMaterial()).setName("§6§lShooter" + Constants.RCLICK).toItemStack());
            player.sendMessage(Constants.PREFIX + "§eVous avez reçu une arme de tier supérieur !");
        }
    }

    public long getCooldown() {

        return (this.tier == 4 ? 250 : this.tier == 3 ? 500 : this.tier == 2 ? 750 : 1000);
    }

    private Material toMaterial() {

        return (this.tier == 4 ? Material.DIAMOND_HOE : this.tier == 3 ? Material.IRON_HOE : this.tier == 2 ? Material.STONE_HOE : Material.WOOD_HOE);
    }
}
