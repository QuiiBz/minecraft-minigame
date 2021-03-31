package fr.quiibz.apollon;

import fr.quiibz.apollon.accounts.AccountManager;
import fr.quiibz.apollon.commands.FriendsCommand;
import fr.quiibz.apollon.commands.MaintenanceCommand;
import fr.quiibz.apollon.data.mongo.MongoManager;
import fr.quiibz.apollon.data.redis.RedisAccess;
import fr.quiibz.apollon.friends.FriendsManager;
import fr.quiibz.apollon.listeners.JoinListener;
import fr.quiibz.apollon.maintenance.MaintenanceManager;
import fr.quiibz.apollon.utils.Serialize;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.accounts.Settings;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Apollon extends Plugin {

    /*
     *  FIELDS
     */

    private static Apollon instance;

    private Serialize serialize;
    private MaintenanceManager maintenanceManager;

    /*
     *  METHODS
     */

    @Override
    public void onEnable() {

        instance = this;

        RedisAccess.init();
        new MongoManager(Account.class, Settings.class);

        new AccountManager();

        this.serialize = new Serialize();
        this.maintenanceManager = new MaintenanceManager(this);

        PluginManager pluginManager = this.getProxy().getPluginManager();
        pluginManager.registerListener(this, new JoinListener());
        pluginManager.registerCommand(this, new MaintenanceCommand("maintenance", this.maintenanceManager));
        pluginManager.registerCommand(this, new FriendsCommand(new FriendsManager()));
    }

    @Override
    public void onDisable() {

        RedisAccess.close();

        this.maintenanceManager.save(this);
    }

    public Serialize getSerialize() {

        return this.serialize;
    }

    public MaintenanceManager getMaintenanceManager() {

        return this.maintenanceManager;
    }

    public static Apollon get() {

        return instance;
    }
}
