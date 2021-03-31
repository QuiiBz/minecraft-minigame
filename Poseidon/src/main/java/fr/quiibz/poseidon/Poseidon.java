package fr.quiibz.poseidon;

import org.bukkit.plugin.java.JavaPlugin;

public class Poseidon extends JavaPlugin {

    /*
     *  FIELDS
     */

    private static Poseidon instance;

    /*
     *  METHODS
     */

    @Override
    public void onEnable() {

        instance = this;
    }

    public static Poseidon get() {

        return instance;
    }
}
