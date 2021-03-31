package fr.quiibz.lobby.pets;

import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.accounts.PetsType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class Pet {

    /*
     *  FIELDS
     */

    private PetsType petsType;
    private Entity entity;

    /*
     *  CONSTRUCTOR
     */

    public Pet(PetsType petsType, Location loc) {

        ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);

        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setHelmet(new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(petsType.getOwner()).toItemStack());
        armorStand.setSmall(true);

        this.entity = armorStand;
    }

    /*
     *  METHODS
     */

    public PetsType getPetsType() {

        return this.petsType;
    }

    public void updateLocation(Location loc) {

        this.entity.teleport(loc.add(loc.getYaw() < 90 ? 0.5 : -0.5, 1, loc.getYaw() > 90 ? 0.5 : -0.5));
    }

    public void delete() {

        this.entity.remove();
    }
}
