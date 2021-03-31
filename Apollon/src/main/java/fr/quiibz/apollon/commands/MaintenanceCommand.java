package fr.quiibz.apollon.commands;

import fr.quiibz.apollon.maintenance.MaintenanceManager;
import fr.quiibz.apollon.utils.Constants;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MaintenanceCommand extends Command {

    /*
     *  FIELDS
     */

    private MaintenanceManager maintenanceManager;

    /*
     *  CONSTRUCTOR
     */

    public MaintenanceCommand(String name, MaintenanceManager maintenanceManager) {

        super(name);

        this.maintenanceManager = maintenanceManager;
    }

    /*
     *  METHODS
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length == 1) {

            switch (args[0]) {

                case "status":
                    sender.sendMessage(Constants.PREFIX + "§eLe mode maintenance est actuellement " + (this.maintenanceManager.isMaintenanceEnabled() ? "§a§lactivé" : "§c§ldésactivé") + ".");
                    break;

                case "on":
                    this.maintenanceManager.setMaintenanceEnabled(true);
                    sender.sendMessage(Constants.PREFIX + "§eLe mode maintenance est maintenance §a§lactivé.");
                    break;

                case "off":
                    this.maintenanceManager.setMaintenanceEnabled(false);
                    sender.sendMessage(Constants.PREFIX + "§eLe mode maintenance est maintenance §c§ldésactivé.");
                    break;

                default:
                    break;
            }

        } else if(args.length == 2) {

            if(args[0].equals("add")) {

                this.maintenanceManager.addPlayer(args[1]);
                sender.sendMessage(Constants.PREFIX + "§eLe joueur §6§l" + args[1] + " §ea été ajouté au mode maintenance.");

            } else if(args[0].equals("remove")) {

                this.maintenanceManager.removePlayer(args[1]);
                sender.sendMessage(Constants.PREFIX + "§eLe joueur §6§l" + args[1] + " §ea été enlevé du mode maintenance.");
            }
        }
    }
}
