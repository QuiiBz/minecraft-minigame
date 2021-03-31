package fr.quiibz.api.registers;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandRegistrable implements Registrable {

    /*
     *  METHODS
     */

    public void register(JavaPlugin plugin) {

        plugin.getCommand(this.getCommand()).setExecutor(this.getExecutor());
    }

    public abstract String getCommand();
    public abstract CommandExecutor getExecutor();
}
