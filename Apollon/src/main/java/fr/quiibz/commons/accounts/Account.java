package fr.quiibz.commons.accounts;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Account implements Cloneable {

    /*
     *  FIELDS
     */

    private UUID uuid;
    private int power;
    private int coins;
    private int visibility;
    private PetsType pet;
    private Map<String, UUID> friends;

    /*
     *  CONSTRUCTOR
     */

    public Account() {}

    public Account(UUID uuid, int power, int coins, int visibility, PetsType pet, Map<String, UUID> friends) {

        this.uuid = uuid;
        this.power = power;
        this.coins = coins;
        this.visibility = visibility;
        this.pet = pet;
        this.friends = friends;
    }

    /*
     *  METHODS
     */

    public UUID getUuid() {

        return this.uuid;
    }

    public void setUuid(UUID uuid) {

        this.uuid = uuid;
    }

    public int getPower() {

        return this.power;
    }

    public void setPower(int power) {

        this.power = power;
    }

    public Rank getRank() {

        return Rank.getRank(this.power);
    }

    public int getCoins() {

        return this.coins;
    }

    public void setCoins(int coins) {

        this.coins = coins;
    }

    public void setVisibility(int visibility) {

        this.visibility = visibility;
    }

    public int getVisibility() {

        return this.visibility;
    }

    public PetsType getPet() {

        return this.pet;
    }

    public void setPet(PetsType pet) {

        this.pet = pet;
    }

    public Map<String, UUID> getFriends() {

        return this.friends;
    }

    public void setFriends(Map<String, UUID> friends) {
        
        this.friends = friends;
    }

    @Override
    public Account clone() {

        try {

            return (Account) super.clone();

        } catch (CloneNotSupportedException e) {

            e.printStackTrace();
        }

        return null;
    }
}
