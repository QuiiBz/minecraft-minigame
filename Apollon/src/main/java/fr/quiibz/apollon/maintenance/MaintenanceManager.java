package fr.quiibz.apollon.maintenance;

import com.google.gson.reflect.TypeToken;
import fr.quiibz.apollon.Apollon;
import fr.quiibz.apollon.utils.FileUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class MaintenanceManager {

    /*
     *  FIELDS
     */

    private Maintenance maintenance;

    /*
     *  CONSTRUCTOR
     */

    public MaintenanceManager(Apollon apollon) {

        File file = new File(apollon.getDataFolder() + "/config", "maintenance.json");
        String json = FileUtils.loadFile(file);

        if(json.equals("")) {

            this.maintenance = new Maintenance(false, new ArrayList<String>());

        } else {

            Type maintenanceType = new TypeToken<Maintenance>(){}.getType();

            this.maintenance = (Maintenance) apollon.getSerialize().deserialize(json, maintenanceType);
        }
    }

    /*
     *  METHODS
     */

    public void save(Apollon apollon) {

        String json = apollon.getSerialize().serialize(this.maintenance);
        FileUtils.saveFile(new File(apollon.getDataFolder() + "/config", "maintenance.json"), json);
    }

    public boolean isMaintenanceEnabled() {

        return this.maintenance.isEnabled();
    }

    public void setMaintenanceEnabled(boolean enabled) {

        this.maintenance.setEnabled(enabled);
    }

    public void addPlayer(String player) {

        this.maintenance.addPlayer(player);
    }

    public void removePlayer(String player) {

        this.maintenance.removePlayer(player);
    }

    public boolean canJoin(ProxiedPlayer player) {

        if(this.isMaintenanceEnabled())
            return this.maintenance.contains(player.getName());

        return true;
    }
}
